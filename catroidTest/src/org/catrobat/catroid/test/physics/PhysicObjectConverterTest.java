package org.catrobat.catroid.test.physics;

import java.util.List;
import java.util.Map;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.content.bricks.SetGravityBrick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicObjectBrick;
import org.catrobat.catroid.physics.PhysicObjectConverter;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.physics.PhysicWorldBrick;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicObjectConverterTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	@Override
	public void setUp() {
	}

	@Override
	public void tearDown() {
	}

	public void testConvert() {
		Project project = createTestProject();

		PhysicWorld physicWorld = new PhysicWorld();
		PhysicObjectConverter physicObjectConverter = new PhysicObjectConverter(physicWorld);
		physicObjectConverter.convert(project);

		List<Sprite> spriteList = project.getSpriteList();
		@SuppressWarnings("unchecked")
		Map<Sprite, PhysicObject> physicObjects = (Map<Sprite, PhysicObject>) TestUtils.getPrivateField(
				"physicObjects", physicWorld, false);

		assertEquals(3, spriteList.size());
		assertEquals(2, physicObjects.size());

		Sprite spriteWithPhysicWorldBrick = spriteList.get(0);
		PhysicWorldBrick physicWorldBrick = (PhysicWorldBrick) spriteWithPhysicWorldBrick.getScript(0).getBrick(0);
		PhysicWorld physicWorldOfSprite = (PhysicWorld) TestUtils.getPrivateField("physicWorld", physicWorldBrick,
				false);
		assertEquals(physicWorld, physicWorldOfSprite);

		Sprite spriteWithPhysicObjectBrick = spriteList.get(1);
		PhysicObjectBrick physicObjectBrick = (PhysicObjectBrick) spriteWithPhysicObjectBrick.getScript(0).getBrick(0);
		PhysicObject physicObject = (PhysicObject) TestUtils.getPrivateField("physicObject", physicObjectBrick, false);
		assertEquals(physicObject, physicObjects.get(spriteWithPhysicObjectBrick));

		Sprite SpriteWithPhysicObjectBrickInSecondScript = spriteList.get(2);
		PhysicObjectBrick physicObjectBrickInSecondScript = (PhysicObjectBrick) SpriteWithPhysicObjectBrickInSecondScript
				.getScript(1).getBrick(0);
		PhysicObject physicObject2 = (PhysicObject) TestUtils.getPrivateField("physicObject",
				physicObjectBrickInSecondScript, false);
		assertEquals(physicObject2, physicObjects.get(SpriteWithPhysicObjectBrickInSecondScript));
	}

	private Project createTestProject() {
		Project project = new Project(null, null);

		Sprite sprite = new Sprite("SpriteWithPhysicWorldBrick");
		Script script = new StartScript(sprite);
		script.addBrick(new SetGravityBrick(sprite, PhysicWorld.DEFAULT_GRAVITY));
		sprite.addScript(script);
		project.addSprite(sprite);

		sprite = new Sprite("SpriteWithPhysicObjectBrick");
		script = new StartScript(sprite);
		script.addBrick(new SetMassBrick(sprite, PhysicObject.DEFAULT_MASS));
		sprite.addScript(script);
		project.addSprite(sprite);

		sprite = new Sprite("SpriteWithPhysicObjectBrickInSecondScript");

		script = new StartScript(sprite);
		script.addBrick(new SetXBrick(sprite, 0));
		sprite.addScript(script);

		script = new BroadcastScript(sprite);
		script.addBrick(new SetFrictionBrick(sprite, PhysicObject.DEFAULT_FRICTION));
		sprite.addScript(script);

		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);

		return project;
	}
}
