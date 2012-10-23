package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

public class SetAngularVelocityBrickTest extends TestCase {

	private float degreesPerSecond = 3.50f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private TurnLeftSpeedBrick angularVelocityBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		physicWorld = new PhysicWorldMock();
		sprite = new Sprite("TestSprite");
		angularVelocityBrick = new TurnLeftSpeedBrick(physicWorld, sprite, degreesPerSecond);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		physicWorld = null;
		sprite = null;
		angularVelocityBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(angularVelocityBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(angularVelocityBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = angularVelocityBrick.clone();
		assertEquals(angularVelocityBrick.getSprite(), clone.getSprite());
		assertEquals(angularVelocityBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(((PhysicWorldMock) physicWorld).wasExecuted());
		angularVelocityBrick.execute();
		assertTrue(((PhysicWorldMock) physicWorld).wasExecuted());
	}

	public void testNullSprite() {
		angularVelocityBrick = new TurnLeftSpeedBrick(null, sprite, degreesPerSecond);
		try {
			angularVelocityBrick.execute();
			fail("Execution of SetAngularVelocityBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	class PhysicWorldMock extends PhysicWorld {

		private PhysicObjectMock phyMockObj;

		public PhysicWorldMock() {
			phyMockObj = new PhysicObjectMock();
		}

		public boolean wasExecuted() {
			return phyMockObj.wasExecuted();
		}

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			return phyMockObj;
		}
	}

	class PhysicObjectMock extends PhysicObject {

		public boolean executed;

		public PhysicObjectMock() {
			super(null);
			executed = false;
		}

		@Override
		public void setRotationSpeed(float radian) {
			executed = true;
		}

		public boolean wasExecuted() {
			return executed;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}

}
