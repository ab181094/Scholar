package com.psygon.tech.scholar.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.ProfileAdapter
import com.psygon.tech.scholar.fragments.PersonalInfoFragment

class ProfileActivity : ParentActivity() {
    private lateinit var profilePager: ViewPager
    private lateinit var pagerAdapter: ProfileAdapter
    private lateinit var fragmentList: MutableList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_profile
    }

    override fun initAll() {
        fragmentList = mutableListOf()
        fragmentList.add(PersonalInfoFragment())

        profilePager = findViewById(R.id.profilePager)
        pagerAdapter = ProfileAdapter(supportFragmentManager, fragmentList)
        profilePager.adapter = pagerAdapter
    }
}
