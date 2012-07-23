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
import at.tugraz.ist.catroid.common.CostumeData;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.stage.NativeAppActivity;
import at.tugraz.ist.catroid.ui.CostumeActivity;
import at.tugraz.ist.catroid.ui.ScriptTabActivity;

public class SetCostumeBrick implements Brick {
	private static final long serialVersionUID = 1L;
	public static String loadedImageNameViaNew;
	private Sprite sprite;
	private CostumeData costumeData;
	// Spinner loses his selection after tabchange....
	private CostumeData oldSelectedCostume;
	private transient View view;

	//We need this because of the wrong behavior of OnItemSelected....
	 
	public static boolean startUpFalseBehavoirCheck = false;

	public SetCostumeBrick(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	public void setCostume(CostumeData costumeData) {
		this.costumeData = costumeData;
	}

	public void execute() {
		if (costumeData != null && sprite != null && sprite.getCostumeDataList().contains(costumeData)) {
			if (!NativeAppActivity.isRunning()) {
				sprite.costume.setCostumeData(costumeData);
			} else {
				sprite.costume.setCostumeDataInternal(costumeData);
			}
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public String getImagePath() {
		return costumeData.getAbsolutePath();
	}

	public View getView(final Context context, int brickId, BaseAdapter adapter) {

		view = View.inflate(context, R.layout.brick_set_costume, null);

		Spinner costumebrickSpinner = (Spinner) view.findViewById(R.id.setcostume_spinner);
		costumebrickSpinner.setAdapter(createCostumeAdapter(context));
		costumebrickSpinner.setClickable(true);
		costumebrickSpinner.setFocusable(true);

		costumebrickSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (startUpFalseBehavoirCheck == false) {
					startUpFalseBehavoirCheck = true;
				} else {

					if (position == 0) {
						costumeData = null;

					} else {

						costumeData = (CostumeData) parent.getItemAtPosition(position);

						if (costumeData.getCostumeName().equals(context.getString(R.string.brick_set_costume_new))) {
							costumeData = null;

							/**
							 * This may be bad? Since I had to change ScriptTabAcitvity.tabHost to static ;)
							 * Flags are selfexplaining...
							 */
							if (ScriptTabActivity.tabHost != null) {
								CostumeActivity.STARTED_FROM_SCRIPTACTIVITY = 1;
								CostumeActivity.GO_BACK_TO_SCRIPTACTIVITY = 1;
								ScriptTabActivity.tabHost.setCurrentTab(1);
							}

						}
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		if (sprite.getCostumeDataList().contains(costumeData)) {
			costumebrickSpinner.setSelection(sprite.getCostumeDataList().indexOf(costumeData) + 1, true);
			oldSelectedCostume = costumeData;
		} else if (loadedImageNameViaNew != null) {
			oldSelectedCostume = sprite.getCostumeDataList().get(sprite.getCostumeDataList().size() - 1);
			costumebrickSpinner.setSelection(sprite.getCostumeDataList().size(), true);
			loadedImageNameViaNew = null;
		} else if (oldSelectedCostume != null) {
			costumebrickSpinner.setSelection(sprite.getCostumeDataList().indexOf(oldSelectedCostume) + 1);
		} else {
			costumebrickSpinner.setSelection(0);
		}

		if (sprite.getName().equals(context.getString(R.string.background))) {
			TextView textView = (TextView) view.findViewById(R.id.tv_set_costume);
			textView.setText(R.string.brick_set_background);
		}

		return view;
	}

	private ArrayAdapter<?> createCostumeAdapter(Context context) {
		ArrayAdapter<CostumeData> arrayAdapter = new ArrayAdapter<CostumeData>(context,
				android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CostumeData dummyCostumeData = new CostumeData();
		dummyCostumeData.setCostumeName(context.getString(R.string.brick_set_costume_none));
		arrayAdapter.add(dummyCostumeData);

		for (CostumeData costumeData : sprite.getCostumeDataList()) {
			arrayAdapter.add(costumeData);
		}

		dummyCostumeData = new CostumeData();
		dummyCostumeData.setCostumeName(context.getString(R.string.brick_set_costume_new));
		arrayAdapter.add(dummyCostumeData);

		return arrayAdapter;
	}

	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_set_costume, null);
		if (sprite.getName().equals(context.getString(R.string.background))) {
			TextView textView = (TextView) prototypeView.findViewById(R.id.tv_set_costume);
			textView.setText(R.string.brick_set_background);
		}
		return prototypeView;
	}

	@Override
	public Brick clone() {
		SetCostumeBrick clonedBrick = new SetCostumeBrick(getSprite());
		if (sprite.costume != null) {
			clonedBrick.setCostume(null);
		}

		return clonedBrick;

	}
}
