package com.psygon.tech.scholar.models

class Content() : BaseStudy() {
    var noOfOptions: Int = -1
    var question: String = ""
    var answer: String = ""
    var option1: String = ""
    var option2: String = ""
    var option3: String = ""
    var option4: String = ""
    var explanation: String = ""
    var isMultiple: Boolean = false
    var chapterID: Int = -1
    var chapterKey: String = ""
}