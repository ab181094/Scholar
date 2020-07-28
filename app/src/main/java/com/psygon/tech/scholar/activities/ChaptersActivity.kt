package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.ChapterAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Chapter
import kotlinx.android.synthetic.main.activity_chapters.*
import java.util.*
import kotlin.collections.LinkedHashMap

class ChaptersActivity : ParentActivity(), ClickResponse {
    private lateinit var chapterRecyclerView: RecyclerView
    private lateinit var adapter: ChapterAdapter
    private lateinit var chaptersList: MutableList<Chapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chapterOne = p0.getValue(Chapter::class.java)

                val index = getIndex(chapterOne)

                if (index > -1) {
                    chaptersList[index] = chapterOne!!
                    adapter.notifyItemChanged(index)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chapterOne = p0.getValue(Chapter::class.java)

                if (chapterOne != null) {
                    chaptersList.add(chapterOne)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val chapterOne = p0.getValue(Chapter::class.java)
                val index = getIndex(chapterOne)

                if (index > -1) {
                    chaptersList.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, chaptersList.size)
                }
            }
        })

        fab.setOnClickListener {
            val hashMap = LinkedHashMap<String, Any>()
            showSingleInputDialog(this, ENTER_CHAPTER_NAME_MSG, CHAPTER_CODE, hashMap)
        }
    }

    private fun getIndex(chapterOne: Chapter?): Int {
        if (chapterOne != null) {
            for (i in 0 until chaptersList.size) {
                val chapter = chaptersList[i]

                if (chapter.key == chapterOne.key) {
                    return i
                }
            }
        }

        return -1
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_chapters
    }

    override fun initAll() {
        chaptersList = mutableListOf()

        chapterRecyclerView = findViewById(R.id.chapterRecyclerView)
        chapterRecyclerView.setHasFixedSize(true)
        chapterRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = ChapterAdapter(this, chaptersList)
        chapterRecyclerView.adapter = adapter

        setTitle(
            this,
            resources.getString(R.string.title_activity_chapters)
        )

        val chapterPath = CHAPTER_PATH + "/${currentTopic.key}"
        database = FirebaseDatabase.getInstance().getReference(chapterPath)
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            CHAPTER_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val chapter = Chapter()
                    chapter.name = name
                    chapter.flag = true
                    chapter.topicKey = currentTopic.key

                    val date = Calendar.getInstance().time
                    chapter.creationDate = date.toString()

                    if (auth.currentUser != null) {
                        chapter.creatorKey = auth.currentUser!!.uid
                        chapter.creatorName = auth.currentUser!!.displayName.toString()
                    }

                    val chapterPath = CHAPTER_PATH + "/${currentTopic.key}"

                    val database = FirebaseDatabase.getInstance().getReference(chapterPath)
                    chapter.key = database.push().key.toString()

                    database.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChild(chapter.name)) {
                                toast(this@ChaptersActivity, CHAPTER_EXISTS_MSG)
                            } else {
                                database.child(chapter.name).setValue(chapter)

                                toast(this@ChaptersActivity, "$CHAPTER \"$name\" $HAS_BEEN_ADDED")
                            }
                        }
                    })
                }
            }
            CHAPTER_UPDATE_CODE -> {
                val name = hashMap[NAME_KEY].toString()

                if (name.isNotEmpty()) {
                    val chapter = hashMap[CHAPTER] as Chapter

                    val chapterPath = CHAPTER_PATH + "/${currentTopic.key}"

                    val database = FirebaseDatabase.getInstance().getReference(chapterPath)
                    database.child(chapter.name).removeValue()

                    chapter.name = name
                    database.child(chapter.name).setValue(chapter)
                    toast(this@ChaptersActivity, "$CHAPTER \"$name\" $HAS_BEEN_UPDATED")
                }
            }
            CHAPTER_REMOVE_CODE -> {
                val chapterPath = CHAPTER_PATH + "/${currentTopic.key}"

                val chapter = hashMap[CHAPTER] as Chapter
                val database = FirebaseDatabase.getInstance().getReference(chapterPath)
                database.child(chapter.name).removeValue()
            }
        }
    }

    fun showContents(position: Int) {
        currentChapter = chaptersList[position]

        val intent = Intent(this@ChaptersActivity, ContentsActivity::class.java)
        startActivity(intent)
    }

    fun updateChapters(chapter: Chapter) {
        if (chapter.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[CHAPTER] = chapter
            showSingleInputDialog(this, ENTER_CHAPTER_NAME_MSG, CHAPTER_UPDATE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_UPDATE_MSG)
        }
    }

    fun removeChapters(chapter: Chapter) {
        if (chapter.creatorKey == auth.uid) {
            val hashMap = LinkedHashMap<String, Any>()
            hashMap[CHAPTER] = chapter
            showConfirmDialog(this, REMOVE_CONFIRMATION_MSG, CHAPTER_REMOVE_CODE, hashMap)
        } else {
            toast(this, ONLY_CREATOR_CAN_REMOVE_MSG)
        }
    }
}
