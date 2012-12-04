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
package org.catrobat.catroid.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.MainMenuActivity.MainMenuItem;
import org.catrobat.catroid.ui.adapter.MainMenuAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class MainMenuFragment extends SherlockFragment {

	private ListView mainMenuListView;

	private MainMenuItemClickListener mainMenuItemClickListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_menu, null);
		mainMenuListView = (ListView) rootView.findViewById(R.id.lv_main_menu);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initMainMenu();
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			mainMenuItemClickListener = (MainMenuItemClickListener) activity;
		} catch (ClassCastException ex) {
			throw new IllegalStateException(activity.getClass().getSimpleName()
					+ " does not implement MainMenuItemClick contract interface.", ex);
		}
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mainMenuItemClickListener = null;
	}

	private void initMainMenu() {
		MainMenuAdapter adapter = new MainMenuAdapter(getActivity(), generateMainMenuItems(), mainMenuItemClickListener);
		mainMenuListView.setAdapter(adapter);
	}

	private List<SlidingMenuItem> generateMainMenuItems() {
		List<SlidingMenuItem> mainMenuItems = new ArrayList<SlidingMenuItem>();

		SlidingMenuItem newProject = new SlidingMenuItem(R.drawable.main_menu_new, R.string.main_menu_new,
				MainMenuItem.NEW);
		SlidingMenuItem continueProject = new SlidingMenuItem(R.drawable.main_menu_continue,
				R.string.main_menu_continue, MainMenuItem.CONTINUE);
		SlidingMenuItem programs = new SlidingMenuItem(R.drawable.main_menu_programs, R.string.main_menu_programs,
				MainMenuItem.PROGRAMS);
		SlidingMenuItem forum = new SlidingMenuItem(R.drawable.main_menu_forum, R.string.main_menu_forum,
				MainMenuItem.FORUM);
		SlidingMenuItem web = new SlidingMenuItem(R.drawable.main_menu_web, R.string.main_menu_web, MainMenuItem.WEB);
		SlidingMenuItem upload = new SlidingMenuItem(R.drawable.main_menu_upload, R.string.main_menu_upload,
				MainMenuItem.UPLOAD);
		SlidingMenuItem about = new SlidingMenuItem(0, R.string.main_menu_about_catroid, MainMenuItem.ABOUT);

		if (ProjectManager.getInstance().getCurrentProject() == null) {
			continueProject.isEnabled = false;
		}

		mainMenuItems.add(newProject);
		mainMenuItems.add(continueProject);
		mainMenuItems.add(programs);
		mainMenuItems.add(forum);
		mainMenuItems.add(web);
		mainMenuItems.add(upload);
		mainMenuItems.add(about);

		return mainMenuItems;
	}

	public class SlidingMenuItem {
		public int iconResId;
		public int titleResId;
		public MainMenuItem mainMenuItem;
		public boolean isEnabled = true;

		public SlidingMenuItem(int iconResId, int titleresId, MainMenuItem mainMenuItem) {
			this.iconResId = iconResId;
			this.titleResId = titleresId;
			this.mainMenuItem = mainMenuItem;
		}
	}

	public interface MainMenuItemClickListener {

		public void onMainMenuItemClick(MainMenuItem item);

	}
}
