package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class SetAngularVelocityBrickTest extends TestCase {
	private float degreesPerSec = 3.50f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private TurnLeftSpeedBrick angularVelocityBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		angularVelocityBrick = new TurnLeftSpeedBrick(physicWorld, sprite, degreesPerSec);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sprite = null;
		physicWorld = null;
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
		angularVelocityBrick = new TurnLeftSpeedBrick(null, sprite, degreesPerSec);
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
			super(new BodyMock(null, 0));
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

	}

	class BodyMock extends Body {

		protected BodyMock(World world, long addr) {
			super(world, addr);
		}

	}

}
