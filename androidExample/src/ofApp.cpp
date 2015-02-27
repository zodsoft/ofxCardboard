#include "ofApp.h"

//--------------------------------------------------------------
void ofApp::setup() {
	ofSetLogLevel(OF_LOG_VERBOSE);
	ofBackground(0, 0, 0);
	ofSetVerticalSync(false);
	tracking.setup();
	planet.setUseVbo(false);
	planet.set(1000, 100);
	planet.setPosition(0, 0, 0);
//	ofLog() << "setup" << endl;

   easycam.setDistance(20);
   cam.setPosition(0, 0, 0);
}

//--------------------------------------------------------------
void ofApp::update() {
//	ofLog() << "update" << endl;

}

//--------------------------------------------------------------
void ofApp::draw() {
	ofBackground(0, 0, 0);
	ofDrawBitmapStringHighlight("Rot :"+ofToString(view.getRotate()), 10, 500);
    view = tracking.getLastHeadView(transform.getHeadView());
    transform.setMatrix(view);
    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);

//    view = node.getLocalTransformMatrix()*view;
    rot *= transform.getQuaternion();

    node.setOrientation(transform.getQuaternion());
    cam.setTransformMatrix(view.getInverse());

//    cam.setOrientation(rot*cam.getOrientationQuat());


    ofSetColor(255, 0, 255);
    ofDrawBitmapStringHighlight("HeadView: "+ofToString(transform.getHeadView(), 10), 10, 100);
    ofDrawBitmapStringHighlight("Gyro :" + ofToString(tracking.mTracker.getLastGyro()), 10, 200);
    ofDrawBitmapStringHighlight("Accel :"+ofToString(tracking.mTracker.getLastAccel()), 10, 300);
    ofDrawBitmapStringHighlight("Rot :"+ofToString(view.getRotate()), 10, 400);

    ofDrawBitmapStringHighlight("Cardboard Camera", ofGetWidth()-ofGetHeight(), ofGetHeight()/2-20);
    cam.begin(ofRectangle(ofGetWidth()-ofGetHeight(), ofGetHeight()/2, ofGetHeight()/2, ofGetHeight()/2));
    ofSetColor(255, 0, 255);
    ofPushMatrix();
    ofVec3f axis;
    float angle;
    rot.getRotate(angle, axis);
	ofRotate(angle, axis.x, axis.y, axis.z);
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
	view = ofMatrix4x4();
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
	planet.set(100, 10);
	planet.setPosition(0, 0, -25);
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
