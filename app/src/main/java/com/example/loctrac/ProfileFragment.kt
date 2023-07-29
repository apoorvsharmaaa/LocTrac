package com.example.loctrac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signOut = requireView().findViewById<Button>(R.id.signOut)
        signOut.setOnClickListener{

            SharedPref.putBoolean(PrefConstant.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
        }


    }



    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}