package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;

public class SetVelocityBrickTest extends AndroidTestCase {
	private Vector2 velocity = new Vector2(3.4f, -4.5f);
	private SetVelocityBrick setVelocityBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicObjectMock = new PhysicObjectMock();
		setVelocityBrick = new SetVelocityBrick(sprite, velocity);
		setVelocityBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		setVelocityBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setVelocityBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setVelocityBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", setVelocityBrick, false));
	}

	public void testClone() {
		Brick clone = setVelocityBrick.clone();

		assertEquals(setVelocityBrick.getSprite(), clone.getSprite());
		assertEquals(setVelocityBrick.getRequiredResources(), clone.getRequiredResources());
		assertEquals(TestUtils.getPrivateField("velocity", setVelocityBrick, false),
				TestUtils.getPrivateField("velocity", clone, false));
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(velocity, physicObjectMock.executedWithVelocity);

		setVelocityBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(velocity, physicObjectMock.executedWithVelocity);
	}

	public void testNullPhysicObject() {
		setVelocityBrick = new SetVelocityBrick(sprite, velocity);
		try {
			setVelocityBrick.execute();
			fail("Execution of SetVelocityBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicObjectMock extends PhysicObject {
		public boolean executed = false;
		public Vector2 executedWithVelocity = null;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setVelocity(Vector2 velocity) {
			executed = true;
			executedWithVelocity = velocity;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
