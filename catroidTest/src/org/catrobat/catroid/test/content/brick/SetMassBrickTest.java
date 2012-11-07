package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

public class SetMassBrickTest extends AndroidTestCase {
	private float mass = 10.50f;
	private SetMassBrick setMassBrick;
	private Sprite sprite;
	private PhysicObjectMock physicObjectMock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicObjectMock = new PhysicObjectMock();
		setMassBrick = new SetMassBrick(sprite, mass);
		setMassBrick.setPhysicObject(physicObjectMock);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicObjectMock = null;
		setMassBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setMassBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setMassBrick.getSprite(), sprite);
	}

	public void testSetPhysicObject() {
		assertEquals(physicObjectMock, TestUtils.getPrivateField("physicObject", setMassBrick, false));
	}

	public void testClone() {
		Brick clone = setMassBrick.clone();

		assertEquals(setMassBrick.getSprite(), clone.getSprite());
		assertEquals(setMassBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		assertFalse(physicObjectMock.executed);
		assertNotSame(mass, physicObjectMock.executedWithMass);

		setMassBrick.execute();

		assertTrue(physicObjectMock.executed);
		assertEquals(mass, physicObjectMock.executedWithMass);
	}

	public void testNullPhysicObject() {
		setMassBrick = new SetMassBrick(sprite, mass);
		try {
			setMassBrick.execute();
			fail("Execution of SetMassBrick with null Sprite did not cause a " + "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	public void testMass() {
		float physicObjectMass = (Float) TestUtils.getPrivateField("mass", setMassBrick, false);
		assertEquals(mass, physicObjectMass);
	}

	private class PhysicObjectMock extends PhysicObject {
		public boolean executed = false;
		public float executedWithMass;

		public PhysicObjectMock() {
			super(null);
		}

		@Override
		public void setMass(float mass) {
			executed = true;
			executedWithMass = mass;
		}

		@Override
		public void setType(Type type) {
		}

		@Override
		protected void setCollisionBits(short categoryBits, short maskBits) {
		}
	}
}
