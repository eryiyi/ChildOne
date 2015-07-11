package com.xiaogang.ChildOne.widget;

import android.os.Environment;

import com.kubility.demo.MP3Recorder;

public  class SoundMeter {
	static final private double EMA_FILTER = 0.6;
//	private MP3Recorder recorder = new MP3Recorder(8000);
	private MP3Recorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MP3Recorder(8000);
//			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			mRecorder.setOutputFile(Environment.getExternalStorageDirectory()+"/"+name);
			try {
//				mRecorder.prepare();
				mRecorder.start(name);
				
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
//			} catch (IOException e) {
//				System.out.print(e.getMessage());
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
//			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

//	public void start() {
//		if (mRecorder != null) {
//			mRecorder.start();
//		}
//	}

	public double getAmplitude() {
		if (mRecorder != null)
//			return (mRecorder.getMaxAmplitude() / 2700.0);
			return 5.0;
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
