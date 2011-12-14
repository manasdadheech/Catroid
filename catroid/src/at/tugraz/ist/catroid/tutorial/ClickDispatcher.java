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
package at.tugraz.ist.catroid.tutorial;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.tutorial.tasks.Task;

/**
 * @author gnu
 * 
 */
public class ClickDispatcher {

	Context context;
	ControlPanel panel;
	Task.Notification currentNotification;
	String notificationValue;
	MotionEvent down;
	MotionEvent second;
	boolean scroll;
	boolean scroll2;
	int itemPosition = 0;

	ClickDispatcher(Context context, ControlPanel panel) {
		this.panel = panel;
		this.context = context;
		scroll = false;
		scroll2 = false;
	}

	public void setCurrentNotification(Task.Notification currentNotification, String notificationValue) {
		this.currentNotification = currentNotification;
		this.notificationValue = notificationValue;
		if (currentNotification == Task.Notification.IF_PROJECT_STARTED || currentNotification == Task.Notification.IF
				|| currentNotification == Task.Notification.WAIT_SECONDS
				|| currentNotification == Task.Notification.REPEAT
				|| currentNotification == Task.Notification.REPEAT_TIMES) {
			Log.i("faxxe", "scrolling to right position!");
			scrollDialogToPosition(currentNotification);
		}
	}

	public void scrollDialogToPosition(Task.Notification currentNotification) {
		Dialog dialog = Tutorial.getInstance(null).getDialog();
		ListView lv = (ListView) dialog.findViewById(R.id.toolboxListView);

		switch (currentNotification) {
			case IF_PROJECT_STARTED:
				itemPosition = 0;
				break;
			case IF:
				itemPosition = 1;
				break;
			case WAIT_SECONDS:
				itemPosition = 2;
				break;
			case REPEAT:
				itemPosition = 7;
				break;
			case REPEAT_TIMES:
				itemPosition = 8;
				break;
			case SET_COSTUME:
				itemPosition = 0;
				break;
			default:
				itemPosition = 0;
		}
		lv.smoothScrollToPosition(itemPosition);
	}

	public void dispatchEvent(MotionEvent ev) {
		dispatchEventReally(ev);
		return;

		//		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
		//			down = MotionEvent.obtain(ev);
		//		}
		//		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
		//			if (scroll2 == false) {
		//				second = MotionEvent.obtain(ev);
		//				scroll2 = true;
		//				return;
		//			}
		//			if (scroll == false) {
		//				scroll = true;
		//				dispatchEventReally(down);
		//				dispatchEventReally(second);
		//				dispatchEventReally(ev);
		//				Log.i("faxxe", "moove!");
		//			} else {
		//				Log.i("faxxe", "moove!");
		//				dispatchEventReally(ev);
		//			}
		//		}
		//		if (ev.getAction() == MotionEvent.ACTION_UP) {
		//			if (scroll == false) {
		//				dispatchEventReally(down);
		//			}
		//			dispatchEventReally(ev);
		//			scroll = false;
		//			scroll2 = false;
		//		}

	}

