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
package at.tugraz.ist.catroid.content;

import at.tugraz.ist.catroid.content.bricks.ScriptBrick;
import at.tugraz.ist.catroid.content.bricks.WhenKeyBrick;
import at.tugraz.ist.catroid.content.bricks.WhenKeyBrick.Key;

public class WhenKeyScript extends Script {

	private static final long serialVersionUID = 1L;
	private int keyCode;

	public WhenKeyScript() {
		keyCode = -1;
	}

	public WhenKeyScript(Sprite sprite) {
		super(sprite);
		super.isFinished = true;
		this.keyCode = -1;
	}

	public WhenKeyScript(Sprite sprite, int keyCode) {
		super(sprite);
		super.isFinished = true;
		this.keyCode = keyCode;
	}

	public WhenKeyScript(Sprite sprite, WhenKeyBrick brick, int keyCode) {
		this(sprite, keyCode);
		this.brick = brick;
		this.keyCode = keyCode;
	}

	@Override
	protected Object readResolve() {
		isFinished = true;
		super.readResolve();
		return this;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	@Override
	public ScriptBrick getScriptBrick() {
		if (brick == null) {
			brick = new WhenKeyBrick(sprite, this, Key.getKeyByKeyCode(keyCode));
		}
		return brick;
	}
}
