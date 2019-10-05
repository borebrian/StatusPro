package com.borebrian.statussaver.video

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.borebrian.statussaver.R
import com.borebrian.statussaver.home.HomeActivity
import com.borebrian.statussaver.utils.Utils
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.content_image_view.*
import org.apache.commons.io.FileUtils
import java.io.File


class VideoActivity : AppCompatActivity(), Player.EventListener {

    private val mHideHandler = Handler()
    @SuppressLint("InlinedApi")
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        videoPv.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                // View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION /* or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION*/
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        //fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        downloadVideo.visibility=View.GONE
        deleteVideo.visibility=View.GONE
        shareVideo.visibility=View.GONE

        val imageFile = File(intent.getStringExtra("path"))
        var statusvideo=0;
        var status2video:Int

        fabsharevideo.setOnClickListener(){
            Toast.makeText(this,"Please select app to share to",Toast.LENGTH_LONG).show()
            Utils.shareFile(this, imageFile)
        }

        fabvideo.setOnClickListener(){

            if(statusvideo==0 && imageFile.toString().contains("statusSaver")){

                /*Toast.makeText(this,"Please select app to share to", Toast.LENGTH_LONG).show()*/
                downloadVideo.visibility=View.GONE;
                deleteVideo.visibility=View.VISIBLE;
                shareVideo.visibility=View.VISIBLE
                fabvideo.setImageDrawable(resources.getDrawable(R.drawable.ic_close_black_24dp))
                status2video=statusvideo
                statusvideo=status2video+1
            }
            else if (statusvideo==0 && imageFile.toString().contains("Statuses")){
                deleteVideo.visibility=View.GONE;
                shareVideo.visibility=View.VISIBLE;
                downloadVideo.visibility=View.VISIBLE
                fabvideo.setImageDrawable(resources.getDrawable(R.drawable.ic_add_black_24dp))
                statusvideo=0
            }
            else if(statusvideo==1){
                deleteVideo.visibility=View.GONE;
                downloadVideo.visibility=View.GONE;
                shareVideo.visibility=View.GONE;
                status2video=0;
                statusvideo=0;
                fabvideo.setImageDrawable(resources.getDrawable(R.drawable.ic_add_black_24dp))
            }





        }

       /* if(imageFile.toString().contains("statusSaver")) {
            fab1video.visibility=View.GONE
            fab2video.visibility=View.GONE;
            fabvideo.visibility=View.GONE;
            fabsharevideo.visibility=View.VISIBLE



        }
        else{
            fabsharevideo.visibility=View.GONE;
            fabvideo.visibility=View.VISIBLE;

        }*/
          /*Toast.makeText(this,imageFile.toString(),Toast.LENGTH_LONG).show()*/

        fab2video.setOnClickListener(){
            val destFile = File("${Environment.getExternalStorageDirectory()}${Utils.WHATSAPP_STATUSES_SAVED_LOCATION}/${imageFile.name}")
            FileUtils.copyFile(imageFile, destFile)
            Utils.addToGallery(this, destFile)
            Toast.makeText(this,"Video stored in gallery", Toast.LENGTH_SHORT).show()
        }
        deletevideo.setOnClickListener(){
            val fdelete =(imageFile)
            if (fdelete.exists())
            {
                if (fdelete.delete())
                {
                    Toast.makeText(this,"Video Deleted successfully from:"+imageFile,Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this,"Video was not deleted!!",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this,"No file to delete",Toast.LENGTH_LONG).show()
                finish()
            }

        }




        //overridePendingTransition(R.anim.slide_down, R.anim.slide_up)


        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        videoPv.setOnClickListener { toggle() }

        initializePlayer()

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // dummy_button.setOnTouchListener(mDelayHideTouchListener)


    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        //fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        videoPv.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }


    private lateinit var player: SimpleExoPlayer

    private fun initializePlayer() {
        // Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)


        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"))

        // Produces Extractor instances for parsing the media data.
        //val extractorsFactory = DefaultExtractorsFactory()

        // This is the MediaSource representing the media to be played.

        val file = File(intent.getStringExtra("path"))
        val videoUri = Uri.fromFile(file)
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri)

        // Prepare the player with the source.

        videoPv.player = player
        player.prepare(videoSource)

        player.addListener(this)

    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        player.playbackState
    }

    override fun onResume() {
        super.onResume()

        player.playWhenReady = true
        player.playbackState

    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {


    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {


    }


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }


}
