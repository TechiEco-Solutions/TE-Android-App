package com.example.bot2

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bot2.adapters.DrawerAdapter
import com.example.bot2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        windowSetup()
        drawerSetup()


    }

    fun drawerSetup(){


        val drawerItems = listOf(
            DrawerItem("Tubby Do" , R.drawable.ic_tubby_do),
            DrawerItem("Empty Tubby", R.drawable.ic_empty_tubby),
            DrawerItem("Add Device", R.drawable.ic_add_device),
            DrawerItem("Preferences", R.drawable.ic_prefrences),
        )


        val drawerAdapter = DrawerAdapter(drawerItems)
        binding.drawerRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = drawerAdapter
        }

        //when an drawer item is clicked
        drawerAdapter.onClick = {itemPosition ->
            Toast.makeText(this, "Clicked on ${drawerItems[itemPosition].title}", Toast.LENGTH_SHORT).show()
        }


        binding.drawerIconBtn.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val headerView: View = binding.navigationView.getHeaderView(0)
        val closeButton: ImageButton = headerView.findViewById(R.id.ic_drawer_close)


        closeButton.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }


    }

    fun windowSetup(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }


}

