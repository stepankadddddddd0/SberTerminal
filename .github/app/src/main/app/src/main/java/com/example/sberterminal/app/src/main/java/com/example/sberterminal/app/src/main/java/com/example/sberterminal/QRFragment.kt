package com.example.sberterminal

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QRFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amount = arguments?.getString("amount") ?: "0"
        view.findViewById<TextView>(R.id.qrAmount).text = "₽ $amount"

        val qrImage = view.findViewById<ImageView>(R.id.qrImage)
        qrImage.setImageBitmap(generateQR("SBER_PAY:$amount"))

        view.findViewById<Button>(R.id.btnPaySmile).setOnClickListener {
            (activity as MainActivity).loadFragment(CameraFragment())
        }
    }

    private fun generateQR(data: String): Bitmap {
        val writer = MultiFormatWriter()
        val matrix: BitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400)
        val bmp = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565)
        for (x in 0 until 400) {
            for (y in 0 until 400) {
                bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }
}
