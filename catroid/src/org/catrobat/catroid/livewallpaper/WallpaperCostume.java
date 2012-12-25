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
import org.catrobat.catroid.common.Values;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.utils.ImageEditing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
	private float brightness = 0;

	private double size = 1;

	private boolean hidden = false;
	private boolean isBackground = false;
	private boolean isLandscape = false;
	private boolean changeMatrix = true;

	private int landscapeRotation;

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

		if (wallpaperHelper.isLandscape()) {
			int temp = x;
			x = y;
			y = temp;

			this.isLandscape = true;
			this.landscapeRotation = wallpaperHelper.getLandscapeRotationDegree();

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
		if (costumeData != null) {
			costumeData.nullifyBitmaps();
		}
	}

	private void updateMatrix() {
		if (changeMatrix) {
			matrix.setRotate(rotation, costumeWidth / 2, costumeHeight / 2);
			matrix.postScale((float) size, (float) size);

			if (isLandscape) {
				centerX = wallpaperHelper.getCenterXCoord() - y - (int) (costumeWidth * size) / 2;
				centerY = wallpaperHelper.getCenterYCoord() - x - (int) (costumeHeight * size) / 2;
			} else {
				centerX = wallpaperHelper.getCenterXCoord() + x - (int) (costumeWidth * size) / 2;
				centerY = wallpaperHelper.getCenterYCoord() - y - (int) (costumeHeight * size) / 2;
			}

			matrix.postTranslate(centerX, centerY);
			changeMatrix = false;
		}
	}

	public void setX(int x) {
		if (isLandscape && landscapeRotation == 90) {
			this.y = -x;
		} else if (isLandscape && landscapeRotation == -90) {
			this.y = x;
		} else {
			this.x = x;
		}
		changeMatrix = true;
	}

	public void setY(int y) {
		if (isLandscape && landscapeRotation == 90) {
			this.x = -y;
		} else if (isLandscape && landscapeRotation == -90) {
			this.x = y;
		} else {
			this.y = y;
		}

		changeMatrix = true;
	}

	public void changeXBy(int x) {
		if (isLandscape && landscapeRotation == 90) {
			this.y -= x;
		} else if (isLandscape && landscapeRotation == -90) {
			this.y += x;
		} else {
			this.x += x;
		}

		changeMatrix = true;

	}

	public void changeYby(int y) {
		if (isLandscape && landscapeRotation == 90) {
			this.x -= y;
		} else if (isLandscape && landscapeRotation == -90) {
			this.x += y;
		} else {
			this.y += y;
		}
		changeMatrix = true;
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
		if (isBackground && costume == null) {
			costume = ImageEditing.createSingleColorBitmap(Values.SCREEN_WIDTH, Values.SCREEN_HEIGHT, Color.WHITE);
			setX(-360);
			setY(592);
		}

		return costume;
	}

	public void setCostume(CostumeData costumeData) {
		this.costumeData = costumeData;
		this.costume = costumeData.getCostumeBitmap();
		this.costumeWidth = costume.getWidth();
		this.costumeHeight = costume.getHeight();
		changeMatrix = true;

	}

	public void setCostumeSize(double size) {
		this.size = size * 0.01;
		changeMatrix = true;
	}

	public void changeCostumeSizeBy(double changeValue) {
		this.size += (changeValue * 0.01);
		if (this.size < 0) {
			this.size = 0;
		}

		changeMatrix = true;
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

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
		ColorMatrix cm = new ColorMatrix();
		cm.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
	}

	public void clearGraphicEffect() {
		paint = new Paint();
	}

	public int getzPosition() {
		return zPosition;
	}

	public void setzPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(float r) {
		this.rotation = r;
		changeMatrix = true;
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
		return paint;
	}

	public int getAlphaValue() {
		return alphaValue;
	}

	public void setAlphaValue(int alphaValue) {
		this.alphaValue = alphaValue;
		paint.setAlpha(alphaValue);
	}

	public void rotate(float r) {
		this.rotation += r;
		changeMatrix = true;
	}

}
