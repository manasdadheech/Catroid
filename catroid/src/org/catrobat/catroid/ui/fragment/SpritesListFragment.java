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
import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.ScriptTabActivity;
import org.catrobat.catroid.ui.adapter.SpriteAdapter;
import org.catrobat.catroid.ui.dialogs.RenameSpriteDialog;
import org.catrobat.catroid.utils.ErrorListenerInterface;
import org.catrobat.catroid.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class SpritesListFragment extends SherlockListFragment {

	private static final String BUNDLE_ARGUMENTS_SPRITE_TO_EDIT = "sprite_to_edit";

	private SpriteAdapter spriteAdapter;
	private ArrayList<Sprite> spriteList;
	private Sprite spriteToEdit;

	private SpriteRenamedReceiver spriteRenamedReceiver;
	private SpritesListChangedReceiver spritesListChangedReceiver;
	private SpritesListInitReceiver spritesListInitReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sprites_list, null);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerForContextMenu(getListView());
		if (savedInstanceState != null) {
			spriteToEdit = (Sprite) savedInstanceState.get(BUNDLE_ARGUMENTS_SPRITE_TO_EDIT);
		}

		try {
			Utils.loadProjectIfNeeded(getActivity(), (ErrorListenerInterface) getActivity());
		} catch (ClassCastException exception) {
			Log.e("CATROID", getActivity().toString() + " does not implement ErrorListenerInterface", exception);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(BUNDLE_ARGUMENTS_SPRITE_TO_EDIT, spriteToEdit);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
		initListeners();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!Utils.checkForSdCard(getActivity())) {
			return;
		}

		StorageHandler.getInstance().fillChecksumContainer();

		if (spriteRenamedReceiver == null) {
			spriteRenamedReceiver = new SpriteRenamedReceiver();
		}

		if (spritesListChangedReceiver == null) {
			spritesListChangedReceiver = new SpritesListChangedReceiver();
		}

		if (spritesListInitReceiver == null) {
			spritesListInitReceiver = new SpritesListInitReceiver();
		}

		IntentFilter intentFilterSpriteRenamed = new IntentFilter(ScriptTabActivity.ACTION_SPRITE_RENAMED);
		getActivity().registerReceiver(spriteRenamedReceiver, intentFilterSpriteRenamed);

		IntentFilter intentFilterSpriteListChanged = new IntentFilter(ScriptTabActivity.ACTION_SPRITES_LIST_CHANGED);
		getActivity().registerReceiver(spritesListChangedReceiver, intentFilterSpriteListChanged);

		IntentFilter intentFilterSpriteListInit = new IntentFilter(ScriptTabActivity.ACTION_SPRITES_LIST_INIT);
		getActivity().registerReceiver(spritesListInitReceiver, intentFilterSpriteListInit);

		spriteAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		ProjectManager projectManager = ProjectManager.getInstance();
		if (projectManager.getCurrentProject() != null) {
			projectManager.saveProject();
		}

		if (spriteRenamedReceiver != null) {
			getActivity().unregisterReceiver(spriteRenamedReceiver);
		}

		if (spritesListChangedReceiver != null) {
			getActivity().unregisterReceiver(spritesListChangedReceiver);
		}

		if (spritesListInitReceiver != null) {
			getActivity().unregisterReceiver(spritesListInitReceiver);
		}
	}

	public Sprite getSpriteToEdit() {
		return spriteToEdit;
	}

	public void handleProjectActivityItemLongClick(View view) {
	}

	private void initListeners() {
		spriteList = (ArrayList<Sprite>) ProjectManager.getInstance().getCurrentProject().getSpriteList();
		spriteAdapter = new SpriteAdapter(getActivity(), R.layout.activity_project_spritelist_item, R.id.sprite_title,
				spriteList);

		setListAdapter(spriteAdapter);
		getListView().setTextFilterEnabled(true);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);

		getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProjectManager.getInstance().setCurrentSprite(spriteAdapter.getItem(position));
				Intent intent = new Intent(getActivity(), ScriptTabActivity.class);
				startActivity(intent);
			}
		});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				spriteToEdit = spriteList.get(position);

				// as long as background sprite is always the first one, we're fine
				if (ProjectManager.getInstance().getCurrentProject().getSpriteList().indexOf(spriteToEdit) == 0) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	private void deleteSpriteFiles() {
		List<CostumeData> costumeDataList = spriteToEdit.getCostumeDataList();
		List<SoundInfo> soundInfoList = spriteToEdit.getSoundList();

		for (CostumeData currentCostumeData : costumeDataList) {
			StorageHandler.getInstance().deleteFile(currentCostumeData.getAbsolutePath());
		}

		for (SoundInfo currentSoundInfo : soundInfoList) {
			StorageHandler.getInstance().deleteFile(currentSoundInfo.getAbsolutePath());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Adapter adapter = getListAdapter();

		spriteToEdit = (Sprite) adapter.getItem(info.position);

		if (ProjectManager.getInstance().getCurrentProject().getSpriteList().indexOf(spriteToEdit) == 0) {
			return;
		}
		getSherlockActivity().getMenuInflater().inflate(R.menu.context_menu_current_project, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.copy:
				break;

			case R.id.cut:
				break;

			case R.id.insert_below:
				break;

			case R.id.move:
				break;

			case R.id.rename:
				showRenameDialog();
				break;

			case R.id.delete:
				deleteSprite();
				break;

		}
		return super.onContextItemSelected(item);
	}

	public void showRenameDialog() {
		RenameSpriteDialog dialog = RenameSpriteDialog.newInstance(spriteToEdit.getName());
		dialog.show(getFragmentManager(), RenameSpriteDialog.DIALOG_FRAGMENT_TAG);
	}

	public void deleteSprite() {
		ProjectManager projectManager = ProjectManager.getInstance();
		projectManager.getCurrentProject().getSpriteList().remove(spriteToEdit);
		deleteSpriteFiles();
		if (projectManager.getCurrentSprite() != null && projectManager.getCurrentSprite().equals(spriteToEdit)) {
			projectManager.setCurrentSprite(null);
		}
	}

	private class SpriteRenamedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptTabActivity.ACTION_SPRITE_RENAMED)) {
				String newSpriteName = intent.getExtras().getString(RenameSpriteDialog.EXTRA_NEW_SPRITE_NAME);
				spriteToEdit.setName(newSpriteName);
			}
		}
	}

	private class SpritesListChangedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptTabActivity.ACTION_SPRITES_LIST_CHANGED)) {
				spriteAdapter.notifyDataSetChanged();
				final ListView listView = getListView();
				listView.post(new Runnable() {
					@Override
					public void run() {
						listView.setSelection(listView.getCount() - 1);
					}
				});
			}
		}
	}

	private class SpritesListInitReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptTabActivity.ACTION_SPRITES_LIST_INIT)) {
				spriteAdapter.notifyDataSetChanged();
			}
		}
	}
}
