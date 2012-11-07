package org.catrobat.catroid.test.content.brick;

import junit.framework.TestCase;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetBounceFactorBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

public class SetBounceFactorBrickTest extends TestCase {
	private float bounceFactor = 35.0f;
	private SetBounceFactorBrick setBounceFactorBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicObjectMock = new PhysicObjectMock();
		setBounceFactorBrick = new SetBounceFactorBrick(sprite, bounceFactor);
		setBounceFactorBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		setBounceFactorBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setBounceFactorBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setBounceFactorBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", setBounceFactorBrick, false));
	}

	public void testClone() {
		Brick clone = setBounceFactorBrick.clone();

		assertEquals(setBounceFactorBrick.getSprite(), clone.getSprite());
		assertEquals(setBounceFactorBrick.getRequiredResources(), clone.getRequiredResources());
		assertEquals(TestUtils.getPrivateField("bounceFactor", setBounceFactorBrick, false),
				TestUtils.getPrivateField("bounceFactor", clone, false));
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(bounceFactor / 100.0f, physicObjectMock.executedWithBounceFactor);

		setBounceFactorBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(bounceFactor / 100.0f, physicObjectMock.executedWithBounceFactor);
	}

	public void testNullPhysicObject() {
		setBounceFactorBrick = new SetBounceFactorBrick(sprite, bounceFactor);
		try {
			setBounceFactorBrick.execute();
			fail("Execution of SetBounceFactorBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
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
