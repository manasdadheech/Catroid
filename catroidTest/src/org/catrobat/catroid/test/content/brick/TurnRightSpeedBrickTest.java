package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

public class TurnRightSpeedBrickTest extends TestCase {
	private float degreesPerSecond = 45.0f;
	private TurnRightSpeedBrick turnRightSpeedBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("TestSprite");
		physicObjectMock = new PhysicObjectMock();
		turnRightSpeedBrick = new TurnRightSpeedBrick(sprite, degreesPerSecond);
		turnRightSpeedBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		turnRightSpeedBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(turnRightSpeedBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(turnRightSpeedBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", turnRightSpeedBrick, false));
	}

	public void testClone() {
		Brick clone = turnRightSpeedBrick.clone();

		assertEquals(turnRightSpeedBrick.getSprite(), clone.getSprite());
		assertEquals(turnRightSpeedBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(-degreesPerSecond, physicObjectMock.executedWithDegrees);

		turnRightSpeedBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(-degreesPerSecond, physicObjectMock.executedWithDegrees);
	}

	public void testNullPhysicObject() {
		turnRightSpeedBrick = new TurnRightSpeedBrick(sprite, degreesPerSecond);
		try {
			turnRightSpeedBrick.execute();
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
