package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

public class SetFrictionBrickTest extends TestCase {
	private float friction = 3.5f;
	private SetFrictionBrick setFrictionBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicObjectMock = new PhysicObjectMock();
		setFrictionBrick = new SetFrictionBrick(sprite, friction);
		setFrictionBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		setFrictionBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setFrictionBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setFrictionBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", setFrictionBrick, false));
	}

	public void testClone() {
		Brick clone = setFrictionBrick.clone();

		assertEquals(setFrictionBrick.getSprite(), clone.getSprite());
		assertEquals(setFrictionBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(friction / 100.0f, physicObjectMock.executedWithFriction);

		setFrictionBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(friction / 100.0f, physicObjectMock.executedWithFriction);
	}

	public void testNullPhysicObject() {
		setFrictionBrick = new SetFrictionBrick(sprite, friction);
		try {
			setFrictionBrick.execute();
			fail("Execution of SetFrictionBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicObjectMock extends PhysicObject {
		public boolean executed = false;
		public float executedWithFriction;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setFriction(float friction) {
			executed = true;
			executedWithFriction = friction;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
