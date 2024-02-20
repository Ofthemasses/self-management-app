package com.ofthemasses.self_management_app.diary

import android.app.Activity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

class DiarySerializer : Activity(){

    companion object {

        private val STORAGE_PERM_CODE = 100;

        private var instance: DiarySerializer? = null;

        private lateinit var DiaryEntries: HashMap<String, DiaryEntry>;

        private fun applicationContext() : Context {
            return instance!!.applicationContext;
        }

        //fun DeserializeToday(): DiaryEntry{
        //}

        fun checkPermission(activity: Activity?) : Boolean {
            if (activity == null){
                return false;
            }


            var result = ContextCompat.checkSelfPermission(activity.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE);

            if (result != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERM_CODE
                )

                return ContextCompat.checkSelfPermission(activity.applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }

            return true;
        }

    }
}