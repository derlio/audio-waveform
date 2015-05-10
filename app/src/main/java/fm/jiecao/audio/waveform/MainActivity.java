package fm.jiecao.audio.waveform;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.derlio.waveform.SamplePlayer;
import com.github.derlio.waveform.WaveformView;
import com.github.derlio.waveform.soundfile.SoundFile;

import java.io.File;
import java.io.IOException;


public class MainActivity extends ActionBarActivity implements WaveformView.WaveformListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WaveformView mWaveformView;
    private NumberProgressBar mLoadFileProgressBar;
    private int mStartPos;
    private int mEndPos;
    private int mOffset;
    private boolean mIsPlaying;
    private SamplePlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaveformView = (WaveformView) findViewById(R.id.waveform);
        mWaveformView.setListener(this);

        mLoadFileProgressBar = (NumberProgressBar) findViewById(R.id.load_file_progressbar);
        mLoadFileProgressBar.setMax(100);

        Thread loadSongfileThread = new Thread() {
            @Override
            public void run() {
                File dir = Environment.getExternalStorageDirectory();
                final File file = new File(dir, "qudali.mp3");
                try {
                    final SoundFile soundFile = SoundFile.create(file.getPath(), new SoundFile.ProgressListener() {

                        int lastProgress = 0;

                        @Override
                        public boolean reportProgress(double fractionComplete) {
                            final int progress = (int) (fractionComplete * 100);
                            if (lastProgress == progress) {
                                return true;
                            }
                            lastProgress = progress;
                            Log.i(TAG, "LOAD FILE PROGRESS:" + progress);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadFileProgressBar.setProgress(progress);
                                }
                            });
                            return true;
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mLoadFileProgressBar.setVisibility(View.GONE);

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            mWaveformView.setSoundFile(soundFile);
                            mWaveformView.recomputeHeights(metrics.density);

                            mWaveformView.setZoomLevel(3);

                            mEndPos = mWaveformView.maxPos();
                            mStartPos = 0;
                            mOffset = 0;

                            mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
                            Log.i(TAG, "getNumSamples:" + soundFile.getNumSamples());


                            MediaPlayer mPlayer = new MediaPlayer();
                            try {
                                mPlayer.setDataSource(file.getPath());
                                mPlayer.prepare();
                                mPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i(TAG, "Duration:" + mPlayer.getCurrentPosition());
                        }
                    });
//            mWaveformView.invalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SoundFile.InvalidInputException e) {
                    e.printStackTrace();
                }
            }
        };
        loadSongfileThread.start();
    }


    private synchronized void updateDisplay() {
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition();
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
        }

        mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
        mWaveformView.invalidate();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void waveformTouchStart(float x) {

    }

    @Override
    public void waveformTouchMove(float x) {

    }

    @Override
    public void waveformTouchEnd() {

    }

    @Override
    public void waveformFling(float x) {

    }

    @Override
    public void waveformDraw() {
        if (mIsPlaying) {
            updateDisplay();
        }
    }

    @Override
    public void waveformZoomIn() {

    }

    @Override
    public void waveformZoomOut() {

    }
}
