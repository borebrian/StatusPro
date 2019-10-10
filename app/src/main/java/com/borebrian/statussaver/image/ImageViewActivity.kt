package com.borebrian.statussaver.image

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import com.borebrian.statussaver.R
import com.bumptech.glide.Glide

import com.borebrian.statussaver.utils.Utils
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.content_image_view.*
import org.apache.commons.io.FileUtils
import java.io.File
import android.app.WallpaperManager

import android.net.Uri
import android.support.v7.app.AlertDialog
import com.borebrian.statussaver.home.HomeActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import android.R




class ImageViewActivity : AppCompatActivity() {

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.borebrian.statussaver.R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        var status=0



        MobileAds.initialize(this, "ca-app-pub-4761500786576152~8215465788")
        interstitialAd = InterstitialAd(this)
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712")
        val request = AdRequest.Builder().build()
        interstitialAd.loadAd(request)
      /*  val adRequest = AdRequest.Builder().build()
        addV.loadAd(adRequest)*/




        download.visibility=View.GONE
        setas.visibility=View.GONE
        delete.visibility=View.GONE
        sharestatus.visibility=View.GONE
        val imageFile = File(intent.getStringExtra("image"))


        fab4.setOnClickListener(){
            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.fromFile(imageFile), "image/jpeg")
            intent.putExtra("mimeType", "image/jpeg")
            this.startActivity(Intent.createChooser(intent, "Set as:"))
        }

 




        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())



        Glide.with(this)
                .load(imageFile)
                .into(imageView)

           fab2.setOnClickListener(){
               val destFile = File("${Environment.getExternalStorageDirectory()}${Utils.WHATSAPP_STATUSES_SAVED_LOCATION}/${imageFile.name}")
               FileUtils.copyFile(imageFile, destFile)
               Utils.addToGallery(this, destFile)
               Toast.makeText(this, this.getString(R.string.status_saved_to_gallery), Toast.LENGTH_SHORT).show()
           }
        fab1.setOnClickListener(){
            Toast.makeText(this,"Please select app to share to",Toast.LENGTH_LONG).show()
            Utils.shareFile(this, imageFile)
        }

        fab3.setOnClickListener(){
            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.fromFile(imageFile), "image/jpeg")
            intent.putExtra("mimeType", "image/jpeg")
            this.startActivity(Intent.createChooser(intent, ""))
        }
        fab4.setOnClickListener(){
            val fdelete =(imageFile)
            if (fdelete.exists())
            {
                if (fdelete.delete())
                {
                    Toast.makeText(this,"Status Deleted successfully from:"+imageFile,Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this,"file was not deleted!!",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this,"No file to delete",Toast.LENGTH_LONG).show()
                finish()
            }



        }

        fab.setOnClickListener(){
            if(status==0 && imageFile.toString().contains("statusSaver")){

                /*Toast.makeText(this,"Please select app to share to", Toast.LENGTH_LONG).show()*/
                sharestatus.visibility=View.VISIBLE;
                setas.visibility=View.VISIBLE;
                download.visibility=View.GONE
                delete.visibility=View.VISIBLE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                var status2=status
                status=status2+1
            }
            else if(status==0 && imageFile.toString().contains("Statuses")){
                sharestatus.visibility=View.VISIBLE;
                setas.visibility=View.VISIBLE;
                download.visibility=View.VISIBLE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                var status2=status
                status=status2+1

            }
            else{
                sharestatus.visibility=View.GONE;
                setas.visibility=View.GONE;
                download.visibility=View.GONE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add_black_24dp))
                var status2=status
                status=0
            }
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
