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
    
    easycam.enableMouseInput();
    easycam.setDistance(20);
    cam.setNearClip(0);
    cam.setFarClip(1000);
    cam.setPosition(ofVec3f(0, 0, -500));

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
    ofBackground(10, 10, 10);
    view = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(view);
    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);
    
    //	float aspectRatio = screen.getWidth() / screen.getHeight();
    //	Matrix.perspectiveM(mMonocular.getTransform().getPerspective(), 0, cdp.getFovY(), aspectRatio, mZNear, mZFar);
    
    ofDrawBitmapStringHighlight("EasyCam View", ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2-20);
    node.setTransformMatrix(view);
    easycam.lookAt(node);
    easycam.begin(ofRectangle(ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    node.draw();
    easycam.end();
    
//    cam.lookAt(node);
   
    cam.lookAt(ofVec3f(0, 0, 0), ofVec3f(0, 1, 0));
    cam.setPosition(0, 0, -500);
    ofMatrix4x4 foo;
    foo.makeFromMultiplicationOf(cam.getProjectionMatrix(), view);
    cam.setTransformMatrix(foo);


    ofDrawBitmapStringHighlight("ofCamera ProjectionMatrix * HeadViewMatrix", ofGetWidth()-ofGetHeight(), ofGetHeight()/2-20);
    cam.begin(ofRectangle(ofGetWidth()-ofGetHeight(), ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    ofSetColor(255, 255, 255);
    node.draw();
    cam.end();

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
