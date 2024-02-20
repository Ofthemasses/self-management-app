package com.ofthemasses.self_management_app.diary

data class DiaryEntry(val Date: String,
                      val Description: String,
                      val ToDos: ArrayList<ArrayList<ToDo>>,
                      val Sections: HashMap<String, String>)
