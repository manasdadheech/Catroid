package org.catrobat.catroid.test.physics;

import java.util.HashSet;
import java.util.Set;

import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.ChangeGhostEffectByNBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.IfOnEdgeBounceBrick;
import org.catrobat.catroid.content.bricks.SetBounceFactorBrick;
import org.catrobat.catroid.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.content.bricks.SetGravityBrick;
import org.catrobat.catroid.content.bricks.SetMassBrick;
import org.catrobat.catroid.content.bricks.SetPhysicObjectTypeBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.physics.PhysicBrickPreparator;
import org.catrobat.catroid.physics.PhysicObject;
import org.catrobat.catroid.physics.PhysicObject.Type;
import org.catrobat.catroid.physics.PhysicObjectBrick;
import org.catrobat.catroid.physics.PhysicWorld;
import org.catrobat.catroid.physics.PhysicWorldBrick;
import org.catrobat.catroid.test.utils.TestUtils;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicBrickPreparatorTest extends AndroidTestCase {
	static {
		GdxNativesLoader.load();
	}

	@Override
	public void setUp() {
	}

	@Override
	public void tearDown() {
	}

	public void testNoPhysicBricksToPrepare() {
		Project project = new Project();
		Sprite sprite = new Sprite("TestSprite");

		Script startScript = new StartScript(sprite);
		startScript.addBrick(new SetXBrick(sprite, 0));
		startScript.addBrick(new ChangeGhostEffectByNBrick(sprite, 5.0f));

		Script broadcastScript = new BroadcastScript(sprite);
		broadcastScript.addBrick(new HideBrick(sprite));
		broadcastScript.addBrick(new BroadcastBrick(sprite));

		Script whenScript = new WhenScript(sprite);
		whenScript.addBrick(new IfOnEdgeBounceBrick(sprite));

		sprite.addScript(startScript);
		sprite.addScript(broadcastScript);
		sprite.addScript(whenScript);
		project.addSprite(sprite);

		PhysicWorldMock physicWorldMock = new PhysicWorldMock();
		PhysicBrickPreparator physicBrickPreparator = new PhysicBrickPreparator(physicWorldMock);
		physicBrickPreparator.prepare(project);

		assertEquals(0, physicWorldMock.executedGetPhysicObjectCount);
	}

	public void testSimplePhysicObjectBrick() {
		Project project = new Project();
		Sprite sprite = new Sprite("TestSprite");
		Script script = new StartScript(sprite);
		PhysicObjectBrick physicObjectBrick = new SetMassBrick(sprite, PhysicObject.DEFAULT_MASS);
		script.addBrick(physicObjectBrick);
		sprite.addScript(script);
		project.addSprite(sprite);

		assertNull(TestUtils.getPrivateField("physicObject", physicObjectBrick, false));

		PhysicWorldMock physicWorldMock = new PhysicWorldMock();
		PhysicBrickPreparator physicBrickPreparator = new PhysicBrickPreparator(physicWorldMock);
		physicBrickPreparator.prepare(project);

		assertEquals(1, physicWorldMock.executedGetPhysicObjectCount);
		assertEquals(1, physicWorldMock.executedGetPhysicObjectSprites.size());
		assertTrue(physicWorldMock.executedGetPhysicObjectSprites.contains(sprite));

		assertNotNull(TestUtils.getPrivateField("physicObject", physicObjectBrick, false));
		assertEquals(physicWorldMock.getPhysicObject(sprite),
				TestUtils.getPrivateField("physicObject", physicObjectBrick, false));
	}

	public void testMultiplePhysicObjectBrick() {
		Project project = new Project();
		Sprite sprite = new Sprite("TestSprite");
		Script script = new StartScript(sprite);
		PhysicObjectBrick[] physicObjectBricks = { new SetMassBrick(sprite, PhysicObject.DEFAULT_MASS),
				new TurnRightSpeedBrick(sprite, 0.0f), new SetPhysicObjectTypeBrick(sprite, Type.DYNAMIC) };
		for (PhysicObjectBrick physicObjectBrick : physicObjectBricks) {
			script.addBrick(physicObjectBrick);
		}
		sprite.addScript(script);
		project.addSprite(sprite);

		for (PhysicObjectBrick physicObjectBrick : physicObjectBricks) {
			assertNull(TestUtils.getPrivateField("physicObject", physicObjectBrick, false));
		}

		PhysicWorldMock physicWorldMock = new PhysicWorldMock();
		PhysicBrickPreparator physicBrickPreparator = new PhysicBrickPreparator(physicWorldMock);
		physicBrickPreparator.prepare(project);

		assertEquals(1, physicWorldMock.executedGetPhysicObjectCount);
		assertEquals(1, physicWorldMock.executedGetPhysicObjectSprites.size());
		assertTrue(physicWorldMock.executedGetPhysicObjectSprites.contains(sprite));

		PhysicObject physicObject = physicWorldMock.getPhysicObject(sprite);
		for (PhysicObjectBrick physicObjectBrick : physicObjectBricks) {
			assertNotNull(TestUtils.getPrivateField("physicObject", physicObjectBrick, false));
			assertEquals(physicObject, TestUtils.getPrivateField("physicObject", physicObjectBrick, false));
		}
	}

	public void testSimplePhysicWorldBrick() {
		Project project = new Project();
		Sprite sprite = new Sprite("TestSprite");
		Script script = new StartScript(sprite);
		PhysicWorldBrick physicWorldBrick = new SetGravityBrick(sprite, PhysicWorld.DEFAULT_GRAVITY);
		script.addBrick(physicWorldBrick);
		sprite.addScript(script);
		project.addSprite(sprite);

		assertNull(TestUtils.getPrivateField("physicWorld", physicWorldBrick, false));

		PhysicWorldMock physicWorldMock = new PhysicWorldMock();
		PhysicBrickPreparator physicBrickPreparator = new PhysicBrickPreparator(physicWorldMock);
		physicBrickPreparator.prepare(project);

		assertNotNull(TestUtils.getPrivateField("physicWorld", physicWorldBrick, false));
		assertEquals(physicWorldMock, TestUtils.getPrivateField("physicWorld", physicWorldBrick, false));
	}

	public void testComplexPhysicBricks() {
		Vector2 gravity = new Vector2(1.2f, -3.4f);
		Project project = new Project();
		Sprite sprite = new Sprite("TestSprite");

		Script startScript = new StartScript(sprite);
		startScript.addBrick(new SetXBrick(sprite, 0));
		startScript.addBrick(new ChangeGhostEffectByNBrick(sprite, 5.0f));
		startScript.addBrick(new SetGravityBrick(sprite, PhysicWorld.DEFAULT_GRAVITY));

		Script broadcastScript = new BroadcastScript(sprite);
		broadcastScript.addBrick(new HideBrick(sprite));
		broadcastScript.addBrick(new BroadcastBrick(sprite));
		broadcastScript.addBrick(new SetGravityBrick(sprite, gravity));

		Script whenScript = new WhenScript(sprite);
		whenScript.addBrick(new IfOnEdgeBounceBrick(sprite));
		whenScript.addBrick(new SetPhysicObjectTypeBrick(sprite, Type.FIXED));
		whenScript.addBrick(new SetBounceFactorBrick(sprite, 1.0f));
		whenScript.addBrick(new SetFrictionBrick(sprite, 0.5f));

		sprite.addScript(startScript);
		sprite.addScript(broadcastScript);
		sprite.addScript(whenScript);
		project.addSprite(sprite);

		PhysicWorldMock physicWorldMock = new PhysicWorldMock();
		PhysicBrickPreparator physicBrickPreparator = new PhysicBrickPreparator(physicWorldMock);
		physicBrickPreparator.prepare(project);

		assertEquals(1, physicWorldMock.executedGetPhysicObjectCount);
		assertTrue(physicWorldMock.executedGetPhysicObjectSprites.contains(sprite));

		PhysicObject physicObject = physicWorldMock.getPhysicObject(sprite);
		for (int index = 0; index < sprite.getNumberOfScripts(); index++) {
			Script script = sprite.getScript(index);

			for (Brick brick : script.getBrickList()) {
				if (brick instanceof PhysicWorldBrick) {
					assertEquals(physicWorldMock, TestUtils.getPrivateField("physicWorld", brick, false));
				} else if (brick instanceof PhysicObjectBrick) {
					assertEquals(physicObject, TestUtils.getPrivateField("physicObject", brick, false));
				}
			}
		}
	}

	private class PhysicWorldMock extends PhysicWorld {
		private int executedGetPhysicObjectCount = 0;
		private Set<Sprite> executedGetPhysicObjectSprites = new HashSet<Sprite>();

		@Override
		public PhysicObject getPhysicObject(Sprite sprite) {
			PhysicObject physicObject = super.getPhysicObject(sprite);

			executedGetPhysicObjectCount++;
			executedGetPhysicObjectSprites.add(sprite);

			return physicObject;
		}
	}
}
