package com.ncorp.sharepost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ncorp.sharepost.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
	private var _binding: FragmentLoginBinding? = null
	private val binding get() = _binding!!


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentLoginBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.registerText.setOnClickListener { kayitOl(it) }
		binding.loginButton.setOnClickListener { loginOl(it) }



	}
	fun loginOl(view: View){
		println("Giriş Yap Tıklandı")


	}
	fun kayitOl(view: View){
		println("Kayıt Ol Tıklandı")
		val action=LoginFragmentDirections.actionLoginFragmentToFeedFragment()
		Navigation.findNavController(view).navigate(action)

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding=null
	}


}