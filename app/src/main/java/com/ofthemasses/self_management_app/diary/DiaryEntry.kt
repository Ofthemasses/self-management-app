package com.ofthemasses.self_management_app.diary

data class DiaryEntry(val date: String,
                      val description: String,
                      val toDos: ArrayList<ArrayList<ToDo?>>,
                      val sections: HashMap<String, String>)
