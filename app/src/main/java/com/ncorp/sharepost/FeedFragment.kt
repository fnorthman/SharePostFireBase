package com.ncorp.sharepost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ncorp.sharepost.databinding.FragmentFeedBinding
import com.ramotion.circlemenu.CircleMenuView

class FeedFragment : Fragment() {

	private var _binding: FragmentFeedBinding? = null
	private val binding get() = _binding!!

	private lateinit var auth: FirebaseAuth

	private var isMenuOpen = false

	private var maxX = 0f
	private var maxY = 0f
	private val edgePadding = 16f  // dp cinsinden kenar boşluğu, istersen dp->px dönüştür

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		auth = Firebase.auth

	}


	override fun onCreateView(

		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentFeedBinding.inflate(inflater, container, false)
		return binding.root


	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		/*binding.dragContainer.setOnClickListener { println("dragcontainer") }
		binding.dragOverlay.setOnClickListener { println("dragoverlay") }*/

		fun uploadPage(view: View) {
			val action = FeedFragmentDirections.actionFeedFragmentToUploadFragment()
			Navigation.findNavController(view).navigate(action)
		}

		fun loginPage(view: View) {
			val action = FeedFragmentDirections.actionFeedFragmentToLoginFragment()
			Navigation.findNavController(view).navigate(action)
		}
		binding.root.post {
			val parentWidth = binding.root.width
			val parentHeight = binding.root.height
			val containerWidth = binding.dragContainer.width
			val containerHeight = binding.dragContainer.height

			maxX = (parentWidth - containerWidth - edgePadding).coerceAtLeast(edgePadding)
			maxY = (parentHeight - containerHeight - edgePadding).coerceAtLeast(edgePadding)
		}

		binding.circleMenu.eventListener = object : CircleMenuView.EventListener() {
			override fun onButtonClickAnimationEnd(view: CircleMenuView, index: Int) {
				if (index == 1) {
					auth.signOut()
					loginPage(requireView())

				}
				if (index == 2) {
					uploadPage(requireView())
				}
			}
		}


		binding.dragOverlay.setOnTouchListener(object : View.OnTouchListener {


			private var lastRawX = 0f
			private var lastRawY = 0f
			private var isDragging = false

			override fun onTouch(v: View, event: MotionEvent): Boolean {
				println("dragoverlay")
				when (event.actionMasked) {

					MotionEvent.ACTION_DOWN -> {
						lastRawX = event.rawX
						lastRawY = event.rawY
						isDragging = false
						return true
					}

					MotionEvent.ACTION_MOVE -> {
						val dx = event.rawX - lastRawX
						val dy = event.rawY - lastRawY

						if (dx * dx + dy * dy > 16) {  // hareket eşiği
							isDragging = true
							val newX = (binding.dragContainer.x + dx).coerceIn(edgePadding, maxX)
							val newY = (binding.dragContainer.y + dy).coerceIn(edgePadding, maxY)
							binding.dragContainer.x = newX
							binding.dragContainer.y = newY

							lastRawX = event.rawX
							lastRawY = event.rawY
						}
						return true
					}

					MotionEvent.ACTION_UP -> {
						if (!isDragging) {
							// Tıklama ise menüyü aç/kapa
							if (isMenuOpen) {
								binding.circleMenu.close(true)
								isMenuOpen = false
							} else {
								binding.circleMenu.open(true)
								isMenuOpen = true
							}
						}
						return true
					}

					else -> return false
				}
			}
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
