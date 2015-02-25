#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup() {
	ofSetLogLevel(OF_LOG_VERBOSE);
	ofBackground(0, 0, 0);
	ofSetVerticalSync(false);
	tracking.setup();
//	ofLog() << "setup" << endl;
}

//--------------------------------------------------------------
void ofApp::update() {
//	ofLog() << "update" << endl;
}

//--------------------------------------------------------------
void ofApp::draw() {
//	ofLog() << "draw" << endl;
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
