package com.ofthemasses.self_management_app

import android.os.Bundle
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ofthemasses.self_management_app.fragments.FullListFragment
import com.ofthemasses.self_management_app.fragments.NextToDoFragment

class MainActivity : FragmentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val viewPager: ViewPager2 = findViewById(R.id.viewPager)
    val adapter = ViewPagerAdapter(this)
    adapter.addFragment(NextToDoFragment())
    adapter.addFragment(FullListFragment())
    viewPager.adapter = adapter
  }
}

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
  private val fragments: MutableList<Fragment> = mutableListOf()

  fun addFragment(fragment: Fragment) {
    fragments.add(fragment)
  }

  override fun getItemCount(): Int {
    return fragments.size
  }

  override fun createFragment(position: Int): Fragment {
    return fragments[position]
  }
}
