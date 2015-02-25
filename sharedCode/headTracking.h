/*
 * headTracking.h
 *
 *  Created on: Feb 24, 2015
 *      Author: dantheman
 */

#pragma once
#include "ofMain.h"
#include "OrientationEKF.h"
#include "ofxAccelerometer.h"
class headTracking {
public:
	headTracking();
	virtual ~headTracking();
    ofMatrix4x4 getLastHeadView(ofMatrix4x4 headView, int offset);
    void processSensorEvent(SensorEvent event);
    void accelerationChanged(SensorEvent & event);
    void gyroChanged(SensorEvent & event);
private:
    ofMatrix4x4 mEkfToHeadTracker;
    ofMatrix4x4 mTmpHeadView;
    vector<float> mTmpRotatedEvent;
    bool mTracking;
    OrientationEKF mTracker;
    long mLastGyroEventTimeNanos;
};


