package com.psygon.tech.scholar.helpers

import com.psygon.tech.scholar.models.Chapter
import com.psygon.tech.scholar.models.Subject
import com.psygon.tech.scholar.models.Topic
import com.psygon.tech.scholar.models.Users

var appUser = Users()
var currentSubject = Subject()
var currentTopic = Topic()
var currentChapter = Chapter()
var suggestionList = mutableListOf<String>()

const val LOG_TEXT = "SCHOLAR_TEST"

const val MENU_SUBJECTS = "Subjects"
const val MENU_TESTS = "Tests"
const val MENU_MY_LIST = "My List"
const val MENU_LEADERBOARD = "Leaderboard"
const val MENU_PARTNER = "Partners"
const val MENU_PROFILE = "Profile"
const val MENU_SIGN_OUT = "Sign out"

const val SUBJECT_CODE = "Subjects"
const val SUBJECT_UPDATE_CODE = "SubjectUpdate"
const val SUBJECT_REMOVE_CODE = "SubjectRemove"
const val TOPIC_CODE = "Topics"
const val TOPIC_UPDATE_CODE = "TopicsUpdate"
const val TOPIC_REMOVE_CODE = "TopicsRemove"
const val CHAPTER_CODE = "Chapters"
const val CHAPTER_UPDATE_CODE = "ChaptersUpdate"
const val CHAPTER_REMOVE_CODE = "ChaptersRemove"
const val CONTENT_CODE = "Contents"
const val TEST_CODE = "onFinish"
const val TEST_UPDATE_CODE = "TestUpdate"
const val TEST_REMOVE_CODE = "TestRemove"
const val SIGN_OUT_CODE = "onSignOut"
const val TEST_CONFIRM_CODE = "onTestConfirm"
const val TEST_CREATE_CODE = "onTestCreate"

const val SUBJECT_PATH = "Subjects"
const val USER_PATH = "Users"
const val TOPIC_PATH = "Topics"
const val CHAPTER_PATH = "Chapters"
const val CONTENT_PATH = "Contents"
const val VERSION_PATH = "Versions"
const val SUMMARY_PATH = "Summary"
const val MY_LIST_PATH = "MyList"
const val TEST_PATH = "Tests"

const val VERSION_KEY = "versionKey"

const val TOPIC_GENERAL = "General"

const val DOWNLOAD_APP_TEXT = ""
const val VERSION_NUMBER = 0
const val PERSONAL_INFORMATION = "Personal Information"

const val USER_BLACKLISTED_MSG = "Sorry. You have been blacklisted!"
const val AUTHENTICATION_FAILED_MSG = "Authentication Failed"
const val WELCOME_MSG = "Welcome"
const val UPDATE_APP_MSG = "Please update your app to the latest version"
const val SIGN_OUT_CONFIRMATION_MSG = "Are you sure to sign out?"
const val REMOVE_CONFIRMATION_MSG = "Are you sure to remove this?"
const val FEATURE_ARRIVAL_MSG = "This feature will be available soon"
const val ENTER_SUBJECT_NAME_MSG = "Enter subject name"
const val ENTER_TOPIC_NAME_MSG = "Enter topic name"
const val ENTER_TEST_NAME_MSG = "Enter the name of the test"
const val ENTER_CHAPTER_NAME_MSG = "Enter chapter name"
const val SUBJECT_EXISTS_MSG = "This subject already exists"
const val TOPIC_EXISTS_MSG = "This topic already exists"
const val CHAPTER_EXISTS_MSG = "This chapter already exists"
const val CONTENT_ADDED_SUCCESSFULLY_MSG = "Content added successfully"
const val ONLY_CREATOR_CAN_REMOVE_MSG = "Only creator can remove this"
const val ONLY_CREATOR_CAN_UPDATE_MSG = "Only creator can update this"
const val CHECK_CORRECT_ANS_MSG = "Check correct answer(s)"
const val FILL_THIS_FIELD_MSG = "Fill this field"
const val ADDED_TO_YOUR_LIST = "Added to your list"
const val TOTAL_TEXT = "Total:"
const val OK_TEXT = "OK"
const val SAVE_TEXT = "Save"
const val CANCEL_TEXT = "Cancel"
const val YES_TEXT = "Yes"
const val NO_TEXT = "No"
const val END_EXAM_CONFIRMATION_MSG = "Are you sure to end this test?"
const val CREATE_TEST_CONFIRMATION_MSG = "Are you sure to create this test?"
const val CONTENT_ADD_WARNING_MSG = "You have to add some content first"
const val HAVE_ANSWERED = "have answered"
const val YOU = "You"
const val I = "I"
const val CORRECTLY_OUT_OF = "correctly out of"
const val TEST_RESULT = "Test Result"
const val SHARE_USING = "Share using"
const val IN = "in"

const val SUBJECT = "Subject"
const val TOPIC = "Topic"
const val CHAPTER = "Chapter"
const val TEST = "Test"
const val POSITION = "position"
const val HAS_BEEN_ADDED = "has been added"
const val HAS_BEEN_UPDATED = "has been updated"
const val NAME_KEY = "name"
const val CONTENT_KEY = "content"
const val QUESTION_KEY = "question"
const val EXAM_KEY = "exam"
const val TEST_KEY = "test"
const val OPTION_COUNT_KEY = "optionCount"
const val ANSWER_KEY = "answer"
const val OPTION_1_KEY = "option1"
const val OPTION_2_KEY = "option2"
const val OPTION_3_KEY = "option3"
const val OPTION_4_KEY = "option4"