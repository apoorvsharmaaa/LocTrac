package com.example.loctrac

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.loctrac.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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

        if (isAllPermissionsGranted()){
            if (isLocationEnabled(this)){
                setUpLocationListener()
            }else{
                showGPSNotEnabledDialog(this)
            }
        }else {
            askForPermission()
        }

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

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = com.google.android.gms.location.LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        Log.d("Location89", "onLocationResult: latitude ${location.latitude}")
                        Log.d("Location89", "onLocationResult: longitude ${location.longitude}")

                        val currentUser = FirebaseAuth.getInstance().currentUser

                        val mail = currentUser?.email.toString()

                        val db = Firebase.firestore
                        //Create a new user with a first and last name

                        val locationData = mutableMapOf<String,Any>(
                           "lat" to location.latitude.toString(),
                            "long" to location.longitude.toString()

                        )

                        db.collection("users").document(mail).update(locationData).addOnSuccessListener {

                        }
                            .addOnFailureListener {

                            }


                    }
                }
            },
            Looper.myLooper()
        )

    }

    //    * Function to check if location of the device is enabled or not
//    */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Enable GPS")
            .setMessage("Required for this App")
            .setCancelable(false)
            .setPositiveButton("Enable now") { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    fun isAllPermissionsGranted(): Boolean {

        for (item in permissions) {

            if ( ContextCompat
                .checkSelfPermission(
                    this,
                    item
                ) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
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
//                openCamera()
                setUpLocationListener()
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