package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.ContentAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Content
import kotlinx.android.synthetic.main.activity_contents.*
import java.util.*
import kotlin.collections.LinkedHashMap

class ContentsActivity : ParentActivity(), ClickResponse {
    private var optionCount = 1

    private lateinit var tvTotal: TextView
    private lateinit var contentRecyclerView: RecyclerView
    private lateinit var adapter: ContentAdapter
    private lateinit var contentList: MutableList<Content>

    private lateinit var row1: TableRow
    private lateinit var row2: TableRow

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    private var isOpen: Boolean = false

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

                    updateTotalText()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val contentOne = p0.getValue(Content::class.java)
                val index = getIndex(contentOne)

                if (index > -1) {
                    contentList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, contentList.size)

                    updateTotalText()
                }
            }
        })

        fab.setOnClickListener {
            animateFab()
        }

        fab_add.setOnClickListener {
            val hashMap = LinkedHashMap<String, Any>()
            showContentDialog(this, CONTENT_CODE, hashMap, optionCount)
        }

        fab_test.setOnClickListener {
            val intent = Intent(this@ContentsActivity, TestActivity::class.java)
            intent.putExtra(CONTENT_KEY, Gson().toJson(contentList))
            startActivity(intent)
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
        return R.layout.activity_contents
    }

    override fun initAll() {
        contentList = mutableListOf()

        tvTotal = findViewById(R.id.tvTotal)
        contentRecyclerView = findViewById(R.id.contentRecyclerView)
        contentRecyclerView.setHasFixedSize(true)
        contentRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = ContentAdapter(this, contentList)
        contentRecyclerView.adapter = adapter

        setTitle(this, resources.getString(R.string.title_activity_contents))

        val contentPath = CONTENT_PATH + "/${currentChapter.key}"
        database = FirebaseDatabase.getInstance().getReference(contentPath)

        row1 = findViewById(R.id.row1)
        row2 = findViewById(R.id.row2)

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)

        updateTotalText()
    }

    private fun updateTotalText() {
        val totalString = "$TOTAL_TEXT ${contentList.size}"
        tvTotal.text = totalString
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            CONTENT_CODE -> {
                val content = Content()
                content.chapterKey = currentChapter.key
                content.flag = true
                content.question = hashMap[QUESTION_KEY].toString()
                content.noOfOptions = hashMap[OPTION_COUNT_KEY] as Int
                content.answer = hashMap[ANSWER_KEY].toString()

                if (content.noOfOptions == 2) {
                    content.option1 = hashMap[OPTION_1_KEY].toString()
                    content.option2 = hashMap[OPTION_2_KEY].toString()
                } else if (content.noOfOptions == 4) {
                    content.option1 = hashMap[OPTION_1_KEY].toString()
                    content.option2 = hashMap[OPTION_2_KEY].toString()
                    content.option3 = hashMap[OPTION_3_KEY].toString()
                    content.option4 = hashMap[OPTION_4_KEY].toString()
                }

                val date = Calendar.getInstance().time
                content.creationDate = date.toString()

                if (auth.currentUser != null) {
                    content.creatorKey = auth.currentUser!!.uid
                    content.creatorName = auth.currentUser!!.displayName.toString()
                }

                val contentPath = CONTENT_PATH + "/${currentChapter.key}"

                val database = FirebaseDatabase.getInstance().getReference(contentPath)
                content.key = database.push().key.toString()
                database.child(content.key).setValue(content)

                toast(this, CONTENT_ADDED_SUCCESSFULLY_MSG)
                optionCount = content.noOfOptions
            }
        }
    }

    private fun animateFab() {
        isOpen = if (isOpen) {
            fab.startAnimation(rotateBackward)
            row1.startAnimation(fabClose)
            row2.startAnimation(fabClose)
            false
        } else {
            fab.startAnimation(rotateForward)
            row1.startAnimation(fabOpen)
            row2.startAnimation(fabOpen)
            true
        }
    }

    fun addToMyList(content: Content) {
        val myListPath = "$MY_LIST_PATH/${auth.uid}"
        val database = FirebaseDatabase.getInstance().getReference(myListPath)
        database.child(database.push().key.toString()).setValue(content)

        toast(this, ADDED_TO_YOUR_LIST)
    }

    fun removeContent(content: Content) {
        if (content.creatorKey == auth.uid) {
            val myContentPath = "$CONTENT_PATH/${content.chapterKey}"
            val database = FirebaseDatabase.getInstance().getReference(myContentPath)
            database.child(content.key).removeValue()
        } else {
            toast(this, ONLY_CREATOR_CAN_REMOVE_MSG)
        }
    }
}