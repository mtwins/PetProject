package com.mheredia.petproject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.mheredia.petproject.data.model.Contact
import com.mheredia.petproject.ui.contacts.AddEditContactsFragment
import com.mheredia.petproject.ui.contacts.ContactsFragment

class MainActivity : AppCompatActivity(), ContactsFragment.ContactInterface, AddEditContactsFragment.AddEditContactInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val addEditFragment = AddEditContactsFragment.newInstance()

    companion object {
        fun route(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_pet_info, R.id.nav_gallery, R.id.nav_contact), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.bringToFront()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun addContactButtonClicked() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        getBundle(Contact())
        fragmentTransaction.add(R.id.nav_host_fragment, addEditFragment)
        fragmentTransaction.commit()
    }

    override fun openEmail(email: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    override fun openPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun editContactButtonClicked(contact: Contact) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val bundle = getBundle(contact)
        addEditFragment.setArguments(bundle)
        fragmentTransaction.add(R.id.nav_host_fragment, addEditFragment)
        fragmentTransaction.commit()
    }

    private fun getBundle(contact: Contact): Bundle {
        val bundle = Bundle()
        bundle.putString("name", contact.name)
        bundle.putString("email", contact.email)
        bundle.putString("phone", contact.phone)
        bundle.putString("notes", contact.notes)
        bundle.putString("userId", contact.userId)
        bundle.putString("contactId", contact.contactId)
        return bundle
    }

    override fun goToContacts() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove( addEditFragment)
        fragmentTransaction.commit()

    }

}