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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.catrobat.catroid.common.CostumeData;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicShapeBuilderStrategyTest implements PhysicShapeBuilderStrategy {

	@Override
	public Shape[] build(CostumeData costumeData) {
		Pixmap pixmap = costumeData.getPixmap();
		boolean[][] array = new boolean[pixmap.getWidth()][pixmap.getHeight()];

		for (int y = 0; y < pixmap.getHeight(); y++) {
			for (int x = 0; x < pixmap.getWidth(); x++) {
				int alpha = pixmap.getPixel(x, y) & 0xff;

				if (alpha > 0) {
					array[x][y] = false;
				}
			}
		}

		List<Pixel> points = new ArrayList<Pixel>();

		List<Pixel> convexPixels = GrahamScan.run(points);

		List<Vector2> xyz = new ArrayList<Vector2>();
		float halfWidth = pixmap.getWidth() / 2.0f;
		float halfHeight = pixmap.getHeight() / 2.0f;
		for (Pixel pixel : convexPixels) {
			float x = PhysicWorldConverter.lengthCatToBox2d(pixel.x - halfWidth);
			float y = PhysicWorldConverter.lengthCatToBox2d(pixel.y - halfHeight);
			xyz.add(new Vector2(x, y));
		}

		return devideShape(xyz.toArray(new Vector2[xyz.size()]));
	}

	private Shape[] devideShape(Vector2[] convexpoints) {
		if (convexpoints.length < 9) {
			List<Vector2> x = Arrays.asList(convexpoints);
			Collections.reverse(x);

			PolygonShape polygon = new PolygonShape();
			polygon.set(x.toArray(new Vector2[x.size()]));
			return new Shape[] { polygon };
		}

		List<Shape> shapes = new ArrayList<Shape>(convexpoints.length / 6 + 1);
		List<Vector2> pointsPerShape = new ArrayList<Vector2>(8);

		Vector2 rome = convexpoints[0];
		int index = 1;
		while (index < convexpoints.length - 1) {
			int k = index + 7;

			int remainingPointsCount = convexpoints.length - index;
			if (remainingPointsCount > 7 && remainingPointsCount < 9) {
				k -= 3;
			}

			pointsPerShape.add(rome);
			for (; index < k && index < convexpoints.length; index++) {
				pointsPerShape.add(convexpoints[index]);
			}

			if (index < convexpoints.length) {
				index--;
			}
			Collections.reverse(pointsPerShape);

			PolygonShape polygon = new PolygonShape();
			polygon.set(pointsPerShape.toArray(new Vector2[pointsPerShape.size()]));
			shapes.add(polygon);

			pointsPerShape.clear();
		}

		return shapes.toArray(new Shape[shapes.size()]);
	}
}
