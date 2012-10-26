package org.catrobat.catroid.test.physics;

import java.util.Map;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicWorldTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	private PhysicWorld physicWorld;
	private World world;
	private Map<Sprite, PhysicObject> physicObjects;

	@SuppressWarnings("unchecked")
	@Override
	public void setUp() {
		physicWorld = new PhysicWorld();
		world = (World) TestUtils.getPrivateField("world", physicWorld, false);
		physicObjects = (Map<Sprite, PhysicObject>) TestUtils.getPrivateField("physicObjects", physicWorld, false);
	}

	@Override
	public void tearDown() {
		physicWorld = null;
		world = null;
		physicObjects = null;
	}

	public void testDefaultSettings() {
		assertEquals(40.0f, PhysicWorld.RATIO);
		assertEquals(20, PhysicWorld.VELOCITY_ITERATIONS);
		assertEquals(20, PhysicWorld.POSITION_ITERATIONS);

		assertEquals(new Vector2(0, -10), PhysicWorld.DEFAULT_GRAVITY);
		assertEquals(false, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

	public void testWrapper() {
		assertNotNull(world);
	}

	public void testGravity() {
		assertEquals(PhysicWorld.DEFAULT_GRAVITY, world.getGravity());

		Vector2 newGravity = new Vector2(-1.2f, 3.4f);
		physicWorld.setGravity(newGravity);

		assertEquals(newGravity, world.getGravity());
	}

	public void testGetNullPhysicObject() {
		try {
			@SuppressWarnings("unused")
			PhysicObject physicObject = physicWorld.getPhysicObject(null);
			fail();
		} catch (Exception exception) {
			// Expected behavior
		}
	}

	public void testGetPhysicObject() {
		Sprite sprite = new Sprite("TestSprite");
		PhysicObject physicObject = physicWorld.getPhysicObject(sprite);

		assertNotNull(physicObject);
		assertEquals(1, physicObjects.size());
		assertTrue(physicObjects.containsKey(sprite));
		assertTrue(physicObjects.containsValue(physicObject));
	}

	public void testGetPhysicObjectCallsCreateObject() {
		PhysicWorldMock physicWorldMock = new PhysicWorldMock();

		assertFalse(physicWorldMock.executed);

		physicWorldMock.getPhysicObject(new Sprite("TestSprite"));

		assertTrue(physicWorldMock.executed);
	}

	public void testCreatePhysicObject() {
		PhysicObject physicObject = (PhysicObject) TestUtils
				.invokeMethod(physicWorld, "createPhysicObject", null, null);
		Body body = (Body) TestUtils.getPrivateField("body", physicObject, false);

		assertTrue(body.isBullet());
	}

	public void testGetSamePhysicObject() {
		Sprite sprite = new Sprite("TestSprite");
		PhysicObject physicObject = physicWorld.getPhysicObject(sprite);
		PhysicObject samePhysicObject = physicWorld.getPhysicObject(sprite);

		assertEquals(1, physicObjects.size());
		assertEquals(physicObject, samePhysicObject);
	}

	public void testStep() {
	}

	private class PhysicWorldMock extends PhysicWorld {
		public boolean executed = false;

		@Override
		protected PhysicObject createPhysicObject() {
			executed = true;
			return null;
		}
	}
}
