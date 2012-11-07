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

import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Sprite;

import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicCostume extends Costume {
	private final PhysicObject physicObject;
	private final PhysicShapeBuilder physicShapeBuilder;

	public PhysicCostume(Sprite sprite, PhysicShapeBuilder physicShapeBuilder, PhysicObject physicObject) {
		super(sprite);

		this.physicShapeBuilder = physicShapeBuilder;
		this.physicObject = physicObject;
	}

	@Override
	protected boolean checkImageChanged() {
		if (super.checkImageChanged()) {
			Shape[] shapes = physicShapeBuilder.getShape(getCostumeData(), getSize());
			physicObject.setShape(shapes);
			return true;
		}

		return false;
	}

	public void updatePositionAndRotation() {
		super.setXYPosition(physicObject.getXPosition(), physicObject.getYPosition());
		super.setRotation(physicObject.getAngle());
	}

	@Override
	public void setXPosition(float x) {
		physicObject.setXPosition(x);
	}

	@Override
	public void setYPosition(float y) {
		physicObject.setYPosition(y);
	}

	@Override
	public void setXYPosition(float x, float y) {
		physicObject.setXYPosition(x, y);
	}

	@Override
	public void setRotation(float degrees) {
		physicObject.setAngle(degrees);
	}

	@Override
	public float getRotation() {
		return physicObject.getAngle();
	}

	@Override
	public float getXPosition() {
		return physicObject.getXPosition();
	}

	@Override
	public float getYPosition() {
		return physicObject.getYPosition();
	}

	@Override
	public void setSize(float size) {
		super.setSize(size);

		Shape[] shapes = physicShapeBuilder.getShape(getCostumeData(), getSize());
		physicObject.setShape(shapes);
	}

}
