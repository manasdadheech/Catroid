package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

public class SetMassBrickTest extends AndroidTestCase {
	private float mass = 10.50f;
	private PhysicWorld physicWorld;
	private Sprite sprite;
	private SetMassBrick setMassBrick;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sprite = new Sprite("testSprite");
		physicWorld = new PhysicWorldMock();
		setMassBrick = new SetMassBrick(sprite, mass);
		setMassBrick.setPhysicObject(physicWorld.getPhysicObject(sprite));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		sprite = null;
		physicWorld = null;
		setMassBrick = null;
	}

	public void testRequiredResources() {
		assertEquals(setMassBrick.getRequiredResources(), Brick.NO_RESOURCES);
	}

	public void testGetSprite() {
		assertEquals(setMassBrick.getSprite(), sprite);
	}

	public void testClone() {
		Brick clone = setMassBrick.clone();

		assertEquals(setMassBrick.getSprite(), clone.getSprite());
		assertEquals(setMassBrick.getRequiredResources(), clone.getRequiredResources());
	}

	public void testExecution() {
		PhysicObjectMock physicObjectMock = (PhysicObjectMock) physicWorld.getPhysicObject(sprite);

		assertFalse(physicObjectMock.executed);

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

	private class PhysicWorldMock extends PhysicWorld {
		private PhysicObjectMock physicObjectMock = new PhysicObjectMock();

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			return physicObjectMock;
		}
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
