#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
//    ofSetVerticalSync(true);
//    ofSetFrameRate(60);
    tracking.setup();
}

//--------------------------------------------------------------
void ofApp::update(){
    SensorEvent gyro;
    gyro.reading.set(90*sin(ofGetElapsedTimef()*0.091231), 0, 0);
    gyro.timestamp = ofGetElapsedTimeMillis();
    gyro.type = GYRO;
    tracking.processSensorEvent(gyro);
    
    
    SensorEvent accel;
    accel.reading.set(sin(ofGetElapsedTimef()*0.091231), -9.8, sin(ofGetElapsedTimef()*0.091231));
    accel.timestamp = ofGetElapsedTimeMillis();
    accel.type = ACCEL;
    tracking.processSensorEvent(accel);
}

//--------------------------------------------------------------
void ofApp::draw(){
    ofPushMatrix();
    ofMatrix4x4 foo = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(foo);
    //	glPushMatrix();
    //	glMatrixMode(GL_PROJECTION);
    //	glPushMatrix();
    //	glMatrixMode(GL_MODELVIEW);
    //	glMultMatrixf((GLfloat*)transform.getHeadView().getPtr());
    ofSetColor(0, 0, 0);
    ofDrawBitmapString(ofToString(transform.getHeadView()), 100, 200);
    ofDrawBitmapString(ofToString(tracking.mTracker.getLastGyro()), 100, 400);
    ofDrawBitmapString(ofToString(tracking.mTracker.getLastAccel()), 100, 500);
    //	glPopMatrix();
    //	glMatrixMode(GL_PROJECTION);
    //	glPopMatrix();
    //	glMatrixMode(GL_MODELVIEW);
    ofPopMatrix();
}

//--------------------------------------------------------------
void ofApp::keyPressed(int key){

}

//--------------------------------------------------------------
void ofApp::keyReleased(int key){

}

//--------------------------------------------------------------
void ofApp::mouseMoved(int x, int y ){

}

//--------------------------------------------------------------
void ofApp::mouseDragged(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mousePressed(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::mouseReleased(int x, int y, int button){

}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h){

}

//--------------------------------------------------------------
void ofApp::gotMessage(ofMessage msg){

}

//--------------------------------------------------------------
void ofApp::dragEvent(ofDragInfo dragInfo){ 

}
