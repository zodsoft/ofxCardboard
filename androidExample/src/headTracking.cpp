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
    mEkfToHeadTracker.set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//    ofQuaternion rotation = ofQuaternion(90, 1, 0, 0);
//    mEkfToHeadTracker.setRotate(rotation);
    mTracker.reset();
	ofxAccelerometer.setup();
	ofxRegisterAccelEvents(this);
}

ofMatrix4x4 headTracking::getLastHeadView(ofMatrix4x4  headView) {
//	ofLog() << "headTracking getLastHeadView" << endl;
	float secondsSinceLastGyroEvent = (ofGetElapsedTimeMicros()
			- mLastGyroEventTimeNanos) * 0.0000000001;

	float secondsToPredictForward = secondsSinceLastGyroEvent
			+ 0.03333333333333333;
	ofMatrix4x4 mat = mTracker.getPredictedGLMatrix(secondsToPredictForward);
	for (int i = 0; i < mTmpHeadView.size(); i++) {
		mTmpHeadView[i] = ((float) mat.getPtr()[i]);
	}
    
    ofMatrix4x4 rotationMatrix = ofMatrix4x4(mTmpHeadView[0], mTmpHeadView[1], mTmpHeadView[2], mTmpHeadView[3], mTmpHeadView[4], mTmpHeadView[5], mTmpHeadView[6], mTmpHeadView[7], mTmpHeadView[8], mTmpHeadView[9], mTmpHeadView[10], mTmpHeadView[11], mTmpHeadView[12], mTmpHeadView[13], mTmpHeadView[14], mTmpHeadView[15]);

	mEkfToHeadTracker = headView * rotationMatrix;
	return mEkfToHeadTracker;
}

void headTracking::accelerationChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::gyroChanged(SensorEvent & event) {
	processSensorEvent(event);
}

void headTracking::processSensorEvent(SensorEvent event) {
	long timeNanos = ofGetElapsedTimeMicros();
	//ofLog() << "headTracking processSensorEvent" << endl;
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
