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
package org.catrobat.catroid.utils;

import java.io.File;
import java.io.FileNotFoundException;

import org.catrobat.catroid.io.StorageHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;

public class ImageEditing {

	public ImageEditing() {

	}

	/**
	 * Scales the bitmap to the specified size.
	 * 
	 * @param bitmap
	 *            the bitmap to resize
	 * @param xSize
	 *            desired x size
	 * @param ySize
	 *            desired y size
	 * @return a new, scaled bitmap
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int xSize, int ySize) {
		if (bitmap == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		float scaleWidth = (((float) xSize) / bitmap.getWidth());
		float scaleHeight = (((float) ySize) / bitmap.getHeight());
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return newBitmap;
	}

	public static Bitmap getScaledBitmapFromPath(String imagePath, int outWidth, int outHeight, boolean justScaleDown) {
		if (imagePath == null) {
			return null;
		}

		int[] imageDimensions = new int[2];
		imageDimensions = getImageDimensions(imagePath);

		int origWidth = imageDimensions[0];
		int origHeight = imageDimensions[1];

		double sampleSizeWidth = (origWidth / (double) outWidth);
		double sampleSizeHeight = origHeight / (double) outHeight;
		double sampleSize = Math.max(sampleSizeWidth, sampleSizeHeight);
		int sampleSizeRounded = (int) Math.floor(sampleSize);

		if (justScaleDown && sampleSize <= 1) {
			return BitmapFactory.decodeFile(imagePath);
		}

		int newHeight = (int) Math.ceil(origHeight / sampleSize);
		int newWidth = (int) Math.ceil(origWidth / sampleSize);

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = sampleSizeRounded;

		Bitmap tempBitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);
		return scaleBitmap(tempBitmap, newWidth, newHeight);
	}

	public static int[] getImageDimensions(String imagePath) {
		int[] imageDimensions = new int[2];

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, o);

		imageDimensions[0] = o.outWidth;
		imageDimensions[1] = o.outHeight;

		return imageDimensions;
	}

	public static Bitmap createSingleColorBitmap(int width, int height, int color) {
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		newBitmap.eraseColor(color);
		return newBitmap;
	}

	public static void overwriteImageFileWithNewBitmap(File imageFile) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap unmutableBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
		int bitmapWidth = unmutableBitmap.getWidth();
		int bitmapHeight = unmutableBitmap.getHeight();
		int[] bitmapPixels = new int[bitmapWidth * bitmapHeight];
		unmutableBitmap.getPixels(bitmapPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);

		Bitmap mutableBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		mutableBitmap.setPixels(bitmapPixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
		StorageHandler.saveBitmapToImageFile(imageFile, mutableBitmap);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int rotationDegree) {
		Matrix rotateMatrix = new Matrix();
		rotateMatrix.postRotate(rotationDegree);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix,
				true);
		return rotatedBitmap;
	}

	public static Bitmap adjustBitmpaBrigthness(Bitmap bitmap, float brightness) {

		Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

		for (int width = 0; width < bitmap.getWidth(); width++) {
			for (int height = 0; height < bitmap.getHeight(); height++) {
				int oldPixelColor = bitmap.getPixel(width, height);

				int red = Color.red(oldPixelColor) + (int) (255 * (brightness - 1));
				int green = Color.green(oldPixelColor) + (int) (255 * (brightness - 1));
				int blue = Color.blue(oldPixelColor) + (int) (255 * (brightness - 1));
				int alpha = Color.alpha(oldPixelColor);

				if (red > 255) {
					red = 255;
				} else if (red < 0) {
					red = 0;
				}
				if (green > 255) {
					green = 255;
				} else if (green < 0) {
					green = 0;
				}
				if (blue > 255) {
					blue = 255;
				} else if (blue < 0) {
					blue = 0;
				}

				int newPixel = Color.argb(alpha, red, green, blue);
				resultBitmap.setPixel(width, height, newPixel);
			}
		}

		return resultBitmap;

	}

	public static Bitmap adjustBitmapAlphaValue(Bitmap bitmap, float alphaValue) {

		Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

		for (int width = 0; width < bitmap.getWidth(); width++) {
			for (int height = 0; height < bitmap.getHeight(); height++) {

				int oldPixelColor = bitmap.getPixel(width, height);

				int red = Color.red(oldPixelColor);
				int green = Color.green(oldPixelColor);
				int blue = Color.blue(oldPixelColor);
				int alpha = Color.alpha(oldPixelColor) + (int) (255 * (alphaValue - 1));

				if (alpha > 255) {
					alpha = 255;
				} else if (alpha < 0) {
					alpha = 0;
				}

				int newPixel = Color.argb(alpha, red, green, blue);
				resultBitmap.setPixel(width, height, newPixel);
			}
		}

		return resultBitmap;

	}

}
