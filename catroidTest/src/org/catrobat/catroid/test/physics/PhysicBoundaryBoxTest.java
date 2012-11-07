package org.catrobat.catroid.test.physics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.catrobat.catroid.common.Values;
import org.catrobat.catroid.physics.PhysicBoundaryBox;
import org.catrobat.catroid.physics.PhysicDebugSettings;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.physics.PhysicWorldConverter;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicBoundaryBoxTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	private World world;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		world = new World(PhysicWorld.DEFAULT_GRAVITY, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

	public void testDefaultSettings() {
		assertEquals(5, PhysicBoundaryBox.FRAME_SIZE);
		assertEquals(0x0002, PhysicBoundaryBox.COLLISION_MASK);
	}

	public void testProperties() {
		assertEquals(0, world.getBodyCount());
		new PhysicBoundaryBox(world).create();
		assertEquals(4, world.getBodyCount());

		Iterator<Body> bodyIterator = world.getBodies();
		while (bodyIterator.hasNext()) {
			Body body = bodyIterator.next();
			assertEquals(BodyType.StaticBody, body.getType());
			assertFalse(body.isSleepingAllowed());

			List<Fixture> fixtures = body.getFixtureList();
			assertEquals(1, fixtures.size());
			for (Fixture fixture : fixtures) {
				Filter filter = fixture.getFilterData();
				assertEquals(PhysicObject.COLLISION_MASK, filter.maskBits);

				if (PhysicDebugSettings.DEBUGFLAG) {
					assertEquals(PhysicObject.COLLISION_MASK, filter.categoryBits);
				} else {
					assertEquals(PhysicBoundaryBox.COLLISION_MASK, filter.categoryBits);
				}
			}
		}
	}

	public void testPositionAndSize() {
		Values.SCREEN_WIDTH = 800;
		Values.SCREEN_HEIGHT = 640;

		float halfWidth = Values.SCREEN_WIDTH / 2;
		float halfHeight = Values.SCREEN_HEIGHT / 2;
		float frameSize = PhysicBoundaryBox.FRAME_SIZE;

		List<Float> boundaryXValues = Arrays.asList(new Float[] { -(halfWidth + frameSize), -halfWidth, halfWidth,
				halfWidth + frameSize });
		List<Float> boundaryYValues = Arrays.asList(new Float[] { -(halfHeight + frameSize), -halfHeight, halfHeight,
				halfHeight + frameSize });

		assertEquals(0, world.getBodyCount());
		new PhysicBoundaryBox(world).create();
		assertEquals(4, world.getBodyCount());

		Body body;
		Iterator<Body> bodyIterator = world.getBodies();

		while (bodyIterator.hasNext()) {
			body = bodyIterator.next();
			List<Fixture> fixtures = body.getFixtureList();
			assertEquals(1, fixtures.size());
			for (Fixture fixture : fixtures) {
				assertEquals(Shape.Type.Polygon, fixture.getType());
				PolygonShape shape = (PolygonShape) fixture.getShape();
				assertEquals(4, shape.getVertexCount());

				Vector2 vertex = new Vector2();
				for (int index = 0; index < shape.getVertexCount(); index++) {
					shape.getVertex(index, vertex);
					vertex = PhysicWorldConverter.vecBox2dToCat(vertex);

					assertTrue(boundaryXValues.contains(vertex.x));
					assertTrue(boundaryYValues.contains(vertex.y));
				}
			}
		}
	}
}
