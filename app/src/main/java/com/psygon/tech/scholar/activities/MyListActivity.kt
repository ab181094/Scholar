package com.psygon.tech.scholar.activities

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.MyListAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Content
import com.psygon.tech.scholar.models.Test
import java.util.*
import kotlin.collections.LinkedHashMap

class MyListActivity : ParentActivity(), ClickResponse {
    private lateinit var tvTotal: TextView
    private lateinit var myListRecyclerView: RecyclerView
    private lateinit var adapter: MyListAdapter
    private lateinit var contentList: MutableList<Content>

    private lateinit var btnCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val contentOne = p0.getValue(Content::class.java)

                val index = getIndex(contentOne)

                if (index > -1) {
                    contentList[index] = contentOne!!
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val contentOne = p0.getValue(Content::class.java)

                if (contentOne != null) {
                    contentList.add(contentOne)
                    adapter.notifyDataSetChanged()

                    val totalString = "$TOTAL_TEXT ${contentList.size}"
                    tvTotal.text = totalString
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val contentOne = p0.getValue(Content::class.java)
                val index = getIndex(contentOne)

                if (index > -1) {
                    contentList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, contentList.size)
                }
            }
        })

        btnCreate.setOnClickListener {
            if (contentList.isNotEmpty()) {
                showConfirmDialog(
                    this,
                    CREATE_TEST_CONFIRMATION_MSG,
                    TEST_CONFIRM_CODE,
                    LinkedHashMap()
                )
            } else {
                showMessageDialog(this, CONTENT_ADD_WARNING_MSG)
            }
        }
    }

    private fun getIndex(contentOne: Content?): Int {
        if (contentOne != null) {
            for (i in 0 until contentList.size) {
                val content = contentList[i]

                if (content.key == contentOne.key) {
                    return i
                }
            }
        }

        return -1
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_my_list
    }

    override fun initAll() {
        contentList = mutableListOf()

        tvTotal = findViewById(R.id.tvTotal)
        myListRecyclerView = findViewById(R.id.myListRecyclerView)
        myListRecyclerView.setHasFixedSize(true)
        myListRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = MyListAdapter(this, contentList)
        myListRecyclerView.adapter = adapter

        setTitle(
            this,
            resources.getString(R.string.title_activity_contents)
        )

        val contentPath = MY_LIST_PATH + "/${auth.uid}"
        database = FirebaseDatabase.getInstance().getReference(contentPath)

        val totalString = "$TOTAL_TEXT ${contentList.size}"
        tvTotal.text = totalString

        btnCreate = findViewById(R.id.btnCreate)
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            TEST_CONFIRM_CODE -> {
                showSingleInputDialog(
                    this,
                    ENTER_TEST_NAME_MSG,
                    TEST_CREATE_CODE,
                    LinkedHashMap()
                )
            }

            TEST_CREATE_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val test = Test()
                    test.name = name
                    test.flag = true
                    test.contentList = contentList

                    val date = Calendar.getInstance().time
                    test.creationDate = date.toString()

                    if (auth.currentUser != null) {
                        test.creatorKey = auth.currentUser!!.uid
                        test.creatorName = auth.currentUser!!.displayName.toString()
                    }

                    val database = FirebaseDatabase.getInstance().getReference(TEST_PATH)
                    test.key = database.push().key.toString()
                    database.child(test.key).setValue(test)

                    toast(this, "$TEST \"$name\" $HAS_BEEN_ADDED")

                    val contentPath = MY_LIST_PATH + "/${auth.uid}"
                    val databaseSecond = FirebaseDatabase.getInstance().getReference(contentPath)
                    databaseSecond.removeValue()

                    Handler().postDelayed({
                        finish()

                    }, 500)
                }
            }
        }
    }
}
