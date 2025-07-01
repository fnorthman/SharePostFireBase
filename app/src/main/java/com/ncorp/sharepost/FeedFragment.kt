package com.ncorp.sharepost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ncorp.sharepost.databinding.FragmentFeedBinding
import com.ramotion.circlemenu.CircleMenuView

class FeedFragment : Fragment() {

	private var _binding: FragmentFeedBinding? = null
	private val binding get() = _binding!!

	private var dX = 0f
	private var dY = 0f
	private var isMenuOpen = false

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

		// Menü açma kapama toggle fonksiyonu
		fun toggleMenu() {
			if (isMenuOpen) {
				binding.circleMenu.close(true)  // menüyü kapat
				isMenuOpen = false
			} else {
				binding.circleMenu.open(true)   // menüyü aç
				isMenuOpen = true
			}
		}

		// Dokunma işlemi
		binding.dragOverlay.setOnTouchListener(object : View.OnTouchListener {
			private var lastRawX = 0f
			private var lastRawY = 0f

			override fun onTouch(v: View, event: MotionEvent): Boolean {
				val parent = binding.root
				when (event.actionMasked) {
					MotionEvent.ACTION_DOWN -> {
						lastRawX = event.rawX
						lastRawY = event.rawY
						return true
					}
					MotionEvent.ACTION_MOVE -> {
						val dx = event.rawX - lastRawX
						val dy = event.rawY - lastRawY

						val newX = (binding.dragContainer.x + dx).coerceIn(0f, (parent.width - binding.dragContainer.width).toFloat())
						val newY = (binding.dragContainer.y + dy).coerceIn(0f, (parent.height - binding.dragContainer.height).toFloat())

						binding.dragContainer.x = newX
						binding.dragContainer.y = newY

						lastRawX = event.rawX
						lastRawY = event.rawY

						return true
					}
					MotionEvent.ACTION_UP -> {
						toggleMenu()
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

