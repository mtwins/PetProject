package com.mheredia.petproject

import android.app.AlarmManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mheredia.petproject.ui.contacts.ContactsFragment
import com.mheredia.petproject.ui.login.LoginActivity
import com.mheredia.petproject.ui.reminders.ReminderFragment
import com.mheredia.petproject.ui.utils.NotificationUtils
import java.util.*


class MainActivity() : AppCompatActivity(), ContactsFragment.ContactInterface, ReminderFragment.ReminderInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var profileImage: ImageView
    val profileStorageRef = storage.reference

    companion object {
        fun route(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        }
        val storage = Firebase.storage

        val REQUEST_IMAGE_CAPTURE = 1
        val PICTURE_REQUEST_CODE = 500


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        //notificaiton
        NotificationUtils().createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_pet_info, R.id.nav_gallery, R.id.nav_contact, R.id.nav_reminders, R.id.logout), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.bringToFront()
        val logoutItem: MenuItem = navView.getMenu().findItem(R.id.logout)
        setLogout(logoutItem)
    }

    private fun setImageSelection(profileImage: ImageView) {
        profileImage.setOnClickListener {
             try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                startActivityForResult(intent, PICTURE_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) { }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
                var selectedImage: Uri? = data?.getData();
                if (selectedImage != null) {
                    val userId = firebaseAuth.currentUser?.uid.toString()
                    storage.reference.child("profile/$userId").putFile(selectedImage).addOnSuccessListener {
                        loadImageIntoImageView("profile/$userId")

                    }
                }
            }
    }

    private fun setLogout(logoutItem: MenuItem) {
        logoutItem.setOnMenuItemClickListener { menuItem ->
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            true
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
        setUpHeader()
        return true
    }

    private fun setUpHeader() {
        val headerTitle: TextView = findViewById(R.id.nav_header_title)
        val headerSubtitle: TextView = findViewById(R.id.nav_header_subtitle)
        profileImage = findViewById(R.id.profile_image)
        val profileImageDownloadUrl = "profile/${firebaseAuth.currentUser?.uid.toString()}"
        loadImageIntoImageView(profileImageDownloadUrl)
        setImageSelection(profileImage)
        headerTitle.text = firebaseAuth.currentUser?.displayName
        headerSubtitle.text = firebaseAuth.currentUser?.email
    }

    private fun loadImageIntoImageView(profileImageDownloadUrl: String) {
        GlideApp.with(this)
            .load(storage.reference.child(profileImageDownloadUrl))
            .error(
                Glide.with(this)
                    .load(getDrawable(R.mipmap.ic_launcher_round))
            )

            .into(profileImage);
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun openEmail(email: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.popBackStack()
    }

    override fun openPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun sendReminder(message: String, id: String, calendar: Calendar) {
        val alarmManager =  getSystemService(Context.ALARM_SERVICE) as AlarmManager
        NotificationUtils().setNotification(this,calendar,message,id, alarmManager)
    }
}