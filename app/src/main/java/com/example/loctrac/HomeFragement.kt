package com.example.loctrac

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragement : Fragment() {

    lateinit var inviteAdapter : InviteAdapter

    private val listContacts: ArrayList<ContactModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FetchContact89" ,"onViewCreated")

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Apoorv",
                "Hno.62 niti khand 2",
                "87%",
                "223"),
            MemberModel
                ("Ayush",
                 "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                 "64%",
                  "210"
            ),
            MemberModel("Anuradha",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "94%",
                "280"),
            MemberModel("Avinash",
                "Gazipur Delhi",
                "77%",
                "350"),
            MemberModel("Arvind",
                "Hno.333 extn1 , Shalimar Garden Delhi-92",
                "74%",
                "215"),
            MemberModel("Asha",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "54%",
                "200"),
            MemberModel("Akshat",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "14%",
                "190"),
            MemberModel("Akriti",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "79%",
                "210"),
            MemberModel("Pramugdha",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "98%",
                "20"),
            MemberModel("ShriKant",
                "Hno.62 Niti Khand-II Indirapuram Ghaziabad UP",
                "100%",
                "200"),

        )
        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter



        Log.d("FetchContact89" , "fetchContacts: start karne wale hai")
        Log.d("FetchContact89", " fetchContacts: Start hogya hai ${listContacts.size}")
        inviteAdapter = InviteAdapter(listContacts)
        fetchDatabaseContacts()

        Log.d("FetchContact89", "End hogya")

        CoroutineScope(Dispatchers.IO).launch{
            Log.d("FetchContact89" , "fetchContacts: Coroutine Start")

            insertDatabaseContacts(fetchContacts())


            Log.d("FetchContact89" , "fetchContact: Coroutine end ${listContacts.size}")
        }


        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter = inviteAdapter

        val threeDots = requireView().findViewById<ImageView>(R.id.icon_three_dots)
        threeDots.setOnClickListener{

            SharedPref.putBoolean(PrefConstant.IS_USER_LOGGED_IN,false)
            FirebaseAuth.getInstance().signOut()
        }

    }

    private fun fetchDatabaseContacts() {
        val database = LocTracDatabase.getDatabase(requireContext())

         database.contactDao().getAllContacts().observe(viewLifecycleOwner){

             listContacts.clear()
             listContacts.addAll(it)

             inviteAdapter.notifyDataSetChanged()
         }

    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = LocTracDatabase.getDatabase(requireContext())

        database.contactDao().insertAll(listContacts)
    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {
        Log.d("FetchContact89", "fetchContacts: start")
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null)
        val listContacts:ArrayList<ContactModel> = ArrayList()

        if (cursor!=null && cursor.count>0){
            while (cursor!=null && cursor.moveToNext()){
                val id =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0){
                    val pCur = cr.query(
                        CommonDataKinds.Phone.CONTENT_URI,
                        null,
                    CommonDataKinds.Phone.CONTACT_ID+" = ?",
                    arrayOf(id),
                        ""
                    )
                    if (pCur!=null && pCur.count>0){
                        while (pCur!=null && pCur.moveToNext()){

                            val phoneNum = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name,phoneNum))
                        }
                        pCur.close()
                    }
                }
            }
            if (cursor!=null){
                cursor.close()
            }
        }
        Log.d("FetchContact89", "fetchContacts: end")
        return listContacts
    }


    companion object {


        @JvmStatic
        fun newInstance() = HomeFragement()

    }
}