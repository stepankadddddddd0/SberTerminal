package com.example.sberterminal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

class CameraFragment : Fragment() {

    private lateinit var previewView: PreviewView
    private lateinit var scanOverlay: ImageView
    private lateinit var statusText: TextView
    private val REQUEST_CODE_CAMERA = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.previewView)
        scanOverlay = view.findViewById(R.id.scanOverlay)
        statusText = view.findViewById(R.id.statusText)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA
            )
        } else {
            startCamera()
        }

        previewView.setOnClickListener {
            simulatePayment()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun simulatePayment() {
        val vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)

        scanOverlay.visibility = View.VISIBLE
        scanOverlay.animate()
            .alpha(1f)
            .setDuration(300)
            .withEndAction {
                statusText.text = "✅ Оплата прошла успешно!"
                statusText.setTextColor(android.graphics.Color.parseColor("#2ECC71"))
                scanOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        previewView.postDelayed({
                            (activity as MainActivity).loadFragment(AmountFragment())
                        }, 1500)
                    }
                    .start()
            }
            .start()
    }
}
