/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.catrobat.catroid.common.CostumeData;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicShapeBuilder {
	private final Map<String, Shape[]> shapeMap = new HashMap<String, Shape[]>();
	private PhysicShapeBuilderStrategy strategy;

	public PhysicShapeBuilder(PhysicShapeBuilderStrategy strategy) {
		this.strategy = strategy;
	}

	public synchronized Shape[] getShape(CostumeData costumeData, float scaleFactor) {
		if (costumeData == null) {
			return null;
		}

		String key = getKey(costumeData, scaleFactor);
		if (shapeMap.containsKey(key)) {
			return shapeMap.get(key);
		}

		Shape[] shapes = shapeMap.get(getKey(costumeData, 1.0f));
		if (shapes == null) {
			shapes = strategy.build(costumeData);
			shapeMap.put(getKey(costumeData, 1.0f), shapes);
		}

		if (scaleFactor != 1.0f) {
			Shape[] scaledShapes = scaleShapes(shapes, scaleFactor);
			shapeMap.put(key, scaledShapes);
			shapes = scaledShapes;
		}

		return shapes;
	}

	private String getKey(CostumeData costumeData, float scaleFactor) {
		return costumeData.getChecksum() + (int) (scaleFactor * 10);
	}

	private Shape[] scaleShapes(Shape[] shapes, float scaleFactor) {
		List<Shape> scaledShapes = new ArrayList<Shape>();

		for (Shape shape : shapes) {
			List<Vector2> vertices = new ArrayList<Vector2>();

			PolygonShape polygon = (PolygonShape) shape;
			for (int index = 0; index < polygon.getVertexCount(); index++) {
				Vector2 vertex = new Vector2();
				polygon.getVertex(index, vertex);
				vertex = vertex.mul(scaleFactor);
				vertices.add(vertex);
			}

			PolygonShape polygonShape = new PolygonShape();
			polygonShape.set(vertices.toArray(new Vector2[vertices.size()]));
			scaledShapes.add(polygonShape);
		}

		return scaledShapes.toArray(new Shape[scaledShapes.size()]);
	}
}
