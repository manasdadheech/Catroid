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
package org.catrobat.catroid.ui.adapter;

import java.util.List;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.fragment.MainMenuFragment.MainMenuItemClickListener;
import org.catrobat.catroid.ui.fragment.MainMenuFragment.SlidingMenuItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends ArrayAdapter<SlidingMenuItem> {

	private List<SlidingMenuItem> items;
	private MainMenuItemClickListener mainMenuItemClickListener;

	public MainMenuAdapter(Context context, List<SlidingMenuItem> items, MainMenuItemClickListener listener) {
		super(context, 0, items);
		this.items = items;
		mainMenuItemClickListener = listener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SlidingMenuItem item = items.get(position);
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_item_main_menu, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_main_menu_item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.tv_main_menu_item_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.icon.setImageResource(item.iconResId);
		holder.title.setText(item.titleResId);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mainMenuItemClickListener != null) {
					mainMenuItemClickListener.onMainMenuItemClick(item.mainMenuItem);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {

		public ImageView icon;
		public TextView title;

	}
}
