package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

public class TurnLeftSpeedBrickTest extends TestCase {
	private float degreesPerSecond = 3.50f;
	private PhysicObjectMock physicObjectMock;
	private Sprite sprite;
	private TurnLeftSpeedBrick turnLeftSpeedBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("TestSprite");
		physicObjectMock = new PhysicObjectMock();
		turnLeftSpeedBrick = new TurnLeftSpeedBrick(sprite, degreesPerSecond);
		turnLeftSpeedBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		turnLeftSpeedBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(turnLeftSpeedBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(turnLeftSpeedBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", turnLeftSpeedBrick, false));
	}

	public void testClone() {
		Brick clone = turnLeftSpeedBrick.clone();

		assertEquals(turnLeftSpeedBrick.getSprite(), clone.getSprite());
		assertEquals(turnLeftSpeedBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(degreesPerSecond, physicObjectMock.executedWithDegrees);

		turnLeftSpeedBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(degreesPerSecond, physicObjectMock.executedWithDegrees);
	}

	public void testNullPhysicObject() {
		turnLeftSpeedBrick = new TurnLeftSpeedBrick(sprite, degreesPerSecond);
		try {
			turnLeftSpeedBrick.execute();
			fail("Execution of SetAngularVelocityBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicObjectMock extends PhysicObject {
		public boolean executed = false;
		public float executedWithDegrees;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setRotationSpeed(float degreesPerSecond) {
			executed = true;
			executedWithDegrees = degreesPerSecond;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
