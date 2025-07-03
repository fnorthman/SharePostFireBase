package com.ncorp.sharepost

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ncorp.sharepost.databinding.FragmentUploadBinding


class UploadFragment : Fragment() {
	private var _binding: FragmentUploadBinding? = null
	private val binding get() = _binding!!

	private lateinit var pickImageLauncher: ActivityResultLauncher<String>
	private var selectedImageBitmap: Bitmap? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
			uri?.let {
				val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
					val source = ImageDecoder.createSource(requireContext().contentResolver, it)
					ImageDecoder.decodeBitmap(source)
				} else {
					MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
				}

				val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true)
				selectedImageBitmap = resizedBitmap
				binding.imagePreview.setImageBitmap(resizedBitmap)
			}
		}

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
		binding.imagePreview.setOnClickListener {
			pickImageLauncher.launch("image/*")
		}
		binding.uploadButton.setOnClickListener { uploadButton(it) }
	}

	fun uploadButton(view: View) {

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}