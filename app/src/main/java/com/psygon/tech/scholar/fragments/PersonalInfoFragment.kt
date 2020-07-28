package com.psygon.tech.scholar.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.ProfileActivity
import com.psygon.tech.scholar.helpers.GlideApp
import com.psygon.tech.scholar.helpers.PERSONAL_INFORMATION
import de.hdodenhof.circleimageview.CircleImageView

/**
 * A simple [Fragment] subclass.
 */
class PersonalInfoFragment : Fragment() {
    private lateinit var profileImage: CircleImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUserID: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_personal_info, container, false)

        initAll(rootView)

        if (activity is ProfileActivity) {
            if (profileActivity().auth.currentUser != null) {
                etName.setText(profileActivity().auth.currentUser!!.displayName)
                etEmail.setText(profileActivity().auth.currentUser!!.email)
                etUserID.setText(profileActivity().auth.currentUser!!.uid)

                GlideApp.with(activity!!)
                    .load(profileActivity().auth.currentUser!!.photoUrl)
                    .into(profileImage)
            }
        }

        return rootView
    }

    private fun initAll(rootView: View) {
        profileImage = rootView.findViewById(R.id.profileImage)
        etName = rootView.findViewById(R.id.etName)
        etEmail = rootView.findViewById(R.id.etEmail)
        etUserID = rootView.findViewById(R.id.etUserID)

        val fragment = childFragmentManager.findFragmentById(R.id.fragmentTitle)
        if (fragment is TitleFragment) {
            fragment.setTitle(PERSONAL_INFORMATION)
        }
    }

    private fun profileActivity(): ProfileActivity {
        return activity as ProfileActivity
    }
}
