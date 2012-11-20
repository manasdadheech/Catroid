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

import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;

/**
 * TODO: Find better name.
 */
public class PhysicBrickPreparator {
	private final PhysicWorld physicWorld;

	public PhysicBrickPreparator(PhysicWorld physicWorld) {
		this.physicWorld = physicWorld;
	}

	public void prepare(Project project) {
		PhysicShapeBuilder physicShapeBuilder = new PhysicShapeBuilder();

		for (Sprite sprite : project.getSpriteList()) {
			PhysicObject physicObject = null;

			for (int scriptIndex = 0; scriptIndex < sprite.getNumberOfScripts(); scriptIndex++) {
				Script script = sprite.getScript(scriptIndex);

				for (Brick brick : script.getBrickList()) {
					if (brick instanceof PhysicWorldBrick) {
						((PhysicWorldBrick) brick).setPhysicWorld(physicWorld);
					} else if (brick instanceof PhysicObjectBrick) {
						if (physicObject == null) {
							physicObject = physicWorld.getPhysicObject(sprite);
							sprite.costume = new PhysicCostume(sprite, physicShapeBuilder, physicObject);
						}
						((PhysicObjectBrick) brick).setPhysicObject(physicObject);
					}
				}
			}
		}
	}
}
