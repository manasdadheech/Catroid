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
/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package at.tugraz.ist.catroid.formulaeditor;

import java.util.Locale;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.ui.dialogs.ChooseLookVariableFragment;
import at.tugraz.ist.catroid.ui.dialogs.FormulaEditorChooseOperatorDialog;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class CatKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

	static final int NUMBER_KEYBOARD = 1;
	static final int FUNCTION_KEYBOARD = 0;
	static final int SENSOR_KEYBOARD = 2;

	private FormulaEditorEditText editText;

	private Keyboard symbolsNumbers;
	private Keyboard symbolsFunctions;
	private Keyboard symbolsSensors;
	private Context context;
	private ChooseLookVariableFragment chooseLookVariablesFragment;
	private FormulaEditorChooseOperatorDialog chooseOperatorDialogFragment;

	public CatKeyboardView(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.context = context;
		setOnKeyboardActionListener(this);
		this.editText = null;
		this.symbolsNumbers = null;

		if (Locale.getDefault().getDisplayLanguage().contentEquals(Locale.GERMAN.getDisplayLanguage())) {
			this.symbolsNumbers = new Keyboard(this.getContext(), R.xml.symbols_de_numbers);
			this.symbolsFunctions = new Keyboard(this.getContext(), R.xml.symbols_de_functions);
			this.symbolsSensors = new Keyboard(this.getContext(), R.xml.symbols_de_sensors);
		} else {//if (Locale.getDefault().getDisplayLanguage().contentEquals(Locale.ENGLISH.getDisplayLanguage())) {
			this.symbolsNumbers = new Keyboard(this.getContext(), R.xml.symbols_eng_numbers);
			this.symbolsFunctions = new Keyboard(this.getContext(), R.xml.symbols_eng_functions);
			this.symbolsSensors = new Keyboard(this.getContext(), R.xml.symbols_eng_sensors);

		}

		this.setKeyboard(symbolsNumbers);

		if (((SherlockFragmentActivity) context).getSupportFragmentManager().findFragmentByTag(
				"chooseLookVariablesDialogFragment") == null) {
			this.chooseLookVariablesFragment = ChooseLookVariableFragment
					.newInstance(android.R.string.dialog_alert_title);

		} else {
			this.chooseLookVariablesFragment = (ChooseLookVariableFragment) ((SherlockFragmentActivity) context)
					.getSupportFragmentManager().findFragmentByTag("chooseLookVariablesDialogFragment");
		}
		this.chooseLookVariablesFragment.setCatKeyboardView(this);

		//		Fragment fragment = ((SherlockFragmentActivity) context).getSupportFragmentManager().findFragmentByTag(
		//				LookFragment.LOOK_FRAGMENT_TAG);
		//		if (fragment == null) {
		//			this.lookFragment = LookFragment.newInstance();
		//
		//		} else {
		//			this.lookFragment = (LookFragment) fragment;
		//		}
		//		this.lookFragment.setCatKeyboardView(this);

		if (((SherlockFragmentActivity) context).getSupportFragmentManager().findFragmentByTag(
				"chooseOperatorDialogFragment") == null) {
			this.chooseOperatorDialogFragment = FormulaEditorChooseOperatorDialog
					.newInstance(android.R.string.dialog_alert_title);

		} else {
			this.chooseOperatorDialogFragment = (FormulaEditorChooseOperatorDialog) ((SherlockFragmentActivity) context)
					.getSupportFragmentManager().findFragmentByTag("chooseOperatorDialogFragment");
		}
		this.chooseOperatorDialogFragment.setCatKeyboardView(this);

		//		LayoutParams relative = new LayoutParams(source);
		//		this.symbols.setShifted(false);
		//		this.symbols_shifted.setShifted(true);
		//		this.setBackgroundColor(0xFF6103);
		//		this.awakenScrollBars();
		//
		//		ArrayList<Key> keys = (ArrayList<Key>) this.symbols.getKeys();
		//
		//				for (int i = 0; i < keys.size(); i++) {
		//					Key key = keys.get(i);
		//					key.iconPreview = key.icon;
		//					key.popupCharacters = key.label;
		//				}

		//    public CatKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		//        super(context, attrs, defStyle);

		Log.i("info", "CatKeyboardView()-Constructor");
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {

		CatKeyEvent catKeyEvent = null;

		switch (primaryCode) {
			case KeyEvent.KEYCODE_SHIFT_RIGHT:
				this.goRight();
				break;
			case KeyEvent.KEYCODE_SHIFT_LEFT:
				this.goLeft();
				break;
			case CatKeyEvent.KEYCODE_LOOK_BUTTON:
				this.chooseLookVariablesFragment.show(((SherlockFragmentActivity) context).getSupportFragmentManager(),
						"chooseLookVariablesDialogFragment");
				break;
			//			case CatKeyEvent.KEYCODE_LOOK_BUTTON:
			//				this.lookFragment.showFragment(context);
			//				break;
			case KeyEvent.KEYCODE_MENU:
				this.chooseOperatorDialogFragment.show(
						((SherlockFragmentActivity) context).getSupportFragmentManager(),
						"chooseOperatorDialogFragment");

				break;
			default:
				catKeyEvent = new CatKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, primaryCode));
				editText.handleKeyEvent(catKeyEvent);
				break;
		}

	}

	public void goLeft() {
		//Log.i("info", "swipeRight()");

		if (this.getKeyboard() == this.symbolsNumbers) {
			this.setKeyboard(this.symbolsSensors);
			return;
		}
		if (this.getKeyboard() == this.symbolsFunctions) {
			this.setKeyboard(this.symbolsNumbers);
			return;
		}
		if (this.getKeyboard() == this.symbolsSensors) {
			this.setKeyboard(this.symbolsFunctions);
			return;
		}

	}

	public void goRight() {

		if (this.getKeyboard() == this.symbolsNumbers) {
			this.setKeyboard(this.symbolsFunctions);
			return;
		}
		if (this.getKeyboard() == this.symbolsFunctions) {
			this.setKeyboard(this.symbolsSensors);
			return;
		}
		if (this.getKeyboard() == this.symbolsSensors) {
			this.setKeyboard(this.symbolsNumbers);
			return;
		}
	}

	@Override
	public void swipeLeft() {
	}

	@Override
	public void swipeRight() {
	}

	@Override
	public void swipeUp() {
	}

	@Override
	public void swipeDown() {
	}

	@Override
	public void onPress(int primaryCode) {
		//		Log.i("info", "CatKeybaordView.onPress(): " + primaryCode);

	}

	@Override
	public void onRelease(int primaryCode) {
		//		Log.i("info", "CatKeybaordView.onRelease(): " + primaryCode);

	}

	@Override
	public void onText(CharSequence text) {
		//		Log.i("info", "CatKeybaordView.onText(): ");

	}

	public void setFormulaEditText(FormulaEditorEditText formulaEditorEditText) {
		editText = formulaEditorEditText;
	}
}
