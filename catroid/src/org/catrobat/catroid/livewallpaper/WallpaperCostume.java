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

package org.catrobat.catroid.livewallpaper;

import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.utils.ImageEditing;

import android.graphics.Bitmap;
import android.util.Log;

public class WallpaperCostume {

	private CostumeData costumeData;
	private Sprite sprite;
	private Bitmap costume = null;

	private int x;
	private int y;
	private int top;
	private int left;

	float rotation = 0f;

	private int zPosition;

	private float alphaValue = 1f;
	private float brightness = 1f;

	private double size = 1;

	private boolean hidden = false;
	private boolean isBackground = false;
	private boolean topNeedsAdjustment = false;
	private boolean leftNeedsAdjustment = false;
	private boolean sizeChanged = false;
	private boolean coordsSwapped = false;

	private Bitmap temp;

	private WallpaperHelper wallpaperHelper;

	public WallpaperCostume(Sprite sprite, CostumeData costumeData) {

		this.wallpaperHelper = WallpaperHelper.getInstance();
		this.sprite = sprite;
		this.zPosition = wallpaperHelper.getProject().getSpriteList().indexOf(sprite);

		if (sprite.getName().equals("Background")) {
			this.isBackground = true;
			this.top = 0;
			this.left = 0;
		} else {
			setY(0);
			setX(0);
		}

		if (costumeData != null) {
			setCostume(costumeData);
		}

		wallpaperHelper.setRefreshRate(50);

		sprite.setWallpaperCostume(this);

	}

	public void clear() {
		alphaValue = 1f;
		brightness = 1f;
		rotation = 0f;
		size = 1;
		hidden = false;
		zPosition = wallpaperHelper.getProject().getSpriteList().indexOf(sprite);
		costume = null;
		costumeData.nullifyBitmaps();
	}

	public float getTop() {
		if (topNeedsAdjustment) {
			this.topNeedsAdjustment = false;
			if (!wallpaperHelper.isLandscape()) {
				this.top = wallpaperHelper.getCenterXCoord() + x - (this.costume.getWidth() / 2);
			} else {
				this.top = wallpaperHelper.getCenterXCoord() + y - (this.costume.getWidth() / 2);
			}
		}
		return top;
	}

	public float getLeft() {
		if (leftNeedsAdjustment) {
			this.leftNeedsAdjustment = false;
			if (!wallpaperHelper.isLandscape()) {
				this.left = wallpaperHelper.getCenterYCoord() - y - (this.costume.getHeight() / 2);
			} else {
				this.left = wallpaperHelper.getCenterYCoord() + x - (this.costume.getHeight() / 2);

			}
		}
		return left;
	}

