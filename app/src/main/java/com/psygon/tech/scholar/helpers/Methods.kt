package com.psygon.tech.scholar.helpers

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.media.RingtoneManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import com.psygon.tech.scholar.MainActivity
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.fragments.TitleFragment
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

fun log(message: Any?) {
    Log.d(LOG_TEXT, message.toString())
}

fun jsonLog(obj: Any?) {
    log(GsonBuilder().setPrettyPrinting().create().toJson(obj))
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun snackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}

fun showProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}

fun hideProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.INVISIBLE
}

fun showMessageDialog(activity: Activity, message: String) {
    val builder = AlertDialog.Builder(activity)
    val inputView = View.inflate(activity, R.layout.custom_message_one, null)

    val tvMessage = inputView.findViewById<TextView>(R.id.tvMessage)
    tvMessage.text = message

    builder.setView(inputView)
    builder.setCancelable(true)

    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun showUpdateAppDialog(activity: Activity, message: String) {
    val builder = AlertDialog.Builder(activity)
    val inputView = View.inflate(activity, R.layout.custom_message_one, null)

    val tvMessage = inputView.findViewById<TextView>(R.id.tvMessage)
    tvMessage.text = message

    builder.setView(inputView)
    builder.setCancelable(false)

    builder.setPositiveButton(
        OK_TEXT
    ) { dialog, which ->
        dialog.dismiss()
        activity.finish()
    }

    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun setTitle(activity: AppCompatActivity, title: String) {
    val fragment = activity.supportFragmentManager.findFragmentById(R.id.fragmentTitle)
    if (fragment is TitleFragment) {
        fragment.setTitle(title)
    }
}

fun showContentDialog(
    activity: Activity,
    responseCode: String,
    hashMap: LinkedHashMap<String, Any>,
    optionCount: Int
) {
    var alertDialog: AlertDialog? = null
    val builder = AlertDialog.Builder(activity)
    val inputView = View.inflate(activity, R.layout.custom_add_content, null)

    val etQuestion = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etQuestion)
    val radioGroupSelection = inputView.findViewById<RadioGroup>(R.id.optionSelection)
    val radioButtonOne = inputView.findViewById<RadioButton>(R.id.radioButtonOne)
    val radioButtonTwo = inputView.findViewById<RadioButton>(R.id.radioButtonTwo)
    val radioButtonFour = inputView.findViewById<RadioButton>(R.id.radioButtonFour)
    val textLayout = inputView.findViewById<TextInputLayout>(R.id.textLayout)
    val etAnswer = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etAnswer)
    val layoutTwo = inputView.findViewById<LinearLayout>(R.id.layoutTwo)
    val checkBoxTwoOne = inputView.findViewById<CheckBox>(R.id.checkBoxTwoOne)
    val checkBoxTwoTwo = inputView.findViewById<CheckBox>(R.id.checkBoxTwoTwo)
    val etOptionTwoOne = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionTwoOne)
    val etOptionTwoTwo = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionTwoTwo)
    val layoutFour = inputView.findViewById<LinearLayout>(R.id.layoutFour)
    val checkBoxFourOne = inputView.findViewById<CheckBox>(R.id.checkBoxFourOne)
    val checkBoxFourTwo = inputView.findViewById<CheckBox>(R.id.checkBoxFourTwo)
    val checkBoxFourThree = inputView.findViewById<CheckBox>(R.id.checkBoxFourThree)
    val checkBoxFourFour = inputView.findViewById<CheckBox>(R.id.checkBoxFourFour)
    val etOptionFourOne = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionFourOne)
    val etOptionFourTwo = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionFourTwo)
    val etOptionFourThree =
        inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionFourThree)
    val etOptionFourFour = inputView.findViewById<MultiAutoCompleteTextView>(R.id.etOptionFourFour)
    val btnCancel = inputView.findViewById<Button>(R.id.btnCancel)
    val btnSave = inputView.findViewById<Button>(R.id.btnSave)

    ArrayAdapter<String>(
        activity,
        android.R.layout.simple_list_item_1,
        suggestionList
    ).also { adapter ->
        etQuestion.setAdapter(adapter)
        etQuestion.setTokenizer(SpaceTokenizer())

        etAnswer.setAdapter(adapter)
        etAnswer.setTokenizer(SpaceTokenizer())

        etOptionTwoOne.setAdapter(adapter)
        etOptionTwoOne.setTokenizer(SpaceTokenizer())

        etOptionTwoTwo.setAdapter(adapter)
        etOptionTwoTwo.setTokenizer(SpaceTokenizer())

        etOptionFourOne.setAdapter(adapter)
        etOptionFourOne.setTokenizer(SpaceTokenizer())

        etOptionFourTwo.setAdapter(adapter)
        etOptionFourTwo.setTokenizer(SpaceTokenizer())

        etOptionFourThree.setAdapter(adapter)
        etOptionFourThree.setTokenizer(SpaceTokenizer())

        etOptionFourFour.setAdapter(adapter)
        etOptionFourFour.setTokenizer(SpaceTokenizer())
    }

    radioGroupSelection.setOnCheckedChangeListener { group, checkedId ->
        when (checkedId) {
            radioButtonOne.id -> {
                textLayout.visibility = View.VISIBLE
                layoutTwo.visibility = View.GONE
                layoutFour.visibility = View.GONE
            }
            radioButtonTwo.id -> {
                textLayout.visibility = View.GONE
                layoutTwo.visibility = View.VISIBLE
                layoutFour.visibility = View.GONE
            }
            else -> {
                textLayout.visibility = View.GONE
                layoutTwo.visibility = View.GONE
                layoutFour.visibility = View.VISIBLE
            }
        }
    }

    when (optionCount) {
        1 -> radioButtonOne.isChecked = true
        2 -> radioButtonTwo.isChecked = true
        else -> radioButtonFour.isChecked = true
    }

    val clickResponse = activity as ClickResponse

    btnCancel.setOnClickListener {
        alertDialog?.dismiss()
    }

    btnSave.setOnClickListener {
        val questionString = etQuestion.text.toString().trim()

        var isValid = false
        if (questionString.isEmpty()) {
            setError(etQuestion)
        } else {
            hashMap[QUESTION_KEY] = questionString

            if (radioButtonOne.isChecked) {
                hashMap[OPTION_COUNT_KEY] = 1
                val answerString = etAnswer.text.toString().trim()

                if (answerString.isEmpty()) {
                    setError(etAnswer)
                } else {
                    hashMap[ANSWER_KEY] = answerString
                    isValid = true
                }
            } else if (radioButtonTwo.isChecked) {
                hashMap[OPTION_COUNT_KEY] = 2

                val option1String = etOptionTwoOne.text.toString().trim()
                val option2String = etOptionTwoTwo.text.toString().trim()

                if (option1String.isEmpty()) {
                    setError(etOptionTwoOne)
                } else if (option2String.isEmpty()) {
                    setError(etOptionTwoTwo)
                } else {
                    hashMap[OPTION_1_KEY] = option1String
                    hashMap[OPTION_2_KEY] = option2String

                    var answerString = ""

                    if (checkBoxTwoOne.isChecked) {
                        answerString += option1String
                    }

                    if (checkBoxTwoTwo.isChecked) {
                        answerString += if (answerString.isEmpty()) {
                            option2String
                        } else {
                            ";$option1String"
                        }
                    }

                    if (answerString.isEmpty()) {
                        setError(etOptionTwoOne, CHECK_CORRECT_ANS_MSG)
                        setError(etOptionTwoTwo, CHECK_CORRECT_ANS_MSG)
                    } else {
                        hashMap[ANSWER_KEY] = answerString
                        isValid = true
                    }
                }
            } else {
                hashMap[OPTION_COUNT_KEY] = 4

                val option1String = etOptionFourOne.text.toString().trim()
                val option2String = etOptionFourTwo.text.toString().trim()
                val option3String = etOptionFourThree.text.toString().trim()
                val option4String = etOptionFourFour.text.toString().trim()

                if (option1String.isEmpty()) {
                    setError(etOptionFourOne)
                } else if (option2String.isEmpty()) {
                    setError(etOptionFourTwo)
                } else if (option3String.isEmpty()) {
                    setError(etOptionFourThree)
                } else if (option4String.isEmpty()) {
                    setError(etOptionFourFour)
                } else {
                    hashMap[OPTION_1_KEY] = option1String
                    hashMap[OPTION_2_KEY] = option2String
                    hashMap[OPTION_3_KEY] = option3String
                    hashMap[OPTION_4_KEY] = option4String

                    var answerString = ""

                    if (checkBoxFourOne.isChecked) {
                        answerString += option1String
                    }

                    if (checkBoxFourTwo.isChecked) {
                        answerString += if (answerString.isEmpty()) {
                            option2String
                        } else {
                            ";$option2String"
                        }
                    }

                    if (checkBoxFourThree.isChecked) {
                        answerString += if (answerString.isEmpty()) {
                            option3String
                        } else {
                            ";$option3String"
                        }
                    }

                    if (checkBoxFourFour.isChecked) {
                        answerString += if (answerString.isEmpty()) {
                            option4String
                        } else {
                            ";$option4String"
                        }
                    }

                    if (answerString.isEmpty()) {
                        setError(etOptionFourOne, CHECK_CORRECT_ANS_MSG)
                        setError(etOptionFourTwo, CHECK_CORRECT_ANS_MSG)
                        setError(etOptionFourThree, CHECK_CORRECT_ANS_MSG)
                        setError(etOptionFourFour, CHECK_CORRECT_ANS_MSG)
                    } else {
                        hashMap[ANSWER_KEY] = answerString
                        isValid = true
                    }
                }
            }
        }

        if (isValid) {
            addTextToSuggestion(questionString)
            when (hashMap[OPTION_COUNT_KEY] as Int) {
                1 -> {
                    val optionString = hashMap[ANSWER_KEY].toString()
                    addTextToSuggestion(optionString)
                }
                2 -> {
                    val optionString1 = hashMap[OPTION_1_KEY].toString()
                    val optionString2 = hashMap[OPTION_2_KEY].toString()
                    addTextToSuggestion(optionString1)
                    addTextToSuggestion(optionString2)
                }
                4 -> {
                    val optionString1 = hashMap[OPTION_1_KEY].toString()
                    val optionString2 = hashMap[OPTION_2_KEY].toString()
                    val optionString3 = hashMap[OPTION_3_KEY].toString()
                    val optionString4 = hashMap[OPTION_4_KEY].toString()
                    addTextToSuggestion(optionString1)
                    addTextToSuggestion(optionString2)
                    addTextToSuggestion(optionString3)
                    addTextToSuggestion(optionString4)
                }
            }

            alertDialog?.dismiss()
            clickResponse.onYesClick(responseCode, hashMap)
        } else {
            alertDialog?.show()
        }
    }

    builder.setView(inputView)
    builder.setCancelable(false)
    alertDialog = builder.create()
    alertDialog.show()
}

