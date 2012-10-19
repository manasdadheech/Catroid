package org.catrobat.catroid.uitest.livewallpaper;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.livewallpaper.LiveWallpaperSettings;
import org.catrobat.catroid.livewallpaper.R;
import org.catrobat.catroid.livewallpaper.WallpaperHelper;
import org.catrobat.catroid.uitest.util.UiTestUtils;
import org.catrobat.catroid.utils.Utils;

import android.preference.CheckBoxPreference;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class LiveWallpaperSettingsTest extends ActivityInstrumentationTestCase2<LiveWallpaperSettings> {

	private Solo solo;
	private Project currentProject;

	public LiveWallpaperSettingsTest() {
		super(LiveWallpaperSettings.class);

	}

	@Override
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());

		UiTestUtils.createTestProject();
		currentProject = ProjectManager.getInstance().getCurrentProject();
		currentProject.setDescription("This is a test");
		WallpaperHelper.getInstance().setProject(currentProject);

	}

	@Override
	public void tearDown() throws Exception {
		UiTestUtils.goBackToHome(getInstrumentation());
		solo.finishOpenedActivities();
		ProjectManager.getInstance().deleteCurrentProject();
		super.tearDown();
		solo = null;
	}

	public void testPreferenceActivity() {
		String title = (String) solo.getCurrentActivity().getTitle();
		assertEquals("The title has not been set properly", solo.getString(R.string.lwp_settings_title), title);

		assertTrue("The General Information preference category was not found",
				solo.searchText(solo.getString(R.string.lwp_general_information)));
		assertTrue("The About Catrobat preference was not foound",
				solo.searchText(solo.getString(R.string.lwp_about_catrobat)));

		assertTrue("The 'Preferences' preference category was not found",
				solo.searchText(solo.getString(R.string.lwp_preferences)));
		assertTrue("The Allow sound preference was not found",
				solo.searchText(solo.getString(R.string.lwp_sound_control)));
	}

	public void testAllowSoundDefaultValue() {

		@SuppressWarnings("deprecation")
		CheckBoxPreference soundPreference = (CheckBoxPreference) getActivity().findPreference(
				solo.getString(R.string.lwp_sound_control));
		assertTrue("The Allow sound preference is not checked by default", soundPreference.isChecked());

	}

	public void testAboutLiveWallpaperDialog() {
		solo.clickOnText(solo.getString(R.string.lwp_about_wallpaper));
		solo.sleep(500);

		assertTrue("The title was not found", solo.searchText(solo.getString(R.string.lwp_about_wallpaper)));

		String name = solo.getString(R.string.lwp_project_name) + " " + currentProject.getName();
		assertTrue("The name of the project was not set properly", solo.searchText(name));

		String licence = solo.getString(R.string.lwp_project_licnese) + " " + solo.getString(R.string.lwp_license_link);
		assertTrue("The licence is not being shown correctly", solo.searchText(licence));

		String description = solo.getString(R.string.lwp_project_description) + " " + currentProject.getDescription();
		assertTrue("The description has not been found", solo.searchText(description));

	}

	public void testAboutCatrobatDialog() {
		solo.clickOnText(solo.getString(R.string.lwp_about_catrobat));
		solo.sleep(500);

		assertTrue("The title is not set properly", solo.searchText(solo.getString(R.string.lwp_about_catrobat)));
		assertTrue("The about text was not found", solo.searchText(solo.getString(R.string.lwp_about_catrobat_text)));
		assertTrue("The version name was not found",
				solo.searchText(Utils.getVersionName(getInstrumentation().getContext())));
	}
}
