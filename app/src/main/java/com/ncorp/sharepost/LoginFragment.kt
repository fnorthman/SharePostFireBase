package com.ncorp.sharepost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ncorp.sharepost.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
	private var _binding: FragmentLoginBinding? = null
	private val binding get() = _binding!!
	private lateinit var auth: FirebaseAuth


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth

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

		val guncelKullanici=auth.currentUser
		if(guncelKullanici!=null){
			val action=LoginFragmentDirections.actionLoginFragmentToFeedFragment()
			Navigation.findNavController(view).navigate(action)
		}


	}

	fun loginOl(view: View) {
		println("Giriş Yap Tıklandı")

		val email = binding.emailEditText.text.toString().trim()
		val password = binding.passwordEditText.text.toString()

		if (email.isNotEmpty() && password.isNotEmpty()) {
			auth.signInWithEmailAndPassword(email, password)
				.addOnSuccessListener {
					val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
					Navigation.findNavController(view).navigate(action)
				}
				.addOnFailureListener { exception ->
					Log.e("FirebaseLogin", "exception type: ${exception::class}, message: ${exception.message}")
					val errorMessage = parseFirebaseLoginError(exception)
					Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
				}
		} else {
			Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
		}
	}
	private fun parseFirebaseLoginError(exception: Exception): String {
		return when (exception) {
			is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
				"Bu e-posta adresine kayıtlı kullanıcı bulunamadı."

			is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
				val msg = exception.message?.lowercase() ?: ""
				return when {
					"email" in msg && "badly formatted" in msg -> "Geçersiz e-posta formatı."
					else -> "Şifre yanlış veya geçersiz giriş bilgisi."
				}
			}

			is com.google.firebase.FirebaseNetworkException ->
				"İnternet bağlantınızı kontrol edin."

			is com.google.firebase.auth.FirebaseAuthException -> {
				val msg = exception.message?.lowercase() ?: ""
				when {
					"too many requests" in msg -> "Çok fazla başarısız giriş denemesi. Lütfen biraz bekleyin."
					else -> "Giriş yapılamadı. Lütfen tekrar deneyin."
				}
			}

			else -> "Beklenmedik bir hata oluştu. Lütfen tekrar deneyin."
		}
	}






	fun kayitOl(view: View) {
		val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
		Navigation.findNavController(view).navigate(action)
	}





	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}