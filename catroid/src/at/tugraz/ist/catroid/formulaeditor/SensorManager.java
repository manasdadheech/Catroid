/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.formulaeditor;

import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.content.Costume;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SensorManager {
	private static Input sensors = null;

	public static void setSensorSourceForNextCall(Input source) {
		sensors = source;
	}

	public static Double getSensorValue(String sensorName) {
		if (sensors == null) {
			sensors = Gdx.input;
		}
		Double sensorValue = 0.0;
		if (sensorName.equals(Sensors.X_ACCELERATION_.sensorName)) {

			sensorValue = Double.valueOf(sensors.getAccelerometerX());
		}
		if (sensorName.equals(Sensors.Y_ACCELERATION_.sensorName)) {
			sensorValue = Double.valueOf(-sensors.getAccelerometerY());
		}
		if (sensorName.equals(Sensors.Z_ACCELERATION_.sensorName)) {
			sensorValue = Double.valueOf(-sensors.getAccelerometerZ());
		}
		if (sensorName.equals(Sensors.AZIMUTH_ORIENTATION_.sensorName)) {
			sensorValue = Double.valueOf(sensors.getAzimuth());
		}
		if (sensorName.equals(Sensors.PITCH_ORIENTATION_.sensorName)) {
			sensorValue = Double.valueOf(sensors.getPitch());
		}
		if (sensorName.equals(Sensors.ROLL_ORIENTATION_.sensorName)) {
			sensorValue = Double.valueOf(-sensors.getRoll());
		}
		//SPRITE VALUES

		if (getCurrentObjectLook() == null) {
			return 0d;
		}

		if (sensorName.equals(Sensors.LOOK_X_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().getXPosition());
		}
		if (sensorName.equals(Sensors.LOOK_Y_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().getYPosition());
		}
		if (sensorName.equals(Sensors.LOOK_GHOSTEFFECT_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().getAlphaValue());
		}
		if (sensorName.equals(Sensors.LOOK_BRIGHTNESS_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().getBrightnessValue());
		}
		if (sensorName.equals(Sensors.LOOK_SIZE_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().scaleX);
		}
		if (sensorName.equals(Sensors.LOOK_ROTATION_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().rotation);
		}
		if (sensorName.equals(Sensors.LOOK_LAYER_.sensorName)) {
			sensorValue = Double.valueOf(getCurrentObjectLook().zPosition);
		}

		sensors = null;
		return sensorValue;
	}

	private static Costume getCurrentObjectLook() {
		if (ProjectManager.getInstance().getCurrentSprite() == null) {
			return null;
		}
		return ProjectManager.getInstance().getCurrentSprite().costume;
	}

}
