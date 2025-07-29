package com.example.pawmepetadoptionapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DummyFragment(s: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textView = TextView(requireContext())
        val title = null
        textView.text = title
        textView.textSize = 24f
        textView.setPadding(32, 64, 32, 32)
        return textView
    }


}
