/*
 * headTracking.cpp
 *
 *  Created on: Feb 24, 2015
 *      Author: dantheman
 */

#include "headTracking.h"

headTracking::headTracking() {

}

headTracking::~headTracking() {

}

void headTracking::setup() {

	mNeckModelTranslation.makeIdentityMatrix();
	mNeckModelTranslation.makeTranslationMatrix(ofVec3f(0.0, -0.075, 0.08));

	mNeckModelEnabled = false;
	reset();
}

ofMatrix4x4 headTracking::getLastHeadView(ofMatrix4x4 headView) {
	if (!mTracker.isReady()) {
		return headView;
	}
	ofOrientation fooRot = ofGetOrientation();
	float rotation = 0.0;
	if (fooRot == OF_ORIENTATION_DEFAULT) {
		rotation = 0.0;
	} else if (fooRot == OF_ORIENTATION_90_LEFT) {
		rotation = 90.0;
	} else if (fooRot == OF_ORIENTATION_90_RIGHT) {
		rotation = 180.0;
	} else {
		rotation = 270.0;
	}

	if (fooRot != mDisplayRotation) {
		mDisplayRotation = fooRot;

		mSensorToDisplay.makeRotationMatrix(
				ofQuaternion(0.0, ofVec3f(1, 0, 0), -rotation, ofVec3f(0, 0, 1),
						0.0, ofVec3f(0, 1, 0)));
		mEkfToHeadTracker.makeRotationMatrix(
				ofQuaternion(0.0, ofVec3f(1, 0, 0), rotation, ofVec3f(0, 0, 1),
						0.0, ofVec3f(0, 0, 1)));
	}
	float secondsSinceLastGyroEvent = (ofGetElapsedTimeMicros()
			- mLastGyroEventTimeNanos) * 1e-6;

	float secondsToPredictForward = secondsSinceLastGyroEvent
			+ 0.03333333333333333;
	mTmpHeadView = mTracker.getPredictedGLMatrix(secondsToPredictForward);
	mTmpHeadView2.makeFromMultiplicationOf(mSensorToDisplay, mTmpHeadView);
	headView.makeFromMultiplicationOf(mTmpHeadView2, mEkfToHeadTracker);

	if (mNeckModelEnabled) {
		mTmpHeadView.makeFromMultiplicationOf(mNeckModelTranslation, headView);
		mTmpHeadView.translate(ofVec3f(0, 0.075, 0.0));
		headView = mTmpHeadView;
	}
	return headView;
}

void headTracking::reset() {
	mTracker.reset();
	gyroBiasEstimator.reset();
}

void headTracking::processSensorEvent(SensorEvent event) {
	if (event.type == ACCEL) {
		mLatestAcc = event.reading;
		mTracker.processAcc(mLatestAcc, event.timestamp);
		gyroBiasEstimator.processAccelerometer(event.reading, event.timestamp);
	} else if (event.type == GYRO) {
		mLastGyroEventTimeNanos = event.timestamp;
		mLatestGyro = event.reading;
		gyroBiasEstimator.processGyroscope(mLatestGyro, event.timestamp);
		mGyroBias  = gyroBiasEstimator.getGyroBias();
		mLatestGyro-=mGyroBias;
		mTracker.processGyro(mLatestGyro, event.timestamp);
	}
}
