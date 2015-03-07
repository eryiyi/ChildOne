package com.xiaogang.ChildOne.ui;



import android.content.res.Configuration;
import android.os.Bundle;
import com.xiaogang.ChildOne.R;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.intf.VideoResult;
import io.vov.vitamio.widget.MediaController;

/**
 * Created by apple on 14-9-7.
 */
public class VideoViewActivity extends BaseActivity implements VideoResult {
    private String path = "";
    private VideoView mVideoView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        path = getIntent().getStringExtra(Constants.VIDEO_URL);

        setContentView(R.layout.videoview);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVideoView.setVideoPath(path);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoResult(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void result(int flag) {
        switch (flag){
            case 1://不能播放此视频
                finish();
                break;
        }
    }
}
