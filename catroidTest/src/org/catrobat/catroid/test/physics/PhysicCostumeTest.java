package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.physics.PhysicCostume;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
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
		PhysicWorld physicWorld = new PhysicWorldMock();
		PhysicObject physicObject = new PhysicObjectMock();

		PhysicCostume costume = new PhysicCostume(sprite, physicWorld, physicObject);
		TestUtils.invokeMethod(costume, "checkImageChanged", null, null);
	}

	public void testUpdatePositionAndRotation() {
	}

	public void testPositionAndAngle() {
	}

	public void testSize() {
	}

	private class CostumeMock extends Costume {

		private boolean imageChanged;

		public CostumeMock(Sprite sprite) {
			super(sprite);
		}

	}

	private class PhysicWorldMock extends PhysicWorld {

	}

	private class PhysicObjectMock extends PhysicObject {

		public PhysicObjectMock() {
			super(new BodyMock(null, 0));
		}
	}

	private class BodyMock extends Body {

		protected BodyMock(World world, long addr) {
			super(world, addr);
		}

	}
}
