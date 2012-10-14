package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.physics.PhysicDebugSettings;

import android.test.AndroidTestCase;

public class PhysicDebugSettingsTest extends AndroidTestCase {

	/*
	 * Tests if all physic settings are configured correctly for the release.
	 * Therefore there is no problem if it fails during programming or debugging.
	 */
	public void testSettingsForRelease() {
		assertFalse(PhysicDebugSettings.DEBUGFLAG);
		assertFalse(PhysicDebugSettings.Render.RENDER_COLLISION_FRAMES);
	}
}
