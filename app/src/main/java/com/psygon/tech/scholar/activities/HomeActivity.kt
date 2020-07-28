package com.psygon.tech.scholar.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.psygon.tech.scholar.MainActivity
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.adapters.HomeAdapter
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.interfaces.ClickResponse
import com.psygon.tech.scholar.models.HomeMenu
import com.psygon.tech.scholar.models.VersionInfo

class HomeActivity : ParentActivity(), ClickResponse {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var menuList: MutableList<HomeMenu>
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()

        addNewMenu(MENU_SUBJECTS, R.mipmap.ic_launcher)
        addNewMenu(MENU_TESTS, R.mipmap.ic_exam)
        addNewMenu(MENU_MY_LIST, R.mipmap.ic_launcher)
//        addNewMenu(MENU_LEADERBOARD, R.mipmap.ic_leaderboard)
//        addNewMenu(MENU_PARTNER, R.mipmap.ic_partner)
        addNewMenu(MENU_PROFILE, R.mipmap.ic_profile)
        addNewMenu(MENU_SIGN_OUT, R.mipmap.ic_launcher)
    }

    private fun addNewMenu(name: String, image: Int) {
        val homeMenu = HomeMenu()
        homeMenu.name = name
        homeMenu.image = image

        menuList.add(homeMenu)
        adapter.notifyDataSetChanged()
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_home
    }

    override fun initAll() {
        menuList = mutableListOf()

        val screenWidth = getScreenWidth(this)
        val layoutInflater = LayoutInflater.from(this)
        val childView = layoutInflater.inflate(R.layout.custom_menu_home, null)

        childView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val menuWidth = childView.measuredWidth
        var columnCount: Int = screenWidth / menuWidth

        if (columnCount < 2) {
            columnCount = 2
        }

        homeRecyclerView = findViewById(R.id.homeRecyclerView)
        homeRecyclerView.setHasFixedSize(true)
        homeRecyclerView.layoutManager = GridLayoutManager(this, columnCount)
        adapter = HomeAdapter(this, menuList)
        homeRecyclerView.adapter = adapter

        setTitle(this, resources.getString(R.string.app_name))

        checkVersionInfo()
    }

    private fun checkVersionInfo() {
        val versionPath = "$VERSION_PATH/$VERSION_KEY"
        val database = FirebaseDatabase.getInstance().getReference(versionPath)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val versionInfo = p0.getValue(VersionInfo::class.java)

                log("Version: ${versionInfo?.versionNumber}")

                if (versionInfo != null) {
                    if (versionInfo.versionNumber > VERSION_NUMBER) {
                        showUpdateAppDialog(
                            this@HomeActivity,
                            UPDATE_APP_MSG
                        )
                    }
                }
            }
        })
    }

    fun getMenuAction(name: String) {
        when (name) {
            MENU_SIGN_OUT -> {
                showConfirmDialog(this, SIGN_OUT_CONFIRMATION_MSG, SIGN_OUT_CODE, LinkedHashMap())
            }

            MENU_SUBJECTS -> {
                val intent = Intent(this@HomeActivity, SubjectsActivity::class.java)
                startActivity(intent)
            }

            MENU_PROFILE -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            MENU_TESTS -> {
                val intent = Intent(this@HomeActivity, ViewTestActivity::class.java)
                startActivity(intent)
            }

            MENU_LEADERBOARD -> {
                toast(this, FEATURE_ARRIVAL_MSG)
            }

            MENU_PARTNER -> {
                toast(this, FEATURE_ARRIVAL_MSG)
            }

            MENU_MY_LIST -> {
                val intent = Intent(this@HomeActivity, MyListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onYesClick(responseCode: String, hashMap: LinkedHashMap<String, Any>) {
        when (responseCode) {
            SIGN_OUT_CODE -> {
                auth.signOut()

                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
