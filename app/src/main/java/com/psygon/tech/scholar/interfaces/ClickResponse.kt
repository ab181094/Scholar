package com.psygon.tech.scholar.interfaces

interface ClickResponse {
    fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>)
}