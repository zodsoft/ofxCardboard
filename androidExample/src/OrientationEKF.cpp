/*
 * OrientationEKF.cpp
 *
 *  Created on: Feb 24, 2015
 *      Author: dantheman
 */
#include "OrientationEKF.h"
#include "So3Util.h"
OrientationEKF::OrientationEKF() {

}

OrientationEKF::~OrientationEKF() {

}

void OrientationEKF::reset() {
	sensorTimeStampGyro = 0;
	sensorTimeStampAcc = 0;
	sensorTimeStampMag = 0;

	mP.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mP += ofMatrix3x3(25.0, 0, 0, 0, 25.00, 0, 0, 0, 25.0);

	mQ.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mQ += ofMatrix3x3(1.0, 0, 0, 0, 1.0, 0, 0, 0, 1.0);

	mR.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mR += ofMatrix3x3(0.0625, 0, 0, 0, 0.0625, 0, 0, 0, 0.0625);

	mRaccel.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mRaccel += ofMatrix3x3(0.5625, 0, 0, 0, 0.5625, 0, 0, 0, 0.5625);

	mS.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mH.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mK.set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	mNu = ofVec3f(0, 0, 0);
	mz = ofVec3f(0, 0, 0);
	mh = ofVec3f(0, 0, 0);
	mu = ofVec3f(0, 0, 0);
	mx = ofVec3f(0, 0, 0);

	down.set(0.0, 0.0, 9.810000000000001);
	north.set(0.0, 1.0, 0.0);

	lastGyro.set(0, 0, 0);
}
bool OrientationEKF::isReady() {
	return sensorTimeStampAcc != 0;
}
float OrientationEKF::getHeadingDegrees() {
	float x = so3SensorFromWorld.g;
	float y = so3SensorFromWorld.h;
	float mag = sqrt(x * x + y * y);

	if (mag < 0.1) {
		return 0.0;
	}

	double heading = -90.0 - atan2(y, x) / 3.141592653589793 * 180.0;
	if (heading < 0.0) {
		heading += 360.0;
	}
	if (heading >= 360.0) {
		heading -= 360.0;
	}
	return heading;
}
void OrientationEKF::setHeadingDegrees(float heading) {
	float currentHeading = getHeadingDegrees();
	float deltaHeading = heading - currentHeading;
	float s = sin(deltaHeading / 180.0 * 3.141592653589793);
	float c = cos(deltaHeading / 180.0 * 3.141592653589793);

	ofMatrix3x3 setHeadingDegreesTempM1 = ofMatrix3x3(c, -s, 0.0, s, c, 0.0,
			0.0, 0.0, 1.0);
	so3SensorFromWorld = so3SensorFromWorld * setHeadingDegreesTempM1;
}

void OrientationEKF::processGyro(ofVec3f gyro, long sensorTimeStamp) {
	float kTimeThreshold = 0.04;
	float kdTdefault = 0.01;
	if (sensorTimeStampGyro != 0) {
		float dT = (float) (sensorTimeStamp - sensorTimeStampGyro)
				* 0.0000000001;
		if (dT > kTimeThreshold)
			dT = gyroFilterValid ? filteredGyroTimestep : kdTdefault;
		else {
			filterGyroTimestep(dT);
		}

		mu.set(gyro.x * -dT, gyro.y * -dT, gyro.z * -dT);
		So3.sO3FromMu(mu, so3LastMotion);

		processGyroTempM1 = so3SensorFromWorld;
		processGyroTempM1 = so3LastMotion * so3SensorFromWorld;
		so3SensorFromWorld = processGyroTempM1;

		updateCovariancesAfterMotion();

		processGyroTempM2 = mQ;
		processGyroTempM2 *= dT * dT;
		mP += processGyroTempM2;
	}
	sensorTimeStampGyro = sensorTimeStamp;
	lastGyro = gyro;
}

void OrientationEKF::mult(ofMatrix3x3 a, ofVec3f v, ofVec3f& result) {
	float x = a.a * v.x + a.b * v.y + a.c * v.z;
	float y = a.d * v.x + a.e * v.y + a.f * v.z;
	float z = a.g * v.x + a.h * v.y + a.i * v.z;
	result.x = x;
	result.y = y;
	result.z = z;
}

