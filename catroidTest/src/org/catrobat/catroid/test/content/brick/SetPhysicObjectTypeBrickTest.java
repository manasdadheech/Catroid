package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetPhysicObjectTypeBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicObject.Type;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

public class SetPhysicObjectTypeBrickTest extends AndroidTestCase {
	private PhysicObject.Type type;
	private SetPhysicObjectTypeBrick setPhysicObjectTypeBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		type = Type.DYNAMIC;
		sprite = new Sprite("testSprite");
		physicObjectMock = new PhysicObjectMock();
		setPhysicObjectTypeBrick = new SetPhysicObjectTypeBrick(sprite, type);
		setPhysicObjectTypeBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		type = null;
		sprite = null;
		physicObjectMock = null;
		setPhysicObjectTypeBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setPhysicObjectTypeBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setPhysicObjectTypeBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", setPhysicObjectTypeBrick, false));
	}

	public void testClone() {
		Brick clone = setPhysicObjectTypeBrick.clone();

		assertEquals(setPhysicObjectTypeBrick.getSprite(), clone.getSprite());
		assertEquals(setPhysicObjectTypeBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(type, physicObjectMock.executedWithType);

		setPhysicObjectTypeBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(type, physicObjectMock.executedWithType);
	}

	public void testNullPhysicObject() {
		setPhysicObjectTypeBrick = new SetPhysicObjectTypeBrick(null, type);
		try {
			setPhysicObjectTypeBrick.execute();
			fail("Execution of SetPhysicObjectTypeBrick with null Sprite did not cause a "
					+ "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
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
