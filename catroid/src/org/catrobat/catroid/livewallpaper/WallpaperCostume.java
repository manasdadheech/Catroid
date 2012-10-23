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
import android.graphics.Matrix;
import android.graphics.Paint;

public class WallpaperCostume {

	private CostumeData costumeData;
	private Sprite sprite;
	private Bitmap costume = null;
	private Matrix matrix;
	private Paint paint;

	private int x;
	private int y;

	private int centerX;
	private int centerY;

	private int costumeWidth;
	private int costumeHeight;

	float rotation = 0f;

	private int zPosition;

	private int alphaValue = 255;
	private float brightness = 1f;

	private double size = 1;

	private boolean hidden = false;
	private boolean isBackground = false;
	private boolean coordsSwapped = false;

	private WallpaperHelper wallpaperHelper;

	public WallpaperCostume(Sprite sprite, CostumeData costumeData) {

		this.wallpaperHelper = WallpaperHelper.getInstance();
		this.sprite = sprite;
		this.zPosition = wallpaperHelper.getProject().getSpriteList().indexOf(sprite);
		this.matrix = new Matrix();

		if (sprite.getName().equals("Background")) {
			this.isBackground = true;
		}

		this.x = 0;
		this.y = 0;

		this.paint = new Paint();

		if (costumeData != null) {
			setCostume(costumeData);
		}
		sprite.setWallpaperCostume(this);

	}

	public void clear() {
		alphaValue = 255;
		brightness = 1f;
		rotation = 0f;
		size = 1;
		hidden = false;
		zPosition = wallpaperHelper.getProject().getSpriteList().indexOf(sprite);
		costume = null;
		costumeData.nullifyBitmaps();
	}

	private void updateMatrix() {
		matrix.setRotate(rotation, costumeWidth / 2, costumeHeight / 2);
		matrix.postScale((float) size, (float) size);

		if (wallpaperHelper.isLandscape()) {
			centerX = wallpaperHelper.getCenterXCoord() - x - (int) (costumeWidth * size) / 2;
			centerY = wallpaperHelper.getCenterYCoord() - y - (int) (costumeHeight * size) / 2;
		} else {
			centerX = wallpaperHelper.getCenterXCoord() + x - (int) (costumeWidth * size) / 2;
			centerY = wallpaperHelper.getCenterYCoord() + y - (int) (costumeHeight * size) / 2;
		}

		matrix.postTranslate(centerX, centerY);
	}

	private void updatePaint() {
		paint.setAlpha(alphaValue);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;

	}

	public void changeXBy(int x) {
		if (wallpaperHelper.isLandscape()) {
			this.x -= x;
		} else {
			this.x += x;

		}

	}

	public void changeYby(int y) {
		if (wallpaperHelper.isLandscape()) {
			this.y -= y;
		} else {
			this.y += y;
		}
	}

	public boolean touchedInsideTheCostume(float touchX, float touchY) {
		if (isBackground || costume == null) {
			return false;
		}

		float right = centerX;
		float bottom = centerY;

		right += costumeWidth;
		bottom += costumeHeight;

		if (touchX > centerX && touchX < right && touchY > centerY && touchY < bottom) {
			return true;
		}

		return false;

	}

	public Bitmap getCostume() {
		if (wallpaperHelper.isLandscape()) {
			if (!coordsSwapped) {
				int temp = x;
				x = y;
				y = temp;
				this.coordsSwapped = true;
			}

		}
		return costume;
	}

	public void setCostume(CostumeData costumeData) {
		this.costumeData = costumeData;
		this.costume = costumeData.getCostumeBitmap();
		this.costumeWidth = costume.getWidth();
		this.costumeHeight = costume.getHeight();

	}

	public void setCostumeSize(double size) {
		this.size = size * 0.01;

	}

	public void changeCostumeSizeBy(double changeValue) {
		this.size += (changeValue * 0.01);
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
		Bitmap temp = costume;
		temp = ImageEditing.adjustBitmpaBrigthness(temp, brightness);
		this.costume = temp;
		temp = null;
	}

	public void clearGraphicEffect() {
		this.alphaValue = 255;
		this.brightness = 1f;
		adjustBrightness();
	}

	public int getzPosition() {
		return zPosition;
	}

	public void setzPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public void setRotation(float r) {
		this.rotation += r;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Matrix getMatrix() {
		updateMatrix();
		return matrix;
	}

	public Paint getPaint() {
		updatePaint();
		return paint;
	}

	public int getAlphaValue() {
		return alphaValue;
	}

	public void setAlphaValue(int alphaValue) {
		this.alphaValue = alphaValue;
	}

}
