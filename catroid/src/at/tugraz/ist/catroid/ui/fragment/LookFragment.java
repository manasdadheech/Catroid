package at.tugraz.ist.catroid.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.formulaeditor.CatKeyEvent;
import at.tugraz.ist.catroid.formulaeditor.CatKeyboardView;

import com.actionbarsherlock.app.SherlockFragment;

public class LookFragment extends SherlockFragment implements OnClickListener, DialogInterface.OnKeyListener {

	private LinearLayout listLinearLayout;
	private ListView listView;
	public final static String LOOK_FRAGMENT_TAG = "lookFragment";

	private CatKeyboardView catKeyboardView;
	private final Integer[] lookResourceIds = { R.string.formula_editor_look_x, R.string.formula_editor_look_y,
			R.string.formula_editor_look_ghosteffect, R.string.formula_editor_look_brightness,
			R.string.formula_editor_look_size, R.string.formula_editor_look_rotation,
			R.string.formula_editor_look_layer };
	private static final int CANCEL_INDEX = -2;

	@Override
	public void onClick(DialogInterface dialog, int index) {
		if (index == CANCEL_INDEX) {

			return;
		}
		Log.v("touched: ", "" + index);
		Log.v("touched: ", lookResourceIds[index].toString());

		int[] keyCode = new int[1];
		keyCode[0] = 0;

		catKeyboardView.onKey(CatKeyEvent.KEYCODE_LOOK_X + index, keyCode);

	}

	public static LookFragment newInstance() { // TODO change this!!! o.o

		LookFragment fragment = new LookFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//		if (savedInstanceState != null) {
		//			restoreInstance = savedInstanceState.getBoolean("restoreInstance");
		//		}
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public void onSaveInstanceState(Bundle saveInstanceState) {
		saveInstanceState.putBoolean("restoreInstance", true);
		super.onSaveInstanceState(saveInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View fragmentView = inflater.inflate(R.layout.fragment_look, container, false);
		listLinearLayout = (LinearLayout) fragmentView.findViewById(R.id.formula_editor_look);
		if (listLinearLayout != null) {

			String[] lookNames = new String[lookResourceIds.length];
			int index = 0;
			for (Integer lookResourceID : lookResourceIds) {
				lookNames[index] = getString(lookResourceID);
				index++;
			}

			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, lookNames);

			listView = new ListView(getActivity());
			listView.setAdapter(arrayAdapter);
			listLinearLayout.addView(listView);
		}

		getSherlockActivity().getSupportActionBar().setTitle(getString(R.string.formula_editor_choose_look_variable));
		//		((SherlockFragmentActivity) context).getSupportActionBar().setTitle(
		//				((SherlockFragmentActivity) context).getResources().getString(
		//						R.string.formula_editor_choose_look_variable));

		return fragmentView;
	}

	public void showFragment(Context context) {
		FragmentActivity activity = (FragmentActivity) context;
		FragmentManager fragmentManager = activity.getSupportFragmentManager();

		//		activity.findViewById(R.id.fragment_formula_editor).setVisibility(View.GONE);

		FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
		fragTransaction.add(R.id.fragment_formula_editor, this);
		fragTransaction.commit();
	}

	public void setCatKeyboardView(CatKeyboardView catKeyboardView) {
		this.catKeyboardView = catKeyboardView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(DialogInterface di, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					FragmentTransaction fragTransaction = getSherlockActivity().getSupportFragmentManager()
							.beginTransaction();
					FormulaEditorFragment formulaEditorFragment = (FormulaEditorFragment) getSherlockActivity()
							.getSupportFragmentManager().findFragmentByTag(
									FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
					fragTransaction.add(R.id.fragment_formula_editor, formulaEditorFragment);
					fragTransaction.commit();

					break;
				default:
					break;
			}
		}
		return true;
	}

}
