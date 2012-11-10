package org.catrobat.catroid.test.physics;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.CostumeData;
import org.catrobat.catroid.physics.PhysicShapeBuilderStrategy;
import org.catrobat.catroid.physics.PhysicShapeBuilderStrategyRectangle;
import org.catrobat.catroid.ui.MainMenuActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PhysicShapeBuilderStrategySpeedTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	static {
		GdxNativesLoader.load();
	}

	public PhysicShapeBuilderStrategySpeedTest() {
		super(MainMenuActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStrategies() {
		PhysicShapeBuilderStrategy[] strategies = { new PhysicShapeBuilderStrategyRectangle() };
		//		, new PhysicShapeBuilderStrategyComplex(), new PhysicShapeBuilderStrategyTest() };

		CostumeData costumeData = new CostumeDataMock(R.drawable.catroid);
		for (PhysicShapeBuilderStrategy strategy : strategies) {
			long start = System.currentTimeMillis();
			Shape[] shapes = strategy.build(costumeData);
			System.out.println("LOG: " + strategy.getClass().getSimpleName() + "@"
					+ (System.currentTimeMillis() - start));
			checkConvexity(shapes);
		}
	}

	private void checkConvexity(Shape[] shapes) {
		// TODO: Implement
	}

	private long getOperatingTime(PhysicShapeBuilderStrategy strategy, CostumeData costumeData) {
		long start = System.currentTimeMillis();
		strategy.build(costumeData);
		return System.currentTimeMillis() - start;
	}

	private class CostumeDataMock extends CostumeData {
		private static final long serialVersionUID = 1L;
		private final Bitmap image;
		private final Pixmap pixmap;

		public CostumeDataMock(int drawableId) {
			image = BitmapFactory.decodeResource(getActivity().getResources(), drawableId);
			pixmap = new PixmapMock();

		}

		@Override
		public Pixmap getPixmap() {
			return pixmap;
		}

		@Override
		public int[] getResolution() {
			return new int[] { image.getWidth(), image.getHeight() };
		}

		private class PixmapMock extends Pixmap {
			public PixmapMock() {
				super(0, 0, Format.Alpha);
			}

			@Override
			public int getWidth() {
				return image.getWidth();
			}

			@Override
			public int getHeight() {
				return image.getHeight();
			}

			@Override
			public int getPixel(int x, int y) {
				return image.getPixel(x, y);
			}
		}
	}
}
