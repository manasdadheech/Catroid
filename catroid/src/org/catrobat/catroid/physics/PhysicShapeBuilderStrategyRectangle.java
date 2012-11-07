/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2012 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.physics;

import org.catrobat.catroid.common.CostumeData;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicShapeBuilderStrategyRectangle implements PhysicShapeBuilderStrategy {

	@Override
	public Shape[] build(CostumeData costumeData) {
		int[] size = costumeData.getResolution();
		float width = size[0];
		float height = size[1];

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(PhysicWorldConverter.lengthCatToBox2d(width / 2.0f),
				PhysicWorldConverter.lengthCatToBox2d(height / 2.0f));

		return new Shape[] { polygonShape };
	}
}
