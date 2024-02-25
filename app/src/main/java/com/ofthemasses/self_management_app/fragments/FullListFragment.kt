package com.ofthemasses.self_management_app.fragments

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material.icons.sharp.FiberManualRecord
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.ofthemasses.self_management_app.diary.DiaryEntry
import com.ofthemasses.self_management_app.diary.DiarySerializer
import com.ofthemasses.self_management_app.diary.ToDo
import com.ofthemasses.self_management_app.ui.theme.SelfmanagementappTheme

class FullListFragment : Fragment() {
  @RequiresApi(Build.VERSION_CODES.R)
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        SelfmanagementappTheme {
          Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FullListView(activity = requireActivity())
          }
        }
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun FullListView(activity: Activity? = null) {
  if (!DiarySerializer.checkPermission(activity)) {
    // TODO Make it so this waits for permission to be activated
    DiarySerializer.checkPermission(activity)
  }

  val entry = DiarySerializer.deserializeToday()
  val todos: ArrayList<ArrayList<ToDo?>> = entry.toDos
  Column() {
    Spacer(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    )
    LazyColumn {
      items(todos.size) { index ->
        todos[index].forEachIndexed { index, todo ->
          todo?.let { notNullTodo -> ToDoEntry(notNullTodo, entry) }
        }

        if (index < todos.size - 1) {
          Spacer(
              modifier = Modifier
                  .height(20.dp)
          )
          Divider()
        }
      }
    }
  }
}

@Composable
fun ToDoEntry(todo: ToDo, entry: DiaryEntry) {
  // TODO these placeholders should be replaced by an empty value
  var icon = remember { mutableStateOf(Icons.Sharp.FiberManualRecord) }
  var tint = remember { mutableStateOf(Color.White) }

  // TODO this cannot be the only way to give the colours to the ToDoIcon function..
  val tickColour = MaterialTheme.colorScheme.secondary
  val dotColour = MaterialTheme.colorScheme.tertiary

  icon.value = ToDoIcon(todo)
  tint.value = ToDoTint(todo, tickColour, dotColour)

  Box(modifier = Modifier
      .fillMaxWidth()
      .background(if (todo.status == -1) MaterialTheme.colorScheme.outline else Color.White)
      .padding(10.dp)
      .clickable {
          todo.status = 1
          DiarySerializer.serializeDiaryEntry(entry)
          icon.value = ToDoIcon(todo)
          tint.value = ToDoTint(todo, tickColour, dotColour)
      }
  ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
          Icon (
              modifier = Modifier.padding(start = 4.dp),
              imageVector = icon.value,
              tint = tint.value,
              contentDescription = null
          )
          Text(
              modifier = Modifier.padding(start = 10.dp),
              text = todo.name,
              style = MaterialTheme.typography.bodyLarge
          )
      }
  }
  Divider()
}

fun ToDoIcon(todo: ToDo): ImageVector {
    return when(todo.status){
        0 -> Icons.Sharp.FiberManualRecord
        1 -> Icons.Sharp.Done
        else -> Icons.Sharp.Close
    }
}

fun ToDoTint(todo: ToDo, tick: Color, dot: Color): Color {
    return when (todo.status){
        0 -> tick
        1 -> dot
        else -> Color.White
    }
}