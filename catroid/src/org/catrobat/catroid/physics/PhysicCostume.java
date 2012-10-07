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
	private final Costume spriteCostume;
	private final PhysicObject physicObject;

	public PhysicCostume(Sprite sprite, PhysicObject physicObject) {
		super(sprite);

		this.spriteCostume = sprite.costume;
		this.physicObject = physicObject;

		constructorFinished = true;
	}

	public Costume getCostume() {
		return spriteCostume;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		return spriteCostume.touchDown(x, y, pointer);
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		spriteCostume.touchUp(x, y, pointer);
	}

	@Override
	public void touchDragged(float x, float y, int pointer) {
		spriteCostume.touchDragged(x, y, pointer);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		spriteCostume.draw(batch, parentAlpha);
	}

	@Override
	public void checkImageChanged() {
		spriteCostume.checkImageChanged();
	}

	@Override
	public Pixmap adjustBrightness(Pixmap currentPixmap) {
		return spriteCostume.adjustBrightness(currentPixmap);
	}

	@Override
	public void refreshTextures() {
		spriteCostume.refreshTextures();
	}

	@Override
	public void aquireXYWidthHeightLock() {
		spriteCostume.aquireXYWidthHeightLock();
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
		return spriteCostume.getWidth();
	}

	@Override
	public float getHeight() {
		return spriteCostume.getHeight();
	}

	@Override
	public void releaseXYWidthHeightLock() {
		spriteCostume.releaseXYWidthHeightLock();
	}

	@Override
	public void setCostumeData(CostumeData costumeData) {
		spriteCostume.setCostumeData(costumeData);
	}

	@Override
	public void setCostumeDataInternal(CostumeData costumeData) {
		spriteCostume.setCostumeDataInternal(costumeData);
	}

	@Override
	public String getImagePath() {
		return spriteCostume.getImagePath();
	}

	@Override
	public void setSize(float size) {
		spriteCostume.setSize(size);
	}

	@Override
	public float getSize() {
		return spriteCostume.getSize();
	}

	@Override
	public void setAlphaValue(float alphaValue) {
		spriteCostume.setAlphaValue(alphaValue);
	}

	@Override
	public void changeAlphaValueBy(float value) {
		spriteCostume.changeAlphaValueBy(value);
	}

	@Override
	public float getAlphaValue() {
		return spriteCostume.getAlphaValue();
	}

	@Override
	public void setBrightnessValue(float percent) {
		spriteCostume.setBrightnessValue(percent);
	}

	@Override
	public void changeBrightnessValueBy(float percent) {
		spriteCostume.changeBrightnessValueBy(percent);
	}

	@Override
	public float getBrightnessValue() {
		return spriteCostume.getBrightnessValue();
	}

	@Override
	public CostumeData getCostumeData() {
		return spriteCostume.getCostumeData();
	}

	@Override
	public float getImageHeight() {
		return spriteCostume.getImageHeight();
	}

	@Override
	public float getImageWidth() {
		return spriteCostume.getImageWidth();
	}

	@Override
	public float getImageX() {
		return spriteCostume.getImageX();
	}

	@Override
	public float getImageY() {
		return spriteCostume.getImageY();
	}

	@Override
	public float getMinHeight() {
		return spriteCostume.getMinHeight();
	}

	@Override
	public float getMinWidth() {
		return spriteCostume.getMinWidth();
	}

	@Override
	public NinePatch getPatch() {
		return spriteCostume.getPatch();
	}

	@Override
	public float getPrefHeight() {
		if (!constructorFinished) {
			return super.getPrefHeight();
		}

		return spriteCostume.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		if (!constructorFinished) {
			return super.getPrefWidth();
		}

		return spriteCostume.getPrefWidth();
	}

	@Override
	public TextureRegion getRegion() {
		return spriteCostume.getRegion();
	}

	@Override
	public void layout() {
		spriteCostume.layout();
	}

	@Override
	public void setAlign(int align) {
		spriteCostume.setAlign(align);
	}

	@Override
	public void setClickListener(ClickListener clickListener) {
		spriteCostume.setClickListener(clickListener);
	}

	@Override
	public void setPatch(NinePatch patch) {
		spriteCostume.setPatch(patch);
	}

	@Override
	public void setRegion(TextureRegion region) {
		if (!constructorFinished) {
			super.setRegion(region);
		} else {
			spriteCostume.setRegion(region);
		}
	}

	@Override
	public void setScaling(Scaling scaling) {
		spriteCostume.setScaling(scaling);
	}

	@Override
	public float getMaxHeight() {
		return spriteCostume.getMaxHeight();
	}

	@Override
	public float getMaxWidth() {
		return spriteCostume.getMaxWidth();
	}

	@Override
	public Actor hit(float x, float y) {
		return spriteCostume.hit(x, y);
	}

	@Override
	public void invalidate() {
		spriteCostume.invalidate();
	}

	@Override
	public void invalidateHierarchy() {
		spriteCostume.invalidateHierarchy();
	}

	@Override
	public boolean needsLayout() {
		return spriteCostume.needsLayout();
	}

	@Override
	public void pack() {
		spriteCostume.pack();
	}

	@Override
	public void setFillParent(boolean fillParent) {
		spriteCostume.setFillParent(fillParent);
	}

	@Override
	public void validate() {
		spriteCostume.validate();
	}

	@Override
	public void act(float delta) {
		spriteCostume.act(delta);
	}

	@Override
	public void action(Action action) {
		spriteCostume.action(action);
	}

	@Override
	public void clearActions() {
		spriteCostume.clearActions();
	}

	@Override
	public Stage getStage() {
		return spriteCostume.getStage();
	}

	@Override
	public boolean isMarkedToRemove() {
		return spriteCostume.isMarkedToRemove();
	}

	@Override
	public boolean keyDown(int keycode) {
		return spriteCostume.keyDown(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return spriteCostume.keyTyped(character);
	}

	@Override
	public boolean keyUp(int keycode) {
		return spriteCostume.keyUp(keycode);
	}

	@Override
	public void markToRemove(boolean remove) {
		spriteCostume.markToRemove(remove);
	}

	@Override
	public void remove() {
		spriteCostume.remove();
	}

	@Override
	public boolean scrolled(int amount) {
		return spriteCostume.scrolled(amount);
	}

	@Override
	public void toLocalCoordinates(Vector2 point) {
		spriteCostume.toLocalCoordinates(point);
	}

	@Override
	public String toString() {
		return spriteCostume.toString();
	}

	@Override
	public boolean touchMoved(float x, float y) {
		return spriteCostume.touchMoved(x, y);
	}

}
