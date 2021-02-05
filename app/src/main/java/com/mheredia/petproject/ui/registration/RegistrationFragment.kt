package com.mheredia.petproject.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.mheredia.petproject.R


// ...

// ...
class EditNameDialogFragment : DialogFragment() {
    private var mEditText: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container)
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
//        mEditText = view.findViewById(R.id.txt_your_name) as EditText
        // Fetch arguments from bundle and set title
//        val title = arguments!!.getString("title", "Enter Name")
        dialog!!.setTitle("Register")
        // Show soft keyboard automatically and request focus to field
//        mEditText!!.requestFocus()
        dialog!!.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

    companion object {
        fun newInstance(): EditNameDialogFragment {
            val frag = EditNameDialogFragment()
            val args = Bundle()
            frag.arguments = args
            return frag
        }
    }
}