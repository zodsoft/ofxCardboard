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
    cam.setPosition(0, 0, 0);
    
}

//--------------------------------------------------------------
void ofApp::update(){
    SensorEvent gyro;
    gyro.reading.set(sin(ofGetElapsedTimef()*0.01231)*0.01, sin(ofGetElapsedTimef()*0.01231), sin(ofGetElapsedTimef()*0.01231)*0.01);
    gyro.timestamp = ofGetElapsedTimeMillis();
    gyro.type = GYRO;
    tracking.processSensorEvent(gyro);
    
    
    SensorEvent accel;
    accel.reading.set(sin(ofGetElapsedTimef()*0.01231), sin(ofGetElapsedTimef()*0.01231)*9.8, sin(ofGetElapsedTimef()*0.01231));
    accel.timestamp = ofGetElapsedTimeMillis();
    accel.type = ACCEL;
    tracking.processSensorEvent(accel);
    

}

//--------------------------------------------------------------
void ofApp::draw(){
    ofBackground(0, 0, 0);
    ofDrawBitmapStringHighlight("Rot :"+ofToString(view.getRotate()), 10, 500);
    view = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(view);
    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);
    
    //    view = node.getLocalTransformMatrix()*view;
    //    rot = cam.getOrientationQuat();
    
    node.setOrientation(transform.getQuaternion());
    cam.setTransformMatrix(node.getGlobalTransformMatrix().getInverse());
    
    //    cam.setOrientation(rot*cam.getOrientationQuat());
    
    
    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);
    ofDrawBitmapStringHighlight("Rot :"+ofToString(view.getRotate()), 10, 400);
    
    ofDrawBitmapStringHighlight("Cardboard Camera", ofGetWidth()-ofGetHeight(), ofGetHeight()/2-20);
    cam.begin(ofRectangle(ofGetWidth()-ofGetHeight(), ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    ofSetColor(255, 0, 255);
    ofSetColor(255, 255, 0);
    ofDrawBox(50, 0, 0, 10, 10, 10);
    ofDrawBox(0, 50, 0, 10, 10, 10);
    ofDrawBox(0, 0, 50, 10, 10, 10);
    ofDrawBox(0, 50, 50, 10, 10, 10);
    ofDrawBox(50, 50, 50, 10, 10, 10);
    
    ofDrawBox(-50, 0, 0, 10, 10, 10);
    ofDrawBox(0, -50, 0, 10, 10, 10);
    ofDrawBox(0, 0, -50, 10, 10, 10);
    ofDrawBox(0, -50, -50, 10, 10, 10);
    ofDrawBox(-50, -50, -50, 10, 10, 10);
    cam.end();
    
    ofDrawBitmapStringHighlight("EasyCam View", ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2-20);
    easycam.lookAt(node);
    easycam.begin(ofRectangle(ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    node.draw();
    easycam.end();
    
    
    easycam.lookAt(cam);
    easycam.begin(ofRectangle(ofGetWidth()-ofGetHeight()/2, 0, ofGetHeight()/2, ofGetHeight()/2));
    cam.draw();
    easycam.end();
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