	public void dispatchEventReally(MotionEvent ev) {

		if (currentNotification != null) {
			Log.i("faxxe", ".currentNotification: " + currentNotification.toString());
		}
		//Todo:
		/*
		 * 1. Notification fuern dialog einrichten und obfrogen
		 * 2. schaun wie des mit die Items im dialog is...
		 * 3. schaun ob akuteller dialog mit dem this.dialog zompasst
		 * 4. so mochn dos eh tuat ')
		 */

		if (scroll == true) {
			Activity currentActivity = (Activity) context;
			currentActivity.dispatchTouchEvent(ev);
			return;
		}

		int x = (int) ev.getX();
		int y = (int) ev.getY();

		if (panel != null) {
			Rect bounds = panel.getPanelBounds();
			//check if event coordinates are the coordinates of the control panel buttons
			if (x >= bounds.left && x <= bounds.right) {
				if (y >= bounds.top && y <= bounds.bottom) {
					dispatchPanel(ev);
					return;
				}
			}
		}

		if (currentNotification == null) {
			return;
		}

		if (currentNotification == Task.Notification.CURRENT_PROJECT_BUTTON) {
			if (context.getClass().getName().compareTo("at.tugraz.ist.catroid.ui.MainMenuActivity") != 0) {
				// TODO: This should never ever happen, something went terribly wrong: how to deal with this?
				return;
			}

			dispatchMainMenu(ev);
			return;
		}

		if (currentNotification == Task.Notification.PROJECT_ADD_SPRITE
				|| currentNotification == Task.Notification.PROJECT_HOME_BUTTON
				|| currentNotification == Task.Notification.PROJECT_LIST_ITEM) {
			if (context.getClass().getName().compareTo("at.tugraz.ist.catroid.ui.ProjectActivity") != 0) {
				// TODO: This should never ever happen, something went terribly wrong: how to deal with this?
				return;
			}
			dispatchProject(ev);
			return;
		}
		if (currentNotification == Task.Notification.PROJECT_STAGE_BUTTON) {
			dispatchProjectStageButton(ev);
			return;
		}

		if (currentNotification == Task.Notification.SCRIPTS_ADD_BRICK
				|| currentNotification == Task.Notification.TAB_COSTUMES
				|| currentNotification == Task.Notification.TAB_SOUNDS) {
			if (context.getClass().getName().compareTo("at.tugraz.ist.catroid.ui.ScriptActivity") != 0) {
				// TODO: This should never ever happen, something went terribly wrong: how to deal with this?
			}
			dispatchSkript(ev);
			return;
		}

		if (currentNotification == Task.Notification.COSTUMES_ADD_COSTUME
				|| currentNotification == Task.Notification.COSTUMES_COPY
				|| currentNotification == Task.Notification.COSTUMES_DELETE
				|| currentNotification == Task.Notification.COSTUMES_PAINTROID
				|| currentNotification == Task.Notification.COSTUMES_RENAME) {
			if (context.getClass().getName().compareTo("at.tugraz.ist.catroid.ui.CostumeActivity") != 0) {
				// TODO: This should never ever happen, something went terribly wrong: how to deal with this?
			}
			dispatchCostumes(ev);
			return;
		}
		if (currentNotification == Task.Notification.SOUNDS_ADD_SOUND) {
			dispatchSounds(ev);
		}

		if (currentNotification == Task.Notification.IF_PROJECT_STARTED || currentNotification == Task.Notification.IF
				|| currentNotification == Task.Notification.WAIT_SECONDS
				|| currentNotification == Task.Notification.REPEAT
				|| currentNotification == Task.Notification.REPEAT_TIMES
				|| currentNotification == Task.Notification.SET_COSTUME) {
			dispatchAddBrickDialog(ev);
			return;
		}

		if (currentNotification == Task.Notification.BRICK_CATEGORY_CONTROL
				|| currentNotification == Task.Notification.BRICK_CATEGORY_MOTION
				|| currentNotification == Task.Notification.BRICK_CATEGORY_SOUND
				|| currentNotification == Task.Notification.BRICK_CATEGORY_LOOKS) {
			Log.i("faxxe", "dispatching brickCategoryDialog " + currentNotification.toString());
			dispatchBrickCategoryDialog(ev);
			return;
		}
		return;
	}

	public void dispatchAddBrickDialog(MotionEvent ev) {
		Log.i("faxxe", "Lasset die Spiele beginnen! -- AddBrickDialog");
		Dialog dialog = Tutorial.getInstance(null).getDialog();
		ListView lv = (ListView) dialog.findViewById(R.id.toolboxListView);
		if (lv == null) {
			Log.i("faxxe", "listview von AddBrickDialog == null --> ERROR");
			return;
		}
		//		lv.smoothScrollToPosition(8);

		if (isItemClicked(lv.getChildAt(itemPosition), ev)) {
			dialog.dispatchTouchEvent(ev);
		}
	}

