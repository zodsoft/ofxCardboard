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
	mEkfToHeadTracker = ofMatrix4x4();
	ofQuaternion rotation = ofQuaternion(0.0, ofVec3f(0, 1, 0), -90.0,
			ofVec3f(1, 0, 0), 0, ofVec3f(0, 0, 1));
	mEkfToHeadTracker.setRotate(rotation);
	mTracker.reset();
	mNeckModelTranslation.makeIdentityMatrix();
	mNeckModelTranslation.setTranslation(0.0, -0.075, 0.08);
	ofxAccelerometer.setup();
	ofxRegisterAccelEvents(this);
}

ofMatrix4x4 headTracking::getLastHeadView(ofMatrix4x4 headView) {
	float rotation = 0.0;

	if (currentRotation == 0) {
		rotation = 0;
	} else if (currentRotation == 1) {
		rotation = 90.0;
	} else if (currentRotation == 2) {
		rotation = 180.0;
	} else {
		rotation = 270.0;
	}

	if (rotation != mDisplayRotation) {
		mDisplayRotation = rotation;
		mSensorToDisplay.setRotate(
				ofQuaternion(0.0, ofVec3f(0, 1, 0), 0.0, ofVec3f(1, 0, 0),
						-rotation, ofVec3f(0, 0, 1)));
		mEkfToHeadTracker.setRotate(
				ofQuaternion(0.0, ofVec3f(0, 1, 0), -90.0, ofVec3f(1, 0, 0), 0,
						ofVec3f(0, 0, 1)));
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
	mEkfToHeadTracker = ofMatrix4x4();
	ofQuaternion rotation = ofQuaternion(0.0, ofVec3f(0, 1, 0), 180.0,
			ofVec3f(1, 0, 0), 0, ofVec3f(0, 0, 1));
	mEkfToHeadTracker.setRotate(rotation);
	mTracker.reset();
}

void headTracking::accelerationChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::gyroChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::processSensorEvent(SensorEvent event) {
	if (event.type == ACCEL) {
		mLatestAcc = event.reading;
		mTracker.processAcc(mLatestAcc, event.timestamp);
	} else if (event.type == GYRO) {
		mLastGyroEventTimeNanos = ofGetElapsedTimeMillis();
		mLatestGyro = event.reading;
		mLatestGyro = mLatestGyro - mGyroBias;
		mTracker.processGyro(mLatestGyro, event.timestamp);
	}
}
