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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.ui.adapter.IconMenuAdapter;
import org.catrobat.catroid.ui.adapter.ProjectAdapter;
import org.catrobat.catroid.ui.dialogs.CopyProjectDialog;
import org.catrobat.catroid.ui.dialogs.CopyProjectDialog.OnCopyProjectListener;
import org.catrobat.catroid.ui.dialogs.CustomIconContextMenu;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog.CurrentProjectChangedListener;
import org.catrobat.catroid.ui.dialogs.RenameProjectDialog;
import org.catrobat.catroid.ui.dialogs.RenameProjectDialog.OnProjectRenameListener;
import org.catrobat.catroid.ui.dialogs.SetDescriptionDialog;
import org.catrobat.catroid.ui.dialogs.SetDescriptionDialog.OnUpdateProjectDescriptionListener;
import org.catrobat.catroid.utils.ErrorListenerInterface;
import org.catrobat.catroid.utils.UtilFile;
import org.catrobat.catroid.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ProjectsListFragment extends SherlockListFragment implements OnProjectRenameListener,
		ErrorListenerInterface, OnUpdateProjectDescriptionListener, OnCopyProjectListener, OnClickListener {

	private static final String BUNDLE_ARGUMENTS_PROJECT_DATA = "project_data";

	public static final String FRAGMENT_TAG = "fragment_projects_list";

	private List<ProjectData> projectList;
	private ProjectData projectToEdit;
	private ProjectAdapter adapter;
	private ProjectsListFragment parentFragment = this;

	private int activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;

	private View viewBelowMyProjectlistNonScrollable;
	private View myprojectlistFooterView;

	private static final int NO_DIALOG_FRAGMENT_ACTIVE = -1;
	private static final int CONTEXT_MENU_ITEM_RENAME = 0;
	private static final int CONTEXT_MENU_ITEM_DESCRIPTION = 1;
	private static final int CONTEXT_MENU_ITEM_DELETE = 2;
	private static final int FOOTER_ADD_PROJECT_ALPHA_VALUE = 35;
	private static final int CONTEXT_MENU_ITEM_COPY = 3;

	private CurrentProjectChangedListener currentProjectChangedListener;

	public ProjectsListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_projects_list, null);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			projectToEdit = (ProjectData) savedInstanceState.getSerializable(BUNDLE_ARGUMENTS_PROJECT_DATA);
		}

		viewBelowMyProjectlistNonScrollable = getActivity().findViewById(R.id.view_below_myprojectlist_non_scrollable);
		viewBelowMyProjectlistNonScrollable.setOnClickListener(this);

		View footerView = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_projects_footer,
				getListView(), false);
		myprojectlistFooterView = footerView.findViewById(R.id.myprojectlist_footerview);
		ImageView footerAddImage = (ImageView) footerView.findViewById(R.id.myprojectlist_footerview_add_image);
		footerAddImage.setAlpha(FOOTER_ADD_PROJECT_ALPHA_VALUE);
		myprojectlistFooterView.setOnClickListener(this);
		getListView().addFooterView(footerView);

		checkForCanceledFragment();
		reattachDialogFragmentListener();
		initAdapter();
		initClickListener();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(BUNDLE_ARGUMENTS_PROJECT_DATA, projectToEdit);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			currentProjectChangedListener = (CurrentProjectChangedListener) activity;
		} catch (ClassCastException ex) {
			throw new IllegalStateException(activity.getClass().getSimpleName()
					+ " does not implement CurrentProjectChanged interface.", ex);
		}

		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		currentProjectChangedListener = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateProjectTitle();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_myprojects, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.menu_add: {
				NewProjectDialog dialog = new NewProjectDialog();
				dialog.show(getFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
				return true;
			}
			case R.id.settings: {
				Intent intent = new Intent(getActivity(), SettingsActivity.class);
				startActivity(intent);
				return true;
			}
			default: {
				return super.onOptionsItemSelected(item);
			}
		}
	}

	@Override
	public void onProjectRename(boolean isCurrentProject) {
		if (isCurrentProject) {
			updateProjectTitle();
		}
		activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
		initAdapter();
	}

	@Override
	public void onCopyProject(boolean orientationChangedWhileCopying) {
		if (!orientationChangedWhileCopying) {
			activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
			initAdapter();
		}
	}

	@Override
	public void onUpdateProjectDescription() {
		activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
		initAdapter();
	}

	@Override
	public void onClick(View v) {
		NewProjectDialog dialog = null;
		switch (v.getId()) {
			case R.id.view_below_myprojectlist_non_scrollable:
				dialog = new NewProjectDialog();
				dialog.show(getActivity().getSupportFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
				break;
			case R.id.myprojectlist_footerview:
				dialog = new NewProjectDialog();
				dialog.show(getActivity().getSupportFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
				break;
		}
	}

	@Override
	public void showErrorDialog(String errorMessage) {
		Utils.displayErrorMessageFragment(getFragmentManager(), errorMessage);
	}

	private void reattachDialogFragmentListener() {
		Fragment activeFragmentDialog;
		if (activeDialogId != NO_DIALOG_FRAGMENT_ACTIVE) {
			switch (activeDialogId) {
				case CONTEXT_MENU_ITEM_RENAME:
					activeFragmentDialog = getFragmentManager().findFragmentByTag(
							RenameProjectDialog.DIALOG_FRAGMENT_TAG);
					RenameProjectDialog displayingRenameProjectDialog = (RenameProjectDialog) activeFragmentDialog;
					displayingRenameProjectDialog.setOnProjectRenameListener(ProjectsListFragment.this);
					break;
				case CONTEXT_MENU_ITEM_DESCRIPTION:
					activeFragmentDialog = getFragmentManager().findFragmentByTag(
							SetDescriptionDialog.DIALOG_FRAGMENT_TAG);
					SetDescriptionDialog displayingSetDescriptionProjectDialog = (SetDescriptionDialog) activeFragmentDialog;
					displayingSetDescriptionProjectDialog
							.setOnUpdateProjectDescriptionListener(ProjectsListFragment.this);
					break;
				case CONTEXT_MENU_ITEM_COPY:
					activeFragmentDialog = getFragmentManager()
							.findFragmentByTag(CopyProjectDialog.DIALOG_FRAGMENT_TAG);
					CopyProjectDialog displayingCopyProjectDialog = (CopyProjectDialog) activeFragmentDialog;
					displayingCopyProjectDialog.setParentFragment(this);
					break;
			}
		}
	}

	private void checkForCanceledFragment() {
		if (getFragmentManager().findFragmentByTag(RenameProjectDialog.DIALOG_FRAGMENT_TAG) == null
				&& activeDialogId == CONTEXT_MENU_ITEM_RENAME) {
			activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
		} else if (getFragmentManager().findFragmentByTag(SetDescriptionDialog.DIALOG_FRAGMENT_TAG) == null
				&& activeDialogId == CONTEXT_MENU_ITEM_DESCRIPTION) {
			activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
		} else if (getFragmentManager().findFragmentByTag(CopyProjectDialog.DIALOG_FRAGMENT_TAG) == null
				&& activeDialogId == CONTEXT_MENU_ITEM_COPY) {
			activeDialogId = NO_DIALOG_FRAGMENT_ACTIVE;
		}
	}

	private void initAdapter() {
		File rootDirectory = new File(Constants.DEFAULT_ROOT);
		File projectCodeFile;
		projectList = new ArrayList<ProjectData>();
		for (String projectName : UtilFile.getProjectNames(rootDirectory)) {
			projectCodeFile = new File(Utils.buildPath(Utils.buildProjectPath(projectName), Constants.PROJECTCODE_NAME));
			projectList.add(new ProjectData(projectName, projectCodeFile.lastModified()));
		}
		Collections.sort(projectList, new Comparator<ProjectData>() {
			@Override
			public int compare(ProjectData project1, ProjectData project2) {
				return Long.valueOf(project2.lastUsed).compareTo(project1.lastUsed);
			}
		});

		adapter = new ProjectAdapter(getActivity(), R.layout.fragment_my_projects_item,
				R.id.my_projects_activity_project_title, projectList);
		setListAdapter(adapter);
	}

	private void initClickListener() {
		getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					if (!ProjectManager.getInstance().loadProject((adapter.getItem(position)).projectName,
							getActivity(), ProjectsListFragment.this, true)) {
						return; // error message already in ProjectManager
								// loadProject
					}

				} catch (ClassCastException exception) {
					Log.e("CATROID", this.getClass().getName() + " does not implement ErrorListenerInterface",
							exception);
					return;
				}

				currentProjectChangedListener.onCurrentProjectChanged();
			}
		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				projectToEdit = projectList.get(position);
				if (projectToEdit == null) {
					return true;
				}

				showEditProjectContextDialog();

				return true;
			}
		});
	}

	private void showEditProjectContextDialog() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag(CustomIconContextMenu.DIALOG_FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		CustomIconContextMenu dialog = CustomIconContextMenu.newInstance(projectToEdit.projectName);
		initCustomContextMenu(dialog);
		dialog.show(getFragmentManager(), CustomIconContextMenu.DIALOG_FRAGMENT_TAG);
	}

	private void initCustomContextMenu(CustomIconContextMenu iconContextMenu) {
		Resources resources = getResources();

		IconMenuAdapter adapter = new IconMenuAdapter(getActivity());
		adapter.addItem(resources, this.getString(R.string.rename), R.drawable.ic_context_rename,
				CONTEXT_MENU_ITEM_RENAME);
		adapter.addItem(resources, this.getString(R.string.set_description), R.drawable.ic_menu_description,
				CONTEXT_MENU_ITEM_DESCRIPTION);
		adapter.addItem(resources, this.getString(R.string.delete), R.drawable.ic_context_delete,
				CONTEXT_MENU_ITEM_DELETE);
		adapter.addItem(resources, this.getString(R.string.copy), R.drawable.ic_context_copy, CONTEXT_MENU_ITEM_COPY);
		iconContextMenu.setAdapter(adapter);

		iconContextMenu.setOnClickListener(new CustomIconContextMenu.IconContextMenuOnClickListener() {
			@Override
			public void onClick(int menuId) {
				activeDialogId = menuId;
				switch (menuId) {
					case CONTEXT_MENU_ITEM_RENAME:
						RenameProjectDialog dialogRenameProject = RenameProjectDialog
								.newInstance(projectToEdit.projectName);
						dialogRenameProject.setOnProjectRenameListener(ProjectsListFragment.this);
						dialogRenameProject.show(getActivity().getSupportFragmentManager(),
								RenameProjectDialog.DIALOG_FRAGMENT_TAG);
						break;
					case CONTEXT_MENU_ITEM_DESCRIPTION:
						SetDescriptionDialog dialogSetDescription = SetDescriptionDialog
								.newInstance(projectToEdit.projectName);
						dialogSetDescription.setOnUpdateProjectDescriptionListener(ProjectsListFragment.this);
						dialogSetDescription.show(getActivity().getSupportFragmentManager(),
								SetDescriptionDialog.DIALOG_FRAGMENT_TAG);
						break;
					case CONTEXT_MENU_ITEM_DELETE:
						deleteProject();
						break;
					case CONTEXT_MENU_ITEM_COPY:
						CopyProjectDialog dialogCopyProject = CopyProjectDialog.newInstance(projectToEdit.projectName);
						dialogCopyProject.setParentFragment(parentFragment);
						dialogCopyProject.show(getActivity().getSupportFragmentManager(),
								CopyProjectDialog.DIALOG_FRAGMENT_TAG);
						break;
				}
			}
		});
	}

	private void deleteProject() {
		ProjectManager projectManager = ProjectManager.getInstance();
		Project currentProject = projectManager.getCurrentProject();

		if (currentProject != null && currentProject.getName().equalsIgnoreCase(projectToEdit.projectName)) {
			projectManager.deleteCurrentProject();
		} else {
			StorageHandler.getInstance().deleteProject(projectToEdit);
		}
		try {
			projectList.remove(projectToEdit);
			if (projectList.size() == 0) {
				projectManager.initializeDefaultProject(getActivity(), this);
			} else {

				projectManager.loadProject((projectList.get(0)).projectName, getActivity(), this, false);
				projectManager.saveProject();
			}
		} catch (ClassCastException exception) {
			Log.e("CATROID", this.getClass().getName() + " does not implement ErrorListenerInterface", exception);
		}

		updateProjectTitle();
		initAdapter();
	}

	private void updateProjectTitle() {
		String title;
		Project currentProject = ProjectManager.INSTANCE.getCurrentProject();

		if (currentProject != null) {
			title = getResources().getString(R.string.project_name) + " " + currentProject.getName();
		} else {
			title = getResources().getString(android.R.string.unknownName);
		}

		getSherlockActivity().getSupportActionBar().setTitle(title);
	}

	public static class ProjectData implements Serializable {

		private static final long serialVersionUID = 1L;

		public String projectName;
		public long lastUsed;

		public ProjectData(String projectName, long lastUsed) {
			this.projectName = projectName;
			this.lastUsed = lastUsed;
		}
	}
}
