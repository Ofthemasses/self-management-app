package com.ofthemasses.self_management_app

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofthemasses.self_management_app.diary.DiarySerializer
import com.ofthemasses.self_management_app.ui.theme.SelfmanagementappTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SelfmanagementappTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Middle(this);
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Middle(activity: Activity? = null) {
    var mainCardText by remember {
        mutableStateOf("No TODO")
    }
    var upcomingCardText by remember {
        mutableStateOf("No TODO")
    }

    if (!DiarySerializer.checkPermission(activity)){
        DiarySerializer.checkPermission(activity)
    }

    val entry = DiarySerializer.deserializeToday();

    var todo = entry.getTodoByIndex(0, 0);
    if (todo != null) mainCardText = todo.name;

    var upcomingTodo = entry.getTodoByIndex(1, 0);
    if (upcomingTodo != null) upcomingCardText = upcomingTodo.name;

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        Divider(
                modifier = Modifier
                    .height(2.dp)
        )
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 500.dp)
                .clickable {
                    if (todo == null) {
                        return@clickable
                    }

                    todo!!.status = 1;
                    todo = upcomingTodo;
                    mainCardText = upcomingCardText;
                    upcomingTodo = entry.getTodoByIndex(1, 0);
                    upcomingCardText = upcomingTodo?.name ?: "No TODO"
                    DiarySerializer.serializeDiaryEntry(entry)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = mainCardText,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }
        Divider(
            modifier = Modifier
                .height(2.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            Text(
                text = upcomingCardText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SelfmanagementappTheme {
        Middle();
    }
}