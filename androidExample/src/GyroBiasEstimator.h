/*
 * GyroBiasEstimator.h
 *
 *  Created on: Feb 27, 2015
 *      Author: dantheman
 */

#pragma once
#include "ofMain.h";

enum State {
	UNCALIBRATED = 0, CALIBRATING, CALIBRATED
};

class Estimate {

	State mState;
	ofVec3f mBias;

	void set(Estimate from) {
		mState = from.mState;
		mBias.set(from.mBias);
	}
};

class GyroBiasEstimator {
public:
	GyroBiasEstimator();
	virtual ~GyroBiasEstimator();

	float GYRO_SMOOTHING_FACTOR = 0.01F;
	float ACC_SMOOTHING_FACTOR = 0.1F;
	ofVec3f UP_VECTOR = new ofVec3f(0.0, 0.0, 1.0);

	float MIN_ACCEL_DOT_WITH_UP = (float) Math.cos(Math.toRadians(10.0));
	float MIN_ACCEL_LENGTH = 1.0E-04;
	float MAX_GYRO_DIFF = 0.01;
	long CALIBRATION_DURATION_NS = 5000000000;
	long MAX_DELAY_BETWEEN_EVENTS_NS = 100000000;

	ofVec3f mLastGyro;

	ofVec3f mCurrGyro;

	ofVec3f mGyroDiff;

	ofVec3f mCurrAcc;

	ofVec3f mAccSmoothed;

	ofVec3f mAccNormalizedTmp;
	float mGyroMagnitudeDiffSmoothed;

	Estimate mBiasEstimate;

	long mCalibrationStartTimeNs;

	long mLastGyroTimeNs;

	long mLastAccTimeNs;

	void processGyroscope(ofVec3f gyro, long sensorTimeStamp) {
		if (this.mBiasEstimate.mState
				== GyroBiasEstimator.Estimate.State.CALIBRATED) {
			return;
		}
		this.mCurrGyro.set(gyro);

		ofVec3f.sub(this.mCurrGyro, this.mLastGyro, this.mGyroDiff);
		float mCurrDiff = (float) this.mGyroDiff.length();
		this.mGyroMagnitudeDiffSmoothed = (0.01F * mCurrDiff
				+ 0.99F * this.mGyroMagnitudeDiffSmoothed);

		this.mLastGyro.set(this.mCurrGyro);
		boolean eventIsDelayed = sensorTimeStamp
				> this.mLastGyroTimeNs + 100000000L;
		this.mLastGyroTimeNs = sensorTimeStamp;

		if (eventIsDelayed) {
			resetCalibration();
			return;
		}

		if ((this.mBiasEstimate.mState
				== GyroBiasEstimator.Estimate.State.CALIBRATING)
				&& (sensorTimeStamp > this.mCalibrationStartTimeNs + 5000000000L)) {
			this.mBiasEstimate.mState =
					GyroBiasEstimator.Estimate.State.CALIBRATED;
			return;
		}

		if (!canCalibrateGyro()) {
			resetCalibration();
			return;
		}

		startCalibration(sensorTimeStamp);
	}

	void resetCalibration() {
		this.mBiasEstimate.mState =
				GyroBiasEstimator.Estimate.State.UNCALIBRATED;
		this.mBiasEstimate.mBias.set(0.0, 0.0, 0.0);
		this.mCalibrationStartTimeNs = -1L;
	}

	void startCalibration(long gyroTimeStamp) {
		if (this.mBiasEstimate.mState
				!= GyroBiasEstimator.Estimate.State.CALIBRATING) {
			this.mBiasEstimate.mBias.set(this.mCurrGyro);
			this.mBiasEstimate.mState =
					GyroBiasEstimator.Estimate.State.CALIBRATING;
			this.mCalibrationStartTimeNs = gyroTimeStamp;
		} else {
			smooth(this.mBiasEstimate.mBias, this.mCurrGyro, 0.01);
		}
	}

	void processAccelerometer(ofVec3f acc, long sensorTimeStamp) {
		if (this.mBiasEstimate.mState
				== GyroBiasEstimator.Estimate.State.CALIBRATED) {
			return;
		}
		this.mCurrAcc.set(acc);
		boolean eventIsDelayed = sensorTimeStamp
				> this.mLastAccTimeNs + 100000000;
		this.mLastAccTimeNs = sensorTimeStamp;
		if (eventIsDelayed) {
			resetCalibration();
			return;
		}
		smooth(this.mAccSmoothed, this.mCurrAcc, 0.1);
	}

	void getEstimate(Estimate output) {
		output.set(this.mBiasEstimate);
	}

	bool canCalibrateGyro() {
		if (this.mAccSmoothed.length() < 9.999999747378752E-05) {
			return false;
		}
		this.mAccNormalizedTmp.set(this.mAccSmoothed);
		this.mAccNormalizedTmp.normalize();

		if (ofVec3f.dot(this.mAccNormalizedTmp, UP_VECTOR)
				< MIN_ACCEL_DOT_WITH_UP) {
			return false;
		}

		if (this.mGyroMagnitudeDiffSmoothed > 0.01) {
			return false;
		}
		return true;
	}

	void smooth(ofVec3f smoothed, ofVec3f newValue, float smoothingFactor) {
		smoothed.x = (smoothingFactor * newValue.x
				+ (1.0 - smoothingFactor) * smoothed.x);
		smoothed.y = (smoothingFactor * newValue.y
				+ (1.0 - smoothingFactor) * smoothed.y);
		smoothed.z = (smoothingFactor * newValue.z
				+ (1.0 - smoothingFactor) * smoothed.z);
	}
};

