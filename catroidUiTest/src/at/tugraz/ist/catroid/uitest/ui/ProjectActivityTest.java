/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.uitest.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.constructionSite.content.ProjectManager;
import at.tugraz.ist.catroid.content.sprite.Sprite;
import at.tugraz.ist.catroid.ui.MainMenuActivity;
import at.tugraz.ist.catroid.uitest.util.UiTestUtils;

import com.jayway.android.robotium.solo.Solo;

public class ProjectActivityTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private Solo solo;
	private static final String TEST_PROJECT_NAME = "projectActivityTest";

	public ProjectActivityTest() {
		super("at.tugraz.ist.catroid.ui", MainMenuActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());

		// Create new test project
		solo.clickOnButton(getActivity().getString(R.string.new_project));
		UiTestUtils.enterText(solo, 0, TEST_PROJECT_NAME);

		solo.clickOnButton(getActivity().getString(R.string.new_project_dialog_create_button));
	}

	@Override
	public void tearDown() throws Exception {
		ProjectManager.getInstance().deleteCurrentProject(getActivity());

		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		getActivity().finish();
		super.tearDown();
	}

	private void addNewSprite(String spriteName, boolean doGoBack) throws InterruptedException {
		UiTestUtils.pause();
		solo.clickOnButton(getActivity().getString(R.string.add_sprite));
		Thread.sleep(50);
		UiTestUtils.enterText(solo, 0, spriteName);

		solo.clickOnButton(getActivity().getString(R.string.new_sprite_dialog_button));
		Thread.sleep(50);
	}

	public void testAddNewSprite() throws InterruptedException {
		final String spriteName = "testSprite";
		addNewSprite(spriteName, true);

		ListView spritesList = (ListView) solo.getCurrentActivity().findViewById(R.id.spriteListView);
		Sprite secondSprite = (Sprite) spritesList.getItemAtPosition(1);
		assertEquals("Sprite at index 1 is not " + spriteName, spriteName, secondSprite.getName());
		assertTrue("Sprite is not in current Project", ProjectManager.getInstance().getCurrentProject().getSpriteList()
				.contains(secondSprite));

		final String spriteName2 = "anotherTestSprite";
		addNewSprite(spriteName2, false);
		spritesList = (ListView) solo.getCurrentActivity().findViewById(R.id.spriteListView);
		Sprite thirdSprite = (Sprite) spritesList.getItemAtPosition(2);
		assertEquals("Sprite at index 2 is not " + spriteName2, spriteName2, thirdSprite.getName());
		assertTrue("Sprite is not in current Project", ProjectManager.getInstance().getCurrentProject().getSpriteList()
				.contains(thirdSprite));
	}

	public void testAddNewSpriteErrors() throws InterruptedException {
		addNewSprite("", true);
		assertTrue("No error message was displayed upon creating a sprite with an empty name.",
				solo.searchText(getActivity().getString(R.string.error_no_name_entered)));
		solo.clickOnButton(0);
		solo.goBack();

		final String spriteName = "testSprite";
		addNewSprite(spriteName, false);
		addNewSprite(spriteName, false);

		assertTrue("No error message was displayed upon creating a sprite with the same name twice.",
				solo.searchText(getActivity().getString(R.string.spritename_already_exists)));
	}

	public void testContextMenu() throws InterruptedException {
		// Create sprites manually so we're able to check for equality
		final String spriteName = "foo";
		final String spriteName2 = "bar";
		Sprite testSprite = new Sprite(spriteName);
		ProjectManager.getInstance().getCurrentProject().addSprite(testSprite);
		Sprite testSprite2 = new Sprite(spriteName2);
		ProjectManager.getInstance().getCurrentProject().addSprite(testSprite2);

		Thread.sleep(500);

		// Rename sprite
		final String newSpriteName = "baz";
		solo.clickLongOnText(spriteName);
		solo.clickOnText(getActivity().getString(R.string.rename));
		Thread.sleep(50);

		UiTestUtils.enterText(solo, 0, newSpriteName);
		solo.clickOnButton(getActivity().getString(R.string.rename_button));
		Thread.sleep(50);

		ListView spritesList = (ListView) solo.getCurrentActivity().findViewById(R.id.spriteListView);
		Sprite sprite = (Sprite) spritesList.getItemAtPosition(1);
		assertEquals("Sprite on position one is not the same that was created", testSprite, sprite);
		assertEquals("Sprite on position wasn't renamed correctly", newSpriteName, sprite.getName());

		// Delete sprite
		solo.clickLongOnText(newSpriteName);
		solo.clickOnText(getActivity().getString(R.string.delete));
		UiTestUtils.pause();
		assertFalse("Sprite is still in Project", ProjectManager.getInstance().getCurrentProject().getSpriteList()
				.contains(sprite));
		assertFalse("Sprite is still in Project", solo.searchText(newSpriteName));

		spritesList = (ListView) solo.getCurrentActivity().findViewById(R.id.spriteListView);
		Sprite sprite2 = (Sprite) spritesList.getItemAtPosition(1);
		assertEquals("Subsequent sprite was not moved up after predecessor's deletion", testSprite2, sprite2);
	}

	public void testMainMenuButton() throws InterruptedException {
		solo.clickOnButton(getActivity().getString(R.string.main_menu));
		Thread.sleep(50);
		assertTrue("Main menu is not visible", solo.searchText(getActivity().getString(R.string.main_menu)));
		assertTrue("Current project is not visible", solo.searchText(getActivity().getString(R.string.current_project)));
	}
}
