package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetPhysicObjectTypeBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicObject.Type;
import org.catrobat.catroid.physics.PhysicWorld;

import android.test.AndroidTestCase;

public class SetPhysicObjectTypeBrickTest extends AndroidTestCase {
	private PhysicObject.Type type;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private SetPhysicObjectTypeBrick setPhysicObjectTypeBrickTest;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		type = Type.DYNAMIC;
		setPhysicObjectTypeBrickTest = new SetPhysicObjectTypeBrick(physicWorld, sprite, type);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorld = null;
		type = null;
		setPhysicObjectTypeBrickTest = null;
	}

	public void testRequiredResources() {
		assertEquals(setPhysicObjectTypeBrickTest.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setPhysicObjectTypeBrickTest.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = setPhysicObjectTypeBrickTest.clone();

		assertEquals(setPhysicObjectTypeBrickTest.getSprite(), clone.getSprite());
		assertEquals(setPhysicObjectTypeBrickTest.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);
		assertNull(physicObjectMock.executedWithType);

		setPhysicObjectTypeBrickTest.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(type, physicObjectMock.executedWithType);
	}

	public void testNullSprite() {
		setPhysicObjectTypeBrickTest = new SetPhysicObjectTypeBrick(physicWorld, null, type);
		try {
			setPhysicObjectTypeBrickTest.execute();
			fail("Execution of SetPhysicObjectTypeBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	private class PhysicWorldMock extends PhysicWorld {
		private PhysicObjectMock physicObjectMock = new PhysicObjectMock();

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			if (sprite == null) {
				throw new NullPointerException();
			}

			return physicObjectMock;
		}
	}

	private class PhysicObjectMock extends PhysicObject {
		public boolean executed = false;
		public Type executedWithType = null;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setType(Type type) {
			executed = true;
			executedWithType = type;
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