	public void dispatchBrickCategoryDialog(MotionEvent ev) {
		Dialog dialog = Tutorial.getInstance(null).getDialog();
		if (dialog == null) {
			Log.i("faxxe", "dialog==null --- ERROR");
			//Massive Failure -- should never happen
			return;
		}
		if (dialog.isShowing() == false) {
			Log.i("faxxe", "dialog not showing --- ERROR");
			//There is something very wrong
			return;
		}
		ListView lv = (ListView) dialog.findViewById(R.id.categoriesListView);
		if (lv == null) {
			Log.i("faxxe", "listview==null!");
		}
		switch (currentNotification) {
			case BRICK_CATEGORY_CONTROL:
				if (isItemClicked(lv.getChildAt(3), ev)) {
					Log.i("faxxe", "brick_category_control");
					dialog.dispatchTouchEvent(ev);
				}
				break;
			case BRICK_CATEGORY_LOOKS:
				if (isItemClicked(lv.getChildAt(1), ev)) {
					Log.i("faxxe", "brick_category_looks");
					dialog.dispatchTouchEvent(ev);
				}
				break;
			case BRICK_CATEGORY_MOTION:
				if (isItemClicked(lv.getChildAt(0), ev)) {
					Log.i("faxxe", "brick_category_motion");
					dialog.dispatchTouchEvent(ev);
				}
				break;
			case BRICK_CATEGORY_SOUND:
				if (isItemClicked(lv.getChildAt(2), ev)) {
					Log.i("faxxe", "brick_category_sound");
					dialog.dispatchTouchEvent(ev);
				}
				break;
			default:
				Log.i("faxxe", "brickCategoryDialogSwitchDefaultBlock");
		}
	}

