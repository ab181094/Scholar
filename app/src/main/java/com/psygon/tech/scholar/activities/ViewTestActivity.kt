package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.ViewTestAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Test

class ViewTestActivity : ParentActivity(), ClickResponse {
    private lateinit var testRecyclerView: RecyclerView
    private lateinit var adapter: ViewTestAdapter
    private lateinit var testList: MutableList<Test>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val testOne = p0.getValue(Test::class.java)

                val index = getIndex(testOne)

                if (index > -1) {
                    testList[index] = testOne!!
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val testOne = p0.getValue(Test::class.java)

                if (testOne != null) {
                    testList.add(testOne)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val testOne = p0.getValue(Test::class.java)
                val index = getIndex(testOne)

                if (index > -1) {
                    testList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, testList.size)
                }
            }
        })
    }

    private fun getIndex(testOne: Test?): Int {
        if (testOne != null) {
            for (i in 0 until testList.size) {
                val subject = testList[i]

                if (subject.key == testOne.key) {
                    return i
                }
            }
        }

        return -1
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_view_test
    }

    override fun initAll() {
        testList = mutableListOf()

        testRecyclerView = findViewById(R.id.testRecyclerView)
        testRecyclerView.setHasFixedSize(true)
        testRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = ViewTestAdapter(this, testList)
        testRecyclerView.adapter = adapter

        setTitle(
            this,
            resources.getString(R.string.title_activity_test)
        )

        database = FirebaseDatabase.getInstance().getReference(TEST_PATH)
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            TEST_UPDATE_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val test = hashMap[TEST] as Test
                    test.name = name

                    val database = FirebaseDatabase.getInstance().getReference(TEST_PATH)
                    database.child(test.key).setValue(test)
                    toast(this@ViewTestActivity, "$TEST \"$name\" $HAS_BEEN_UPDATED")
                }
            }
            TEST_REMOVE_CODE -> {
                val test = hashMap[TEST] as Test

                val database = FirebaseDatabase.getInstance().getReference(TEST_PATH)
                database.child(test.key).removeValue()
            }
        }
    }

    fun updateTest(test: Test) {
        if (test.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[TEST] = test
            showSingleInputDialog(this, ENTER_TEST_NAME_MSG, TEST_UPDATE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_UPDATE_MSG)
        }
    }

    fun removeTest(test: Test) {
        if (test.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[TEST] = test
            showConfirmDialog(this, REMOVE_CONFIRMATION_MSG, TEST_REMOVE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_REMOVE_MSG)
        }
    }

    fun showTest(test: Test) {
        val intent = Intent(this@ViewTestActivity, OnlyTestActivity::class.java)
        intent.putExtra(TEST_KEY, Gson().toJson(test))
        startActivity(intent)
    }
}
