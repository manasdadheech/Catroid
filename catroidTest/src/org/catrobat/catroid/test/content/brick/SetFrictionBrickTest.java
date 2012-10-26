package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

public class SetFrictionBrickTest extends TestCase {
	private float friction = 3.5f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private SetFrictionBrick frictionBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		frictionBrick = new SetFrictionBrick(physicWorld, sprite, friction);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorld = null;
		frictionBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(frictionBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(frictionBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = frictionBrick.clone();

		assertEquals(frictionBrick.getSprite(), clone.getSprite());
		assertEquals(frictionBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);

		frictionBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(friction / 100.0f, physicObjectMock.executedWithFriction);
	}

	public void testNullSprite() {
		frictionBrick = new SetFrictionBrick(null, sprite, friction);
		try {
			frictionBrick.execute();
			fail("Execution of SetFrictionBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicWorldMock extends PhysicWorld {
		private PhysicObjectMock physicObjectMock = new PhysicObjectMock();

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			return physicObjectMock;
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
