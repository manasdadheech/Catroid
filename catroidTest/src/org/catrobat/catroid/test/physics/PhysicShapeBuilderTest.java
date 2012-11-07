package org.catrobat.catroid.test.physics;

import java.io.File;
import java.io.IOException;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.content.Costume;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.physics.PhysicShapeBuilder;
import org.catrobat.catroid.physics.PhysicShapeBuilderStrategyRectangle;
import org.catrobat.catroid.physics.PhysicWorldConverter;
import org.catrobat.catroid.test.R;
import org.catrobat.catroid.test.utils.TestUtils;
import org.catrobat.catroid.utils.UtilFile;

import android.test.AndroidTestCase;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicShapeBuilderTest extends AndroidTestCase {
	// TODO: Refactor test.
	private static final int IMAGE_FILE_ID = R.raw.icon;

	private PhysicShapeBuilder builder;
	private final String projectName = "testProject";
	private File testImage;
	private CostumeData costumeData;
	private Costume costume;

	@Override
	public void setUp() throws Exception {

		createProject();

		costumeData = new CostumeData();
		costumeData.setCostumeFilename(testImage.getName());
		costumeData.setCostumeName("CostumeName");

		Sprite sprite = new Sprite("");
		costume = new Costume(sprite);
		costume.setCostumeData(costumeData);
		builder = new PhysicShapeBuilder(new PhysicShapeBuilderStrategyRectangle());
	}

	@Override
	protected void tearDown() throws Exception {
		File projectFile = new File(Constants.DEFAULT_ROOT + "/" + projectName);

		if (projectFile.exists()) {
			UtilFile.deleteDirectory(projectFile);
		}
		if (testImage != null && testImage.exists()) {
			testImage.delete();
		}
	}

	public void testRectangle() {

		Shape[] shapes = builder.getShape(costume.getCostumeData(), 1.0f);
		assertEquals(1, shapes.length);
		PolygonShape polyShape = (PolygonShape) shapes[0];//[19/67]

		float[][] points = getRectPointsOfImage();

		Vector2 tempVertex = new Vector2();
		for (int i = 0; i < polyShape.getVertexCount(); i++) {
			polyShape.getVertex(i, tempVertex);
			float x = tempVertex.x;
			float y = tempVertex.y;
			assertEquals(points[i][0], x);
			assertEquals(points[i][1], y);
		}
	}

	public void testRectangle2() {
		float scale = 0.5f;
		Shape[] shapes = builder.getShape(costume.getCostumeData(), scale);
		assertEquals(1, shapes.length);
		PolygonShape polyShape = (PolygonShape) shapes[0];//[19/67]

		float[][] points = getRectPointsOfImage();

		Vector2 tempVertex = new Vector2();
		for (int i = 0; i < polyShape.getVertexCount(); i++) {
			polyShape.getVertex(i, tempVertex);
			float x = tempVertex.x;
			float y = tempVertex.y;
			assertEquals(points[i][0] * scale, x);
			assertEquals(points[i][1] * scale, y);
		}
	}

	public void testRectangle3() {
		Shape[] shapes1 = builder.getShape(costume.getCostumeData(), 0.6f);
		Shape[] shapes2 = builder.getShape(costume.getCostumeData(), 0.6f);
		assertEquals(shapes1, shapes2);
	}

	private float[][] getRectPointsOfImage() {
		int image_x = costume.getCostumeData().getResolution()[0];
		int image_y = costume.getCostumeData().getResolution()[1];
		float image_x_box2d = PhysicWorldConverter.lengthCatToBox2d(image_x);
		float image_y_box2d = PhysicWorldConverter.lengthCatToBox2d(image_y);
		float image_x_box2d_coord = image_x_box2d / 2;
		float image_y_box2d_coord = image_y_box2d / 2;
		float[][] points = new float[4][2];

		points[0][0] = -image_x_box2d_coord;
		points[0][1] = -image_y_box2d_coord;

		points[1][0] = +image_x_box2d_coord;
		points[1][1] = -image_y_box2d_coord;

		points[2][0] = +image_x_box2d_coord;
		points[2][1] = +image_y_box2d_coord;

		points[3][0] = -image_x_box2d_coord;
		points[3][1] = +image_y_box2d_coord;

		return points;

	}

	private void createProject() throws IOException {
		File projectFile = new File(Constants.DEFAULT_ROOT + "/" + projectName);

		if (projectFile.exists()) {
			UtilFile.deleteDirectory(projectFile);
		}

		Project project = new Project(getContext(), projectName);
		StorageHandler.getInstance().saveProject(project);
		ProjectManager.getInstance().setProject(project);

		testImage = TestUtils.saveFileToProject(this.projectName, "testImage.png", IMAGE_FILE_ID, getContext(),
				TestUtils.TYPE_IMAGE_FILE);

	}

}
