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
package at.tugraz.ist.catroid.physics;

import java.lang.reflect.Method;

import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Script;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.bricks.Brick;
import at.tugraz.ist.catroid.content.bricks.SetBounceFactorBrick;
import at.tugraz.ist.catroid.content.bricks.SetFrictionBrick;
import at.tugraz.ist.catroid.content.bricks.SetGravityBrick;
import at.tugraz.ist.catroid.content.bricks.SetMassBrick;
import at.tugraz.ist.catroid.content.bricks.SetPhysicObjectTypeBrick;
import at.tugraz.ist.catroid.content.bricks.SetVelocityBrick;
import at.tugraz.ist.catroid.content.bricks.TurnLeftSpeedBrick;
import at.tugraz.ist.catroid.content.bricks.TurnRightSpeedBrick;

/**
 * TODO: Find better name.
 */
public class PhysicObjectConverter {

	public PhysicObjectConverter() {
	}

	public void convert(Project project) {
		PhysicWorld physicWorld = project.getPhysicWorld();
		physicWorld.ignoreSteps = 0;
		for (Sprite sprite : project.getSpriteList()) {

			for (int scriptIndex = 0; scriptIndex < sprite.getNumberOfScripts(); scriptIndex++) {
				Script script = sprite.getScript(scriptIndex);

				for (Brick brick : script.getBrickList()) {
					if (brick instanceof SetPhysicObjectTypeBrick) {
						PhysicObject physicObject = physicWorld.getPhysicObject(sprite);
						sprite.costume = new PhysicSpriteCostume(sprite, physicObject);
					}

					// For god's sake, what have I done here?
					if (brick instanceof SetPhysicObjectTypeBrick || brick instanceof SetMassBrick
							|| brick instanceof SetGravityBrick || brick instanceof SetVelocityBrick
							|| brick instanceof TurnLeftSpeedBrick || brick instanceof TurnRightSpeedBrick
							|| brick instanceof SetBounceFactorBrick || brick instanceof SetFrictionBrick) {
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
