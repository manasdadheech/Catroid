/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.ui.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.common.SoundInfo;
import at.tugraz.ist.catroid.utils.UtilFile;

public class SoundAdapter extends ArrayAdapter<SoundInfo> {

	protected ArrayList<SoundInfo> soundInfoItems;
	protected Context context;

	private Set<Integer> checkedSounds = new HashSet<Integer>();

	private OnSoundCheckedListener onSoundCheckedListener;
	private OnSoundPlayPauseListener onSoundPlayPauseListener;
	private OnSoundEditListener onSoundEditListener;

	public SoundAdapter(final Context context, int textViewResourceId, ArrayList<SoundInfo> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		soundInfoItems = items;
	}

	public void setOnSoundCheckedListener(OnSoundCheckedListener listener) {
		onSoundCheckedListener = listener;
	}

	public void setOnSoundPlayPauseListener(OnSoundPlayPauseListener listener) {
		onSoundPlayPauseListener = listener;
	}

	public void setOnSoundEditListener(OnSoundEditListener listener) {
		onSoundEditListener = listener;
	}

	public Set<Integer> getCheckedSounds() {
		return checkedSounds;
	}

	public int getSingleCheckedSound() {
		if (checkedSounds.size() > 1) {
			throw new IllegalArgumentException("There are more than one checked sounds");
		}

		return checkedSounds.iterator().next();
	}

	public int getCheckedSoundsCount() {
		return checkedSounds.size();
	}

	public void uncheckAllSounds() {
		checkedSounds.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.fragment_sound_soundlist_item, null);
		}

		final SoundInfo soundInfo = soundInfoItems.get(position);

		convertView.findViewById(R.id.sound_name).setTag(position);
		convertView.findViewById(R.id.btn_sound_play).setTag(position);
		convertView.findViewById(R.id.btn_sound_pause).setTag(position);

		if (soundInfo != null) {
			ImageView soundImage = (ImageView) convertView.findViewById(R.id.sound_img);
			TextView soundNameTextView = (TextView) convertView.findViewById(R.id.sound_name);
			Button pauseSoundButton = (Button) convertView.findViewById(R.id.btn_sound_pause);
			Button playSoundButton = (Button) convertView.findViewById(R.id.btn_sound_play);
			TextView soundFileSize = (TextView) convertView.findViewById(R.id.sound_size);
			TextView soundDuration = (TextView) convertView.findViewById(R.id.sound_duration);
			CheckBox soundCheckBox = (CheckBox) convertView.findViewById(R.id.cb_sound_select);

			if (soundInfo.isPlaying) {
				soundImage.setImageDrawable(context.getResources().getDrawable(R.drawable.speaker_playing));
				playSoundButton.setVisibility(Button.GONE);
				pauseSoundButton.setVisibility(Button.VISIBLE);
			} else if (soundInfo.isPaused) {
				soundImage.setImageDrawable(context.getResources().getDrawable(R.drawable.speaker));
				playSoundButton.setVisibility(Button.VISIBLE);
				pauseSoundButton.setVisibility(Button.GONE);
			} else {
				soundImage.setImageDrawable(context.getResources().getDrawable(R.drawable.speaker));
				playSoundButton.setVisibility(Button.VISIBLE);
				pauseSoundButton.setVisibility(Button.GONE);
			}

			//setting filesize and duration
			try {
				MediaPlayer tempPlayer = new MediaPlayer();
				tempPlayer.setDataSource(soundInfo.getAbsolutePath());
				tempPlayer.prepare();

				//setting duration TextView:
				long milliseconds = tempPlayer.getDuration();
				int seconds = (int) ((milliseconds / 1000) % 60);
				int minutes = (int) ((milliseconds / 1000) / 60);
				int hours = (int) ((milliseconds / 1000) / 3600);

				soundDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
				soundFileSize.setText(UtilFile.getSizeAsString(new File(soundInfo.getAbsolutePath())));
			} catch (IOException e) {
				e.printStackTrace();
			}

			soundNameTextView.setText(soundInfo.getTitle());
			soundNameTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onSoundEditListener != null) {
						onSoundEditListener.onSoundRename(position);
					}
				}
			});

			soundCheckBox.setChecked(checkedSounds.contains(position));
			soundCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						checkedSounds.add(position);
					} else {
						checkedSounds.remove(position);
					}

					if (onSoundCheckedListener != null) {
						onSoundCheckedListener.onSoundChecked(position, isChecked);
					}
				}
			});

			playSoundButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onSoundPlayPauseListener != null) {
						onSoundPlayPauseListener.onSoundPlay(v);
					}
				}
			});

			pauseSoundButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onSoundPlayPauseListener != null) {
						onSoundPlayPauseListener.onSoundPause(v);
					}
				}
			});
		}

		return convertView;
	}

	public interface OnSoundPlayPauseListener {

		public void onSoundPlay(View v);

		public void onSoundPause(View v);

	}

	public interface OnSoundEditListener {

		public void onSoundRename(int position);

	}

	public interface OnSoundCheckedListener {

		public void onSoundChecked(int position, boolean isChecked);

	}
}
