package com.ofthemasses.self_management_app.diary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.ofthemasses.self_management_app.BuildConfig
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DiarySerializer : Activity() {

  companion object {
    private val STORAGE_PERM_CODE = 100

    private val DIARY_FOLDER_PATH =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() +
            "/Vimwiki/diary/"

    private var instance: DiarySerializer? = null

    private var DiaryEntries = HashMap<String, DiaryEntry>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun deserializeToday(): DiaryEntry {
      val curDate = LocalDateTime.now().minusHours(1)
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      val curDateString = formatter.format(curDate)

      val file = File(String.format("%s%s.md", DIARY_FOLDER_PATH, curDateString))
      return deserializeMarkdown(file)
    }

    fun serializeDiaryEntry(entry: DiaryEntry): Boolean {
      val entryBuilder = StringBuilder()

      entryBuilder
          .appendLine(String.format("# %s", entry.date))
          .appendLine()
          .appendLine(String.format("### %s", entry.description))
          .appendLine()
          .appendLine("## To-Do List")

      entry.toDos.forEachIndexed { index, section ->
        if (index != 0) entryBuilder.appendLine("---")
        section.forEach { todo ->
          val formattedTodo =
              when (todo?.status) {
                -1 -> "- ~~${todo.name}~~"
                0 -> "- [ ] ${todo.name}"
                1 -> "- [X] ${todo.name}"
                else -> ""
              }
          entryBuilder.appendLine(formattedTodo)
        }
      }

      entry.sections.forEach { (title, description) ->
        entryBuilder.appendLine(title).append(description)
      }

      try {
        File(String.format("%s%s.md", DIARY_FOLDER_PATH, entry.date))
            .writeText(entryBuilder.toString())
        return true
      } catch (e: Exception) {
        return false
      }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermission(activity: Activity?): Boolean {
      if (activity == null) {
        return false
      }

      if (!Environment.isExternalStorageManager()) {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        activity.startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))

        return Environment.isExternalStorageManager()
      }

      return true
    }

    private fun applicationContext(): Context {
      return instance!!.applicationContext
    }

    private fun deserializeMarkdown(file: File): DiaryEntry {
      val date = file.nameWithoutExtension

      if (DiaryEntries.containsKey(date)) {
        return DiaryEntries[date]!!
      }

      var description = ""
      val toDos = ArrayList<ArrayList<ToDo?>>()
      toDos.add(ArrayList<ToDo?>())
      val sections = ArrayList<Pair<String, String>>()

      var handlingTodos = false
      var handlingSections = false
      var index = 0
      var todoSection = 0
      var sectionIndex = -1
      try {
        file.forEachLine { line ->
          if (handlingTodos) {
            handlingTodos = !line.startsWith("##")
            handlingSections = !handlingTodos
            if (!handlingSections) todoSection = handleTodo(line, todoSection, toDos)
          }

          if (handlingSections) {
            sectionIndex = handleSection(line, sectionIndex, sections)
          }

          if (index == 2) {
            description = line.drop(4)
          }

          if (index == 4) {
            handlingTodos = true
          }
          index++
        }
      } catch (e: Exception) {
        Log.e(e.message!!, e.stackTraceToString())
      }

      val entry = DiaryEntry(date, description, toDos, sections)

      DiaryEntries[date] = entry

      return entry
    }

    private fun handleTodo(line: String, section: Int, todos: ArrayList<ArrayList<ToDo?>>): Int {
      if (line.startsWith("---")) {
        todos.add(ArrayList<ToDo?>())
        return section + 1
      }

      if (!line.startsWith('-')) {
        todos[section].add(null)
        return section
      }

      val todoString = line.drop(2)

      when (todoString.substring(0, 2)) {
        "~~" -> {
          todos[section].add(ToDo(-1, todoString.drop(2).substringBefore("~~")))
        }
        "[ " -> {
          todos[section].add(ToDo(0, todoString.drop(4)))
        }
        "[X",
        "[x" -> {
          todos[section].add(ToDo(1, todoString.drop(4)))
        }
      }

      return section
    }

    private fun handleSection(
        line: String,
        sectionIndex: Int,
        sections: ArrayList<Pair<String, String>>
    ): Int {
      if (line.startsWith("##")) {
        sections.add(Pair(line, ""))
        return sectionIndex + 1
      }

      val section = sections[sectionIndex]
      sections[sectionIndex] = section.copy(second = section.second + line + "\n")
      return sectionIndex
    }
  }
}
