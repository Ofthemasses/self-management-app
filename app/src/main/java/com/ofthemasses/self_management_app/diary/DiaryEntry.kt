package com.ofthemasses.self_management_app.diary

import java.util.Collections

data class DiaryEntry(val date: String,
                      val description: String,
                      val toDos: ArrayList<ArrayList<ToDo?>>,
                      val sections: HashMap<String, String>){
    fun getTodoByIndex(index: Int, statusFilter: Int? = null): ToDo? {
        val flattenedTodos = toDos.flatten();

        if (index < 0 || index >= flattenedTodos.size) return null;

        if (statusFilter == null){
            return flattenedTodos.firstOrNull { it != null }
        }
        return flattenedTodos.filterNotNull().firstOrNull { it.status == statusFilter }
    }
}
