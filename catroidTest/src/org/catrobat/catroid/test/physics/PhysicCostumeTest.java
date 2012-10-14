package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.physics.PhysicWorld;

import android.test.AndroidTestCase;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicCostumeTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	private World world;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		world = new World(PhysicWorld.DEFAULT_GRAVITY, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		world = null;
	}

	public void testProperties() {
	}

	private class CostumeMock extends Costume {

		public CostumeMock(Sprite sprite) {
			super(sprite);
		}

	}
}
