package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicWorldTest extends AndroidTestCase {
	// TODO: Test it!
	static {
		GdxNativesLoader.load();
	}

	private PhysicWorld physicWorld;
	private World world;

	@Override
	public void setUp() {
		physicWorld = new PhysicWorld();
		world = (World) TestUtils.getPrivateField("world", physicWorld, false);
	}

	@Override
	public void tearDown() {
		physicWorld = null;
		world = null;
	}

	public void testDefaultSettings() {
		assertEquals(40.0f, PhysicWorld.RATIO);
		assertEquals(20, PhysicWorld.VELOCITY_ITERATIONS);
		assertEquals(20, PhysicWorld.POSITION_ITERATIONS);

		assertEquals(new Vector2(0, -10), PhysicWorld.DEFAULT_GRAVITY);
		assertEquals(false, PhysicWorld.IGNORE_SLEEPING_OBJECTS);
	}

	public void testWrapper() {
		assertNotNull(world);
	}

	public void testGravity() {
		assertEquals(PhysicWorld.DEFAULT_GRAVITY, world.getGravity());

		Vector2 newGravity = new Vector2(-1.2f, 3.4f);
		physicWorld.setGravity(null, newGravity);

		assertEquals(newGravity, world.getGravity());
	}

	public void testChangeCostume() {
	}

	public void testGetPhysicObject() {
	}

	public void testStep() {
	}

}
