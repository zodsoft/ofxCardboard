#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup() {
	ofSetLogLevel(OF_LOG_VERBOSE);
	ofBackground(0, 0, 0);
	ofSetVerticalSync(false);
	tracking.setup();
	planet.setUseVbo(false);
	planet.set(10, 100);
	planet.setPosition(0, 0, 0);
//	ofLog() << "setup" << endl;

//    easycam.setDistance(200);
    cam.setNearClip(0);
    cam.setFarClip(1000);
    cam.setPosition(ofVec3f(0, 0, 200));
}

//--------------------------------------------------------------
void ofApp::update() {
//	ofLog() << "update" << endl;
}

//--------------------------------------------------------------
void ofApp::draw() {
    view = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(view);
    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);


    cam.lookAt(ofVec3f(0, 0, 0), ofVec3f(0, 1, 0));
    cam.setPosition(0, 0, -500);
    ofMatrix4x4 foo;
    foo.makeFromMultiplicationOf(cam.getGlobalTransformMatrix(), node.getGlobalTransformMatrix());
    cam.setTransformMatrix(foo);

    ofDrawBitmapStringHighlight("ofCamera ProjectionMatrix * HeadViewMatrix", ofGetWidth()-ofGetHeight(), ofGetHeight()/2-20);
    cam.begin(ofRectangle(ofGetWidth()-ofGetHeight(), ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    ofSetColor(255, 255, 0);
    node.draw();
    planet.draw();
    cam.end();


    ofDrawBitmapStringHighlight("EasyCam View", ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2-20);
    node.setTransformMatrix(view);
    easycam.lookAt(node, ofVec3f(0, 1, 0));
    easycam.begin(ofRectangle(ofGetWidth()-ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    node.draw();
    cam.draw();
    easycam.end();


}

//--------------------------------------------------------------
void ofApp::keyPressed(int key) {
}

//--------------------------------------------------------------
void ofApp::keyReleased(int key) {

}

//--------------------------------------------------------------
void ofApp::windowResized(int w, int h) {
	ofLog() << "windowResized" << endl;
}

//--------------------------------------------------------------
void ofApp::touchDown(int x, int y, int id) {

}

//--------------------------------------------------------------
void ofApp::touchMoved(int x, int y, int id) {

}

//--------------------------------------------------------------
void ofApp::touchUp(int x, int y, int id) {
	tracking.setup();
}

//--------------------------------------------------------------
void ofApp::touchDoubleTap(int x, int y, int id) {

}

//--------------------------------------------------------------
void ofApp::touchCancelled(int x, int y, int id) {

}

//--------------------------------------------------------------
void ofApp::swipe(ofxAndroidSwipeDir swipeDir, int id) {

}

//--------------------------------------------------------------
void ofApp::pause() {

}

//--------------------------------------------------------------
void ofApp::stop() {

}

//--------------------------------------------------------------
void ofApp::resume() {

}

//--------------------------------------------------------------
void ofApp::reloadTextures() {
	planet.set(10, 100);
	planet.setPosition(0, 0, 0);
}

//--------------------------------------------------------------
bool ofApp::backPressed() {
	return false;
}

//--------------------------------------------------------------
void ofApp::okPressed() {

}
;

//--------------------------------------------------------------
void ofApp::cancelPressed() {

}
;