fun addTextToSuggestion(string: String) {
    val parts = string.split(" ")

    for (part in parts) {
        if (!suggestionList.contains(part)) {
            suggestionList.add(part)
        }
    }
}

fun setError(editText: EditText) {
    editText.error = FILL_THIS_FIELD_MSG
    editText.requestFocus()
}

fun setError(editText: EditText, message: String) {
    editText.error = message
    editText.requestFocus()
}

fun showSingleInputDialog(
    activity: Activity,
    hint: String,
    responseCode: String,
    hashMap: LinkedHashMap<String, Any>
) {
    var alertDialog: AlertDialog? = null
    val builder = AlertDialog.Builder(activity)
    val inputView = View.inflate(activity, R.layout.custom_input_one, null)
    val etName = inputView.findViewById<EditText>(R.id.etName)

    etName.hint = hint
    val clickResponse = activity as ClickResponse

    when {
        hashMap.containsKey(SUBJECT) -> {
            val subject = hashMap[SUBJECT] as Subject
            etName.setText(subject.name)
            etName.setSelection(subject.name.length)
        }
        hashMap.containsKey(TOPIC) -> {
            val topic = hashMap[TOPIC] as Topic
            etName.setText(topic.name)
            etName.setSelection(topic.name.length)
        }
        hashMap.containsKey(CHAPTER) -> {
            val chapter = hashMap[CHAPTER] as Chapter
            etName.setText(chapter.name)
            etName.setSelection(chapter.name.length)
        }
        hashMap.containsKey(TEST) -> {
            val test = hashMap[TEST] as Test
            etName.setText(test.name)
            etName.setSelection(test.name.length)
        }
    }

    builder.setPositiveButton(
        SAVE_TEXT
    ) { _, _ ->
        val nameString = etName.text.toString().trim()
        hashMap[NAME_KEY] = nameString

        alertDialog?.dismiss()
        clickResponse.onYesClick(responseCode, hashMap)
    }

    builder.setNegativeButton(
        CANCEL_TEXT
    ) { _, _ ->
        alertDialog?.dismiss()
    }

    builder.setView(inputView)
    builder.setCancelable(true)
    alertDialog = builder.create()
    alertDialog.show()
}

