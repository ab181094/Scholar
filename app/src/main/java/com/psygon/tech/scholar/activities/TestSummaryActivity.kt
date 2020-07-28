package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.SummaryAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.models.ExamModel
import com.psygon.tech.scholar.models.Test
import kotlinx.android.synthetic.main.activity_summary.*

class TestSummaryActivity : ParentActivity() {
    private lateinit var summaryRecyclerView: RecyclerView
    private lateinit var adapter: SummaryAdapter
    private lateinit var examList: MutableList<ExamModel>

    private lateinit var tvSummary: TextView
    private lateinit var test: Test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {
            examList = mutableListOf()

            val examString = bundle.getString(EXAM_KEY)
            val testString = bundle.getString(TEST_KEY)

            val collectionType2 = object : TypeToken<Test>() {}.type
            test = Gson().fromJson(testString, collectionType2)

            val collectionType = object : TypeToken<MutableList<ExamModel>>() {}.type
            examList = Gson().fromJson(examString, collectionType)
        }

        initAll()

        var correctCount = 0
        for (examModel in examList) {
            if (examModel.isCorrect)
                correctCount++
        }

        var summaryString = "$YOU $HAVE_ANSWERED $correctCount $QUESTION_KEY"
        if (correctCount > 1) {
            summaryString += "s"
        }
        summaryString += " $CORRECTLY_OUT_OF ${examList.size}"
        tvSummary.text = summaryString

        storeSummary(auth, test.key, test.name, TEST, correctCount, examList.size)

        fab.setOnClickListener {
            var shareString = "$I $HAVE_ANSWERED $correctCount $QUESTION_KEY"
            if (correctCount > 1) {
                shareString += "s"
            }
            shareString += " $CORRECTLY_OUT_OF ${examList.size} $IN $TEST_KEY \"${test.name}\""

            shareString += ".$DOWNLOAD_APP_TEXT"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, TEST_RESULT)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareString)
            startActivity(Intent.createChooser(shareIntent, SHARE_USING))
        }
    }

    override fun initAll() {
        summaryRecyclerView = findViewById(R.id.summaryRecyclerView)
        summaryRecyclerView.setHasFixedSize(true)
        summaryRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = SummaryAdapter(this, examList)
        summaryRecyclerView.adapter = adapter

        tvSummary = findViewById(R.id.tvSummary)

        setTitle(
            this,
            resources.getString(R.string.title_activity_summary)
        )
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_test_summary
    }
}
