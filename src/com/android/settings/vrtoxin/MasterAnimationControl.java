/*
 * Copyright (C) 2014 VRToxin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.vrtoxin;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.widget.Toast;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class MasterAnimationControl extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_TOAST_ANIMATION = "toast_animation";
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";
    private static final String TOAST_TEXT_COLOR = "toast_text_color";
    private static final String POWER_MENU_ANIMATIONS = "power_menu_animations";

    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;
    private ListPreference mToastAnimation;
    private ColorPickerPreference mTextColor;

    ListPreference mPowerMenuAnimations;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_AOSP;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        final ContentResolver resolver = activity.getContentResolver();

        addPreferencesFromResource(R.xml.vrtoxin_animations);

	    PreferenceScreen prefSet = getPreferenceScreen();
    
        int intColor = 0xffffffff;
        String hexColor = String.format("#%08x", (0xffffffff & 0xffffffff));

        mToastAnimation = (ListPreference) prefSet.findPreference(KEY_TOAST_ANIMATION);
        mToastAnimation.setSummary(mToastAnimation.getEntry());
        int CurrentToastAnimation = Settings.System.getInt(
                getContentResolver(),Settings.System.TOAST_ANIMATION, 1);
        mToastAnimation.setValueIndex(CurrentToastAnimation);
        mToastAnimation.setOnPreferenceChangeListener(this);

        // ListView Animations
        mListViewAnimation = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_ANIMATION);
        int listviewanimation = Settings.System.getInt(getContentResolver(),
                Settings.System.LISTVIEW_ANIMATION, 0);
        mListViewAnimation.setValue(String.valueOf(listviewanimation));
        mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_INTERPOLATOR);
        int listviewinterpolator = Settings.System.getInt(getContentResolver(),
                Settings.System.LISTVIEW_INTERPOLATOR, 0);
        mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
        mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        mListViewInterpolator.setOnPreferenceChangeListener(this);
        mListViewInterpolator.setEnabled(listviewanimation > 0);

        mTextColor =
                (ColorPickerPreference) findPreference(TOAST_TEXT_COLOR);
        intColor = Settings.System.getInt(getContentResolver(),
                Settings.System.TOAST_TEXT_COLOR, 0xffffffff);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mTextColor.setNewPreviewColor(intColor);
        mTextColor.setSummary(hexColor);
        mTextColor.setOnPreferenceChangeListener(this);

        mPowerMenuAnimations = (ListPreference) findPreference(POWER_MENU_ANIMATIONS);
        mPowerMenuAnimations.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.POWER_MENU_ANIMATIONS, 0)));
        mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
        mPowerMenuAnimations.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        if (KEY_TOAST_ANIMATION.equals(key)) {
            int index = mToastAnimation.findIndexOfValue((String) objValue);
            Settings.System.putString(getContentResolver(),
                    Settings.System.TOAST_ANIMATION, (String) objValue);
            mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
            Toast.makeText(getActivity(), "Toast animation test!!!",
                    Toast.LENGTH_SHORT).show();
        }
        if (KEY_LISTVIEW_ANIMATION.equals(key)) {
            int value = Integer.parseInt((String) objValue);
            int index = mListViewAnimation.findIndexOfValue((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION,
                    value);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            mListViewInterpolator.setEnabled(value > 0);
        }
        if (KEY_LISTVIEW_INTERPOLATOR.equals(key)) {
            int value = Integer.parseInt((String) objValue);
            int index = mListViewInterpolator.findIndexOfValue((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR,
                    value);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
        }
        if (preference == mTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(objValue)));
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.TOAST_TEXT_COLOR, intHex);
            preference.setSummary(hex);
        }
        if (preference == mPowerMenuAnimations) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_ANIMATIONS,
                    Integer.valueOf((String) objValue));
            mPowerMenuAnimations.setValue(String.valueOf(objValue));
            mPowerMenuAnimations.setSummary(mPowerMenuAnimations.getEntry());
        }
        return true;
    }
}
