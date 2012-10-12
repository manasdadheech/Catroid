package org.catrobat.catroid.test.content.brick;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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
		setMassBrick = new SetMassBrick(physicWorld, sprite, mass);
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
		assertFalse(((PhysicWorldMock) physicWorld).wasExecuted());
		setMassBrick.execute();
		assertTrue(((PhysicWorldMock) physicWorld).wasExecuted());
	}

	public void testNullSprite() {
		setMassBrick = new SetMassBrick(null, sprite, mass);
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
			return phyMockObj;
		}
	}

	class PhysicObjectMock extends PhysicObject {

		public boolean executed;

		public PhysicObjectMock() {
			super(new BodyMock(null, 0));
			executed = false;
		}

		@Override
		public void setMass(float mass) {
			executed = true;
		}

		public boolean wasExecuted() {
			return executed;
		}

		@Override
		public void setType(Type type) {
		}

	}

	class BodyMock extends Body {

		protected BodyMock(World world, long addr) {
			super(world, addr);
		}

	}

}
