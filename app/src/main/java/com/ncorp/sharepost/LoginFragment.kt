package com.ncorp.sharepost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
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


	}

	fun loginOl(view: View) {
		println("Giriş Yap Tıklandı")

		val email = binding.emailEditText.text.toString().trim()
		val password = binding.passwordEditText.text.toString()

		// Boş alan kontrolü
		if (email.isEmpty()) {
			binding.emailEditText.error = "Lütfen e-posta girin"
			return
		}
		if (password.isEmpty()) {
			binding.passwordEditText.error = "Lütfen şifre girin"
			return
		}

		auth.signInWithEmailAndPassword(email, password)
			.addOnSuccessListener {
				val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
				Navigation.findNavController(view).navigate(action)
			}
			.addOnFailureListener { exception ->
				val message = parseFirebaseLoginError(exception.localizedMessage ?: "")
				Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
			}
	}

	// Hata mesajlarını daha kullanıcı dostu yapmak için:
	private fun parseFirebaseLoginError(message: String): String {
		val msg = message.lowercase()
		return when {
			"no user record" in msg || "user not found" in msg -> "Bu e-posta adresine kayıtlı kullanıcı bulunamadı."
			"password is invalid" in msg || "wrong password" in msg -> "Şifre yanlış."
			"network error" in msg -> "İnternet bağlantınızı kontrol edin."
			"too many requests" in msg -> "Çok fazla başarısız giriş denemesi, lütfen biraz bekleyin."
			"email address is badly formatted" in msg -> "Geçersiz e-posta formatı."
			else -> "Giriş yapılamadı. Lütfen tekrar deneyin."
		}
	}


	fun kayitOl(view: View) {
		println("Kayıt Ol Tıklandı")

		val email = binding.emailEditText.text.toString().trim()
		val password = binding.passwordEditText.text.toString()

		// Öncelikle boşluk kontrolü yapalım (AND olmalı, OR değil)
		if (email.isEmpty() || password.isEmpty()) {
			if (email.isEmpty()) {
				binding.emailEditText.error = "Email boş olamaz"
				binding.emailEditText.requestFocus()
			}
			if (password.isEmpty()) {
				binding.passwordEditText.error = "Şifre boş olamaz"
				binding.passwordEditText.requestFocus()
			}
			return
		}

		// Firebase ile kayıt işlemi
		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
			if (task.isSuccessful) {
				val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
				Navigation.findNavController(view).navigate(action)
			}
		}.addOnFailureListener { exception ->
			val errorMessage = exception.localizedMessage ?: "Bilinmeyen hata"
			val userFriendlyMessage = parseFirebaseError(errorMessage)

			when {
				errorMessage.contains("email", ignoreCase = true) -> {
					binding.emailEditText.error = userFriendlyMessage
					binding.emailEditText.requestFocus()
				}
				errorMessage.contains("password", ignoreCase = true) -> {
					binding.passwordEditText.error = userFriendlyMessage
					binding.passwordEditText.requestFocus()
				}
				else -> {
					Toast.makeText(requireContext(), userFriendlyMessage, Toast.LENGTH_LONG).show()
				}
			}
		}
	}

	// Kullanıcı dostu hata mesajı üretici
	private fun parseFirebaseError(message: String): String {
		val msg = message.lowercase()
		return when {
			"email address is badly formatted" in msg ->
				"Geçersiz e-posta formatı. Lütfen doğru formatta bir e-posta girin."
			"password is weak" in msg ||
					"password should be at least 6 characters" in msg ->
				"Şifre çok zayıf. En az 6 karakter olmalı."
			"email address is already in use" in msg ||
					"email already exists" in msg ->
				"Bu e-posta zaten kayıtlı. Lütfen farklı bir e-posta deneyin."
			"network error" in msg ||
					"network request failed" in msg ->
				"İnternet bağlantınızı kontrol edin ve tekrar deneyin."
			"user not found" in msg ->
				"Bu e-posta ile kayıtlı bir kullanıcı bulunamadı."
			"invalid password" in msg ||
					"wrong password" in msg ->
				"Şifre yanlış. Lütfen tekrar deneyin."
			"too many requests" in msg ||
					"quota exceeded" in msg ->
				"Çok fazla deneme yapıldı. Lütfen bir süre sonra tekrar deneyin."
			"user disabled" in msg ->
				"Bu kullanıcı devre dışı bırakılmıştır. Daha fazla bilgi için destek ile iletişime geçin."
			"operation not allowed" in msg ->
				"Bu işlem şu anda desteklenmiyor."
			else ->
				"Beklenmedik bir hata oluştu. Lütfen tekrar deneyin."
		}
	}



	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}



}