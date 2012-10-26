package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;

public class SetVelocityBrickTest extends AndroidTestCase {
	private Vector2 velocity = new Vector2(3.4f, -4.5f);
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private SetVelocityBrick setVelocityBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		setVelocityBrick = new SetVelocityBrick(physicWorld, sprite, velocity);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorld = null;
		setVelocityBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setVelocityBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setVelocityBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = setVelocityBrick.clone();

		assertEquals(setVelocityBrick.getSprite(), clone.getSprite());
		assertEquals(setVelocityBrick.getRequiredResources(), clone.getRequiredResources());
		assertEquals(TestUtils.getPrivateField("velocity", setVelocityBrick, false),
				TestUtils.getPrivateField("velocity", clone, false));
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);
		assertNull(physicObjectMock.executedWithVelocity);

		setVelocityBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(velocity, physicObjectMock.executedWithVelocity);
	}

	public void testNullSprite() {
		setVelocityBrick = new SetVelocityBrick(null, sprite, velocity);
		try {
			setVelocityBrick.execute();
			fail("Execution of SetVelocityBrick with null Sprite did not cause a "
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
		public Vector2 executedWithVelocity = null;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setVelocity(Vector2 velocity) {
			executed = true;
			executedWithVelocity = velocity;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
