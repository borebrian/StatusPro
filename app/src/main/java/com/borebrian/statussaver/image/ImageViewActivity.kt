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
import android.content.Context

import android.net.Uri
import android.support.v7.app.AlertDialog
import com.borebrian.statussaver.home.HomeActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd


class ImageViewActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var mAdView: AdView
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        setSupportActionBar(toolbar)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712";
        mInterstitialAd.loadAd(AdRequest.Builder().build());

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        var status=0
        showInterstitialAd()



        // Interstitial
      /*  mInterstitialAd.adUnitId = "ca-app-pub-7643266345625929/3567501731"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.show()
*/
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713








          val adRequest = AdRequest.Builder().build()
          addV.loadAd(adRequest)



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
            showInterstitialAd()

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
            showInterstitialAd()
        }

        fab3.setOnClickListener(){
            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.fromFile(imageFile), "image/jpeg")
            intent.putExtra("mimeType", "image/jpeg")
            this.startActivity(Intent.createChooser(intent, ""))
            showInterstitialAd()

        }
        fab4.setOnClickListener(){
            showAdd()
            val fdelete =(imageFile)
            if (fdelete.exists())
            {
                if (fdelete.delete())
                {
                    showInterstitialAd()

                    Toast.makeText(this,"Status Deleted successfully from:"+imageFile,Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }
                else
                {
                    showInterstitialAd()

                    Toast.makeText(this,"file was not deleted!!",Toast.LENGTH_LONG).show();
                }
            }
            else{
                showInterstitialAd()
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
                mInterstitialAd.show()
            }
            else if(status==0 && imageFile.toString().contains("Statuses")){
                sharestatus.visibility=View.VISIBLE;
                setas.visibility=View.VISIBLE;
                download.visibility=View.VISIBLE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                var status2=status
                status=status2+1
                mInterstitialAd.show()

            }
            else{
                sharestatus.visibility=View.GONE;
                setas.visibility=View.GONE;
                download.visibility=View.GONE
                delete.visibility=View.GONE
                fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add_black_24dp))
                var status2=status
                status=0
                mInterstitialAd.show()
            }
        }
    }
    fun showAdd(){
        /* if (mInterstitialAd.isLoaded()) {*/
        mInterstitialAd = InterstitialAd(this);
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712";
        mInterstitialAd.loadAd(AdRequest.Builder().build());
        mInterstitialAd.show()

        /* }
         else{*/
        /*  Toast.makeText(this,"Not loaded",Toast.LENGTH_LONG).show()
          mInterstitialAd = InterstitialAd(this);
          mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
          mInterstitialAd.loadAd(AdRequest.Builder().build());
          mInterstitialAd.show()*/

    }


    fun showInterstitialAd() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
    else{
            mInterstitialAd = InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(AdRequest.Builder().build());
            call()

        }}
    fun call(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            
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
