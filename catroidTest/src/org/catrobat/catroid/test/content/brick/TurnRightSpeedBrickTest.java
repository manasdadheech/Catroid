package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

public class TurnRightSpeedBrickTest extends TestCase {
	private float degreesPerSecond = 45.0f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private TurnRightSpeedBrick turnRightSpeedBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		physicWorld = new PhysicWorldMock();
		sprite = new Sprite("TestSprite");
		turnRightSpeedBrick = new TurnRightSpeedBrick(physicWorld, sprite, degreesPerSecond);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		physicWorld = null;
		sprite = null;
		turnRightSpeedBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(turnRightSpeedBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(turnRightSpeedBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = turnRightSpeedBrick.clone();

		assertEquals(turnRightSpeedBrick.getSprite(), clone.getSprite());
		assertEquals(turnRightSpeedBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);

		turnRightSpeedBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(-degreesPerSecond, physicObjectMock.executedWithDegrees);
	}

	public void testNullSprite() {
		turnRightSpeedBrick = new TurnRightSpeedBrick(null, sprite, degreesPerSecond);
		try {
			turnRightSpeedBrick.execute();
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
