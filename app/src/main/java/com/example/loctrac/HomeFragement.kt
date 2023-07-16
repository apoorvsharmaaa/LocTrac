package com.example.loctrac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragement : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel("Apoorv"),
            MemberModel("Ayush"),
            MemberModel("Anuradha"),
            MemberModel("Avinash"),
            MemberModel("Arvind"),
            MemberModel("Asha"),
            MemberModel("Akshat"),
            MemberModel("Akriti"),
            MemberModel("Pramugdha"),
            MemberModel("ShriKant")

        )
        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
    }



    companion object {


        @JvmStatic
        fun newInstance() = HomeFragement()

    }
}