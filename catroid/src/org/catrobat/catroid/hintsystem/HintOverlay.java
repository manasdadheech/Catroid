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
package org.catrobat.catroid.hintsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author peter
 * 
 */
public class HintOverlay extends SurfaceView implements SurfaceHolder.Callback {
	private Context context;
	List<Object> surfaceObjects;
	private Paint paint = new Paint();
	int alpha = 255;
	private Resources res;
	private Bitmap bitmap;

	public HintOverlay(Context context) {
		super(context);
		this.context = context;
		this.setBackgroundColor(Color.BLACK);
		this.getBackground().setAlpha(100);
		this.setZOrderOnTop(true); //necessary 
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		getHolder().addCallback(this);
		surfaceObjects = Collections.synchronizedList(new ArrayList<Object>());
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		res = context.getResources();
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		//bitmap = BitmapFactory.decodeResource(res, R.drawable., opts);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Hint.getInstance().removeHint();
		return true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 100, 100, paint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		getHolder().removeCallback(this);

	}

}
