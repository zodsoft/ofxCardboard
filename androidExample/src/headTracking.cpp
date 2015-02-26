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
    mTmpHeadView.assign(16, 0.0);
    ofQuaternion rotation = ofQuaternion(180.0, ofVec3f(0, 1, 0), 180.0, ofVec3f(1, 0, 0), 0, ofVec3f(0, 0, 1));
    mEkfToHeadTracker.makeRotationMatrix(rotation);
    mTracker.reset();
	ofxAccelerometer.setup();
	ofxRegisterAccelEvents(this);
}

ofMatrix4x4 headTracking::getLastHeadView(ofMatrix4x4  headView) {
//	ofLog() << "headTracking getLastHeadView" << endl;
	float secondsSinceLastGyroEvent = (ofGetElapsedTimeMillis()
			- mLastGyroEventTimeNanos) * 0.0000000001;

	float secondsToPredictForward = secondsSinceLastGyroEvent
			+ 0.03333333333333333;
	ofMatrix4x4 mat = mTracker.getPredictedGLMatrix(secondsToPredictForward);
	headView.makeFromMultiplicationOf(mat, mEkfToHeadTracker);
	return headView;
}

void headTracking::accelerationChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::gyroChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::processSensorEvent(SensorEvent event) {
	long timeNanos = ofGetElapsedTimeMillis();
	mTmpRotatedEvent.x = (-event.reading.y);
	mTmpRotatedEvent.y = event.reading.x;
	mTmpRotatedEvent.z = event.reading.z;

	if (event.type == ACCEL) {
		mTracker.processAcc(event.reading, event.timestamp);
	} else if (event.type == GYRO) {
		mLastGyroEventTimeNanos = timeNanos;
		mTracker.processGyro(mTmpRotatedEvent, event.timestamp);
	}

}
