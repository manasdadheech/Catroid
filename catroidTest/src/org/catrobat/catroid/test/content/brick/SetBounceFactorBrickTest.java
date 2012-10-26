package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetBounceFactorBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

public class SetBounceFactorBrickTest extends TestCase {
	private float bounceFactor = 35.0f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private SetBounceFactorBrick bounceFactorBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		bounceFactorBrick = new SetBounceFactorBrick(physicWorld, sprite, bounceFactor);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorld = null;
		bounceFactorBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(bounceFactorBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(bounceFactorBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = bounceFactorBrick.clone();

		assertEquals(bounceFactorBrick.getSprite(), clone.getSprite());
		assertEquals(bounceFactorBrick.getRequiredResources(), clone.getRequiredResources());
		assertEquals(TestUtils.getPrivateField("bounceFactor", bounceFactorBrick, false),
				TestUtils.getPrivateField("bounceFactor", clone, false));
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);

		bounceFactorBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(bounceFactor / 100.0f, physicObjectMock.executedWithBounceFactor);
	}

	public void testNullSprite() {
		bounceFactorBrick = new SetBounceFactorBrick(null, sprite, bounceFactor);
		try {
			bounceFactorBrick.execute();
			fail("Execution of SetBounceFactorBrick with null Sprite did not cause a "
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
		public float executedWithBounceFactor;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setBounceFactor(float bounceFactor) {
			executed = true;
			executedWithBounceFactor = bounceFactor;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
