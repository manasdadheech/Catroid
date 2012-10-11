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
package at.tugraz.ist.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.content.Script;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.WhenKeyScript;

public class WhenKeyBrick extends ScriptBrick {

	private static final long serialVersionUID = 1L;
	private WhenKeyScript whenKeyScript;
	private Sprite sprite;

	private transient View view;

	public static enum Key {
		KEY_PADUP(22), KEY_PADDOWN(21), KEY_PADLEFT(19), KEY_PADRIGHT(20), KEY_X(23), KEY_CIRCLE(255), KEY_SQUARE(99), KEY_TRIANGLE(
				100), KEY_LEFT(102), KEY_RIGHT(103), KEY_START(108), KEY_SELECT(109), KEY_MENU(82);

		private int keyCode;

		private Key(int keyCode) {
			this.keyCode = keyCode;
		}

		public int getKeyCode() {
			return this.keyCode;
		}

		public static Key getKeyByKeyCode(int keyCode) {
			for (int i = 0; i < Key.values().length; i++) {
				Key enumKey = Key.values()[i];
				if (enumKey.getKeyCode() == keyCode) {
					return enumKey;
				}
			}
			return null;
		}

		public static int getKeyPositionByKey(Key key) {
			for (int i = 0; i < Key.values().length; i++) {
				Key enumKey = Key.values()[i];
				if (enumKey.getKeyCode() == key.getKeyCode()) {
					return i;
				}
			}
			return 0;
		}
	}

	private transient Key key;

	public WhenKeyBrick() {
		if (this.key == null) {
			this.key = Key.KEY_PADUP;
		}
	}

	public WhenKeyBrick(Sprite sprite, WhenKeyScript whenKeyScript, Key key) {
		this.sprite = sprite;
		this.whenKeyScript = whenKeyScript;
		this.key = key;

		if (this.key == null) {
			this.key = Key.KEY_PADUP;
		}

		if (this.whenKeyScript == null) {
			this.whenKeyScript = new WhenKeyScript(this.sprite, this.key.getKeyCode());
		} else {
			this.whenKeyScript.setKeyCode(this.key.getKeyCode());
		}
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public void execute() {
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter adapter) {
		if (whenKeyScript == null) {
			whenKeyScript = new WhenKeyScript(sprite, key.getKeyCode());
		}

		view = View.inflate(context, R.layout.brick_xperia_play_key_down, null);
		TextView viewText = (TextView) view.findViewById(R.id.WhenBrickActionKeyPressed);
		viewText.setText(" " + viewText.getText());

		final Spinner keySpinner = (Spinner) view.findViewById(R.id.xperia_play_key_spinner);

		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context,
				R.array.xperia_play_key_chooser, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		keySpinner.setAdapter(arrayAdapter);
		keySpinner.setClickable(true);
		keySpinner.setFocusable(true);

		keySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				key = Key.values()[position];
				if (whenKeyScript == null) {
					whenKeyScript = new WhenKeyScript(sprite, key.getKeyCode());
				} else {
					whenKeyScript.setKeyCode(key.getKeyCode());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		//keySpinner.setSelection(key.ordinal());
		keySpinner.setSelection(Key.getKeyPositionByKey(key));
		keySpinner.setFocusable(false);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_xperia_play_key_down, null);
		TextView viewText = (TextView) prototypeView.findViewById(R.id.WhenBrickActionKeyPressed);
		viewText.setText(" " + viewText.getText());
		return prototypeView;
		//return View.inflate(context, R.layout.brick_xperia_play_key_down, null);
	}

	@Override
	public Brick clone() {
		return new WhenKeyBrick(sprite, whenKeyScript, key);
	}

	@Override
	public Script initScript(Sprite sprite) {
		if (whenKeyScript == null) {
			whenKeyScript = new WhenKeyScript(sprite, key.getKeyCode());
		}

		return whenKeyScript;
	}

}
