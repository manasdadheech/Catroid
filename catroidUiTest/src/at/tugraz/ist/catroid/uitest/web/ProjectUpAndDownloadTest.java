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
package at.tugraz.ist.catroid.uitest.web;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import at.tugraz.ist.catroid.ProjectManager;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.common.Constants;
import at.tugraz.ist.catroid.common.StandardProjectHandler;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.bricks.Brick;
import at.tugraz.ist.catroid.io.StorageHandler;
import at.tugraz.ist.catroid.ui.MainMenuActivity;
import at.tugraz.ist.catroid.uitest.util.UiTestUtils;
import at.tugraz.ist.catroid.utils.UtilFile;
import at.tugraz.ist.catroid.utils.Utils;
import at.tugraz.ist.catroid.web.ServerCalls;

import com.jayway.android.robotium.solo.Solo;

public class ProjectUpAndDownloadTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private static final String TEST_FILE_DOWNLOAD_URL = "http://catroidtest.ist.tugraz.at/catroid/download/";

	private Solo solo;
	private String testProject = UiTestUtils.PROJECTNAME1;
	private String newTestProject = UiTestUtils.PROJECTNAME2;
	private String saveToken;
	private int serverProjectId;

	Project standardProject;

	public ProjectUpAndDownloadTest() {
		super("at.tugraz.ist.catroid", MainMenuActivity.class);
		UiTestUtils.clearAllUtilTestProjects();
	}

	@Override
	@UiThreadTest
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		saveToken = prefs.getString(Constants.TOKEN, "0");
	}

	@Override
	public void tearDown() throws Exception {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prefs.edit().putString(Constants.TOKEN, saveToken).commit();
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
	}

	private void setServerURLToTestUrl() throws Throwable {
		runTestOnUiThread(new Runnable() {
			public void run() {
				ServerCalls.useTestUrl = true;
			}
		});
	}

	public void testUploadProjectSuccess() throws Throwable {
		setServerURLToTestUrl();

		createTestProject(testProject);
		addABrickToProject();

		//intent to the main activity is sent since changing activity orientation is not working
		//after executing line "UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);" 
		Intent intent = new Intent(getActivity(), MainMenuActivity.class);
		getActivity().startActivity(intent);

		UiTestUtils.createValidUser(getActivity());

		uploadProject();

		UiTestUtils.clearAllUtilTestProjects();

		downloadProject();
	}

	private void createTestProject(String projectToCreate) {
		File directory = new File(Constants.DEFAULT_ROOT + "/" + projectToCreate);
		if (directory.exists()) {
			UtilFile.deleteDirectory(directory);
		}
		assertFalse("testProject was not deleted!", directory.exists());

		solo.clickOnButton(getActivity().getString(R.string.new_project));
		solo.enterText(0, projectToCreate);
		solo.clickOnButton(solo.getString(R.string.ok));
		solo.sleep(2000);

		File file = new File(Constants.DEFAULT_ROOT + "/" + projectToCreate + "/" + Constants.PROJECTCODE_NAME);
		assertTrue(projectToCreate + " was not created!", file.exists());
	}

	private void addABrickToProject() {
		solo.clickInList(0);
		UiTestUtils.addNewBrick(solo, R.string.brick_wait);
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);
	}

	private void uploadProject() {
		solo.clickOnText(getActivity().getString(R.string.upload_project));
		solo.sleep(500);

		// enter a new title
		solo.clearEditText(0);
		solo.clickOnEditText(0);
		solo.enterText(0, newTestProject);

		// enter a description
		solo.clearEditText(1);
		solo.clickOnEditText(1);
		solo.enterText(1, "the project description");

		//		solo.setActivityOrientation(Solo.LANDSCAPE);

		solo.clickOnButton(getActivity().getString(R.string.upload_button));

		solo.sleep(500);

		try {
			solo.setActivityOrientation(Solo.LANDSCAPE);

			solo.waitForDialogToClose(10000);
			assertTrue("Upload failed. Internet connection?",
					solo.searchText(getActivity().getString(R.string.success_project_upload)));
			String resultString = (String) UiTestUtils.getPrivateField("resultString", ServerCalls.getInstance());
			JSONObject jsonObject;
			jsonObject = new JSONObject(resultString);
			serverProjectId = jsonObject.optInt("projectId");

			solo.clickOnButton(0);
		} catch (JSONException e) {
			fail("JSON exception orrured");
		}
	}

	private void downloadProject() {
		String downloadUrl = TEST_FILE_DOWNLOAD_URL + serverProjectId + Constants.CATROID_EXTENTION;
		downloadUrl += "?fname=" + newTestProject;

		Intent intent = new Intent(getActivity(), MainMenuActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(downloadUrl));
		launchActivityWithIntent("at.tugraz.ist.catroid", MainMenuActivity.class, intent);

		boolean waitResult = solo.waitForActivity("MainMenuActivity", 10000);
		assertTrue("Download takes too long.", waitResult);
		assertTrue("Testproject2 not loaded.", solo.searchText(newTestProject));
		assertTrue("OverwriteRenameDialog not showed.",
				solo.searchText(getActivity().getString(R.string.overwrite_text)));

		solo.clickOnText(getActivity().getString(R.string.overwrite_rename));
		assertTrue("No text field to enter new name.", solo.searchEditText(newTestProject));
		solo.clickOnButton(getActivity().getString(R.string.ok));
		assertTrue("No error showed because of duplicate names.",
				solo.searchText(getActivity().getString(R.string.error_project_exists)));
		solo.clickOnButton(getActivity().getString(R.string.close));
		solo.clearEditText(0);
		solo.enterText(0, testProject);
		solo.clickOnButton(getActivity().getString(R.string.ok));
		assertTrue("Download not successful.",
				solo.searchText(getActivity().getString(R.string.success_project_download)));

		String projectPath = Constants.DEFAULT_ROOT + "/" + testProject;
		File downloadedDirectory = new File(projectPath);
		File downloadedProjectFile = new File(projectPath + "/" + Constants.PROJECTCODE_NAME);
		assertTrue("Downloaded Directory does not exist.", downloadedDirectory.exists());
		assertTrue("Downloaded Project File does not exist.", downloadedProjectFile.exists());

		projectPath = Constants.DEFAULT_ROOT + "/" + newTestProject;
		downloadedDirectory = new File(projectPath);
		downloadedProjectFile = new File(projectPath + "/" + Constants.PROJECTCODE_NAME);
		assertTrue("Original Directory does not exist.", downloadedDirectory.exists());
		assertTrue("Original Project File does not exist.", downloadedProjectFile.exists());
	}

	public void testProjectUploadWithStandardProjectName() throws Throwable {
		if (!createAndSaveStandardProject() || standardProject == null) {
			fail("Standard project not created");
		}
		solo.clickOnButton(solo.getString(R.string.my_projects));
		solo.clickOnText(solo.getString(R.string.default_project_name), 2);
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);

		solo.sleep(300);
		setServerURLToTestUrl();
		UiTestUtils.createValidUser(getActivity());

		String uploadButtonText = solo.getString(R.string.upload_button);

		solo.clickOnButton(solo.getString(R.string.upload_project));
		solo.waitForText(uploadButtonText);
		solo.clickOnButton(uploadButtonText);
		solo.sleep(200);
		assertTrue("When uploading a project with the standard project name,  the error message should be shown",
				solo.searchText(solo.getString(R.string.error_upload_project_with_default_name)));

		solo.clickOnButton(solo.getString(R.string.close));
		while (solo.scrollUp()) {

		}
		solo.clearEditText(0);
		solo.enterText(0, testProject);
		solo.clickOnButton(uploadButtonText);
		solo.waitForDialogToClose(10000);

		assertTrue("Upload of unmodified standard project should not be possible, but succeeded",
				solo.searchText(solo.getString(R.string.error_upload_default_project)));
	}

	public void testProjectUploadModifiedStandardProjectSwitchCostumes() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickOnText(solo.getString(R.string.default_project_sprites_catroid_normalcat));
		solo.clickOnText(solo.getString(R.string.default_project_sprites_catroid_banzaicat));
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectDeleteScript() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickLongOnText(solo.getString(R.string.brick_when_started));
		String deleteText = solo.getString(R.string.delete);
		solo.waitForText(deleteText);
		solo.clickOnText(deleteText);
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectDeleteBrick() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickOnText(solo.getString(R.string.action_tapped));
		Brick firstBrick = ProjectManager.INSTANCE.getCurrentScript().getBrick(0);
		ProjectManager.INSTANCE.getCurrentScript().removeBrick(firstBrick);
		solo.clickOnText(solo.getString(R.string.sounds));
		solo.clickOnText(solo.getString(R.string.scripts));
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectAddBrick() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickOnText(solo.getString(R.string.action_tapped));
		UiTestUtils.addNewBrick(solo, R.string.brick_stop_all_sounds);
		solo.clickOnText(solo.getString(R.string.brick_when_started));
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectModifyInput() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickOnEditText(0);
		solo.clearEditText(0);
		solo.enterText(0, "500.0");
		solo.sleep(200);
		solo.clickOnText(solo.getString(R.string.ok));
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectDeleteCostume() throws Throwable {
		goToScriptsRoutine();
		solo.sleep(100);
		solo.clickOnText(solo.getString(R.string.costumes));
		solo.clickOnButton(solo.getString(R.string.sound_delete));
		String buttonOkText = solo.getString(R.string.ok);
		solo.waitForText(buttonOkText);
		solo.clickOnText(buttonOkText);
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectDeleteSprite() throws Throwable {
		if (!createAndSaveStandardProject() || standardProject == null) {
			fail("Standard project not created");
		}
		solo.clickOnButton(solo.getString(R.string.my_projects));
		solo.clickOnText(solo.getString(R.string.default_project_name), 2);
		solo.clickLongOnText(solo.getString(R.string.default_project_sprites_catroid_name));
		String deleteText = solo.getString(R.string.delete);
		solo.waitForText(deleteText);
		solo.clickOnText(deleteText);
		solo.sleep(200);
		uploadTestRoutine();
	}

	public void testProjectUploadModifiedStandardProjectAddSprite() throws Throwable {
		if (!createAndSaveStandardProject() || standardProject == null) {
			fail("Standard project not created");
		}
		solo.clickOnButton(solo.getString(R.string.my_projects));
		solo.clickOnText(solo.getString(R.string.default_project_name), 2);

		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_add_button);
		solo.waitForText(solo.getString(R.string.new_sprite_dialog_title));

		solo.clearEditText(0);
		solo.enterText(0, "new sprite");
		solo.sleep(200);
		solo.sendKey(Solo.ENTER);
		solo.sleep(200);
		uploadTestRoutine();
	}

	private void goToScriptsRoutine() throws IOException {
		if (!createAndSaveStandardProject() || standardProject == null) {
			fail("Standard project not created");
		}
		solo.clickOnButton(solo.getString(R.string.my_projects));
		solo.clickOnText(solo.getString(R.string.default_project_name), 2);

		solo.clickOnText(solo.getString(R.string.default_project_sprites_catroid_name));
		solo.sleep(200);
		assertTrue("Project was not yet modified, xml should be the same",
				Utils.isProjectStandardProject(getInstrumentation().getTargetContext(), standardProject.getName()));
	}

	private void uploadTestRoutine() throws Throwable {
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);
		solo.sleep(300);
		assertFalse("Project was modified, xml should have changed",
				Utils.isProjectStandardProject(getInstrumentation().getTargetContext(), standardProject.getName()));

		setServerURLToTestUrl();
		UiTestUtils.createValidUser(getActivity());
		String uploadButtonText = solo.getString(R.string.upload_button);
		solo.clickOnButton(solo.getString(R.string.upload_project));
		solo.waitForText(uploadButtonText);
		while (solo.scrollUp()) {

		}
		solo.clearEditText(0);
		solo.enterText(0, testProject);
		solo.clickOnButton(uploadButtonText);
		solo.waitForDialogToClose(10000);

		assertTrue("Project was modified - upload should have worked, but it failed",
				solo.searchText(solo.getString(R.string.success_project_upload)));
	}

	private boolean createAndSaveStandardProject() {
		try {
			standardProject = StandardProjectHandler.createAndSaveStandardProject(
					solo.getString(R.string.default_project_name), getInstrumentation().getTargetContext());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		ProjectManager.INSTANCE.setProject(standardProject);
		StorageHandler.getInstance().saveProject(standardProject);
		return true;
	}
}