void OrientationEKF::processAcc(ofVec3f acc, long sensorTimeStamp) {
	mz.set(acc.x, acc.y, acc.z);

	if (sensorTimeStampAcc != 0) {
		accObservationFunctionForNumericalJacobian(so3SensorFromWorld, mNu);

		float eps = 1.0E-07f;
		for (int dof = 0; dof < 3; dof++) {
			ofVec3f delta = processAccVDelta;
			if (dof == 0)
				delta.x = eps;
			else if (dof == 1)
				delta.y = eps;
			else
				delta.z = eps;

			So3.sO3FromMu(delta, processAccTempM1);
			processAccTempM2 = processAccTempM1 * so3SensorFromWorld;

			accObservationFunctionForNumericalJacobian(processAccTempM2,
					processAccTempV1);

			ofVec3f withDelta = processAccTempV1;

			processAccTempV2 = mNu - withDelta;
			processAccTempV2 *= 1.0 / eps;
			if (dof == 0) {
				mH.a = processMagTempV2.x;
				mH.d = processMagTempV2.y;
				mH.g = processMagTempV2.z;
			} else if (dof == 1) {
				mH.b = processMagTempV2.x;
				mH.e = processMagTempV2.y;
				mH.h = processMagTempV2.z;
			} else {
				mH.c = processMagTempV2.x;
				mH.f = processMagTempV2.y;
				mH.i = processMagTempV2.z;
			}
		}

		processAccTempM3 = mH;
		processAccTempM3.transpose();
		processAccTempM4 = mP * processAccTempM3;
		processAccTempM5 = mH * processAccTempM4;
		mS = processAccTempM5 + mRaccel;

		processAccTempM3 = mS;
		processAccTempM3.invert();
		processAccTempM4 = mH;
		processAccTempM4.transpose();
		processAccTempM5 = processAccTempM4 * processAccTempM3;
		mK = mP * processAccTempM5;

		mult(mK, mNu, mx);

		processAccTempM3 = mK * mH;
		processAccTempM4.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
		processAccTempM4 -= processAccTempM3;
		processAccTempM3 = processAccTempM4 * mP;
		mP = processAccTempM3;

		So3.sO3FromMu(mx, so3LastMotion);

		so3SensorFromWorld = so3LastMotion * so3SensorFromWorld;

		updateCovariancesAfterMotion();
	} else {
		So3.sO3FromTwoVec(down, mz, so3SensorFromWorld);
	}
	sensorTimeStampAcc = sensorTimeStamp;
}
void OrientationEKF::processMag(ofVec3f mag, long sensorTimeStamp) {
	mz.set(mag.x, mag.y, mag.z);
	mz.normalize();

	ofVec3f downInSensorFrame;

	downInSensorFrame.x = so3SensorFromWorld.c;
	downInSensorFrame.y = so3SensorFromWorld.f;
	downInSensorFrame.z = so3SensorFromWorld.i;

	processMagTempV1 = mz.getCrossed(downInSensorFrame);
	ofVec3f perpToDownAndMag = processMagTempV1;
	perpToDownAndMag.normalize();

	processMagTempV2 = downInSensorFrame.getCrossed(perpToDownAndMag);
	ofVec3f magHorizontal = processMagTempV2;

	magHorizontal.normalize();
	mz.set(magHorizontal);

	if (sensorTimeStampMag != 0L) {
		magObservationFunctionForNumericalJacobian(so3SensorFromWorld, mNu);

		float eps = 1.0E-07;
		for (int dof = 0; dof < 3; dof++) {
			ofVec3f delta = processMagTempV3;
			if (dof == 0)
				delta.x = eps;
			else if (dof == 1)
				delta.y = eps;
			else
				delta.z = eps;

			So3.sO3FromMu(delta, processMagTempM1);
			processMagTempM2 = processMagTempM1 * so3SensorFromWorld;

			magObservationFunctionForNumericalJacobian(processMagTempM2,
					processMagTempV4);

			ofVec3f withDelta = processMagTempV4;

			processMagTempV5 = mNu - withDelta;
			processMagTempV5 *= 1.0 / eps;

			if (dof == 0) {
				mH.a = processMagTempV5.x;
				mH.d = processMagTempV5.y;
				mH.g = processMagTempV5.z;
			} else if (dof == 1) {
				mH.b = processMagTempV5.x;
				mH.e = processMagTempV5.y;
				mH.h = processMagTempV5.z;
			} else {
				mH.c = processMagTempV5.x;
				mH.f = processMagTempV5.y;
				mH.i = processMagTempV5.z;
			}
		}

		mH.transpose(processMagTempM4);
		processMagTempM5 = mP * processMagTempM4;
		processMagTempM6 = mH * processMagTempM5;
		mS = processMagTempM6 + mR;

		processMagTempM4 = mS;
		processMagTempM4.invert();
		processMagTempM5 = mH;
		processMagTempM5.transpose();
		processMagTempM6 = processMagTempM5 * processMagTempM4;
		mK = mP * processMagTempM6;

		mult(mK, mNu, mx);

		processMagTempM4 = mK * mH;
		processMagTempM5.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
		processMagTempM5 -= processMagTempM4;
		processMagTempM4 = processMagTempM5 * mP;
		mP = processMagTempM4;

		So3.sO3FromMu(mx, so3LastMotion);

		processMagTempM4 = so3LastMotion * so3SensorFromWorld;
		so3SensorFromWorld = processMagTempM4;

		updateCovariancesAfterMotion();
	} else {
		magObservationFunctionForNumericalJacobian(so3SensorFromWorld, mNu);
		So3.sO3FromMu(mx, so3LastMotion);

		processMagTempM4 = so3LastMotion * so3SensorFromWorld;
		so3SensorFromWorld = processMagTempM4;

		updateCovariancesAfterMotion();
	}
	sensorTimeStampMag = sensorTimeStamp;
}

