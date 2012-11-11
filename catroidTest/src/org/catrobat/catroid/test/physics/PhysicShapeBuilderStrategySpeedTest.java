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
		//{ new PhysicShapeBuilderStrategyRectangle(),
		//	, new PhysicShapeBuilderStrategyComplex(), new PhysicShapeBuilderStrategyTest() };

		CostumeData costumeData = new CostumeDataMock(R.drawable.catroid);
		for (PhysicShapeBuilderStrategy strategy : strategies) {
			long start = System.currentTimeMillis();
			Shape[] shapes = strategy.build(costumeData);
			System.out.println("LOG: " + strategy.getClass().getSimpleName() + " @ "
					+ (System.currentTimeMillis() - start));
		}
	}

	private class CostumeDataMock extends CostumeData {
		private static final long serialVersionUID = 1L;
		private final Pixmap pixmap;

		public CostumeDataMock(int drawableId) {
			pixmap = new PixmapMock(drawableId);
		}

		@Override
		public Pixmap getPixmap() {
			return pixmap;
		}

		@Override
		public int[] getResolution() {
			return new int[] { pixmap.getWidth(), pixmap.getHeight() };
		}

		private class PixmapMock extends Pixmap {
			private final Bitmap image;

			public PixmapMock(int drawableId) {
				super(0, 0, Format.Alpha);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inScaled = false;
				image = BitmapFactory.decodeResource(getActivity().getResources(), drawableId, options);
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

		private class PixmapArrayMock extends Pixmap {
			private int[][] image;

			public PixmapArrayMock() {
				super(0, 0, Format.Alpha);

				int alpha = 0xff;
				String[] asciImage = { "        ", "   XXX  ", "  XXXXXX", "    X   ", "        ", " XXXXX" };
				image = new int[asciImage.length][asciImage[0].length()];

				for (int y = 0; y < asciImage.length; y++) {
					char[] line = asciImage[y].toCharArray();
					for (int x = 0; x < line.length; x++) {
						image[y][x] = (line[x] == 'X') ? alpha : 0;
					}
				}
			}

			@Override
			public int getWidth() {
				return image[0].length;
			}

			@Override
			public int getHeight() {
				return image.length;
			}

			@Override
			public int getPixel(int x, int y) {
				return image[y][x];
			}
		}
	}
}
