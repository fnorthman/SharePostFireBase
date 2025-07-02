package com.ncorp.sharepost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ncorp.sharepost.databinding.FragmentLoginBinding
import com.ncorp.sharepost.databinding.FragmentUploadBinding


class UploadFragment : Fragment() {
	private var _binding: FragmentUploadBinding? = null
	private val binding get() = _binding!!


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentUploadBinding.inflate(inflater, container, false)
		val view = binding.root
		return view


	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.cancelButton.setOnClickListener {
			val action = UploadFragmentDirections.actionUploadFragmentToFeedFragment()
			Navigation.findNavController(view).navigate(action)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding=null
	}


}