ofMatrix4x4 OrientationEKF::getGLMatrix() {
	return glMatrixFromSo3(so3SensorFromWorld);
}

ofMatrix4x4 OrientationEKF::getPredictedGLMatrix(
		float secondsAfterLastGyroEvent) {
	ofLog() << "headTracking getLastHeadView" << secondsAfterLastGyroEvent
			<< endl;
	float dT = secondsAfterLastGyroEvent;
	ofVec3f pmu = getPredictedGLMatrixTempV1;
	pmu.set(lastGyro.x * -dT, lastGyro.y * -dT, lastGyro.z * -dT);
	ofMatrix3x3 so3PredictedMotion;
	;
	So3.sO3FromMu(pmu, so3PredictedMotion);

	ofMatrix3x3 so3PredictedState;
	so3PredictedState = so3PredictedMotion * so3SensorFromWorld;

	return glMatrixFromSo3(so3PredictedState);
}

ofMatrix4x4 OrientationEKF::glMatrixFromSo3(ofMatrix3x3 so3) {
	ofMatrix4x4 rotationMatrix;
	float * ptr = rotationMatrix.getPtr();
	for (int r = 0; r < 3; r++) {
		for (int c = 0; c < 3; c++) {
			if (r == 0) {
				if (c == 0) {
					ptr[(4 * c + r)] = so3.a;
				} else if (c == 1) {
					ptr[(4 * c + r)] = so3.b;
				} else {
					ptr[(4 * c + r)] = so3.c;
				}
			} else if (r == 1) {
				if (c == 0) {
					ptr[(4 * c + r)] = so3.d;
				} else if (c == 1) {
					ptr[(4 * c + r)] = so3.e;
				} else {
					ptr[(4 * c + r)] = so3.f;
				}
			} else {
				if (c == 0) {
					ptr[(4 * c + r)] = so3.g;
				} else if (c == 1) {
					ptr[(4 * c + r)] = so3.h;
				} else {
					ptr[(4 * c + r)] = so3.i;
				}
			}

		}
	}
	float tmp62_61 = (ptr[11] = 0.0);
	ptr[7] = tmp62_61;
	ptr[3] = tmp62_61;
	float tmp86_85 = (ptr[14] = 0.0);
	ptr[13] = tmp86_85;
	ptr[12] = tmp86_85;

	ptr[15] = 1.0;

	return rotationMatrix;
}
void OrientationEKF::filterGyroTimestep(float timeStep) {
	float kFilterCoeff = 0.95;
	float kMinSamples = 10.0;
	if (!timestepFilterInit) {
		filteredGyroTimestep = timeStep;
		numGyroTimestepSamples = 1;
		timestepFilterInit = true;
	} else {
		filteredGyroTimestep = (kFilterCoeff * filteredGyroTimestep
				+ 0.05000001 * timeStep);

		if (++numGyroTimestepSamples > kMinSamples)
			gyroFilterValid = true;
	}

}
void OrientationEKF::updateCovariancesAfterMotion() {
	updateCovariancesAfterMotionTempM1 = so3LastMotion;
	updateCovariancesAfterMotionTempM1.transpose();
	updateCovariancesAfterMotionTempM2 = mP
			* updateCovariancesAfterMotionTempM1;
	mP = so3LastMotion * updateCovariancesAfterMotionTempM2;
	so3LastMotion.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
}
void OrientationEKF::accObservationFunctionForNumericalJacobian(
		ofMatrix3x3 so3SensorFromWorldPred, ofVec3f & result) {
	mult(so3SensorFromWorldPred, down, mh);
	So3.sO3FromTwoVec(mh, mz, accObservationFunctionForNumericalJacobianTempM);

	So3.muFromSO3(accObservationFunctionForNumericalJacobianTempM, result);

}
void OrientationEKF::magObservationFunctionForNumericalJacobian(
		ofMatrix3x3 so3SensorFromWorldPred, ofVec3f & result) {
	mult(so3SensorFromWorldPred, north, mh);
	So3.sO3FromTwoVec(mh, mz, magObservationFunctionForNumericalJacobianTempM);

	So3.muFromSO3(magObservationFunctionForNumericalJacobianTempM, result);

}
ofVec3f OrientationEKF::getLastAccel() {
	return mz;
}
ofVec3f OrientationEKF::getLastGyro() {
	return lastGyro;
}
void OrientationEKF::arrayAssign(vector<vector<float> > data, ofMatrix3x3 m) {
	m.set(data[0][0], data[0][1], data[0][2], data[1][0], data[1][1],
			data[1][2], data[2][0], data[2][1], data[2][2]);
}
