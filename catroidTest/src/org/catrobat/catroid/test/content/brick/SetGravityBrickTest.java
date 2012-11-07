package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;

public class SetGravityBrickTest extends AndroidTestCase {
	private Vector2 gravity = new Vector2(1.2f, -3.4f);
	private SetGravityBrick setGravityBrick;
	private Sprite sprite;
	private PhysicWorldMock physicWorldMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorldMock = new PhysicWorldMock();
		setGravityBrick = new SetGravityBrick(sprite, gravity);
		setGravityBrick.setPhysicWorld(physicWorldMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorldMock = null;
		setGravityBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setGravityBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setGravityBrick.getSprite(), sprite);
	}

	public void testSetPhysicWorld() {
		assertEquals(physicWorldMock, TestUtils.getPrivateField("physicWorld", setGravityBrick, false));
	}

	public void testClone() {
		Brick clone = setGravityBrick.clone();
		assertEquals(setGravityBrick.getSprite(), clone.getSprite());
		assertEquals(setGravityBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicWorldMock.executed);
		assertNotSame(gravity, physicWorldMock.executedWithGravity);

		setGravityBrick.execute();

		assertTrue(physicWorldMock.executed);
		assertEquals(gravity, physicWorldMock.executedWithGravity);
	}

	public void testNullPhysicWorld() {
		setGravityBrick = new SetGravityBrick(sprite, gravity);
		try {
			setGravityBrick.execute();
			fail("Execution of SetGravityBrick with null Sprite did not cause a " + "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicWorldMock extends PhysicWorld {
		public boolean executed = false;
		public Vector2 executedWithGravity = null;

		@Override
		public void setGravity(Vector2 gravity) {
			executed = true;
			executedWithGravity = gravity;
		}

	}
}
