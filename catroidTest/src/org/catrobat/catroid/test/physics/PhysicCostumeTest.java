package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.physics.PhysicCostume;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicCostumeTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheckImageChanged() {
		Sprite sprite = new Sprite("TestSprite");
		PhysicWorldMock physicWorld = new PhysicWorldMock();
		PhysicCostumeMock physicCostume = new PhysicCostumeMock(sprite, physicWorld, null);

		physicCostume.setImageChanged(false);
		assertFalse(physicCostume.checkImageChanged());
		assertFalse(physicWorld.changeCostumeExecuted);

		physicCostume.setImageChanged(true);
		assertTrue(physicCostume.checkImageChanged());
		assertTrue(physicWorld.changeCostumeExecuted);
		assertEquals(sprite, physicWorld.changeCostumeExecutedWithSprite);
	}

	public void testUpdatePositionAndRotation() {
		Sprite sprite = new Sprite("TestSprite");
		PhysicWorld physicWorld = new PhysicWorld();
		PhysicObject physicObject = physicWorld.getPhysicObject(sprite);
		PhysicCostumeUpdateMock physicCostume = new PhysicCostumeUpdateMock(sprite, physicWorld, physicObject);

		Vector2 position = new Vector2(1.2f, 3.4f);
		float rotation = 3.14f;

		physicCostume.setXYPosition(position.x, position.y);
		physicCostume.setRotation(rotation);

		assertNotSame(position, physicCostume.getCostumePosition());
		assertNotSame(rotation, physicCostume.getCostumeRotation());

		physicCostume.updatePositionAndRotation();

		assertEquals(position, physicCostume.getCostumePosition());
		assertEquals(rotation, physicCostume.getCostumeRotation());
	}

	public void testPositionAndAngle() {
		PhysicWorld physicWorld = new PhysicWorld();
		PhysicObject physicObject = physicWorld.getPhysicObject(new Sprite("TestSprite"));
		PhysicCostume physicCostume = new PhysicCostume(null, physicWorld, physicObject);

		float x = 1.2f;
		physicCostume.setXPosition(x);
		assertEquals(x, physicObject.getXPosition());

		float y = -3.4f;
		physicCostume.setYPosition(y);
		assertEquals(y, physicObject.getYPosition());

		x = 5.6f;
		y = 7.8f;
		physicCostume.setXYPosition(x, y);
		assertEquals(new Vector2(x, y), physicObject.getXYPosition());

		float rotation = 9.0f;
		physicCostume.setRotation(rotation);
		assertEquals(rotation, physicObject.getAngle());

		assertEquals(x, physicCostume.getXPosition());
		assertEquals(y, physicCostume.getYPosition());
		assertEquals(rotation, physicCostume.getRotation());
	}

	public void testSize() {
		Sprite sprite = new Sprite("TestSprite");
		PhysicWorldMock physicWorld = new PhysicWorldMock();
		PhysicCostume physicCostume = new PhysicCostume(sprite, physicWorld, null);

		assertFalse(physicWorld.changeCostumeExecuted);
		physicCostume.setSize(3.14f);
		assertTrue(physicWorld.changeCostumeExecuted);
		assertEquals(sprite, physicWorld.changeCostumeExecutedWithSprite);
	}

	private class PhysicCostumeUpdateMock extends PhysicCostume {

		public PhysicCostumeUpdateMock(Sprite sprite, PhysicWorld physicWorld, PhysicObject physicObject) {
			super(sprite, physicWorld, physicObject);
		}

		public Vector2 getCostumePosition() {
			float x = super.getXPosition();
			float y = super.getYPosition();

			return new Vector2(x, y);
		}

		public float getCostumeRotation() {
			return super.getRotation();
		}
	}

	private class PhysicCostumeMock extends PhysicCostume {

		public PhysicCostumeMock(Sprite sprite, PhysicWorld physicWorld, PhysicObject physicObject) {
			super(sprite, physicWorld, physicObject);
		}

		@Override
		protected boolean checkImageChanged() {
			return super.checkImageChanged();
		}

		public void setImageChanged(boolean imageChanged) {
			this.imageChanged = imageChanged;
		}

	}

	private class PhysicWorldMock extends PhysicWorld {

		public boolean changeCostumeExecuted = false;
		public Sprite changeCostumeExecutedWithSprite = null;

		@Override
		public void changeCostume(Sprite sprite) {
			changeCostumeExecuted = true;
			changeCostumeExecutedWithSprite = sprite;
		}

	}

}
