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
package org.catrobat.catroid.uitest.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.StandardProjectHandler;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.SetCostumeBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.MyProjectsActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.uitest.util.UiTestUtils;
import org.catrobat.catroid.utils.UtilFile;
import org.catrobat.catroid.utils.Utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MainMenuActivityTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	private Solo solo;
	private String testProject = UiTestUtils.PROJECTNAME1;
	private String testProject2 = UiTestUtils.PROJECTNAME2;
	private String testProject3 = UiTestUtils.PROJECTNAME3;
	private String projectNameWithBlacklistedCharacters = "<H/ey, lo\"ok, :I'\\m s*pe?ci>al! ?äö|üß<>";
	private String projectNameWithWhitelistedCharacters = "[Hey+, =lo_ok. I'm; -special! ?äöüß<>]";

	private static final float CATROBAT_LANGUAGE_VERSION_NOT_SUPPORTED = 0.0f;

	public MainMenuActivityTest() {
		super(MainMenuActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		UiTestUtils.goBackToHome(getInstrumentation());
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		UtilFile.deleteDirectory(new File(Utils.buildProjectPath(projectNameWithBlacklistedCharacters)));
		UtilFile.deleteDirectory(new File(Utils.buildProjectPath(projectNameWithWhitelistedCharacters)));
		super.tearDown();
		solo = null;
	}

	public void testCreateNewProject() {
		File directory = new File(Constants.DEFAULT_ROOT + "/" + testProject);
		UtilFile.deleteDirectory(directory);
		assertFalse("testProject was not deleted!", directory.exists());

		String hintNewProjectText = solo.getString(R.string.new_project_dialog_hint);

		solo.clickOnButton(getActivity().getString(R.string.main_menu_new));
		solo.waitForText(hintNewProjectText);
		EditText addNewProjectEditText = solo.getEditText(0);
		//check if hint is set
		assertEquals("Not the proper hint set", hintNewProjectText, addNewProjectEditText.getHint());
		assertEquals("There should no text be set", "", addNewProjectEditText.getText().toString());
		String buttonOKText = solo.getString(R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.waitForActivity(ProjectActivity.class.getSimpleName());

		File file = new File(Constants.DEFAULT_ROOT + "/" + testProject + "/" + Constants.PROJECTCODE_NAME);
		assertTrue(testProject + " was not created!", file.exists());
	}

	public void testCreateNewProjectErrors() {
		solo.clickOnButton(getActivity().getString(R.string.main_menu_new));
		Button okButton = (Button) solo.getView(R.id.new_project_ok_button);

		assertFalse("New project ok button is enabled!", okButton.isEnabled());
		solo.clickOnButton(0);

		File directory = new File(Constants.DEFAULT_ROOT + "/" + testProject);
		directory.mkdirs();
		solo.sleep(50);
		solo.clickOnButton(0);

		assertTrue("No error message was displayed upon creating a project with the same name twice.",
				solo.searchText(getActivity().getString(R.string.error_project_exists)));
		solo.clickOnButton(0);

		directory = new File(Utils.buildProjectPath("te?st"));
		directory.mkdirs();
		solo.sleep(50);
		solo.clickOnButton(0);

		assertTrue("No error message was displayed upon creating a project with the same name twice.",
				solo.searchText(getActivity().getString(R.string.error_project_exists)));
		solo.clickOnButton(getActivity().getString(R.string.close));

		UtilFile.deleteDirectory(directory);
	}

	public void testCreateNewProjectWithBlacklistedCharacters() {
		String directoryPath = Utils.buildProjectPath(projectNameWithBlacklistedCharacters);
		File directory = new File(directoryPath);
		UtilFile.deleteDirectory(directory);

		solo.clickOnButton(getActivity().getString(R.string.main_menu_new));
		String buttonOKText = solo.getString(R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.waitForActivity(ProjectActivity.class.getSimpleName());

		File file = new File(Utils.buildPath(directoryPath, Constants.PROJECTCODE_NAME));
		assertTrue("Project with blacklisted characters was not created!", file.exists());
	}

	public void testCreateNewProjectWithWhitelistedCharacters() {
		String directoryPath = Utils.buildProjectPath(projectNameWithWhitelistedCharacters);
		File directory = new File(directoryPath);
		UtilFile.deleteDirectory(directory);

		solo.clickOnButton(getActivity().getString(R.string.main_menu_new));
		String buttonOKText = solo.getString(R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.waitForActivity(ProjectActivity.class.getSimpleName());

		File file = new File(Utils.buildPath(directoryPath, Constants.PROJECTCODE_NAME));
		assertTrue("Project file with whitelisted characters was not created!", file.exists());
	}

	public void testOrientation() throws NameNotFoundException {
		/// Method 1: Assert it is currently in portrait mode.
		assertEquals(Configuration.ORIENTATION_PORTRAIT, getActivity().getResources().getConfiguration().orientation);

		/// Method 2: Retreive info about Activity as collected from AndroidManifest.xml
		// https://developer.android.com/reference/android/content/pm/ActivityInfo.html
		PackageManager packageManager = getActivity().getPackageManager();
		ActivityInfo activityInfo = packageManager.getActivityInfo(getActivity().getComponentName(),
				PackageManager.GET_ACTIVITIES);

		// Note that the activity is _indeed_ rotated on your device/emulator!
		// Robotium can _force_ the activity to be in landscape mode (and so could we, programmatically)
		solo.setActivityOrientation(Solo.LANDSCAPE);

		assertEquals(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, activityInfo.screenOrientation);
	}

	public void testLoadProject() {
		File directory = new File(Constants.DEFAULT_ROOT + "/" + testProject2);
		UtilFile.deleteDirectory(directory);
		assertFalse(testProject2 + " was not deleted!", directory.exists());

		createTestProject(testProject2);
		solo.sleep(200);

		solo.clickOnButton(getActivity().getString(R.string.main_menu_programs));
		solo.waitForActivity(MyProjectsActivity.class.getSimpleName());
		solo.clickOnText(testProject2);
		ListView spritesList = (ListView) solo.getCurrentActivity().findViewById(android.R.id.list);
		Sprite first = (Sprite) spritesList.getItemAtPosition(1);
		assertEquals("Sprite at index 1 is not \"cat\"!", "cat", first.getName());
		Sprite second = (Sprite) spritesList.getItemAtPosition(2);
		assertEquals("Sprite at index 2 is not \"dog\"!", "dog", second.getName());
		Sprite third = (Sprite) spritesList.getItemAtPosition(3);
		assertEquals("Sprite at index 3 is not \"horse\"!", "horse", third.getName());
		Sprite fourth = (Sprite) spritesList.getItemAtPosition(4);
		assertEquals("Sprite at index 4 is not \"pig\"!", "pig", fourth.getName());
	}

	public void testResume() {
		File directory = new File(Constants.DEFAULT_ROOT + "/" + testProject3);
		UtilFile.deleteDirectory(directory);
		assertFalse(testProject3 + " was not deleted!", directory.exists());

		createTestProject(testProject3);
		solo.sleep(200);

		solo.clickOnButton(getActivity().getString(R.string.main_menu_programs));
		solo.waitForActivity(MyProjectsActivity.class.getSimpleName());
		solo.clickOnText(testProject3);
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.goBack();
		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		solo.clickOnButton(getActivity().getString(R.string.main_menu_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName());

		ListView spritesList = (ListView) solo.getCurrentActivity().findViewById(android.R.id.list);
		Sprite first = (Sprite) spritesList.getItemAtPosition(1);
		assertEquals("Sprite at index 1 is not \"cat\"!", "cat", first.getName());
		Sprite second = (Sprite) spritesList.getItemAtPosition(2);
		assertEquals("Sprite at index 2 is not \"dog\"!", "dog", second.getName());
		Sprite third = (Sprite) spritesList.getItemAtPosition(3);
		assertEquals("Sprite at index 3 is not \"horse\"!", "horse", third.getName());
		Sprite fourth = (Sprite) spritesList.getItemAtPosition(4);
		assertEquals("Sprite at index 4 is not \"pig\"!", "pig", fourth.getName());
	}

	public void testAboutCatroid() {
		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		solo.sleep(200);
		solo.clickOnMenuItem(getActivity().getString(R.string.main_menu_about_catroid));
		solo.sleep(200);
		ArrayList<TextView> textViewList = solo.getCurrentTextViews(null);

		assertEquals("Title is not correct!", getActivity().getString(R.string.about_title), textViewList.get(0)
				.getText().toString());
		assertEquals("About text not correct!", getActivity().getString(R.string.about_text), textViewList.get(1)
				.getText().toString());
		assertEquals("Link text is not correct!", getActivity().getString(R.string.about_catroid_license_link_text),
				textViewList.get(2).getText().toString());
		solo.goBack();
	}

	public void testShouldDisplayDialogIfVersionNumberTooHigh() throws Throwable {
		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		// Prevent Utils from returning true in isApplicationDebuggable
		UiTestUtils.setPrivateField2(Utils.class, null, "isUnderTest", true);

		boolean result = UiTestUtils
				.createTestProjectOnLocalStorageWithCatrobatLanguageVersion(CATROBAT_LANGUAGE_VERSION_NOT_SUPPORTED);
		assertTrue("Could not create test project.", result);

		runTestOnUiThread(new Runnable() {
			public void run() {
				ProjectManager.INSTANCE.loadProject(UiTestUtils.DEFAULT_TEST_PROJECT_NAME, getActivity(),
						getActivity(), true);
			}
		});

		solo.getText(solo.getString(R.string.error_project_compatability), true);
		solo.clickOnButton(0);
		solo.waitForDialogToClose(500);
	}

	public void testPlayButton() {
		// FIXME
		//		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);
		//		solo.assertCurrentActivity("StageActivity not showing!", StageActivity.class);
	}

	// TODO edit this to work with login dialog

	//	public void testRenameToExistingProject() {
	//		createTestProject(existingProject);
	//		solo.clickOnButton(getActivity().getString(R.string.main_menu_upload));
	//		solo.clickOnEditText(0);
	//		solo.enterText(0, "");
	//		solo.enterText(0, existingProject);
	//		solo.goBack();
	//		solo.clickOnEditText(1);
	//		solo.goBack();
	//		solo.clickOnButton(getActivity().getString(R.string.upload_button));
	//		assertTrue("No error message was displayed upon renaming the project to an existing one.",
	//				solo.searchText(getActivity().getString(R.string.error_project_exists)));
	//	}

	//	public void testDefaultProject() throws IOException {
	//		File directory = new File(Constants.DEFAULT_ROOT + "/" + getActivity().getString(R.string.default_project_name));
	//		UtilFile.deleteDirectory(directory);
	//
	//		StorageHandler handler = StorageHandler.getInstance();
	//		ProjectManager project = ProjectManager.getInstance();
	//		project.setProject(handler.createDefaultProject(solo.getCurrentActivity()));
	//		UiTestUtils.clickOnImageButton(solo, R.id.btn_action_play);
	//		solo.sleep(8000);
	//		Bitmap bitmap = project.getCurrentProject().getSpriteList().get(1).getCostume().getBitmap();
	//		assertNotNull("Bitmap is null", bitmap);
	//		assertTrue("Sprite not visible", project.getCurrentProject().getSpriteList().get(1).isVisible());
	//
	//		directory = new File(Constants.DEFAULT_ROOT + "/" + getActivity().getString(R.string.default_project_name));
	//		UtilFile.deleteDirectory(directory);
	//	}

	public void createTestProject(String projectName) {
		StorageHandler storageHandler = StorageHandler.getInstance();

		int xPosition = 457;
		int yPosition = 598;
		double size = 0.8;

		Project project = new Project(getActivity(), projectName);
		Sprite firstSprite = new Sprite("cat");
		Sprite secondSprite = new Sprite("dog");
		Sprite thirdSprite = new Sprite("horse");
		Sprite fourthSprite = new Sprite("pig");
		Script testScript = new StartScript(firstSprite);
		Script otherScript = new StartScript(secondSprite);
		HideBrick hideBrick = new HideBrick(firstSprite);
		ShowBrick showBrick = new ShowBrick(firstSprite);
		SetSizeToBrick setSizeToBrick = new SetSizeToBrick(secondSprite, size);
		ComeToFrontBrick comeToFrontBrick = new ComeToFrontBrick(firstSprite);
		PlaceAtBrick placeAtBrick = new PlaceAtBrick(secondSprite, xPosition, yPosition);

		// adding Bricks: ----------------
		testScript.addBrick(hideBrick);
		testScript.addBrick(showBrick);
		testScript.addBrick(setSizeToBrick);
		testScript.addBrick(comeToFrontBrick);

		otherScript.addBrick(placeAtBrick); // secondSprite
		otherScript.setPaused(true);
		// -------------------------------

		firstSprite.addScript(testScript);
		secondSprite.addScript(otherScript);

		project.addSprite(firstSprite);
		project.addSprite(secondSprite);
		project.addSprite(thirdSprite);
		project.addSprite(fourthSprite);

		storageHandler.saveProject(project);
	}

	public void testOverrideMyFirstProject() {
		String standardProjectName = solo.getString(R.string.default_project_name);
		Project standardProject = null;

		try {
			standardProject = StandardProjectHandler.createAndSaveStandardProject(standardProjectName,
					getInstrumentation().getTargetContext());
		} catch (IOException e) {
			fail("Could not create standard project");
			e.printStackTrace();
		}

		if (standardProject == null) {
			fail("Could not create standard project");
		}
		ProjectManager.INSTANCE.setProject(standardProject);
		StorageHandler.getInstance().saveProject(standardProject);

		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		solo.sleep(300);
		solo.clickOnButton(solo.getString(R.string.main_menu_programs));
		solo.waitForActivity(MyProjectsActivity.class.getSimpleName());

		Sprite backgroundSprite = standardProject.getSpriteList().get(0);
		Script startingScript = backgroundSprite.getScript(0);
		assertEquals("Number of bricks in background sprite was wrong", 1, backgroundSprite.getNumberOfBricks());
		startingScript.addBrick(new SetCostumeBrick(backgroundSprite));
		startingScript.addBrick(new SetCostumeBrick(backgroundSprite));
		startingScript.addBrick(new SetCostumeBrick(backgroundSprite));
		assertEquals("Number of bricks in background sprite was wrong", 4, backgroundSprite.getNumberOfBricks());
		ProjectManager.INSTANCE.setCurrentSprite(backgroundSprite);
		ProjectManager.INSTANCE.setCurrentScript(startingScript);
		ProjectManager.INSTANCE.saveProject();

		UiTestUtils.goBackToHome(getInstrumentation());
		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		solo.sleep(300);
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getInstrumentation()
				.getTargetContext());
		assertEquals("Standard project was not set in shared preferences", standardProjectName,
				defaultSharedPreferences.getString(Constants.PREF_PROJECTNAME_KEY, null));

		Utils.saveToPreferences(getInstrumentation().getTargetContext(), Constants.PREF_PROJECTNAME_KEY, null);
		assertEquals("Standard project was not reset to null in shared preferences", null,
				defaultSharedPreferences.getString(Constants.PREF_PROJECTNAME_KEY, null));

		Intent intent = new Intent(solo.getCurrentActivity(), ProjectActivity.class);
		ProjectManager.INSTANCE.setProject(null);
		solo.getCurrentActivity().startActivity(intent);
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.sleep(500);
		assertEquals("Number of bricks in background sprite was wrong - standard project was overwritten", 4,
				ProjectManager.INSTANCE.getCurrentProject().getSpriteList().get(0).getNumberOfBricks());
	}
}
