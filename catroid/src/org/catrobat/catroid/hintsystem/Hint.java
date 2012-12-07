/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2012 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.hintsystem;

import org.catrobat.catroid.common.Values;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * @author peter
 * 
 */
public class Hint {

	private static Hint hint = new Hint();

	private static Context context;
	private WindowManager windowManager;
	private HintOverlay hintOverlay;

	private Hint() {

	}

	public static Hint getInstance() {
		if (hint == null) {
			hint = new Hint();
		}
		return hint;
	}

	public static void setContext(Context context) {
		Hint.context = context;
	}

	public void overlayHint() {
		float density = context.getResources().getDisplayMetrics().density;
		ScreenParameters screenparameters = ScreenParameters.getInstance();
		screenparameters.setDensity(density);

		WindowManager.LayoutParams windowParameters = createLayoutParameters();
		windowManager = ((Activity) context).getWindowManager();
		hintOverlay = new HintOverlay(context);
		windowManager.addView(hintOverlay, windowParameters);

	}

	public void removeHint() {
		windowManager.removeView(hintOverlay);
		hintOverlay = null;
		System.gc();
		System.runFinalization();
	}

	public WindowManager.LayoutParams createLayoutParameters() {
		WindowManager.LayoutParams windowParameters = new WindowManager.LayoutParams();
		windowParameters.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		windowParameters.height = Values.SCREEN_HEIGHT;
		windowParameters.width = Values.SCREEN_WIDTH;
		windowParameters.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		windowParameters.format = PixelFormat.TRANSLUCENT;
		return windowParameters;
	}
}
