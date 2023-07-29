package com.example.loctrac

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_CONTACTS
    )

    val permissionCode = 78
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermission()

        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener { menuItem ->

            if (menuItem.itemId==R.id.nav_guard){
                inflateFragment(GuardFragment.newInstance())
            }
            else if(menuItem.itemId==R.id.nav_home){
                inflateFragment(HomeFragement.newInstance())
            }
            else if(menuItem.itemId==R.id.nav_dashboard){
                inflateFragment(MapsFragment())
            }
            else if(menuItem.itemId==R.id.nav_profile) {
                inflateFragment(ProfileFragment.newInstance())
            }



            true
        }

        bottomBar.selectedItemId = R.id.nav_home


        val currentUser = FirebaseAuth.getInstance().currentUser
        val name = currentUser?.displayName.toString()
        val mail = currentUser?.email.toString()
        val phoneNumber = currentUser?.phoneNumber.toString()
        val imageUrl = currentUser?.photoUrl.toString()

        val db = Firebase.firestore
        //Create a new user with a first and last name

        val user = hashMapOf(
            "first_name" to name,
            "mail" to mail,
            "phoneNumber" to phoneNumber,
            "imageUrl" to imageUrl


        )

        db.collection("users").document(mail).set(user).addOnSuccessListener {

        }
            .addOnFailureListener {

            }



    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this,permissions,permissionCode)


    }


    private fun inflateFragment(newInstance: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if(requestCode == permissionCode){
            if (allPermissionGranted()){
                openCamera()
            }else{

            }
        }
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {
        for (item in permissions){
            if (ContextCompat.checkSelfPermission(this,item) == PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true

    }
}