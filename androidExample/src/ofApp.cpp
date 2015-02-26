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
}

//--------------------------------------------------------------
void ofApp::update() {
//	ofLog() << "update" << endl;
}

//--------------------------------------------------------------
void ofApp::draw() {
//	ofLog() << "draw" << endl;

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
	planet.draw();

	glPopMatrix();
	glMatrixMode(GL_PROJECTION);
	glPopMatrix();
	glMatrixMode(GL_MODELVIEW);
	ofPopMatrix();
	ofPopView();
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
