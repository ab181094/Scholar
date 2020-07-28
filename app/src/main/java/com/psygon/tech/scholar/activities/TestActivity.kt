package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.TestAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.Content
import com.psygon.tech.scholar.models.ExamModel

class TestActivity : ParentActivity(), ClickResponse {
    private lateinit var testRecyclerView: RecyclerView
    private lateinit var adapter: TestAdapter
    private lateinit var examList: MutableList<ExamModel>

    private lateinit var btnFinish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            val contentString = bundle.getString(CONTENT_KEY)

            val collectionType = object : TypeToken<MutableList<Content>>() {}.type
            val contentList: MutableList<Content> = Gson().fromJson(contentString, collectionType)

            examList = mutableListOf()
            for (content in contentList) {
                val modelExamModel = ExamModel()
                modelExamModel.content = content
                examList.add(modelExamModel)
            }

            examList.shuffle()
        }

        initAll()

        btnFinish.setOnClickListener {
            val hashMap = LinkedHashMap<String, Any>()
            showConfirmDialog(this, END_EXAM_CONFIRMATION_MSG, TEST_CODE, hashMap)
        }
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_test
    }

    override fun initAll() {
        testRecyclerView = findViewById(R.id.testRecyclerView)
        testRecyclerView.setHasFixedSize(true)
        testRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = TestAdapter(this, examList)
        testRecyclerView.adapter = adapter

        btnFinish = findViewById(R.id.btnFinish)

        setTitle(
            this,
            resources.getString(R.string.title_activity_test)
        )

        val contentPath = CONTENT_PATH + "/${currentChapter.key}"
        database = FirebaseDatabase.getInstance().getReference(contentPath)
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            TEST_CODE -> {
                for (examModel in examList) {
                    if (toLowerCase(examModel.content.answer) == toLowerCase(examModel.givenAnswer)) {
                        examModel.isCorrect = true
                    }
                }

                val examString = Gson().toJson(examList)

                val intent = Intent(this@TestActivity, SummaryActivity::class.java)
                intent.putExtra(EXAM_KEY, examString)
                startActivity(intent)
                finish()
            }
        }
    }
}