	public void setX(int x) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.x = -x;
		} else {
			this.x = x;
		}
		this.topNeedsAdjustment = true;

	}

	public void setY(int y) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.y = -y;
		} else {
			this.y = y;
		}
		this.leftNeedsAdjustment = true;

	}

	public void setXYPosition(int xPosition, int yPosition) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.x = -xPosition;
			this.y = -yPosition;
		} else {
			this.x = xPosition;
			this.y = yPosition;
		}
		this.topNeedsAdjustment = true;
		this.leftNeedsAdjustment = true;
	}

	public void changeXBy(int x) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.x -= x;
		} else {
			this.x += x;

		}
		this.topNeedsAdjustment = true;

	}

	public void changeYby(int y) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.y -= y;
		} else {
			this.y += y;
		}
		this.leftNeedsAdjustment = true;
	}

	public void changeXYBy(int xValue, int yValue) {
		if (wallpaperHelper.isLandscape() && wallpaperHelper.getLandscapeRotationDegree() == -90) {
			this.x -= xValue;
			this.y -= yValue;
		} else {
			this.x += xValue;
			this.y += yValue;

		}
		this.topNeedsAdjustment = true;
		this.leftNeedsAdjustment = true;
	}

	public boolean touchedInsideTheCostume(float x, float y) {
		if (isBackground || costume == null) {
			return false;
		}

		float right = top;
		float bottom = left;

		right += costume.getWidth();
		bottom += costume.getHeight();

		if (x > top && x < right && y > left && y < bottom) {
			return true;
		}

		return false;

	}

	public Bitmap getCostume() {

		if (wallpaperHelper.isLandscape()) {

			if (!coordsSwapped) {
				int temp = this.top;
				this.top = this.left;
				this.left = temp;
				this.coordsSwapped = true;
			}

		}

		return costume;
	}

	public void setCostume(CostumeData costumeData) {
		this.costumeData = costumeData;
		this.costume = costumeData.getCostumeBitmap();

		if (sizeChanged) {
			resizeCostume();
		}

	}

	public void setCostumeSize(double size) {
		this.sizeChanged = true;
		this.size = size * 0.01;
		if (costumeData != null) {
			this.costume = costumeData.getCostumeBitmap();
			resizeCostume();
		}

	}

	public void changeCostumeSizeBy(double changeValue) {
		this.sizeChanged = true;
		this.size += (changeValue * 0.01);
		resizeCostume();
	}

	private void resizeCostume() {
		this.temp = costume;

		int newWidth = (int) (temp.getWidth() * size);
		int newHeight = (int) (temp.getHeight() * size);

		if (newWidth <= 0) {
			newWidth = 1;
		}

		if (newHeight <= 0) {
			newHeight = 1;
		}

		Log.v("SIZE", String.valueOf(newWidth) + "  " + String.valueOf(newHeight));

		this.temp = ImageEditing.scaleBitmap(temp, newWidth, newHeight);
		this.costume = temp;
		this.temp = null;

		this.topNeedsAdjustment = true;
		this.leftNeedsAdjustment = true;

	}

	public CostumeData getCostumeData() {
		return costumeData;
	}

	public boolean isCostumeHidden() {
		return hidden;
	}

	public void setCostumeHidden(boolean hideCostume) {
		this.hidden = hideCostume;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean isBackground() {
		return isBackground;
	}

	public void setBackground(boolean isBackground) {
		this.isBackground = isBackground;
	}

	public float getAlphaValue() {
		return alphaValue;
	}

	public void setAlphaValue(float alphaValue) {
		this.alphaValue = alphaValue;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float percentage) {

		if (percentage < 0f) {
			percentage = 0f;
		}

		this.brightness = percentage;

		adjustBrightness();
	}

	public void changeBrightness(float percentage) {

		this.brightness += percentage;
		if (this.brightness < 0f) {
			this.brightness = 0f;
		}

		adjustBrightness();
	}

	private void adjustBrightness() {
		this.temp = costume;
		this.temp = ImageEditing.adjustBitmpaBrigthness(temp, brightness);
		this.costume = temp;
		this.temp = null;
	}

	public void setGhostEffect(float alpha) {
		if (alpha < 0f) {
			this.alphaValue = 0f;
		} else if (alpha > 1f) {
			this.alphaValue = 1f;
		}
		this.alphaValue = alpha;

		adjustGhostEffect();
	}

	public void changeGhostEffect(float alpha) {
		this.alphaValue += alpha;

		if (this.alphaValue < 0f) {
			this.alphaValue = 0f;
		} else if (this.alphaValue > 1f) {
			this.alphaValue = 1f;
		}

		adjustGhostEffect();

	}

	private void adjustGhostEffect() {
		this.temp = costume;
		this.temp = ImageEditing.adjustBitmapAlphaValue(temp, alphaValue);
		this.costume = temp;
		this.temp = null;

	}

	public void clearGraphicEffect() {
		this.alphaValue = 1f;
		this.brightness = 1f;
		adjustBrightness();
		adjustGhostEffect();
	}

	public void rotate() {
		this.temp = costume;
		//this.costume = ImageEditing.rotateBitmap(costumeData.getCostumeBitmap(), (int) this.rotation);
		this.temp = ImageEditing.rotateBitmap(temp, (int) this.rotation);
		this.costume = temp;
		this.temp = null;
		this.topNeedsAdjustment = true;
		this.leftNeedsAdjustment = true;
	}

	public int getzPosition() {
		return zPosition;
	}

	public void setzPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public void setRotation(float r) {
		this.rotation += r;
		rotate();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
