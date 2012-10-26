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
package org.catrobat.catroid.physics;

import java.lang.reflect.Method;

import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetBounceFactorBrick;
import org.catrobat.catroid.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.content.bricks.SetGravityBrick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.content.bricks.SetPhysicObjectTypeBrick;
import org.catrobat.catroid.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.content.bricks.TurnRightSpeedBrick;

/**
 * TODO: Find better name.
 */
public class PhysicObjectConverter {

	private final PhysicWorld physicWorld;

	public PhysicObjectConverter(PhysicWorld physicWorld) {
		this.physicWorld = physicWorld;
	}

	public void convert(Project project) {
		PhysicShapeBuilder physicShapeBuilder = new PhysicShapeBuilder();

		physicWorld.ignoreSteps = 0;
		for (Sprite sprite : project.getSpriteList()) {

			for (int scriptIndex = 0; scriptIndex < sprite.getNumberOfScripts(); scriptIndex++) {
				Script script = sprite.getScript(scriptIndex);

				for (Brick brick : script.getBrickList()) {

					// For god's sake, what have I done here?
					if (brick instanceof SetPhysicObjectTypeBrick || brick instanceof SetMassBrick
							|| brick instanceof SetGravityBrick || brick instanceof SetVelocityBrick
							|| brick instanceof TurnLeftSpeedBrick || brick instanceof TurnRightSpeedBrick
							|| brick instanceof SetBounceFactorBrick || brick instanceof SetFrictionBrick) {

						if (sprite.costume instanceof Costume) {
							PhysicObject physicObject = physicWorld.getPhysicObject(sprite);
							sprite.costume = new PhysicCostume(sprite, physicShapeBuilder, physicObject);
						}

						Class<?>[] classes = { PhysicWorld.class };
						Method setter;
						try {
							setter = brick.getClass().getDeclaredMethod("setPhysicWorld", classes);
							setter.invoke(brick, physicWorld);
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		}
	}
}
