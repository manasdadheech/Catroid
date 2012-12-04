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
package org.catrobat.catroid.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.stage.PreStageActivity;
import org.catrobat.catroid.transfers.CheckTokenTask;
import org.catrobat.catroid.transfers.CheckTokenTask.OnCheckTokenCompleteListener;
import org.catrobat.catroid.transfers.ProjectDownloadService;
import org.catrobat.catroid.ui.dialogs.AboutDialogFragment;
import org.catrobat.catroid.ui.dialogs.LoginRegisterDialog;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog.CurrentProjectChangedListener;
import org.catrobat.catroid.ui.dialogs.UploadProjectDialog;
import org.catrobat.catroid.ui.fragment.MainMenuFragment.MainMenuItemClickListener;
import org.catrobat.catroid.ui.fragment.ProjectsListFragment;
import org.catrobat.catroid.ui.fragment.SpritesListFragment;
import org.catrobat.catroid.utils.ErrorListenerInterface;
import org.catrobat.catroid.utils.StatusBarNotificationManager;
import org.catrobat.catroid.utils.UtilZip;
import org.catrobat.catroid.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class MainMenuActivity extends BaseSlidingFragmentActivity implements MainMenuItemClickListener,
		OnCheckTokenCompleteListener, ErrorListenerInterface, CurrentProjectChangedListener {

	public enum ActiveScreen {

		SPRITES_LIST(SpritesListFragment.class, SpritesListFragment.FRAGMENT_TAG),
		PROJECTS_LIST(ProjectsListFragment.class, ProjectsListFragment.FRAGMENT_TAG);

		private final Class<?> clss;
		private final String fragmentTag;

		private ActiveScreen(Class<?> clss, String fragmentTag) {
			this.clss = clss;
			this.fragmentTag = fragmentTag;
		}

		public Class<?> getActiveFragmetClass() {
			return clss;
		}

		public String getActiveFragmentTag() {
			return fragmentTag;
		}
	}

	public enum MainMenuItem {
		NEW, CONTINUE, PROGRAMS, FORUM, WEB, UPLOAD, ABOUT;
	}

	public static Intent createIntent(Context context, ActiveScreen mainFragment) {
		Intent intent = new Intent(context, MainMenuActivity.class);
		intent.putExtra(EXTRA_ACTIVE_SCREEN, mainFragment);
		return intent;
	}

	private static final String TAG = "MainMenuActivity";
	private static final String PROJECTNAME_TAG = "fname=";

	private static final String EXTRA_ACTIVE_SCREEN = "extra_active_screen";

	private ProjectManager projectManager;
	private ActiveScreen activeScreen;

	private boolean ignoreResume = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		Utils.updateScreenWidthAndHeight(this);

		projectManager = ProjectManager.getInstance();
		Utils.loadProjectIfNeeded(this, this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			activeScreen = (ActiveScreen) extras.getSerializable(EXTRA_ACTIVE_SCREEN);
		}
		if (activeScreen == null) {
			activeScreen = ActiveScreen.SPRITES_LIST;
		}

		if (getSupportFragmentManager().findFragmentByTag(SpritesListFragment.FRAGMENT_TAG) == null) {
			switchFragment(activeScreen);
		}

		// Load external project from URL or local file system.
		Uri loadExternalProjectUri = getIntent().getData();
		getIntent().setData(null);

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

			Intent downloadIntent = new Intent(this, ProjectDownloadService.class);
			downloadIntent.putExtra("receiver", new DownloadReceiver(new Handler()));
			downloadIntent.putExtra("downloadName", projectName);
			downloadIntent.putExtra("url", url);
			int notificationId = createNotification(projectName);
			downloadIntent.putExtra("notificationId", notificationId);
			startService(downloadIntent);
		} else if (loadExternalProjectUri.getScheme().equals("file")) {
			String path = loadExternalProjectUri.getPath();
			int a = path.lastIndexOf('/') + 1;
			int b = path.lastIndexOf('.');
			String projectName = path.substring(a, b);
			if (!UtilZip.unZipFile(path, Utils.buildProjectPath(projectName))) {
				Utils.displayErrorMessageFragment(getSupportFragmentManager(),
						getResources().getString(R.string.error_load_project));
			}
		}

		ignoreResume = false;
		PreStageActivity.shutdownPersistentResources();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!Utils.checkForSdCard(this)) {
			return;
		}
		if (ProjectManager.INSTANCE.getCurrentProject() == null) {
			return;
		}
		if (!ignoreResume) {
			PreStageActivity.shutdownPersistentResources();
		}
		ignoreResume = false;

		ProjectManager.INSTANCE.loadProject(projectManager.getCurrentProject().getName(), this, this, false);

		StatusBarNotificationManager.INSTANCE.displayDialogs(this, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// onPause is sufficient --> gets called before "process_killed",
		// onStop(), onDestroy(), onRestart()
		// also when you switch activities
		if (ProjectManager.INSTANCE.getCurrentProject() != null) {
			ProjectManager.INSTANCE.saveProject();
			Utils.saveToPreferences(this, Constants.PREF_PROJECTNAME_KEY, projectManager.getCurrentProject().getName());
		}
	}

	// Code from Stackoverflow to reduce memory problems
	// onDestroy() and unbindDrawables() methods taken from
	// http://stackoverflow.com/a/6779067
	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.ll_main_menu_activity_root));
		System.gc();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onMainMenuItemClick(MainMenuItem item) {
		switch (item) {
			case NEW: {
				handleNewButton();
				toggle();
				break;
			}
			case CONTINUE: {
				handleContinueButton();
				toggle();
				break;
			}
			case PROGRAMS: {
				handleProgramsButton();
				toggle();
				break;
			}
			case FORUM: {
				handleForumButton();
				toggle();
				break;
			}
			case WEB: {
				handleWebButton();
				toggle();
				break;
			}
			case UPLOAD: {
				handleUploadButton();
				toggle();
				break;
			}
			case ABOUT: {
				handleAboutButton();
				toggle();
				break;
			}
		}
	}

	@Override
	public void onTokenNotValid() {
		showLoginRegisterDialog();
	}

	@Override
	public void onCheckTokenSuccess() {
		UploadProjectDialog uploadProjectDialog = new UploadProjectDialog();
		uploadProjectDialog.show(getSupportFragmentManager(), UploadProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showErrorDialog(String errorMessage) {
		Utils.displayErrorMessageFragment(getSupportFragmentManager(), errorMessage);
	}

	@Override
	public void onCurrentProjectChanged() {
		switchFragment(ActiveScreen.SPRITES_LIST);
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

	private void switchFragment(ActiveScreen activeFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = Fragment.instantiate(this, activeFragment.getActiveFragmetClass().getName());
		ft.replace(R.id.ll_main_menu_activity_root, fragment, activeFragment.getActiveFragmentTag());
		ft.commit();
	}

	private int createNotification(String downloadName) {
		StatusBarNotificationManager manager = StatusBarNotificationManager.INSTANCE;
		int notificationId = manager.createNotification(downloadName, this, Constants.DOWNLOAD_NOTIFICATION);
		return notificationId;
	}

	private void handleContinueButton() {
		if (ProjectManager.INSTANCE.getCurrentProject() != null) {
			switchFragment(ActiveScreen.SPRITES_LIST);
		}
	}

	private void handleNewButton() {
		NewProjectDialog dialog = new NewProjectDialog();
		dialog.setCurrentProjectChangedListener(this);
		dialog.show(getSupportFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	private void handleProgramsButton() {
		switchFragment(ActiveScreen.PROJECTS_LIST);
	}

	private void handleUploadButton() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = preferences.getString(Constants.TOKEN, null);

		if (token == null || token.length() == 0 || token.equals("0")) {
			showLoginRegisterDialog();
		} else {
			CheckTokenTask checkTokenTask = new CheckTokenTask(this, token);
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

	private void handleAboutButton() {
		AboutDialogFragment aboutDialog = new AboutDialogFragment();
		aboutDialog.show(getSupportFragmentManager(), AboutDialogFragment.DIALOG_FRAGMENT_TAG);
	}

	private void showLoginRegisterDialog() {
		LoginRegisterDialog loginRegisterDialog = new LoginRegisterDialog();
		loginRegisterDialog.show(getSupportFragmentManager(), LoginRegisterDialog.DIALOG_FRAGMENT_TAG);
	}

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
}
