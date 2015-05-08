package fm.jiecao.audio.waveform;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

import fm.jiecao.audio.waveform.library.SamplePlayer;
import fm.jiecao.audio.waveform.library.WaveformView;
import fm.jiecao.audio.waveform.library.soundfile.SoundFile;


public class MainActivity extends ActionBarActivity {

    private WaveformView mWaveformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaveformView = (WaveformView) findViewById(R.id.waveform);
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "qudali.mp3");
        try {
            SoundFile soundFile = SoundFile.create(file.getPath(), new SoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {
                    return true;
                }
            });

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            mWaveformView.setSoundFile(soundFile);
            mWaveformView.recomputeHeights(metrics.density);

            SamplePlayer mPlayer = new SamplePlayer(soundFile);
            mPlayer.start();
//            mWaveformView.invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SoundFile.InvalidInputException e) {
            e.printStackTrace();
        }
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
}
