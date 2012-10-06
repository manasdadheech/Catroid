package org.catrobat.catroid.uitest.content.brick;

import java.util.ArrayList;

import org.catrobat.catroid.content.bricks.TurnLeftSpeedBrick;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.ScriptTabActivity;
import org.catrobat.catroid.ui.adapter.BrickAdapter;
import org.catrobat.catroid.ui.fragment.ScriptFragment;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import com.jayway.android.robotium.solo.Solo;

public class LeftRotationSpeedBrickTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private Solo solo;
	private Project project;
	private TurnLeftSpeedBrick setAngularVelocityBrick;

	public LeftRotationSpeedBrickTest() {
		super(MainMenuActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		createProject();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		getActivity().finish();
		super.tearDown();
	}

	@Smoke
	public void testSetAngularVelocityBrick() {
		ScriptTabActivity activity = (ScriptTabActivity) solo.getCurrentActivity();
		ScriptFragment fragment = (ScriptFragment) activity.getTabFragment(ScriptTabActivity.INDEX_TAB_SCRIPTS);
		BrickAdapter adapter = fragment.getAdapter();

		int childrenCount = adapter.getChildCountFromLastGroup();
		int groupCount = adapter.getScriptCount();

		assertEquals("Incorrect number of bricks.", 2 + 1, solo.getCurrentListViews().get(0).getChildCount()); // don't forget the footer
		assertEquals("Incorrect number of bricks.", 1, childrenCount);

		ArrayList<Brick> projectBrickList = project.getSpriteList().get(0).getScript(0).getBrickList();
		assertEquals("Incorrect number of bricks.", 1, projectBrickList.size());

		assertEquals("Wrong Brick instance.", projectBrickList.get(0), adapter.getChild(groupCount - 1, 0));
		String textSetRotationSpeed = solo.getString(R.string.brick_turn_left_speed);
		assertNotNull("TextView does not exist.", solo.getText(textSetRotationSpeed));

		float angularVelocity = 10.0f;

		UiTestUtils.clickEnterClose(solo, 0, Float.toString(angularVelocity));
		float actualAngularVelocity = (Float) UiTestUtils.getPrivateField("degreesPerSec", setAngularVelocityBrick);
		assertEquals("Text not updated", Float.toString(angularVelocity), solo.getEditText(0).getText().toString());
		assertEquals("Value in Brick is not updated", angularVelocity, actualAngularVelocity);
	}

	public void testResizeInputField() {
		for (int editTextIndex = 0; editTextIndex < 1; editTextIndex++) {
			UiTestUtils.testDoubleEditText(solo, editTextIndex, 123456789.0, 50, false);
			UiTestUtils.testDoubleEditText(solo, editTextIndex, 1.0, 50, true);
			UiTestUtils.testDoubleEditText(solo, editTextIndex, 123.0, 50, true);
			UiTestUtils.testDoubleEditText(solo, editTextIndex, -1, 50, true);
		}
	}

	private void createProject() {
		project = new Project(null, "testProject");
		Sprite sprite = new Sprite("cat");
		Script script = new StartScript(sprite);
		setAngularVelocityBrick = new TurnLeftSpeedBrick(null, sprite, 0.0f);
		script.addBrick(setAngularVelocityBrick);

		sprite.addScript(script);
		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}

}
