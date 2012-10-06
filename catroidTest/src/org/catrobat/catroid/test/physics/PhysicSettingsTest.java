package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.physics.PhysicSettings;

import android.test.AndroidTestCase;

public class PhysicSettingsTest extends AndroidTestCase {

	/*
	 * Tests if all physic settings are configured correctly for the release.
	 * Therefore there is no problem if it fails during programming or debugging.
	 */
	public void testSettingsForRelease() {
		assertFalse(PhysicSettings.DEBUGFLAG);
		assertFalse(PhysicSettings.Render.RENDER_COLLISION_FRAMES);
	}
}
