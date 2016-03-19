/* 
 * Copyright (C) 2014 DarkKat
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.vrtoxin.SeekBarPreference;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ExpansionView extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PREF_CAT_COLORS = "colors";
    private static final String EXPANSION_VIEW_TEXT_COLOR = "expansion_view_text_color";
    private static final String EXPANSION_VIEW_FONT_STYLE = "expansion_view_font_style";
    private static final String EXPANSION_VIEW_TEXT_SIZE = "expansion_view_text_size";
    private static final String EXPANSION_VIEW_TEXT_CUSTOM = "expansion_view_text_custom";
    private static final String EXPANSION_VIEW_ICON_COLOR = "expansion_view_icon_color";
    private static final String EXPANSION_VIEW_RIPPLE_COLOR = "expansion_view_ripple_color";
    private static final String EXPANSION_VIEW_WEATHER_SHOW_CURRENT = "expansion_view_weather_show_current";
    private static final String EXPANSION_VIEW_WEATHER_ICON_TYPE = "expansion_view_weather_icon_type";
    private static final String EXPANSION_VIEW_WEATHER_ICON_COLOR = "expansion_view_weather_icon_color";
    private static final String EXPANSION_VIEW_WEATHER_TEXT_COLOR = "expansion_view_weather_text_color";
    private static final String EXPANSION_VIEW_BACKGROUND = "expansion_view_background";
    private static final String EXPANSION_VIEW_BACKGROUND_COLOR = "expansion_view_background_color";

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;
    private static final int VRTOXIN_BLUE = 0xff1976D2;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET  = 0;

    private ColorPickerPreference mExpansionViewTextColor;
    private ListPreference mExpansionViewFontStyle;
    private SeekBarPreference mExpansionViewTextSize;
    private EditTextPreference mCustomText;
    private ColorPickerPreference mExpansionViewIconColor;
    private ColorPickerPreference mExpansionViewRippleColor;
    private SwitchPreference mShowCurrent;
    private ListPreference mIconType;
    private ColorPickerPreference mExpansionViewWeatherIconColor;
    private ColorPickerPreference mExpansionViewWeatherTextColor;
    private ColorPickerPreference mExpansionViewBgColor;
    private SwitchPreference mShowBg;

    private ContentResolver mResolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    public void refreshSettings() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.expansion_view);
        mResolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

        boolean showBg = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_BACKGROUND, 0) == 1;

        PreferenceCategory catColors =
                (PreferenceCategory) findPreference(PREF_CAT_COLORS);

        mExpansionViewTextColor =
                (ColorPickerPreference) findPreference(EXPANSION_VIEW_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_TEXT_COLOR,
                WHITE); 
        mExpansionViewTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpansionViewTextColor.setSummary(hexColor);
        mExpansionViewTextColor.setDefaultColors(WHITE, VRTOXIN_BLUE);
        mExpansionViewTextColor.setOnPreferenceChangeListener(this);

        mExpansionViewFontStyle = (ListPreference) findPreference(EXPANSION_VIEW_FONT_STYLE);
        mExpansionViewFontStyle.setOnPreferenceChangeListener(this);
        mExpansionViewFontStyle.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.EXPANSION_VIEW_FONT_STYLE, 0)));
        mExpansionViewFontStyle.setSummary(mExpansionViewFontStyle.getEntry());

        mExpansionViewTextSize =
                (SeekBarPreference) findPreference(EXPANSION_VIEW_TEXT_SIZE);
        mExpansionViewTextSize.setValue(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.EXPANSION_VIEW_TEXT_SIZE, 14));
        mExpansionViewTextSize.setOnPreferenceChangeListener(this);

        mCustomText = (EditTextPreference) findPreference(EXPANSION_VIEW_TEXT_CUSTOM);
        mCustomText.getEditText().setHint(getResources().getString(
                com.android.internal.R.string.default_empty_shade_text));
        mCustomText.setOnPreferenceChangeListener(this);
        updateCustomTextPreference();

        mExpansionViewIconColor =
                (ColorPickerPreference) findPreference(EXPANSION_VIEW_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_ICON_COLOR,
                WHITE); 
        mExpansionViewIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpansionViewIconColor.setSummary(hexColor);
        mExpansionViewIconColor.setDefaultColors(WHITE, VRTOXIN_BLUE);
        mExpansionViewIconColor.setOnPreferenceChangeListener(this);

        mExpansionViewRippleColor =
                (ColorPickerPreference) findPreference(EXPANSION_VIEW_RIPPLE_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_RIPPLE_COLOR,
                WHITE); 
        mExpansionViewRippleColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpansionViewRippleColor.setSummary(hexColor);
        mExpansionViewRippleColor.setDefaultColors(WHITE, VRTOXIN_BLUE);
        mExpansionViewRippleColor.setOnPreferenceChangeListener(this);

        mShowCurrent =
                (SwitchPreference) findPreference(EXPANSION_VIEW_WEATHER_SHOW_CURRENT);
        mShowCurrent.setChecked(Settings.System.getInt(mResolver,
               Settings.System.EXPANSION_VIEW_WEATHER_SHOW_CURRENT, 1) == 1);
        mShowCurrent.setOnPreferenceChangeListener(this);

        mIconType = (ListPreference) findPreference(EXPANSION_VIEW_WEATHER_ICON_TYPE);
        int iconType = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_WEATHER_ICON_TYPE, 0);
        mIconType.setValue(String.valueOf(iconType));
        mIconType.setSummary(mIconType.getEntry());
        mIconType.setOnPreferenceChangeListener(this);

        mExpansionViewWeatherIconColor =
                (ColorPickerPreference) findPreference(EXPANSION_VIEW_WEATHER_ICON_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_WEATHER_ICON_COLOR,
                WHITE); 
        mExpansionViewWeatherIconColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpansionViewWeatherIconColor.setSummary(hexColor);
        mExpansionViewWeatherIconColor.setDefaultColors(WHITE, VRTOXIN_BLUE);
        mExpansionViewWeatherIconColor.setOnPreferenceChangeListener(this);

        mExpansionViewWeatherTextColor =
                (ColorPickerPreference) findPreference(EXPANSION_VIEW_WEATHER_TEXT_COLOR);
        intColor = Settings.System.getInt(mResolver,
                Settings.System.EXPANSION_VIEW_WEATHER_TEXT_COLOR,
                WHITE); 
        mExpansionViewWeatherTextColor.setNewPreviewColor(intColor);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mExpansionViewWeatherTextColor.setSummary(hexColor);
        mExpansionViewWeatherTextColor.setDefaultColors(WHITE, VRTOXIN_BLUE);
        mExpansionViewWeatherTextColor.setOnPreferenceChangeListener(this);

        mShowBg =
                (SwitchPreference) findPreference(EXPANSION_VIEW_BACKGROUND);
        mShowBg.setChecked(showBg);
        mShowBg.setOnPreferenceChangeListener(this);

        if (showBg) {
            mExpansionViewBgColor =
                    (ColorPickerPreference) findPreference(EXPANSION_VIEW_BACKGROUND_COLOR);
            intColor = Settings.System.getInt(mResolver,
                    Settings.System.EXPANSION_VIEW_BACKGROUND_COLOR, WHITE); 
            mExpansionViewBgColor.setNewPreviewColor(intColor);
            hexColor = String.format("#%08x", (0xffffffff & intColor));
            mExpansionViewBgColor.setSummary(hexColor);
            mExpansionViewBgColor.setOnPreferenceChangeListener(this);
        } else {
            catColors.removePreference(findPreference(EXPANSION_VIEW_BACKGROUND_COLOR));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(com.android.internal.R.drawable.ic_settings_backup_restore)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                showDialogInner(DLG_RESET);
                return true;
             default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value;
        String hex;
        int intHex;
        int intValue;
        int index;

        if (preference == mExpansionViewTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_TEXT_COLOR, intHex);
            preference.setSummary(hex);
            return true;
        } else if (preference == mExpansionViewFontStyle) {
            int val = Integer.parseInt((String) newValue);
            index = mExpansionViewFontStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.EXPANSION_VIEW_FONT_STYLE, val);
            mExpansionViewFontStyle.setSummary(mExpansionViewFontStyle.getEntries()[index]);
            return true;
        } else if (preference == mCustomText) {
            String text = (String) newValue;
            Settings.System.putString(mResolver,
                    Settings.System.EXPANSION_VIEW_TEXT_CUSTOM, text);
            updateCustomTextPreference();
        } else if (preference == mExpansionViewTextSize) {
            int width = ((Integer)newValue).intValue();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.EXPANSION_VIEW_TEXT_SIZE, width);
            return true;
        } else if (preference == mExpansionViewIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_ICON_COLOR, intHex);
            preference.setSummary(hex);
        } else if (preference == mExpansionViewRippleColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_RIPPLE_COLOR, intHex);
            preference.setSummary(hex);
        } else if (preference == mShowCurrent) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.EXPANSION_VIEW_WEATHER_SHOW_CURRENT,
                    value ? 1 : 0);
            return true;
        } else if (preference == mIconType) {
            intValue = Integer.valueOf((String) newValue);
            index = mIconType.findIndexOfValue((String) newValue);
            Settings.System.putInt(mResolver,
                    Settings.System.EXPANSION_VIEW_WEATHER_ICON_TYPE,
                    intValue);
            mIconType.setSummary(mIconType.getEntries()[index]);
            return true;
        } else if (preference == mExpansionViewWeatherIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_WEATHER_ICON_COLOR, intHex);
            preference.setSummary(hex);
        } else if (preference == mExpansionViewWeatherTextColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_WEATHER_TEXT_COLOR, intHex);
            preference.setSummary(hex);
        } else if (preference == mExpansionViewIconColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_ICON_COLOR, intHex);
            preference.setSummary(hex);
        } else if (preference == mShowBg) {
            value = (Boolean) newValue;
            Settings.System.putInt(mResolver,
                    Settings.System.EXPANSION_VIEW_BACKGROUND,
                    value ? 1 : 0);
            refreshSettings();
            return true;
        } else if (preference == mExpansionViewBgColor) {
            hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mResolver,
                Settings.System.EXPANSION_VIEW_BACKGROUND_COLOR, intHex);
            preference.setSummary(hex);
        }
        return false;
    }

    private void updateCustomTextPreference() {
        String customText = Settings.System.getString(mResolver,
                Settings.System.EXPANSION_VIEW_TEXT_CUSTOM);
        if (customText == null) {
            customText = "";
        }
        mCustomText.setText(customText);
        mCustomText.setSummary(customText);
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        ExpansionView getOwner() {
            return (ExpansionView) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case DLG_RESET:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reset)
                    .setMessage(R.string.dlg_reset_values_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setNeutralButton(R.string.reset_android,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_FONT_STYLE, 0);
                            Settings.System.putString(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_CUSTOM,
                                    "Nothing to see here");
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_SIZE, 20);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_ICON_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_RIPPLE_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_WEATHER_ICON_COLOR,
                                    WHITE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_WEATHER_TEXT_COLOR,
                                    WHITE);
                            getOwner().refreshSettings();
                        }
                    })
                    .setPositiveButton(R.string.reset_vrtoxin,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_FONT_STYLE, 20);
                            Settings.System.putString(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_CUSTOM,
                                    "VRToxin Is Fuckin Awesome!!!!");
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_TEXT_SIZE, 25);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_ICON_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_RIPPLE_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_WEATHER_ICON_COLOR,
                                    VRTOXIN_BLUE);
                            Settings.System.putInt(getOwner().mResolver,
                                    Settings.System.EXPANSION_VIEW_WEATHER_TEXT_COLOR,
                                    VRTOXIN_BLUE);
                            getOwner().refreshSettings();
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_AOSP;
    }
}
