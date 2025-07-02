package com.ncorp.sharepost

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ncorp.sharepost.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
	private lateinit var auth: FirebaseAuth
	private var _binding: FragmentRegisterBinding? = null
	private val binding get() = _binding!!
	private lateinit var pickImageLauncher: ActivityResultLauncher<String>
	private var selectedImageBitmap: Bitmap? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth
		pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
			uri?.let {
				val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
					val source = ImageDecoder.createSource(requireContext().contentResolver, it)
					ImageDecoder.decodeBitmap(source)
				} else {
					MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
				}

				val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true)
				selectedImageBitmap = resizedBitmap
				binding.profileImageView.setImageBitmap(resizedBitmap)
			}
		}


	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentRegisterBinding.inflate(inflater, container, false)
		return binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		binding.registerButton.setOnClickListener {
			kayitOl(it)


		}
		binding.profileImageView.setOnClickListener {
			profilePhoto(it)
		}


	}

	fun profilePhoto(view: View) {
		println("Profil Fotoğrafı Tıklandı")
		pickImageLauncher.launch("image/*")
	}


	fun kayitOl(view: View) {
		println("Kayıt Ol Tıklandı")

		val email = binding.emailEditText.text.toString().trim()
		val password = binding.passwordEditText.text.toString()
		val id = binding.usernameEditText.text.toString()



		// Öncelikle boşluk kontrolü yapalım (AND olmalı, OR değil)
		if (email.isEmpty() || password.isEmpty() || id.isEmpty()) {
			if (email.isEmpty()) {
				binding.emailEditText.error = "Email boş olamaz"
				binding.emailEditText.requestFocus()
			}
			if (password.isEmpty()) {
				binding.passwordEditText.error = "Şifre boş olamaz"
				binding.passwordEditText.requestFocus()
			}
			if (id.isEmpty()) {
				binding.usernameEditText.error = "Kullanıcı adı boş olamaz"
				binding.usernameEditText.requestFocus()
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
					Toast.makeText(requireContext(), userFriendlyMessage, Toast.LENGTH_LONG)
						.show()
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