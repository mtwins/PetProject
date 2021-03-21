package com.mheredia.petproject.ui.petInfo.vaccine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.mheredia.petproject.R


class VaccineHomeActivity : AppCompatActivity() {
    val test=""
    var petId=""
    companion object {
        fun newIntent(context: Context, petId: String): Intent {
            return Intent(context, VaccineHomeActivity::class.java).apply {
                putExtra("petId", petId)
            }
        }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccine)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        setSupportActionBar(toolbar)

        petId = intent.getStringExtra("petId").toString()
//        val fab = findViewById<FloatingActionButton>(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, ViewVaccineFragment(petId), "FirstFragment")
        transaction.addToBackStack(null)
        transaction.commit()


    }
}