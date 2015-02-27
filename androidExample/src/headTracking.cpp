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
	ofxAccelerometer.setup();
	ofxRegisterAccelEvents(this);
}

ofMatrix4x4 headTracking::getLastHeadView(ofMatrix4x4 headView) {
//	ofLog() << "headTracking getLastHeadView" << endl;
	float secondsSinceLastGyroEvent = (ofGetElapsedTimeMicros()
			- mLastGyroEventTimeNanos) * 1e-6;

	float secondsToPredictForward = secondsSinceLastGyroEvent
			+ 0.03333333333333333;
	ofMatrix4x4 mat = mTracker.getPredictedGLMatrix(secondsToPredictForward);
	mat.makeFromMultiplicationOf(mat, mEkfToHeadTracker);
	headView = mat;
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
//	processSensorEvent(event);
	mTmpRotatedEvent.x = (-event.reading.y);
	mTmpRotatedEvent.y = event.reading.x;
	mTmpRotatedEvent.z = event.reading.z;

	mTracker.processAcc(mTmpRotatedEvent, event.timestamp);
}

void headTracking::gyroChanged(SensorEvent & event) {
	long timeNanos = event.timestamp;
	mTmpRotatedEvent.x = (-event.reading.y);
	mTmpRotatedEvent.y = event.reading.x;
	mTmpRotatedEvent.z = event.reading.z;
	mLastGyroEventTimeNanos = event.timestamp;
	mTracker.processGyro(mTmpRotatedEvent, event.timestamp);
}

//void headTracking::processSensorEvent(SensorEvent event) {
//	long timeNanos = event.timestamp;
//	mTmpRotatedEvent.x = (-event.reading.y);
//	mTmpRotatedEvent.y = event.reading.x;
//	mTmpRotatedEvent.z = event.reading.z;
//
//	if (event.type == ACCEL) {
//		mTracker.processAcc(mTmpRotatedEvent, event.timestamp);
//	} else if (event.type == GYRO) {
//		mLastGyroEventTimeNanos = event.timestamp;
//		mTracker.processGyro(mTmpRotatedEvent, event.timestamp);
//	}
//
//}
