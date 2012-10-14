package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.physics.PhysicWorld;

import android.test.AndroidTestCase;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicWorldTest extends AndroidTestCase {
	// TODO: Test it!
	static {
		GdxNativesLoader.load();
	}

	private World world;

	@Override
	public void setUp() {
		world = new World(PhysicWorld.DEFAULT_GRAVITY, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

	@Override
	public void tearDown() {
		world = null;
	}

	public void testWrapper() {
		assertNotNull(world);
	}

}
