package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.SubjectAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Subject
import kotlinx.android.synthetic.main.activity_subjects.*
import java.util.*
import kotlin.collections.LinkedHashMap

class SubjectsActivity : ParentActivity(), ClickResponse {
    private lateinit var subjectRecyclerView: RecyclerView
    private lateinit var adapter: SubjectAdapter
    private lateinit var subjectList: MutableList<Subject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val subjectOne = p0.getValue(Subject::class.java)

                val index = getIndex(subjectOne)

                if (index > -1) {
                    subjectList[index] = subjectOne!!
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val subjectOne = p0.getValue(Subject::class.java)

                if (subjectOne != null) {
                    subjectList.add(subjectOne)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val subjectOne = p0.getValue(Subject::class.java)
                val index = getIndex(subjectOne)

                if (index > -1) {
                    subjectList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, subjectList.size)
                }
            }
        })

        fab.setOnClickListener {
            val hashMap = LinkedHashMap<String, Any>()
            showSingleInputDialog(this, ENTER_SUBJECT_NAME_MSG, SUBJECT_CODE, hashMap)
        }
    }

    private fun getIndex(subjectOne: Subject?): Int {
        if (subjectOne != null) {
            for (i in 0 until subjectList.size) {
                val subject = subjectList[i]

                if (subject.key == subjectOne.key) {
                    return i
                }
            }
        }

        return -1
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            SUBJECT_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val subject = Subject()
                    subject.name = name
                    subject.flag = true

                    val date = Calendar.getInstance().time
                    subject.creationDate = date.toString()

                    if (auth.currentUser != null) {
                        subject.creatorKey = auth.currentUser!!.uid
                        subject.creatorName = auth.currentUser!!.displayName.toString()
                    }

                    val database = FirebaseDatabase.getInstance().getReference(SUBJECT_PATH)
                    subject.key = database.push().key.toString()

                    database.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChild(subject.name)) {
                                toast(this@SubjectsActivity, SUBJECT_EXISTS_MSG)
                            } else {
                                database.child(subject.name).setValue(subject)
                                toast(this@SubjectsActivity, "$SUBJECT \"$name\" $HAS_BEEN_ADDED")
                            }
                        }
                    })
                }
            }

            SUBJECT_UPDATE_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val subject = hashMap[SUBJECT] as Subject

                    val database = FirebaseDatabase.getInstance().getReference(SUBJECT_PATH)
                    database.child(subject.name).removeValue()

                    subject.name = name
                    database.child(subject.name).setValue(subject)
                    toast(this@SubjectsActivity, "$SUBJECT \"$name\" $HAS_BEEN_UPDATED")
                }
            }

            SUBJECT_REMOVE_CODE -> {
                val subject = hashMap[SUBJECT] as Subject

                val database = FirebaseDatabase.getInstance().getReference(SUBJECT_PATH)
                database.child(subject.name).removeValue()
            }
        }
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_subjects
    }

    override fun initAll() {
        subjectList = mutableListOf()

        subjectRecyclerView = findViewById(R.id.subjectRecyclerView)
        subjectRecyclerView.setHasFixedSize(true)
        subjectRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = SubjectAdapter(this, subjectList)
        subjectRecyclerView.adapter = adapter

        setTitle(this, resources.getString(R.string.title_activity_subjects))

        database = FirebaseDatabase.getInstance().getReference(SUBJECT_PATH)
    }

    fun showChapters(position: Int) {
        currentSubject = subjectList[position]

        val intent = Intent(this@SubjectsActivity, TopicsActivity::class.java)
        startActivity(intent)
    }

    fun updateSubject(subject: Subject) {
        if (subject.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[SUBJECT] = subject
            showSingleInputDialog(this, ENTER_SUBJECT_NAME_MSG, SUBJECT_UPDATE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_UPDATE_MSG)
        }
    }

    fun removeSubject(subject: Subject) {
        if (subject.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[SUBJECT] = subject
            showConfirmDialog(this, REMOVE_CONFIRMATION_MSG, SUBJECT_REMOVE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_REMOVE_MSG)
        }
    }
}
