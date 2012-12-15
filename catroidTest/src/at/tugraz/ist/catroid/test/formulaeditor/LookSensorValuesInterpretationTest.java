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
package at.tugraz.ist.catroid.test.formulaeditor;

import android.test.AndroidTestCase;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.formulaeditor.Formula;
import at.tugraz.ist.catroid.formulaeditor.Sensors;

public class LookSensorValuesInterpretationTest extends AndroidTestCase {

	private static final float LOOK_ALPHA = 0.5f;
	private static final float LOOK_Y_POSITION = 23.4f;
	private static final float LOOK_X_POSITION = 5.6f;
	private static final float LOOK_BRIGHTNESS = 0.7f;
	private static final float LOOK_SCALE = 90.3f;
	private static final float LOOK_ROTATION = 30.7f;
	private static final int LOOK_ZPOSITION = 3;
	private static final float DELTA = 0.01f;

	@Override
	protected void setUp() {
		Sprite testSprite = new Sprite("sprite");
		ProjectManager.getInstance().setCurrentSprite(testSprite);

		testSprite.costume.setXPosition(LOOK_X_POSITION);
		testSprite.costume.setYPosition(LOOK_Y_POSITION);
		testSprite.costume.setAlphaValue(LOOK_ALPHA);
		testSprite.costume.setBrightnessValue(LOOK_BRIGHTNESS);
		testSprite.costume.scaleX = LOOK_SCALE;
		testSprite.costume.scaleY = LOOK_SCALE;
		testSprite.costume.rotation = LOOK_ROTATION;
		testSprite.costume.zPosition = LOOK_ZPOSITION;
	}

	public void testCostumeSensorValues() {

		Formula costumeXPositionFormula = new Formula(Sensors.LOOK_X_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_X_POSITION,
				costumeXPositionFormula.interpretFloat(), DELTA);

		Formula costumeYPositionFormula = new Formula(Sensors.LOOK_Y_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_Y_POSITION,
				costumeYPositionFormula.interpretFloat(), DELTA);

		Formula costumeAlphaValueFormula = new Formula(Sensors.LOOK_GHOSTEFFECT_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_ALPHA,
				costumeAlphaValueFormula.interpretFloat(), DELTA);

		Formula costumeBrightnessFormula = new Formula(Sensors.LOOK_BRIGHTNESS_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_BRIGHTNESS,
				costumeBrightnessFormula.interpretFloat(), DELTA);

		Formula costumeScaleFormula = new Formula(Sensors.LOOK_SIZE_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_SCALE, costumeScaleFormula.interpretFloat(),
				DELTA);

		Formula costumeRotateFormula = new Formula(Sensors.LOOK_ROTATION_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_ROTATION,
				costumeRotateFormula.interpretFloat(), DELTA);

		Formula costumeZPositionFormula = new Formula(Sensors.LOOK_LAYER_.sensorName);
		assertEquals("Formula interpretation is not as expected", LOOK_ZPOSITION,
				costumeZPositionFormula.interpretInteger());

	}
}