	public void dispatchPanel(MotionEvent ev) {
		//unterscheide buttons play, pause, forward, backward
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		Rect bounds = panel.getPanelBounds();

		//		check if event coordinates are the coordinates of the control panel buttons
		if (x >= bounds.left && x <= bounds.left + 50) {
			if (y >= bounds.top && y <= bounds.bottom) {
				Tutorial.getInstance(null).resumeTutorial();
				//panel.pressPlay();
			}
		}
		bounds.left = bounds.left + 70;

		if (x >= bounds.left && x <= bounds.left + 50) {
			if (y >= bounds.top && y <= bounds.bottom) {
				Tutorial.getInstance(null).stopButtonTutorial();
				//				Toast toast = Toast.makeText(context, "PAUSE", Toast.LENGTH_SHORT);
				//				toast.show();
			}
		}

		bounds.left = bounds.left + 70;

		if (x >= bounds.left && x <= bounds.left + 50) {
			if (y >= bounds.top && y <= bounds.bottom) {
				Toast toast = Toast.makeText(context, "FORWARD", Toast.LENGTH_SHORT);
				toast.show();
			}
		}

		bounds.left = bounds.left + 70;

		if (x >= bounds.left && x <= bounds.left + 50) {
			if (y >= bounds.top && y <= bounds.bottom) {
				Toast toast = Toast.makeText(context, "BACKWARD", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	public boolean isItemClicked(View view, MotionEvent ev) {
		Log.i("faxxe", "in: isItemClicked");
		float x = ev.getX();
		float y = ev.getY();
		if (view == null) {
			Log.i("faxxe", "ERROR: view==null");
			return false;
		}
		float leftX = getRelativeLeft(view);
		float topY = getRelativeTop(view);
		float rightX = leftX + view.getWidth();
		float bottomY = topY + view.getHeight();
		if (x > leftX && x < rightX && y > topY && y < bottomY) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScriptsButton(int leftX, int rightX, int topY, int bottomY, MotionEvent ev) {
		int tabHeigth = topY - bottomY;
		int tabWidth = rightX - leftX;
		if ((ev.getX() > leftX) && (ev.getX() < leftX + (tabWidth / 3)) && (ev.getY() > topY)
				&& (ev.getY() < topY + tabHeigth)) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean isCostumesButton(int leftX, int rightX, int topY, int bottomY, MotionEvent ev) {
		int tabHeigth = topY - bottomY;
		int tabWidth = rightX - leftX;
		if ((ev.getX() > leftX + (tabWidth / 3)) && (ev.getX() < leftX + ((tabWidth / 3) * 2)) && (ev.getY() > topY)
				&& (ev.getY() < topY + tabHeigth)) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean isSoundsButton(int leftX, int rightX, int topY, int bottomY, MotionEvent ev) {
		int tabHeigth = topY - bottomY;
		int tabWidth = rightX - leftX;
		if ((ev.getX() > leftX + ((tabWidth / 3) * 2)) && (ev.getX() < leftX + tabWidth) && (ev.getY() > topY)
				&& (ev.getY() < topY + tabHeigth)) {
			return (true);
		} else {
			return (false);
		}
	}

	public void dispatchSkript(MotionEvent ev) {
		Activity currentActivity = (Activity) context;
		Log.i("faxxe", "current context is from: " + currentActivity.getLocalClassName());
		if (currentActivity.getLocalClassName().compareTo("ui.ScriptActivity") == 0) {
			currentActivity = currentActivity.getParent();
		}
		if (currentActivity.getLocalClassName().compareTo("ui.CostumeActivity") == 0) {
			currentActivity = currentActivity.getParent();
		}
		if (currentActivity.getLocalClassName().compareTo("ui.SoundActivity") == 0) {
			currentActivity = currentActivity.getParent();
		}
		ImageButton addSpriteButton = (ImageButton) currentActivity.findViewById(R.id.btn_action_add_sprite);

		TabHost tabHost = (TabHost) currentActivity.findViewById(android.R.id.tabhost);

		ArrayList<View> tabViews = tabHost.getTouchables();
		LinearLayout tab1 = (LinearLayout) tabViews.get(0);
		LinearLayout tab2 = (LinearLayout) tabViews.get(1);
		LinearLayout tab3 = (LinearLayout) tabViews.get(2);

		if ((currentNotification == Task.Notification.TAB_SCRIPTS) && isLinearLayoutClicked(tab1, ev)) {
			tabHost.setCurrentTab(0);
		}
		if ((currentNotification == Task.Notification.TAB_SOUNDS) && isLinearLayoutClicked(tab3, ev)) {
			tabHost.setCurrentTab(2);
		}
		if ((currentNotification == Task.Notification.TAB_COSTUMES) && isLinearLayoutClicked(tab2, ev)) {
			tabHost.setCurrentTab(1);
		}
		if (isImageButtonClicked(ev, addSpriteButton) && currentNotification == Task.Notification.SCRIPTS_ADD_BRICK) {
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				//				Tutorial.getInstance(context).pauseTutorial();
				currentNotification = null;
			}
			currentActivity.dispatchTouchEvent(ev);
		}
	}

	public void dispatchProjectStageButton(MotionEvent ev) {
		if (currentNotification == Task.Notification.PROJECT_STAGE_BUTTON) {
			Log.i("faxxe", ".........................................PROJECT_STAGE_BUTTON");
			Activity currentActivity = (Activity) context;
			if (currentActivity.getLocalClassName().compareTo("ui.ScriptActivity") == 0) {
				currentActivity = currentActivity.getParent();
			}
			if (currentActivity.getLocalClassName().compareTo("ui.CostumeActivity") == 0) {
				currentActivity = currentActivity.getParent();
			}
			if (currentActivity.getLocalClassName().compareTo("ui.SoundActivity") == 0) {
				currentActivity = currentActivity.getParent();
			}
			ImageButton actionPlayButton = (ImageButton) currentActivity.findViewById(R.id.btn_action_play);
			if (isImageButtonClicked(ev, actionPlayButton)) {
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					Tutorial.getInstance(null).stopTutorial();
				}
				currentActivity.dispatchTouchEvent(ev);
			}
		}
	}

	public void dispatchProject(MotionEvent ev) {
		Activity currentActivity = (Activity) context;

		if (currentNotification == Task.Notification.PROJECT_ADD_SPRITE) {
			ImageButton addSpriteButton = (ImageButton) currentActivity.findViewById(R.id.btn_action_add_sprite);
			if (isImageButtonClicked(ev, addSpriteButton)) {
				currentActivity.dispatchTouchEvent(ev);
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					currentNotification = null;
				}
			}
		}

		if (currentNotification == Task.Notification.PROJECT_HOME_BUTTON) {

		}

		if (currentNotification == Task.Notification.PROJECT_LIST_ITEM) {
			ListActivity listActivity = (ListActivity) context;
			ListView listView = listActivity.getListView();

			// TODO: There has to be added a possibility to address specific entries of the list
			if (isListItemClicked(ev, listView, Integer.parseInt(notificationValue)) != -1) {
				listView.dispatchTouchEvent(ev);
			}
		}
	}

	public int isListItemClicked(MotionEvent event, ListView listView, int index) {
		event.setLocation(event.getX(), event.getY() - 100); // please anyone find out the real height of the titlebar!
		int y = listView.getChildAt(index).getTop();
		int x = listView.getChildAt(index).getLeft();
		int maxx = listView.getChildAt(index).getRight();
		int maxy = listView.getChildAt(index).getBottom();
		Log.i("faxxe", "touched!" + x + " " + y + " " + maxx + " " + maxy + " " + event.getX() + " " + event.getY());

		if (event.getX() < maxx && event.getX() > x && event.getY() < maxy && event.getY() > y) {
			Log.i("faxxe",
					"irgendwos" + x + " " + y + " " + maxx + " " + maxy + " " + event.getX() + " " + event.getY());
			listView.dispatchTouchEvent(event);
		}

		return -1;
	}

	public void dispatchMainMenu(MotionEvent ev) {
		Log.i("faxxe", "mainmenudispatcher");
		Activity currentActivity = (Activity) context;
		Button currentProjectButton = (Button) currentActivity.findViewById(R.id.current_project_button);
		Button aboutButton = (Button) currentActivity.findViewById(R.id.about_catroid_button);
		Button webButton = (Button) currentActivity.findViewById(R.id.web_button);
		Button newProjectButton = (Button) currentActivity.findViewById(R.id.new_project_button);

		if (currentNotification == Task.Notification.CURRENT_PROJECT_BUTTON) {
			if (isButtonClicked(ev, currentProjectButton)) {
				currentActivity.dispatchTouchEvent(ev);
			}
		}

		//		if (isButtonClicked(ev, tutorialButton)) {
		//			currentActivity.dispatchTouchEvent(ev);
		//		}
	}

	public void dispatchCostumes(MotionEvent ev) {

		Activity currentActivity = (Activity) context;
		currentActivity = currentActivity.getParent();
		Log.i("faxxe", "CostumesDispatcher!");
		if (currentNotification == Task.Notification.COSTUMES_ADD_COSTUME) {
			ImageButton addSpriteButton = (ImageButton) currentActivity.findViewById(R.id.btn_action_add_sprite);
			if (isImageButtonClicked(ev, addSpriteButton) && addSpriteButton != null) {
				currentActivity.dispatchTouchEvent(ev);
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					currentNotification = null;
				}
			}
		}
		return;
	}

	public void dispatchSounds(MotionEvent ev) {
		Activity currentActivity = (Activity) context;
		currentActivity = currentActivity.getParent();
		Log.i("faxxe", "Sounddispatcher!");
		if (currentNotification == Task.Notification.SOUNDS_ADD_SOUND) {
			ImageButton addSpriteButton = (ImageButton) currentActivity.findViewById(R.id.btn_action_add_sprite);
			if (isImageButtonClicked(ev, addSpriteButton) && addSpriteButton != null) {
				currentActivity.dispatchTouchEvent(ev);
				if (ev.getAction() == MotionEvent.ACTION_UP) {
					currentNotification = null;
				}
			}
		}
		return;
	}

	public boolean isButtonClicked(MotionEvent event, Button button) {
		int location[] = new int[2];
		button.getLocationOnScreen(location);
		int width = button.getWidth();
		int height = button.getHeight();

		if (event.getX() > location[0] && event.getX() < location[0] + width && event.getY() > location[1]
				&& event.getY() < location[1] + height) {
			return (true);
		} else {
			return (false);
		}
	}

	public boolean isImageButtonClicked(MotionEvent event, ImageButton button) {
		int location[] = new int[2];
		button.getLocationOnScreen(location);
		int width = button.getWidth();
		int height = button.getHeight();

		if (event.getX() > location[0] && event.getX() < location[0] + width && event.getY() > location[1]
				&& event.getY() < location[1] + height) {
			return (true);
		} else {
			return (false);
		} //check if event coordinates are the coordinates of the control panel buttons

	}

	public boolean isLinearLayoutClicked(LinearLayout linearLayout, MotionEvent event) {
		int location[] = new int[2];
		linearLayout.getLocationOnScreen(location);
		int width = linearLayout.getWidth();
		int height = linearLayout.getHeight();

		if (event.getX() >= location[0] && event.getX() <= location[0] + width && event.getY() >= location[1]
				&& event.getY() <= location[1] + height) {
			return (true);
		} else {
			return (false);
		}
	}

	//stolen from: http://stackoverflow.com/questions/3619693/how-to-get-views-position

	private int getRelativeLeft(View myView) {
		if (myView.getParent() == myView.getRootView()) {
			return myView.getLeft();
		} else {
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
		}
	}

	private int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView()) {
			return myView.getTop();
		} else {
			return myView.getTop() + getRelativeTop((View) myView.getParent());
		}
	}

}