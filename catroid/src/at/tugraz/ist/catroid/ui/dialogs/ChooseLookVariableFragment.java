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
package at.tugraz.ist.catroid.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.formulaeditor.CatKeyEvent;
import at.tugraz.ist.catroid.formulaeditor.CatKeyboardView;

public class ChooseLookVariableFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private CatKeyboardView catKeyboardView;
	private final Integer[] lookResourceIds = { R.string.formula_editor_look_x, R.string.formula_editor_look_y,
			R.string.formula_editor_look_ghosteffect, R.string.formula_editor_look_brightness,
			R.string.formula_editor_look_size, R.string.formula_editor_look_rotation,
			R.string.formula_editor_look_layer };
	private static final int CANCEL_INDEX = -2;

	@Override
	public void onClick(DialogInterface dialog, int index) {
		if (index == CANCEL_INDEX) {
			this.dismiss();
			return;
		}
		Log.v("touched: ", "" + index);
		Log.v("touched: ", lookResourceIds[index].toString());

		int[] keyCode = new int[1];
		keyCode[0] = 0;

		catKeyboardView.onKey(CatKeyEvent.KEYCODE_LOOK_X + index, keyCode);

	}

	public static ChooseLookVariableFragment newInstance(int title) {
		ChooseLookVariableFragment fragment = new ChooseLookVariableFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setCancelable(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		String[] lookNames = new String[lookResourceIds.length];
		int index = 0;
		for (Integer lookResourceID : lookResourceIds) {
			lookNames[index] = getString(lookResourceID);
			index++;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.formula_editor_choose_look_variable));
		builder.setNegativeButton(getString(R.string.cancel_button), this);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, lookNames);

		builder.setAdapter(arrayAdapter, this);

		return builder.create();

	}

	public void setCatKeyboardView(CatKeyboardView catKeyboardView) {
		this.catKeyboardView = catKeyboardView;

	}

}