fun toUppercase(string: String): String {
    return string.toUpperCase(Locale.getDefault())
}

fun toLowerCase(string: String): String {
    return string.toLowerCase(Locale.getDefault())
}

fun getScreenWidth(activity: Activity): Int {
    val display = activity.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

fun showConfirmDialog(
    activity: Activity,
    message: String,
    responseCode: String,
    hashMap: LinkedHashMap<String, Any>
) {
    val clickResponse = activity as ClickResponse

    var alertDialog: AlertDialog? = null
    val builder = AlertDialog.Builder(activity)
    val inputView = View.inflate(activity, R.layout.custom_message_one, null)

    val tvMessage = inputView.findViewById<TextView>(R.id.tvMessage)
    tvMessage.text = message

    builder.setPositiveButton(
        YES_TEXT
    ) { _, _ ->
        alertDialog?.dismiss()
        clickResponse.onYesClick(responseCode, hashMap)
    }

    builder.setNegativeButton(
        NO_TEXT
    ) { _, _ ->
        alertDialog?.dismiss()
    }

    builder.setView(inputView)
    builder.setCancelable(true)
    alertDialog = builder.create()
    alertDialog.show()
}

fun checkString(string: String?): String {
    return string ?: ""
}

fun showNotification(context: Context, title: String?, body: String?) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_ONE_SHOT
    )

    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(context)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setSound(soundUri)
        .setContentIntent(pendingIntent)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(0, notificationBuilder.build())
}

fun encode(string: String): String {
    return URLEncoder.encode(string, StandardCharsets.UTF_8.name())
}

fun encodeEmail(string: String): String {
    var emailString = string.replace("@", "_")
    emailString = emailString.replace(".", "_")
    emailString = emailString.replace("-", "_")
    return emailString
}

fun loadPredefinedSuggestedWords(context: Context) {
    val suggestions: Array<String> = context.resources.getStringArray(R.array.suggestion_array)

    for (suggestion in suggestions) {
        suggestionList.add(suggestion)
    }
}

fun storeSummary(
    auth: FirebaseAuth,
    key: String,
    name: String,
    type: String,
    correctCount: Int,
    totalCount: Int
) {
    val summary = Summary()
    summary.key = key
    summary.correctCount = correctCount
    summary.totalCount = totalCount
    summary.name = name
    summary.type = type

    val summaryPath = SUMMARY_PATH + "/${auth.uid}"
    val database = FirebaseDatabase.getInstance().getReference(summaryPath)
    database.child(summary.key).setValue(summary)
}