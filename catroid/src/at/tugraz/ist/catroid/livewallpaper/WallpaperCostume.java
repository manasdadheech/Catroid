/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tugraz.ist.catroid.livewallpaper;

import android.graphics.Bitmap;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.common.CostumeData;
import at.tugraz.ist.catroid.content.Costume;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Sprite;

public class WallpaperCostume {
	private static WallpaperCostume wallpaperCostume;

	private CostumeData costumeData;
	private Costume backgroundCostume;

	private Bitmap costume = null;
	private Bitmap background = null;

	private float top;
	private float left;

	private int screenWidthHalf;
	private int screenHeightHalf;

	private boolean coordsSetManually = false;
	private boolean costumeHidden = false;
	private boolean sizeSetManually = false;

	private WallpaperCostume() {

		Project currentProject = ProjectManager.getInstance().getCurrentProject();

		this.screenHeightHalf = currentProject.virtualScreenHeight / 2;
		this.screenWidthHalf = currentProject.virtualScreenWidth / 2;
	}

	public static WallpaperCostume getInstance() {
		if (wallpaperCostume == null) {
			wallpaperCostume = new WallpaperCostume();
		}

		return wallpaperCostume;
	}

	public void initCostumeToDraw(CostumeData costumeData, boolean isBackground) {
		this.costumeData = costumeData;
		Bitmap bitmap = costumeData.getImageBitmap();

		if (!coordsSetManually) {
			this.top = screenWidthHalf - (bitmap.getWidth() / 2);
			this.left = screenHeightHalf - (bitmap.getHeight() / 2);
		}

		if (isBackground) {
			this.background = bitmap;
		} else {
			this.costume = bitmap;

		}

	}

	public void initSetSize(Sprite sprite, Bitmap bitmap, double size) {
		if (sprite.getName().equals("Background")) {
			bitmap.setDensity((int) size);
		}
	}

	public boolean touchedInsideTheCostume(float x, float y) {
		float right = costume.getWidth() + top;
		float bottom = costume.getHeight() + left;

		if (x > top && x < right && y > left && y < bottom) {
			return true;
		}

		return false;

	}

	public Bitmap getCostume() {
		return costume;
	}

	public void setCostume(Bitmap costume) {
		this.costume = costume;
	}

	public Bitmap getBackground() {
		return background;
	}

	public void setBackground(Bitmap background) {
		this.background = background;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
		coordsSetManually = true;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
		coordsSetManually = true;
	}

	public CostumeData getCostumeData() {
		return costumeData;
	}

	public void setCostumeData(CostumeData costumeData) {
		this.costumeData = costumeData;
	}

	public boolean isCostumeHidden() {
		return costumeHidden;
	}

	public void setCostumeHidden(boolean hideCostume) {
		this.costumeHidden = hideCostume;
	}

}
