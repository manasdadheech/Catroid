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
package org.catrobat.catroid.physics;

import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Sprite;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.utils.Scaling;

public class PhysicCostume extends Costume {

	private final boolean constructorFinished;
	private final PhysicWorld physicWorld;
	private final PhysicObject physicObject;

	public PhysicCostume(Sprite sprite, PhysicWorld physicWorld, PhysicObject physicObject) {
		super(sprite);

		this.physicWorld = physicWorld;
		this.physicObject = physicObject;

		constructorFinished = true;
	}

	@Override
	protected boolean checkImageChanged() {
		if (super.checkImageChanged()) {
			physicWorld.changeCostume(sprite);
			return true;
		}

		return false;
	}

	public void setSpriteXYPosition(float x, float y) {
		super.setXYPosition(x, y);
	}

	public void setSpriteRotation(float degrees) {
		super.setRotation(degrees);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		return super.touchDown(x, y, pointer);
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		super.touchDragged(x, y, pointer);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	public Pixmap adjustBrightness(Pixmap currentPixmap) {
		return super.adjustBrightness(currentPixmap);
	}

	@Override
	public void refreshTextures() {
		super.refreshTextures();
	}

	@Override
	public void aquireXYWidthHeightLock() {
		super.aquireXYWidthHeightLock();
	}

	@Override
	public void setXPosition(float x) {
		physicObject.setXPosition(x);
	}

	@Override
	public void setYPosition(float y) {
		physicObject.setYPosition(y);
	}

	@Override
	public void setXYPosition(float x, float y) {
		physicObject.setXYPosition(x, y);
	}

	@Override
	public void setRotation(float degrees) {
		physicObject.setAngle(degrees);
	}

	@Override
	public float getRotation() {
		return physicObject.getAngle();
	}

	@Override
	public float getXPosition() {
		return physicObject.getXPosition();
	}

	@Override
	public float getYPosition() {
		return physicObject.getYPosition();
	}

	@Override
	public float getWidth() {
		return super.getWidth();
	}

	@Override
	public float getHeight() {
		return super.getHeight();
	}

	@Override
	public void releaseXYWidthHeightLock() {
		super.releaseXYWidthHeightLock();
	}

	@Override
	public void setCostumeData(CostumeData costumeData) {
		super.setCostumeData(costumeData);
	}

	@Override
	public void setCostumeDataInternal(CostumeData costumeData) {
		super.setCostumeDataInternal(costumeData);
	}

	@Override
	public String getImagePath() {
		return super.getImagePath();
	}

	@Override
	public void setSize(float size) {
		super.setSize(size);
		physicWorld.changeCostume(sprite);
	}

	@Override
	public float getSize() {
		return super.getSize();
	}

	@Override
	public void setAlphaValue(float alphaValue) {
		super.setAlphaValue(alphaValue);
	}

	@Override
	public void changeAlphaValueBy(float value) {
		super.changeAlphaValueBy(value);
	}

	@Override
	public float getAlphaValue() {
		return super.getAlphaValue();
	}

	@Override
	public void setBrightnessValue(float percent) {
		super.setBrightnessValue(percent);
	}

	@Override
	public void changeBrightnessValueBy(float percent) {
		super.changeBrightnessValueBy(percent);
	}

	@Override
	public float getBrightnessValue() {
		return super.getBrightnessValue();
	}

	@Override
	public CostumeData getCostumeData() {
		return super.getCostumeData();
	}

	@Override
	public float getImageHeight() {
		return super.getImageHeight();
	}

	@Override
	public float getImageWidth() {
		return super.getImageWidth();
	}

	@Override
	public float getImageX() {
		return super.getImageX();
	}

	@Override
	public float getImageY() {
		return super.getImageY();
	}

	@Override
	public float getMinHeight() {
		return super.getMinHeight();
	}

	@Override
	public float getMinWidth() {
		return super.getMinWidth();
	}

	@Override
	public NinePatch getPatch() {
		return super.getPatch();
	}

	@Override
	public float getPrefHeight() {
		return super.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		return super.getPrefWidth();
	}

	@Override
	public TextureRegion getRegion() {
		return super.getRegion();
	}

	@Override
	public void layout() {
		super.layout();
	}

	@Override
	public void setAlign(int align) {
		super.setAlign(align);
	}

	@Override
	public void setClickListener(ClickListener clickListener) {
		super.setClickListener(clickListener);
	}

	@Override
	public void setPatch(NinePatch patch) {
		super.setPatch(patch);
	}

	@Override
	public void setRegion(TextureRegion region) {
		if (!constructorFinished) {
			super.setRegion(region);
		} else {
			super.setRegion(region);
		}
	}

	@Override
	public void setScaling(Scaling scaling) {
		super.setScaling(scaling);
	}

	@Override
	public float getMaxHeight() {
		return super.getMaxHeight();
	}

	@Override
	public float getMaxWidth() {
		return super.getMaxWidth();
	}

	@Override
	public Actor hit(float x, float y) {
		return super.hit(x, y);
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void invalidateHierarchy() {
		super.invalidateHierarchy();
	}

	@Override
	public boolean needsLayout() {
		return super.needsLayout();
	}

	@Override
	public void pack() {
		super.pack();
	}

	@Override
	public void setFillParent(boolean fillParent) {
		super.setFillParent(fillParent);
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void action(Action action) {
		super.action(action);
	}

	@Override
	public void clearActions() {
		super.clearActions();
	}

	@Override
	public Stage getStage() {
		return super.getStage();
	}

	@Override
	public boolean isMarkedToRemove() {
		return super.isMarkedToRemove();
	}

	@Override
	public boolean keyDown(int keycode) {
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return super.keyTyped(character);
	}

	@Override
	public boolean keyUp(int keycode) {
		return super.keyUp(keycode);
	}

	@Override
	public void markToRemove(boolean remove) {
		super.markToRemove(remove);
	}

	@Override
	public void remove() {
		super.remove();
	}

	@Override
	public boolean scrolled(int amount) {
		return super.scrolled(amount);
	}

	@Override
	public void toLocalCoordinates(Vector2 point) {
		super.toLocalCoordinates(point);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean touchMoved(float x, float y) {
		return super.touchMoved(x, y);
	}

}
