package com.farsheel.statussaver.image

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.farsheel.statussaver.R
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.content_image_view.*
import java.io.File


class ImageViewActivity : AppCompatActivity() {

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        fab1.visibility=View.GONE;
        fab2.visibility=View.GONE;

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        val imageFile = File(intent.getStringExtra("image"))

        Glide.with(this)
                .load(imageFile)
                .into(imageView)




        fab.setOnClickListener(){
            Toast.makeText(this,"Please select app to share to", Toast.LENGTH_LONG).show()
            fab1.visibility=View.VISIBLE;
            fab2.visibility=View.VISIBLE;
        }
    }



    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        mScaleGestureDetector?.onTouchEvent(motionEvent)
        return true
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f))
            imageView.scaleX = mScaleFactor
            imageView.scaleY = mScaleFactor
            return true
        }
    }


}
