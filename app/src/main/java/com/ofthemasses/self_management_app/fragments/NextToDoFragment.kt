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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.ofthemasses.self_management_app.diary.DiarySerializer
import com.ofthemasses.self_management_app.ui.theme.SelfmanagementappTheme

class NextToDoFragment : Fragment() {
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
                NextToDoView(activity = requireActivity())
            }
        }
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun NextToDoView(activity: Activity? = null) {
  var mainCardText by remember { mutableStateOf("No TODO") }
  var upcomingCardText by remember { mutableStateOf("No TODO") }

  if (!DiarySerializer.checkPermission(activity)) {
    // TODO Make it so this waits for permission to be activated
    DiarySerializer.checkPermission(activity)
  }

  // TODO Update these variables while the app is running to avoid having to re open app
  val entry = DiarySerializer.deserializeToday()

  var todo = entry.getTodoByIndex(0, 0)
  if (todo != null) mainCardText = todo.name

  var upcomingTodo = entry.getTodoByIndex(1, 0)
  if (upcomingTodo != null) upcomingCardText = upcomingTodo.name

  Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(
        modifier =
            Modifier.height(100.dp).fillMaxWidth().background(MaterialTheme.colorScheme.primary))
    Divider(modifier = Modifier.height(2.dp))
    Box(
        modifier =
            Modifier.size(width = 300.dp, height = 500.dp).clickable {
              if (todo == null) {
                return@clickable
              }

              todo!!.status = 1
              todo = upcomingTodo
              mainCardText = upcomingCardText
              upcomingTodo = entry.getTodoByIndex(1, 0)
              upcomingCardText = upcomingTodo?.name ?: "No TODO"
              DiarySerializer.serializeDiaryEntry(entry)
            },
        contentAlignment = Alignment.Center,
    ) {
      Text(
          text = mainCardText,
          style = MaterialTheme.typography.headlineLarge,
          textAlign = TextAlign.Center)
    }
    Divider(modifier = Modifier.height(2.dp))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text(
          text = upcomingCardText,
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center)
    }
  }
}
