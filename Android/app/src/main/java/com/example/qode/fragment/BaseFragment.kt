package com.example.qode.fragment

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun title() : String
}