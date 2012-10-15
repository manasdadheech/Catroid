package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.physics.PhysicWorld;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
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

	public void testDefaultValues() {
		assertEquals(40.0f, PhysicWorld.RATIO);
		assertEquals(20, PhysicWorld.VELOCITY_ITERATIONS);
		assertEquals(20, PhysicWorld.POSITION_ITERATIONS);

		assertEquals(new Vector2(0, -10), PhysicWorld.DEFAULT_GRAVITY);
		assertEquals(false, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

}
