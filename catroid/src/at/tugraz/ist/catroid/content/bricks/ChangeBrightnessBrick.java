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
package at.tugraz.ist.catroid.content.bricks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.ui.dialogs.EditDoubleDialog;

public class ChangeBrightnessBrick implements Brick, OnDismissListener {
	private static final long serialVersionUID = 1L;
	private double changeBrightness;
	private Sprite sprite;

	public ChangeBrightnessBrick(Sprite sprite, double changeBrightness) {
		this.sprite = sprite;
		this.changeBrightness = changeBrightness;
	}

	public void execute() {
		double brightnessValue = sprite.getBrightnessValue();
		brightnessValue += changeBrightness;
		sprite.setBrightnessValue(brightnessValue);
	}

	public Sprite getSprite() {
		return this.sprite;
	}

	public double getChangeBrightness() {
		return changeBrightness;
	}

	public View getView(Context context, int brickId, BaseExpandableListAdapter adapter) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View brickView = inflater.inflate(R.layout.construction_brick_change_brightness, null);

		EditText editX = (EditText) brickView.findViewById(R.id.construction_brick_change_brightness_edit_text);
		editX.setText(String.valueOf(changeBrightness));

		EditDoubleDialog dialogX = new EditDoubleDialog(context, editX, changeBrightness, true);
		dialogX.setOnDismissListener(this);
		dialogX.setOnCancelListener((OnCancelListener) context);

		editX.setOnClickListener(dialogX);

		return brickView;
	}

	public View getPrototypeView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View brickView = inflater.inflate(R.layout.toolbox_brick_change_brightness, null);
		return brickView;
	}

	@Override
	public Brick clone() {
		return new ChangeBrightnessBrick(getSprite(), getChangeBrightness());
	}

	public void onDismiss(DialogInterface dialog) {
		EditDoubleDialog inputDialog = (EditDoubleDialog) dialog;
		changeBrightness = inputDialog.getValue();
		dialog.cancel();
	}
}