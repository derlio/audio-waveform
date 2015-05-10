# audio-waveform (for Android)
--------------------------
##What
#####Show audio file's <strong>waveform</strong>, not spectrum.
#####This lib based on ringdroid, but only contains WaveformView and related classes. 
<br/>
##How
* Add this to your build.gradle</br>
`compile 'com.github.derlio.waveform:library:1.0@aar'`
* Use **SimpleWaveformView** in your layout<br/>
```
        <com.github.derlio.waveform.SimpleWaveformView
            android:id="@+id/waveform"
            android:layout_width="match_parent"
            android:layout_height="144dp"
            app:waveformColor="#fb6155"
            app:indicatorColor="@android:color/holo_green_dark"
        />
```
<strong>*app:waveformColor*</strong> is the waveform line's color, <strong>*app:indicatorColor*</strong> is the current playing indicator's color.
* Then get it from Activity, and prepare your audio file
```
final File file = new File(dir, "qudali.mp3");
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
                            
                            return true;
                        }
                    });
```
***Make sure below codes should not be in the Main Thread.***
User **SoundFile.ProgressListener** to get file load progress, param **fractionComplete** is from 0.0 to 1.0, and return *true* means to continue decode, return *false* will abort the decode.
* Then use
```
mWaveformView.setAudioFile(soundFile);
mWaveformView.invalidate();
```
to set the audio file to WaveformView. It does work.
* If you want to show current playing indicator, just implements **SimpleWaveformView.WaveformListener**, use below codes in **onWaveformDraw**.
```
int now = mPlayer.getCurrentPosition();
mWaveformView.setPlaybackPosition(now);
```

<br/>
This library is just satisfied my current situation. It has much more works to do. Hope you can help me~
