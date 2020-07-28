package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.TopicAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Topic
import kotlinx.android.synthetic.main.activity_topics.*
import java.util.*
import kotlin.collections.LinkedHashMap

class TopicsActivity : ParentActivity(), ClickResponse {
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var adapter: TopicAdapter
    private lateinit var topicsList: MutableList<Topic>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val topicOne = p0.getValue(Topic::class.java)

                val index = getIndex(topicOne)

                if (index > -1) {
                    topicsList[index] = topicOne!!
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val topicOne = p0.getValue(Topic::class.java)

                if (topicOne != null) {
                    topicsList.add(topicOne)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val topicOne = p0.getValue(Topic::class.java)
                val index = getIndex(topicOne)

                if (index > -1) {
                    topicsList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, topicsList.size)
                }
            }
        })

        fab.setOnClickListener {
            val hashMap = LinkedHashMap<String, Any>()
            showSingleInputDialog(this, ENTER_TOPIC_NAME_MSG, TOPIC_CODE, hashMap)
        }
    }

    private fun getIndex(topicOne: Topic?): Int {
        if (topicOne != null) {
            for (i in 0 until topicsList.size) {
                val topic = topicsList[i]

                if (topic.key == topicOne.key) {
                    return i
                }
            }
        }

        return -1
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_topics
    }

    override fun initAll() {
        topicsList = mutableListOf()

        topicRecyclerView = findViewById(R.id.topicsRecyclerView)
        topicRecyclerView.setHasFixedSize(true)
        topicRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = TopicAdapter(this, topicsList)
        topicRecyclerView.adapter = adapter

        setTitle(this, resources.getString(R.string.title_activity_topics))

        val topicPath = TOPIC_PATH + "/${currentSubject.key}"
        database = FirebaseDatabase.getInstance().getReference(topicPath)
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            TOPIC_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val topic = Topic()
                    topic.name = name
                    topic.flag = true
                    topic.subjectKey = currentSubject.key

                    val date = Calendar.getInstance().time
                    topic.creationDate = date.toString()

                    if (auth.currentUser != null) {
                        topic.creatorKey = auth.currentUser!!.uid
                        topic.creatorName = auth.currentUser!!.displayName.toString()
                    }

                    val topicPath = TOPIC_PATH + "/${currentSubject.key}"

                    val database = FirebaseDatabase.getInstance().getReference(topicPath)
                    topic.key = database.push().key.toString()

                    database.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChild(topic.name)) {
                                toast(this@TopicsActivity, TOPIC_EXISTS_MSG)
                            } else {
                                database.child(topic.name).setValue(topic)
                                toast(this@TopicsActivity, "$TOPIC \"$name\" $HAS_BEEN_ADDED")
                            }
                        }
                    })
                }
            }

            TOPIC_UPDATE_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val topic = hashMap[TOPIC] as Topic

                    val topicPath = TOPIC_PATH + "/${currentSubject.key}"

                    val database = FirebaseDatabase.getInstance().getReference(topicPath)
                    database.child(topic.name).removeValue()

                    topic.name = name
                    database.child(topic.name).setValue(topic)
                    toast(this@TopicsActivity, "$TOPIC \"$name\" $HAS_BEEN_UPDATED")
                }
            }

            TOPIC_REMOVE_CODE -> {
                val topicPath = TOPIC_PATH + "/${currentSubject.key}"

                val topic = hashMap[TOPIC] as Topic
                val database = FirebaseDatabase.getInstance().getReference(topicPath)
                database.child(topic.name).removeValue()
            }
        }
    }

    fun showChapters(position: Int) {
        currentTopic = topicsList[position]

        val intent = Intent(this@TopicsActivity, ChaptersActivity::class.java)
        startActivity(intent)
    }

    fun updateTopics(topic: Topic) {
        if (topic.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[TOPIC] = topic
            showSingleInputDialog(this, ENTER_TOPIC_NAME_MSG, TOPIC_UPDATE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_UPDATE_MSG)
        }
    }

    fun removeTopics(topic: Topic) {
        if (topic.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[TOPIC] = topic
            showConfirmDialog(this, REMOVE_CONFIRMATION_MSG, TOPIC_REMOVE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_REMOVE_MSG)
        }
    }
}
