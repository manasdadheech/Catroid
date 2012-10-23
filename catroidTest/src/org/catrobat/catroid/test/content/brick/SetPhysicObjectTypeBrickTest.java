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
		assertFalse(((PhysicWorldMock) physicWorld).wasExecuted());
		setPhysicObjectTypeBrickTest.execute();
		assertTrue(((PhysicWorldMock) physicWorld).wasExecuted());
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
			if (sprite == null) {
				return null;
			}

			return phyMockObj;
		}
	}

	class PhysicObjectMock extends PhysicObject {

		public boolean executed;

		public PhysicObjectMock() {
			super(null);
			executed = false;
		}

		public boolean wasExecuted() {
			return executed;
		}

		@Override
		public void setType(Type type) {
			executed = true;
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}

	}

}
