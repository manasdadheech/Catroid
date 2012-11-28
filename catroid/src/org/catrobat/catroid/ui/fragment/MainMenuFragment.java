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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.stage.PreStageActivity;
import org.catrobat.catroid.transfers.CheckTokenTask;
import org.catrobat.catroid.transfers.CheckTokenTask.OnCheckTokenCompleteListener;
import org.catrobat.catroid.transfers.ProjectDownloadService;
import org.catrobat.catroid.ui.MyProjectsActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.ui.adapter.MainMenuAdapter;
import org.catrobat.catroid.ui.dialogs.AboutDialogFragment;
import org.catrobat.catroid.ui.dialogs.LoginRegisterDialog;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog;
import org.catrobat.catroid.ui.dialogs.UploadProjectDialog;
import org.catrobat.catroid.utils.ErrorListenerInterface;
import org.catrobat.catroid.utils.StatusBarNotificationManager;
import org.catrobat.catroid.utils.UtilZip;
import org.catrobat.catroid.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class MainMenuFragment extends SherlockFragment implements OnCheckTokenCompleteListener, ErrorListenerInterface {

	private class DownloadReceiver extends ResultReceiver {

		public DownloadReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == Constants.UPDATE_DOWNLOAD_PROGRESS) {
				long progress = resultData.getLong("currentDownloadProgress");
				boolean endOfFileReached = resultData.getBoolean("endOfFileReached");
				Integer notificationId = resultData.getInt("notificationId");
				String projectName = resultData.getString("projectName");
				if (endOfFileReached) {
					progress = 100;
				}
				String notificationMessage = "Download " + progress + "% "
						+ getString(R.string.notification_percent_completed) + ":" + projectName;

				StatusBarNotificationManager.INSTANCE.updateNotification(notificationId, notificationMessage,
						Constants.DOWNLOAD_NOTIFICATION, endOfFileReached);
			}
		}
	}

	private static final String TAG = "MainMenuFragment";
	private static final String PROJECTNAME_TAG = "fname=";

	private ProjectManager projectManager;

	private View rootView;

	private boolean ignoreResume = false;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity activity = getActivity();
		Utils.updateScreenWidthAndHeight(activity);

		projectManager = ProjectManager.getInstance();
		Utils.loadProjectIfNeeded(activity, this);

		initMainMenu();

		// Load external project from URL or local file system.
		Uri loadExternalProjectUri = activity.getIntent().getData();
		activity.getIntent().setData(null);

		// TODO move this block (external url loading) to ProjectAcitivty
		if (loadExternalProjectUri == null) {
			return;
		}
		if (loadExternalProjectUri.getScheme().equals("http")) {
			String url = loadExternalProjectUri.toString();
			int projectNameIndex = url.lastIndexOf(PROJECTNAME_TAG) + PROJECTNAME_TAG.length();
			String projectName = url.substring(projectNameIndex);
			try {
				projectName = URLDecoder.decode(projectName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "Could not decode project name: " + projectName, e);
			}

			Intent downloadIntent = new Intent(activity, ProjectDownloadService.class);
			downloadIntent.putExtra("receiver", new DownloadReceiver(new Handler()));
			downloadIntent.putExtra("downloadName", projectName);
			downloadIntent.putExtra("url", url);
			int notificationId = createNotification(projectName);
			downloadIntent.putExtra("notificationId", notificationId);
			activity.startService(downloadIntent);
		} else if (loadExternalProjectUri.getScheme().equals("file")) {
			String path = loadExternalProjectUri.getPath();
			int a = path.lastIndexOf('/') + 1;
			int b = path.lastIndexOf('.');
			String projectName = path.substring(a, b);
			if (!UtilZip.unZipFile(path, Utils.buildProjectPath(projectName))) {
				Utils.displayErrorMessageFragment(getFragmentManager(),
						getResources().getString(R.string.error_load_project));
			}
		}

		ignoreResume = false;
		PreStageActivity.shutdownPersistentResources();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_menu, null);
		return rootView;
	}

	public int createNotification(String downloadName) {
		StatusBarNotificationManager manager = StatusBarNotificationManager.INSTANCE;
		int notificationId = manager.createNotification(downloadName, getActivity(), Constants.DOWNLOAD_NOTIFICATION);
		return notificationId;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!Utils.checkForSdCard(getActivity())) {
			return;
		}
		if (ProjectManager.INSTANCE.getCurrentProject() == null) {
			return;
		}
		if (!ignoreResume) {
			PreStageActivity.shutdownPersistentResources();
		}
		ignoreResume = false;

		ProjectManager.INSTANCE.loadProject(projectManager.getCurrentProject().getName(), getActivity(), this, false);

		StatusBarNotificationManager.INSTANCE.displayDialogs(getActivity(), this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// onPause is sufficient --> gets called before "process_killed",
		// onStop(), onDestroy(), onRestart()
		// also when you switch activities
		if (ProjectManager.INSTANCE.getCurrentProject() != null) {
			ProjectManager.INSTANCE.saveProject();
			Utils.saveToPreferences(getActivity(), Constants.PREF_PROJECTNAME_KEY, projectManager.getCurrentProject()
					.getName());
		}
	}

	// Code from Stackoverflow to reduce memory problems
	// onDestroy() and unbindDrawables() methods taken from
	// http://stackoverflow.com/a/6779067
	@Override
	public void onDestroy() {
		super.onDestroy();

		unbindDrawables(rootView);
		System.gc();
	}

	@Override
	public void onTokenNotValid() {
		showLoginRegisterDialog();
	}

	@Override
	public void onCheckTokenSuccess() {
		UploadProjectDialog uploadProjectDialog = new UploadProjectDialog();
		uploadProjectDialog.show(getFragmentManager(), UploadProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	private void showLoginRegisterDialog() {
		LoginRegisterDialog loginRegisterDialog = new LoginRegisterDialog();
		loginRegisterDialog.show(getFragmentManager(), LoginRegisterDialog.DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showErrorDialog(String errorMessage) {
		Utils.displayErrorMessageFragment(getFragmentManager(), errorMessage);
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	private void initMainMenu() {
		ListView mainMenuListView = (ListView) rootView.findViewById(R.id.lv_main_menu);
		MainMenuAdapter adapter = new MainMenuAdapter(getActivity(), generateMainMenuItems());
		mainMenuListView.setAdapter(adapter);
	}

	private List<MainMenuItem> generateMainMenuItems() {
		List<MainMenuItem> mainMenuItems = new ArrayList<MainMenuItem>();

		MainMenuItem newProject = new MainMenuItem(R.drawable.main_menu_new, R.string.main_menu_new,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleNewButton();
					}
				});
		MainMenuItem continueProject = new MainMenuItem(R.drawable.main_menu_continue, R.string.main_menu_continue,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleContinueButton();
					}
				});
		MainMenuItem programs = new MainMenuItem(R.drawable.main_menu_programs, R.string.main_menu_programs,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleProgramsButton();
					}
				});
		MainMenuItem forum = new MainMenuItem(R.drawable.main_menu_forum, R.string.main_menu_forum,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleForumButton();
					}
				});
		MainMenuItem web = new MainMenuItem(R.drawable.main_menu_web, R.string.main_menu_web, new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleWebButton();
			}
		});
		MainMenuItem upload = new MainMenuItem(R.drawable.main_menu_upload, R.string.main_menu_upload,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleUploadButton();
					}
				});
		MainMenuItem settings = new MainMenuItem(0, R.string.main_menu_settings, new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleSettingsButton();
			}
		});
		MainMenuItem about = new MainMenuItem(0, R.string.main_menu_about_catroid, new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAboutButton();
			}
		});

		if (projectManager.getCurrentProject() == null) {
			continueProject.isEnabled = false;
		}

		mainMenuItems.add(newProject);
		mainMenuItems.add(continueProject);
		mainMenuItems.add(programs);
		mainMenuItems.add(forum);
		mainMenuItems.add(web);
		mainMenuItems.add(upload);
		mainMenuItems.add(settings);
		mainMenuItems.add(about);

		return mainMenuItems;
	}

	public class MainMenuItem {
		public int iconResId;
		public int titleResId;
		public OnClickListener onClickListener;
		public boolean isEnabled = true;

		public MainMenuItem(int iconResId, int titleresId, OnClickListener onClickListener) {
			this.iconResId = iconResId;
			this.titleResId = titleresId;
			this.onClickListener = onClickListener;
		}
	}

	private void handleContinueButton() {
		if (ProjectManager.INSTANCE.getCurrentProject() != null) {
			Intent intent = new Intent(getActivity(), ProjectActivity.class);
			startActivity(intent);
		}
	}

	private void handleNewButton() {
		NewProjectDialog dialog = new NewProjectDialog();
		dialog.show(getFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	private void handleProgramsButton() {
		Intent intent = new Intent(getActivity(), MyProjectsActivity.class);
		startActivity(intent);
	}

	private void handleUploadButton() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = preferences.getString(Constants.TOKEN, null);

		if (token == null || token.length() == 0 || token.equals("0")) {
			showLoginRegisterDialog();
		} else {
			CheckTokenTask checkTokenTask = new CheckTokenTask(getActivity(), token);
			checkTokenTask.setOnCheckTokenCompleteListener(this);
			checkTokenTask.execute();
		}
	}

	private void handleWebButton() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getText(R.string.catroid_website).toString()));
		startActivity(browserIntent);
	}

	private void handleForumButton() {
		Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getText(R.string.catrobat_forum).toString()));
		startActivity(browerIntent);
	}

	private void handleSettingsButton() {
		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		startActivity(intent);
	}

	private void handleAboutButton() {
		AboutDialogFragment aboutDialog = new AboutDialogFragment();
		aboutDialog.show(getFragmentManager(), AboutDialogFragment.DIALOG_FRAGMENT_TAG);
	}
}
