#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup(){
//    ofSetVerticalSync(true);
//    ofSetFrameRate(60);
    tracking.setup();
    
    SensorEvent gyro;
    gyro.reading.set(0, 0, 0);
    gyro.timestamp = ofGetElapsedTimeMillis();
    gyro.type = GYRO;
    tracking.processSensorEvent(gyro);
    
    
    SensorEvent accel;
    accel.reading.set(sin(ofGetElapsedTimef()*0.091231)*10.0, -1, sin(ofGetElapsedTimef()*0.091231));
    accel.timestamp = ofGetElapsedTimeMillis();
    accel.type = ACCEL;
    tracking.processSensorEvent(accel);
}

//--------------------------------------------------------------
void ofApp::update(){
    SensorEvent gyro;
    gyro.reading.set(sin(ofGetElapsedTimef()), sin(ofGetElapsedTimef()), sin(ofGetElapsedTimef()));
    gyro.timestamp = ofGetElapsedTimeMillis();
    gyro.type = GYRO;
    tracking.processSensorEvent(gyro);
    
    
    SensorEvent accel;
    accel.reading.set(cos(ofGetElapsedTimef())*9.8, sin(ofGetElapsedTimef()), sin(ofGetElapsedTimef()));
    accel.timestamp = ofGetElapsedTimeMillis();
    accel.type = ACCEL;
    tracking.processSensorEvent(accel);
}

//--------------------------------------------------------------
void ofApp::draw(){
    ofMatrix4x4 view = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(view);
    ofSetColor(255, 0, 255);
    ofDrawBitmapString(ofToString(transform.getHeadView(), 20), 100, 200);
    ofDrawBitmapString(ofToString(tracking.mTracker.getLastGyro()), 100, 400);
    ofDrawBitmapString(ofToString(tracking.mTracker.getLastAccel()), 100, 500);
    
    //	float aspectRatio = screen.getWidth() / screen.getHeight();
    //	Matrix.perspectiveM(mMonocular.getTransform().getPerspective(), 0, cdp.getFovY(), aspectRatio, mZNear, mZFar);
    
    ofMatrix4x4 perspective;
    perspective.makePerspectiveMatrix(65.0, ofGetWidth() / ofGetHeight(), 0,
                                      1000);
    ofPushView();
    ofViewport(ofRectangle(0, 0, ofGetWidth(), ofGetHeight()));
    ofPushMatrix();
    glPushMatrix();
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glMatrixMode(GL_MODELVIEW);
    ofSetMatrixMode(OF_MATRIX_PROJECTION);
    ofLoadIdentityMatrix();
    ofLoadMatrix(perspective);
    ofSetMatrixMode(OF_MATRIX_MODELVIEW);
    ofLoadIdentityMatrix();
    ofLoadMatrix( ( view ));
    ofSetColor(255, 255, 0);
    ofDrawBox(0, 0, 0, 10, 10, 10);
    
    glPopMatrix();
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glMatrixMode(GL_MODELVIEW);
    ofPopMatrix();
    ofPopView();}

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
