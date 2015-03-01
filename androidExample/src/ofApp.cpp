#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup() {
	ofSetOrientation(OF_ORIENTATION_90_LEFT);
	ofSetLogLevel(OF_LOG_VERBOSE);
	ofBackground(0, 0, 0);
	ofSetVerticalSync(false);

	planet.setUseVbo(false);
	planet.set(1000, 100);
	planet.setPosition(0, 0, 0);
//	ofLog() << "setup" << endl;

	easycam.setDistance(20);
	cam.setPosition(0, 0, 0);

	ofxAccelerometer.setup();
	ofxRegisterAccelEvents(this);

	tracking.reset();
	invert = ofNode();
	node = ofNode();
	view = ofMatrix4x4();
}

void ofApp::accelerationChanged(SensorEvent & event) {
	accelEvent = event;
	tracking.processSensorEvent(accelEvent);
}

void ofApp::gyroChanged(SensorEvent & event) {
	gyroEvent = event;
	tracking.processSensorEvent(gyroEvent);
}
//--------------------------------------------------------------
void ofApp::update() {

}

//--------------------------------------------------------------
void ofApp::draw() {

	ofBackground(0, 0, 0);
	ofDrawBitmapStringHighlight("Rot :" + ofToString(view.getRotate()), 10,
			500);
	ofMatrix4x4 headView;
	headView.makeIdentityMatrix();
	headView = tracking.getLastHeadView(transform.getHeadView());

//    ofMatrix4x4 translate;
//    translate.makeTranslationMatrix(ofVec3f(0.06, 0, 0));
//    view.makeIdentityMatrix();
//    view*=headView;

	transform.setMatrix(headView);

	invert = node;
	node.setTransformMatrix(headView);
//	if(cam.getOrientationEuler().lengthSquared() - node.getLookAtDir().lengthSquared() != 0){
		rot.slerp(0.5, invert.getOrientationQuat(), node.getOrientationQuat());
		node.setOrientation(rot);
		cam.setTransformMatrix(node.getGlobalTransformMatrix().getInverse());
//	}

	cam.setPosition(-0.6/2.0, 0, 0);
//    cam.setOrientation(rot);

	ofSetColor(255, 0, 255);
	ofDrawBitmapStringHighlight(
			"HeadView: " + ofToString(transform.getHeadView(), 10), 10, 100);
	ofDrawBitmapStringHighlight(
			"Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
	ofDrawBitmapStringHighlight(
			"Accel :" + ofToString(tracking.mTracker.getLastAccel()), 10, 300);
	ofDrawBitmapStringHighlight("Rot :" + ofToString(node.getOrientationQuat()), 10,
			400);

	ofDrawBitmapStringHighlight("Cardboard Camera",
			ofGetWidth() - ofGetHeight(), ofGetHeight() / 2 - 20);
	cam.begin(
			ofRectangle(0, 0,
					ofGetWidth() / 2, ofGetHeight()));
	ofSetColor(255, 0, 255);
	ofPushMatrix();
//    ofVec3f axis;
//    float angle;
//    rot.getRotate(angle, axis);
//	ofRotate(angle, axis.x, axis.y, axis.z);
	planet.drawWireframe();
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
	ofPopMatrix();
	cam.end();


	cam.setPosition(0.6/2.0, 0, 0);
	cam.begin(
				ofRectangle(ofGetWidth() / 2, 0,
						ofGetWidth() / 2, ofGetHeight()));
		ofSetColor(255, 0, 255);
		ofPushMatrix();
//	    ofVec3f axis;
//	    float angle;
//	    rot.getRotate(angle, axis);
//		ofRotate(angle, axis.x, axis.y, axis.z);
		planet.drawWireframe();
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
		ofPopMatrix();
		cam.end();

//	ofDrawBitmapStringHighlight("EasyCam View",
//			ofGetWidth() - ofGetHeight() / 2, ofGetHeight() / 2 - 20);
//	easycam.lookAt(node);
//	easycam.begin(
//			ofRectangle(ofGetWidth() - ofGetHeight() / 2, ofGetHeight() / 2,
//					ofGetHeight() / 2, ofGetHeight() / 2));
//	node.draw();
//	easycam.end();
//
//	easycam.lookAt(cam);
//	easycam.begin(
//			ofRectangle(ofGetWidth() - ofGetHeight() / 2, 0, ofGetHeight() / 2,
//					ofGetHeight() / 2));
//	cam.draw();
//	easycam.end();

	ofSetColor(255, 0, 255);
	ofDrawBitmapStringHighlight(
			"HeadView: " + ofToString(transform.getHeadView(), 10), 10, 100);
	ofDrawBitmapStringHighlight(
			"Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
	ofDrawBitmapStringHighlight(
			"Accel :" + ofToString(tracking.mTracker.getLastAccel()), 10, 300);

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
	tracking.reset();

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
	planet.setUseVbo(false);
	planet.set(1000, 100);
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
