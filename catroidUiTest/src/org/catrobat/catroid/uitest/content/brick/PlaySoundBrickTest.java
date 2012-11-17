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
package org.catrobat.catroid.uitest.content.brick;

import java.io.File;
import java.util.ArrayList;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.io.SoundManager;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import android.media.MediaPlayer;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class PlaySoundBrickTest extends ActivityInstrumentationTestCase2<ScriptActivity> {
	private final int RESOURCE_SOUND = org.catrobat.catroid.uitest.R.raw.longsound;
	private final int RESOURCE_SOUND2 = org.catrobat.catroid.uitest.R.raw.testsoundui;

	private Solo solo;
	private String soundName = "testSound1";
	private String soundName2 = "testSound2";
	private File soundFile;
	private File soundFile2;
	private ArrayList<SoundInfo> soundInfoList;

	public PlaySoundBrickTest() {
		super(ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		UiTestUtils.clearAllUtilTestProjects();

		ProjectManager projectManager = ProjectManager.getInstance();
		Project project = new Project(null, UiTestUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite firstSprite = new Sprite("cat");
		Script testScript = new StartScript(firstSprite);

		PlaySoundBrick playSoundBrick = new PlaySoundBrick(firstSprite);
		testScript.addBrick(playSoundBrick);

		firstSprite.addScript(testScript);
		project.addSprite(firstSprite);

		projectManager.setProject(project);
		projectManager.setCurrentSprite(firstSprite);
		projectManager.setCurrentScript(testScript);
		soundInfoList = projectManager.getCurrentSprite().getSoundList();

		soundFile = UiTestUtils.saveFileToProject(UiTestUtils.DEFAULT_TEST_PROJECT_NAME, "longsound.mp3",
				RESOURCE_SOUND, getInstrumentation().getContext(), UiTestUtils.FileTypes.SOUND);
		SoundInfo soundInfo = new SoundInfo();
		soundInfo.setSoundFileName(soundFile.getName());
		soundInfo.setTitle(soundName);

		soundFile2 = UiTestUtils.saveFileToProject(UiTestUtils.DEFAULT_TEST_PROJECT_NAME, "testsoundui.mp3",
				RESOURCE_SOUND2, getInstrumentation().getContext(), UiTestUtils.FileTypes.SOUND);
		SoundInfo soundInfo2 = new SoundInfo();
		soundInfo2.setSoundFileName(soundFile2.getName());
		soundInfo2.setTitle(soundName2);

		soundInfoList.add(soundInfo);
		soundInfoList.add(soundInfo2);
		ProjectManager.getInstance().getFileChecksumContainer()
				.addChecksum(soundInfo.getChecksum(), soundInfo.getAbsolutePath());
		ProjectManager.getInstance().getFileChecksumContainer()
				.addChecksum(soundInfo2.getChecksum(), soundInfo2.getAbsolutePath());

		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		UiTestUtils.goBackToHome(getInstrumentation());
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		if (soundFile.exists()) {
			soundFile.delete();
		}
		if (soundFile2.exists()) {
			soundFile2.delete();
		}
		super.tearDown();
		solo = null;
	}

	public void testSelectandPlaySoundFile() {
		solo.clickOnText(solo.getString(R.string.broadcast_nothing_selected));
		solo.sleep(1000);
		assertTrue(soundName + " is not in Spinner", solo.searchText(soundName));
		assertTrue(soundName2 + " is not in Spinner", solo.searchText(soundName2));
		solo.clickOnText(soundName);
		assertTrue(soundName + " is not selected in Spinner", solo.searchText(soundName));
		MediaPlayer mediaPlayer = SoundManager.getInstance().getMediaPlayer();
		UiTestUtils.clickOnActionBar(solo, R.id.menu_start);
		solo.waitForActivity(StageActivity.class.getSimpleName());
		solo.sleep(2000);
		assertTrue("mediaPlayer is not playing", mediaPlayer.isPlaying());
		assertEquals("wrong file playing", 7592, mediaPlayer.getDuration());
		solo.goBack();
		solo.goBack();

		solo.waitForActivity(ScriptActivity.class.getSimpleName());
		solo.clickOnText(soundName);
		solo.clickOnText(soundName2);
		assertTrue(soundName2 + " is not selected in Spinner", solo.searchText(soundName2));
		mediaPlayer = SoundManager.getInstance().getMediaPlayer();
		UiTestUtils.clickOnActionBar(solo, R.id.menu_start);
		solo.waitForActivity(StageActivity.class.getSimpleName());
		solo.sleep(2000);
		assertTrue("mediaPlayer is not playing", mediaPlayer.isPlaying());
		assertEquals("wrong file playing", 4875, mediaPlayer.getDuration());
	}

	public void testSpinnerUpdatesDelete() {
		String spinnerNothingText = solo.getString(R.string.broadcast_nothing_selected);
		String buttonDeleteText = solo.getString(R.string.delete_lowercase);
		solo.clickOnText(spinnerNothingText);
		assertTrue(soundName + " is not in Spinner", solo.searchText(soundName));
		assertTrue(soundName2 + " is not in Spinner", solo.searchText(soundName2));
		solo.goBack();
		solo.clickOnText(solo.getString(R.string.sounds));
		solo.waitForText(buttonDeleteText);
		solo.clickOnButton(buttonDeleteText);
		solo.clickOnButton(solo.getString(R.string.ok));
		solo.clickOnText(solo.getString(R.string.scripts));
		solo.clickOnText(spinnerNothingText);
		assertFalse(soundName + " is still in Spinner", solo.searchText(soundName));
		assertTrue(soundName2 + " is not in Spinner", solo.searchText(soundName2));
	}

	public void testSpinnerUpdatesRename() {
		String newName = "nameRenamed";
		String spinnerNothingText = solo.getString(R.string.broadcast_nothing_selected);

		solo.clickOnText(spinnerNothingText);
		assertTrue(soundName + " is not in Spinner", solo.searchText(soundName));
		assertTrue(soundName2 + " is not in Spinner", solo.searchText(soundName2));
		solo.goBack();
		solo.clickOnText(solo.getString(R.string.sounds));
		solo.clickOnView(solo.getView(R.id.sound_title));
		solo.clearEditText(0);
		solo.enterText(0, newName);
		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.sleep(100);
		solo.setActivityOrientation(Solo.PORTRAIT);
		solo.sleep(300);
		solo.goBack();
		solo.sendKey(Solo.ENTER);
		solo.waitForDialogToClose(500);
		solo.sleep(500);
		solo.clickOnText(solo.getString(R.string.scripts));
		solo.clickOnText(spinnerNothingText);
		assertTrue(newName + " is not in Spinner", solo.searchText(newName));
		assertTrue(soundName2 + " is not in Spinner", solo.searchText(soundName2));
	}
}
