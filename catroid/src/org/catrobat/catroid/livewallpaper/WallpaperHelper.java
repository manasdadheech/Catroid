/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.livewallpaper;

import org.catrobat.catroid.common.Values;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.SoundManager;

import android.os.Handler;

public class WallpaperHelper {

	private static WallpaperHelper wallpaperHelper;

	private Project project;

	private int centerXCoord;
	private int centerYCoord;

	private int landscapeRotationDegree = 0;

	private long refreshRate = 100;

	private Handler drawingThreadHandler;
	private Runnable drawingThread;
	private boolean soundAllowed = true;

	private boolean isLandscape = false;

	private boolean isLiveWallpaper = false;

	public WallpaperHelper() {

	}

	public static WallpaperHelper getInstance() {
		if (wallpaperHelper == null) {
			wallpaperHelper = new WallpaperHelper();
		}

		return wallpaperHelper;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		setCenterCoordinates();
		this.project = project;
	}

	public int getCenterXCoord() {
		return centerXCoord;
	}

	public void setCenterXCoord(int centerXCoord) {
		this.centerXCoord = centerXCoord;
	}

	public int getCenterYCoord() {
		return centerYCoord;
	}

	public void setCenterYCoord(int centerYCoord) {
		this.centerYCoord = centerYCoord;
	}

	public void destroy() {

		for (Sprite sprite : project.getSpriteList()) {
			if (sprite.getWallpaperCostume() != null) {
				sprite.pause();
				sprite.finish();
				sprite.getWallpaperCostume().clear();

			}
		}

		this.isLandscape = false;
		this.isLiveWallpaper = false;

		SoundManager.getInstance().stopAllSounds();

	}

	public boolean isLiveWallpaper() {
		return isLiveWallpaper;
	}

	public void setLiveWallpaper(boolean isLiveWallpaper) {
		this.isLiveWallpaper = isLiveWallpaper;
	}

	public Handler getDrawingThreadHandler() {
		return drawingThreadHandler;
	}

	public void setDrawingThreadHandler(Handler drawingThreadHandler) {
		this.drawingThreadHandler = drawingThreadHandler;
	}

	public Runnable getDrawingThread() {
		return drawingThread;
	}

	public void setDrawingThread(Runnable drawingThread) {
		this.drawingThread = drawingThread;
	}

	public void setCenterCoordinates() {
		this.centerYCoord = Values.SCREEN_HEIGHT / 2;
		this.centerXCoord = Values.SCREEN_WIDTH / 2;
	}

	public boolean isLandscape() {
		return isLandscape;
	}

	public void setLandscape(boolean isLandscape) {
		this.isLandscape = isLandscape;
	}

	public boolean isSoundAllowed() {
		return soundAllowed;
	}

	public void setSoundAllowed(boolean soundAllowed) {
		this.soundAllowed = soundAllowed;
	}

	public long getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(long refreshRate) {
		this.refreshRate = refreshRate;
	}

	public int getLandscapeRotationDegree() {
		return landscapeRotationDegree;
	}

	public void setLandscapeRotationDegree(int landscapeRotationDegree) {
		this.landscapeRotationDegree = landscapeRotationDegree;
	}

}
