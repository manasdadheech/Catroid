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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.catrobat.catroid.content.Sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicWorld {
	static {
		GdxNativesLoader.load();
	}

	public final static float RATIO = 40.0f;
	public final static int VELOCITY_ITERATIONS = 20;
	public final static int POSITION_ITERATIONS = 20;

	public final static Vector2 DEFAULT_GRAVITY = new Vector2(0.0f, -10.0f);
	public final static boolean IGNORE_SLEEPING_OBJECTS = false;

	private final World world;
	private final Map<Sprite, PhysicObject> physicObjects;
	private final PhysicShapeBuilder shapeBuilder;
	private Box2DDebugRenderer renderer;
	public int ignoreSteps = 0;

	public PhysicWorld() {
		world = new World(PhysicWorld.DEFAULT_GRAVITY, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
		physicObjects = new HashMap<Sprite, PhysicObject>();
		shapeBuilder = new PhysicShapeBuilder();

		new PhysicBoundaryBox(world).create();
	}

	public void step(float deltaTime) {
		if (ignoreSteps < 6) {
			ignoreSteps += 1;
		} else {
			world.step(deltaTime, PhysicWorld.VELOCITY_ITERATIONS, PhysicWorld.POSITION_ITERATIONS);
		}
		updateSprites();

	}

	private void updateSprites() {
		PhysicObject physicObject;
		PhysicCostume costume;
		for (Entry<Sprite, PhysicObject> entry : physicObjects.entrySet()) {
			physicObject = entry.getValue();
			physicObject.setIfOnEdgeBounce(false);

			costume = (PhysicCostume) entry.getKey().costume;
			costume.aquireXYWidthHeightLock();
			costume.updatePositionAndRotation();
			costume.releaseXYWidthHeightLock();
		}
	}

	public void render(Matrix4 perspectiveMatrix) {
		//				if (PhysicRenderer.instance.renderer == null) {
		//					PhysicRenderer.instance.renderer = new ShapeRenderer();
		//				}
		//				PhysicRenderer.instance.render(perspectiveMatrix);

		if (renderer == null) {
			renderer = new Box2DDebugRenderer(PhysicDebugSettings.Render.RENDER_BODIES,
					PhysicDebugSettings.Render.RENDER_JOINTS, PhysicDebugSettings.Render.RENDER_AABBs,
					PhysicDebugSettings.Render.RENDER_INACTIVE_BODIES);
		}
		renderer.render(world, perspectiveMatrix.scl(PhysicWorld.RATIO));
	}

	public void setGravity(Sprite sprite, Vector2 gravity) {
		world.setGravity(gravity);
	}

	public PhysicObject getPhysicObject(Sprite sprite) {
		if (sprite == null) {
			throw new NullPointerException();
		}

		if (physicObjects.containsKey(sprite)) {
			return physicObjects.get(sprite);
		}

		BodyDef bodyDef = new BodyDef();
		bodyDef.bullet = true;
		PhysicObject physicObject = new PhysicObject(world.createBody(bodyDef));
		physicObjects.put(sprite, physicObject);

		return physicObject;
	}

	public void changeCostume(Sprite sprite) {
		Shape[] shapes = shapeBuilder.getShape(sprite.costume.getCostumeData(), sprite.costume.getSize());
		physicObjects.get(sprite).setShape(shapes);
	}

}
