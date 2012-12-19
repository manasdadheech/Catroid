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

import java.util.ArrayList;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.adapter.BrickAdapter;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

public class BroadcastBricksTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {

	private Solo solo;
	private Project project;

	public BroadcastBricksTest() {
		super(MainMenuActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		createProject();
		solo = new Solo(getInstrumentation(), getActivity());
		UiTestUtils.getIntoScriptActivityFromMainMenu(solo);
	}

	@Override
	public void tearDown() throws Exception {
		UiTestUtils.goBackToHome(getInstrumentation());
		solo.finishOpenedActivities();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
		solo = null;
	}

	@Smoke
	public void testBroadcastBricks() {
		ListView view = UiTestUtils.getScriptListView(solo);
		BrickAdapter adapter = (BrickAdapter) view.getAdapter();

		int childrenCount = ProjectManager.getInstance().getCurrentSprite().getScript(adapter.getScriptCount() - 1)
				.getBrickList().size();
		assertEquals("Incorrect number of bricks.", 3 + 1, UiTestUtils.getScriptListView(solo).getChildCount()); // don't forget the footer
		assertEquals("Incorrect number of bricks.", 2, childrenCount);

		ArrayList<Brick> projectBrickList = project.getSpriteList().get(0).getScript(0).getBrickList();
		assertEquals("Incorrect number of bricks.", 2, projectBrickList.size());
		assertTrue("Wrong Brick instance.", projectBrickList.get(0) instanceof BroadcastBrick);
		assertTrue("Wrong Brick instance.", adapter.getItem(1) instanceof BroadcastBrick);

		String testString = "test";
		String testString2 = "test2";
		String testString3 = "test3";

		String buttonNewBroadcastText = solo.getString(R.string.new_broadcast_message);
		solo.clickOnText(buttonNewBroadcastText, 1);
		solo.clearEditText(0);
		solo.enterText(0, testString);
		solo.sleep(200);
		solo.sendKey(Solo.ENTER);
		solo.sleep(400);
		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.sleep(300);
		solo.setActivityOrientation(Solo.PORTRAIT);
		solo.sleep(600);
		solo.clickOnText(solo.getString(R.string.brick_broadcast_receive)); //just to get focus for solo

		int firstBrickSpinnerIndex = 1;
		int secondBrickSpinnerIndex = firstBrickSpinnerIndex + 1;
		int thirdBrickSpinnerIndex = firstBrickSpinnerIndex + 2;

		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(firstBrickSpinnerIndex)
				.getSelectedItem());
		assertNotSame("Wrong selection", testString, solo.getCurrentSpinners().get(secondBrickSpinnerIndex)
				.getSelectedItem());

		solo.pressSpinnerItem(secondBrickSpinnerIndex, 2);
		solo.sleep(200);
		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(secondBrickSpinnerIndex)
				.getSelectedItem());

		solo.pressSpinnerItem(thirdBrickSpinnerIndex, 2);
		solo.sleep(200);
		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(thirdBrickSpinnerIndex)
				.getSelectedItem());

		solo.clickOnText(buttonNewBroadcastText, 2);
		solo.clearEditText(0);
		solo.enterText(0, testString2);
		solo.sleep(200);
		solo.sendKey(Solo.ENTER);
		solo.sleep(400);
		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.sleep(300);
		solo.setActivityOrientation(Solo.PORTRAIT);
		solo.sleep(600);
		solo.clickOnScreen(200, 200);

		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(firstBrickSpinnerIndex)
				.getSelectedItem());
		assertEquals("Wrong selection", testString2, (String) solo.getCurrentSpinners().get(secondBrickSpinnerIndex)
				.getSelectedItem());
		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(thirdBrickSpinnerIndex)
				.getSelectedItem());

		solo.clickOnText(buttonNewBroadcastText, 3);
		solo.clearEditText(0);
		solo.enterText(0, testString3);
		solo.sleep(200);
		solo.sendKey(Solo.ENTER);
		solo.sleep(400);
		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.sleep(300);
		solo.setActivityOrientation(Solo.PORTRAIT);
		solo.sleep(600);
		solo.clickOnScreen(200, 200);

		assertEquals("Wrong selection", testString, (String) solo.getCurrentSpinners().get(firstBrickSpinnerIndex)
				.getSelectedItem());
		assertEquals("Wrong selection", testString2, (String) solo.getCurrentSpinners().get(secondBrickSpinnerIndex)
				.getSelectedItem());
		assertEquals("Wrong selection", testString3, (String) solo.getCurrentSpinners().get(thirdBrickSpinnerIndex)
				.getSelectedItem());

		solo.pressSpinnerItem(secondBrickSpinnerIndex, 4);
		solo.sleep(200);
		assertEquals("Wrong selection", testString3, (String) solo.getCurrentSpinners().get(secondBrickSpinnerIndex)
				.getSelectedItem());
	}

	private void createProject() {
		project = new Project(null, UiTestUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite sprite = new Sprite("cat");
		Script script = new BroadcastScript(sprite);
		BroadcastBrick broadcastBrick = new BroadcastBrick(sprite);
		BroadcastWaitBrick broadcastWaitBrick = new BroadcastWaitBrick(sprite);
		script.addBrick(broadcastBrick);
		script.addBrick(broadcastWaitBrick);

		sprite.addScript(script);
		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}
}
