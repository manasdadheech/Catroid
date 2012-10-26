package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

public class TurnLeftSpeedBrickTest extends TestCase {
	private float degreesPerSecond = 3.50f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private TurnLeftSpeedBrick turnLeftSpeedBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		physicWorld = new PhysicWorldMock();
		sprite = new Sprite("TestSprite");
		turnLeftSpeedBrick = new TurnLeftSpeedBrick(physicWorld, sprite, degreesPerSecond);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		physicWorld = null;
		sprite = null;
		turnLeftSpeedBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(turnLeftSpeedBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(turnLeftSpeedBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = turnLeftSpeedBrick.clone();

		assertEquals(turnLeftSpeedBrick.getSprite(), clone.getSprite());
		assertEquals(turnLeftSpeedBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);

		turnLeftSpeedBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(degreesPerSecond, physicObjectMock.executedWithDegrees);
	}

	public void testNullSprite() {
		turnLeftSpeedBrick = new TurnLeftSpeedBrick(null, sprite, degreesPerSecond);
		try {
			turnLeftSpeedBrick.execute();
			fail("Execution of SetAngularVelocityBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicWorldMock extends PhysicWorld {
		public PhysicObjectMock physicObjectMock = new PhysicObjectMock();

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			return physicObjectMock;
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
