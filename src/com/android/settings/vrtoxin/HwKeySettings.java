/*
 * Copyright (C) 2014-2015 Slimroms
 * Copyright (C) 2015 VRToxin Project
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


import android.content.Context;
import android.content.res.Resources;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.util.vrtoxin.AppHelper;
import com.android.internal.util.vrtoxin.ActionConstants;
import com.android.internal.util.vrtoxin.DeviceUtils;
import com.android.internal.util.vrtoxin.DeviceUtils.FilteredDeviceFeaturesArray;
import com.android.internal.util.vrtoxin.HwKeyHelper;
 
import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.Utils;
import com.android.settings.vrtoxin.util.ShortcutPickerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HwKeySettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, OnPreferenceClickListener,
        ShortcutPickerHelper.OnPickListener, Indexable {

    private static final String CATEGORY_KEYS = "button_keys";
    private static final String CATEGORY_BACK = "button_keys_back";
    private static final String CATEGORY_CAMERA = "button_keys_camera";
    private static final String CATEGORY_HOME = "button_keys_home";
    private static final String CATEGORY_MENU = "button_keys_menu";
    private static final String CATEGORY_ASSIST = "button_keys_assist";
    private static final String CATEGORY_APPSWITCH = "button_keys_appSwitch";

    private static final String KEYS_CATEGORY_BINDINGS = "keys_bindings";
    private static final String KEYS_ENABLE_CUSTOM = "enable_hardware_rebind";
    private static final String KEYS_BACK_PRESS = "keys_back_press";
    private static final String KEYS_BACK_LONG_PRESS = "keys_back_long_press";
    private static final String KEYS_BACK_DOUBLE_TAP = "keys_back_double_tap";
    private static final String KEYS_CAMERA_PRESS = "keys_camera_press";
    private static final String KEYS_CAMERA_LONG_PRESS = "keys_camera_long_press";
    private static final String KEYS_CAMERA_DOUBLE_TAP = "keys_camera_double_tap";
    private static final String KEYS_HOME_PRESS = "keys_home_press";
    private static final String KEYS_HOME_LONG_PRESS = "keys_home_long_press";
    private static final String KEYS_HOME_DOUBLE_TAP = "keys_home_double_tap";
    private static final String KEYS_MENU_PRESS = "keys_menu_press";
    private static final String KEYS_MENU_LONG_PRESS = "keys_menu_long_press";
    private static final String KEYS_MENU_DOUBLE_TAP = "keys_menu_double_tap";
    private static final String KEYS_ASSIST_PRESS = "keys_assist_press";
    private static final String KEYS_ASSIST_LONG_PRESS = "keys_assist_long_press";
    private static final String KEYS_ASSIST_DOUBLE_TAP = "keys_assist_double_tap";
    private static final String KEYS_APP_SWITCH_PRESS = "keys_app_switch_press";
    private static final String KEYS_APP_SWITCH_LONG_PRESS = "keys_app_switch_long_press";
    private static final String KEYS_APP_SWITCH_DOUBLE_TAP = "keys_app_switch_double_tap";
    private static final String CATEGORY_VOLUME = "volume_keys";
    private static final String KEY_VOLUME_CONTROL_RING_STREAM = "volume_keys_control_ring_stream";
    private static final String KEY_VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";
    private static final String KEY_VOLUME_WAKE_DEVICE = "volume_key_wake_device";
    private static final String KEY_SWAP_VOLUME_BUTTONS = "swap_volume_buttons";

    private static final int DLG_SHOW_WARNING_DIALOG = 0;
    private static final int DLG_SHOW_ACTION_DIALOG  = 1;
    private static final int DLG_RESET_TO_DEFAULT    = 2;

    private static final int MENU_RESET = Menu.FIRST;

    // Masks for checking presence of hardware keys.
    // Must match values in frameworks/base/core/res/res/values/config.xml
    private static final int KEY_MASK_HOME       = 0x01;
    private static final int KEY_MASK_BACK       = 0x02;
    private static final int KEY_MASK_MENU       = 0x04;
    private static final int KEY_MASK_ASSIST     = 0x08;
    private static final int KEY_MASK_APP_SWITCH = 0x10;
    private static final int KEY_MASK_CAMERA     = 0x20;
    private static final int KEY_MASK_VOLUME     = 0x40;

    private SwitchPreference mEnableCustomBindings;
    private Preference mBackPressAction;
    private Preference mBackLongPressAction;
    private Preference mBackDoubleTapAction;
    private Preference mCameraPressAction;
    private Preference mCameraLongPressAction;
    private Preference mCameraDoubleTapAction;
    private Preference mHomePressAction;
    private Preference mHomeLongPressAction;
    private Preference mHomeDoubleTapAction;
    private Preference mMenuPressAction;
    private Preference mMenuLongPressAction;
    private Preference mMenuDoubleTapAction;
    private Preference mAssistPressAction;
    private Preference mAssistLongPressAction;
    private Preference mAssistDoubleTapAction;
    private Preference mAppSwitchPressAction;
    private Preference mAppSwitchLongPressAction;
    private Preference mAppSwitchDoubleTapAction;
    private ListPreference mVolumeKeyCursorControl;
    private SwitchPreference mVolumeControlRingStream;
    private SwitchPreference mVolumeKeyWakeControl;
    private SwitchPreference mSwapVolumeButtons;

    private boolean mCheckPreferences;
    private Map<String, String> mKeySettings = new HashMap<String, String>();

    private ShortcutPickerHelper mPicker;
    private String mPendingSettingsKey;
    private static FilteredDeviceFeaturesArray sFinalActionDialogArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPicker = new ShortcutPickerHelper(getActivity(), this);

        // Before we start filter out unsupported options on the
        // ListPreference values and entries
        Resources res = getResources();
        sFinalActionDialogArray = new FilteredDeviceFeaturesArray();
        sFinalActionDialogArray = DeviceUtils.filterUnsupportedDeviceFeatures(getActivity(),
            res.getStringArray(res.getIdentifier(
                    "shortcut_action_hwkey_values", "array", "com.android.settings")),
            res.getStringArray(res.getIdentifier(
                    "shortcut_action_hwkey_entries", "array", "com.android.settings")));

        // Attach final settings screen.
        reloadSettings();

        setHasOptionsMenu(true);
    }

    private PreferenceScreen reloadSettings() {
        mCheckPreferences = false;
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.hwkey_settings);
        prefs = getPreferenceScreen();

        int deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        boolean hasBackKey = (deviceKeys & KEY_MASK_BACK) != 0;
        boolean hasHomeKey = (deviceKeys & KEY_MASK_HOME) != 0;
        boolean hasMenuKey = (deviceKeys & KEY_MASK_MENU) != 0;
        boolean hasAssistKey = (deviceKeys & KEY_MASK_ASSIST) != 0;
        boolean hasAppSwitchKey = (deviceKeys & KEY_MASK_APP_SWITCH) != 0;
        boolean hasCameraKey = (deviceKeys & KEY_MASK_CAMERA) != 0;
        boolean hasVolumeKeys = (deviceKeys & KEY_MASK_VOLUME) != 0;

        PreferenceCategory keysCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_KEYS);
        PreferenceCategory keysBackCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_BACK);
        PreferenceCategory keysCameraCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_CAMERA);
        PreferenceCategory keysHomeCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_HOME);
        PreferenceCategory keysMenuCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_MENU);
        PreferenceCategory keysAssistCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_ASSIST);
        PreferenceCategory keysAppSwitchCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_APPSWITCH);
        PreferenceCategory volumeCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_VOLUME);

        mEnableCustomBindings = (SwitchPreference) prefs.findPreference(
                KEYS_ENABLE_CUSTOM);
        mBackPressAction = (Preference) prefs.findPreference(
                KEYS_BACK_PRESS);
        mBackLongPressAction = (Preference) prefs.findPreference(
                KEYS_BACK_LONG_PRESS);
        mBackDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_BACK_DOUBLE_TAP);
        mCameraPressAction = (Preference) prefs.findPreference(
                KEYS_CAMERA_PRESS);
        mCameraLongPressAction = (Preference) prefs.findPreference(
                KEYS_CAMERA_LONG_PRESS);
        mCameraDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_CAMERA_DOUBLE_TAP);
        mHomePressAction = (Preference) prefs.findPreference(
                KEYS_HOME_PRESS);
        mHomeLongPressAction = (Preference) prefs.findPreference(
                KEYS_HOME_LONG_PRESS);
        mHomeDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_HOME_DOUBLE_TAP);
        mMenuPressAction = (Preference) prefs.findPreference(
                KEYS_MENU_PRESS);
        mMenuLongPressAction = (Preference) prefs.findPreference(
                KEYS_MENU_LONG_PRESS);
        mMenuDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_MENU_DOUBLE_TAP);
        mAssistPressAction = (Preference) prefs.findPreference(
                KEYS_ASSIST_PRESS);
        mAssistLongPressAction = (Preference) prefs.findPreference(
                KEYS_ASSIST_LONG_PRESS);
        mAssistDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_ASSIST_DOUBLE_TAP);
        mAppSwitchPressAction = (Preference) prefs.findPreference(
                KEYS_APP_SWITCH_PRESS);
        mAppSwitchLongPressAction = (Preference) prefs.findPreference(
                KEYS_APP_SWITCH_LONG_PRESS);
        mAppSwitchDoubleTapAction = (Preference) prefs.findPreference(
                KEYS_APP_SWITCH_DOUBLE_TAP);

        if (hasBackKey) {
            // Back key
            setupOrUpdatePreference(mBackPressAction,
                    HwKeyHelper.getPressOnBackBehavior(getActivity(), false),
                    Settings.System.KEY_BACK_ACTION);

            // Back key longpress
            setupOrUpdatePreference(mBackLongPressAction,
                    HwKeyHelper.getLongPressOnBackBehavior(getActivity(), false),
                    Settings.System.KEY_BACK_LONG_PRESS_ACTION);

            // Back key double tap
            setupOrUpdatePreference(mBackDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnBackBehavior(getActivity(), false),
                    Settings.System.KEY_BACK_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysBackCategory);
        }

        if (hasVolumeKeys) {
            int volumeControlRingtone = Settings.System.getInt(getContentResolver(),
                    Settings.System.VOLUME_KEYS_CONTROL_RING_STREAM, 1);
            mVolumeControlRingStream = (SwitchPreference)
                    prefs.findPreference(KEY_VOLUME_CONTROL_RING_STREAM);
            mVolumeControlRingStream.setChecked(volumeControlRingtone > 0);
            int cursorControlAction = Settings.System.getInt(getContentResolver(),
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0);
            mVolumeKeyCursorControl = initActionList(KEY_VOLUME_KEY_CURSOR_CONTROL,
                    cursorControlAction);
            int wakeControlAction = Settings.System.getInt(getContentResolver(),
                    Settings.System.VOLUME_WAKE_SCREEN, 0);
            mVolumeKeyWakeControl = initSwitch(KEY_VOLUME_WAKE_DEVICE, (wakeControlAction == 1));
            int swapVolumeKeys = Settings.System.getInt(getContentResolver(),
                    Settings.System.SWAP_VOLUME_KEYS_ON_ROTATION, 0);
            mSwapVolumeButtons = (SwitchPreference)
                    prefs.findPreference(KEY_SWAP_VOLUME_BUTTONS);
            mSwapVolumeButtons.setChecked(swapVolumeKeys > 0);
        } else {
            prefs.removePreference(volumeCategory);
        }

        if (hasCameraKey) {
            // Camera key
            setupOrUpdatePreference(mCameraPressAction,
                    HwKeyHelper.getPressOnCameraBehavior(getActivity(), false),
                    Settings.System.KEY_CAMERA_ACTION);

            // Camera key longpress
            setupOrUpdatePreference(mCameraLongPressAction,
                    HwKeyHelper.getLongPressOnCameraBehavior(getActivity(), false),
                    Settings.System.KEY_CAMERA_LONG_PRESS_ACTION);

            // Camera key double tap
            setupOrUpdatePreference(mCameraDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnCameraBehavior(getActivity(), false),
                    Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysCameraCategory);
        }

        if (hasHomeKey) {
            // Home key
            setupOrUpdatePreference(mHomePressAction,
                    HwKeyHelper.getPressOnHomeBehavior(getActivity(), false),
                    Settings.System.KEY_HOME_ACTION);

            // Home key long press
            setupOrUpdatePreference(mHomeLongPressAction,
                    HwKeyHelper.getLongPressOnHomeBehavior(getActivity(), false),
                    Settings.System.KEY_HOME_LONG_PRESS_ACTION);

            // Home key double tap
            setupOrUpdatePreference(mHomeDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnHomeBehavior(getActivity(), false),
                    Settings.System.KEY_HOME_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysHomeCategory);
        }

        if (hasMenuKey) {
            // Menu key
            setupOrUpdatePreference(mMenuPressAction,
                    HwKeyHelper.getPressOnMenuBehavior(getActivity(), false),
                    Settings.System.KEY_MENU_ACTION);

            // Menu key longpress
            setupOrUpdatePreference(mMenuLongPressAction,
                    HwKeyHelper.getLongPressOnMenuBehavior(getActivity(), false, hasAssistKey),
                    Settings.System.KEY_MENU_LONG_PRESS_ACTION);

            // Menu key double tap
            setupOrUpdatePreference(mMenuDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnMenuBehavior(getActivity(), false),
                    Settings.System.KEY_MENU_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysMenuCategory);
        }

        if (hasAssistKey) {
            // Assistant key
            setupOrUpdatePreference(mAssistPressAction,
                    HwKeyHelper.getPressOnAssistBehavior(getActivity(), false),
                    Settings.System.KEY_ASSIST_ACTION);

            // Assistant key longpress
            setupOrUpdatePreference(mAssistLongPressAction,
                    HwKeyHelper.getLongPressOnAssistBehavior(getActivity(), false),
                    Settings.System.KEY_ASSIST_LONG_PRESS_ACTION);

            // Assistant key double tap
            setupOrUpdatePreference(mAssistDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnAssistBehavior(getActivity(), false),
                    Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysAssistCategory);
        }

        if (hasAppSwitchKey) {
            // App switch key
            setupOrUpdatePreference(mAppSwitchPressAction,
                    HwKeyHelper.getPressOnAppSwitchBehavior(getActivity(), false),
                    Settings.System.KEY_APP_SWITCH_ACTION);

            // App switch key longpress
            setupOrUpdatePreference(mAppSwitchLongPressAction,
                    HwKeyHelper.getLongPressOnAppSwitchBehavior(getActivity(), false),
                    Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION);

            // App switch key double tap
            setupOrUpdatePreference(mAppSwitchDoubleTapAction,
                    HwKeyHelper.getDoubleTapOnAppSwitchBehavior(getActivity(), false),
                    Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION);
        } else {
            prefs.removePreference(keysAppSwitchCategory);
        }

        boolean enableHardwareRebind = Settings.System.getInt(getContentResolver(),
                Settings.System.HARDWARE_KEY_REBINDING, 0) == 1;
        mEnableCustomBindings = (SwitchPreference) findPreference(KEYS_ENABLE_CUSTOM);
        mEnableCustomBindings.setChecked(enableHardwareRebind);
        mEnableCustomBindings.setOnPreferenceChangeListener(this);

        // Handle warning dialog.
        SharedPreferences preferences =
                getActivity().getSharedPreferences("hw_key_settings", Activity.MODE_PRIVATE);
        if (hasHomeKey && !hasHomeKey() && !preferences.getBoolean("no_home_action", false)) {
            preferences.edit()
                    .putBoolean("no_home_action", true).commit();
            showDialogInner(DLG_SHOW_WARNING_DIALOG, null, 0);
        } else if (hasHomeKey()) {
            preferences.edit()
                    .putBoolean("no_home_action", false).commit();
        }

        mCheckPreferences = true;
        return prefs;
    }

    private ListPreference initActionList(String key, int value) {
        ListPreference list = (ListPreference) getPreferenceScreen().findPreference(key);
        list.setValue(Integer.toString(value));
        list.setSummary(list.getEntry());
        list.setOnPreferenceChangeListener(this);
        return list;
    }

    private SwitchPreference initSwitch(String key, boolean checked) {
        SwitchPreference switchPreference = (SwitchPreference) getPreferenceManager()
                .findPreference(key);
        if (switchPreference != null) {
            switchPreference.setChecked(checked);
            switchPreference.setOnPreferenceChangeListener(this);
        }
        return switchPreference;
    }

    private void handleSwitchChange(SwitchPreference pref, Object newValue, String setting) {
        Boolean value = (Boolean) newValue;
        int intValue = (value) ? 1 : 0;
        Settings.System.putInt(getContentResolver(), setting, intValue);
    }

    private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getContentResolver(), setting, Integer.valueOf(value));
    }

    private void setupOrUpdatePreference(
            Preference preference, String action, String settingsKey) {
        if (preference == null || action == null) {
            return;
        }

        if (action.startsWith("**")) {
            preference.setSummary(getDescription(action));
        } else {
            preference.setSummary(AppHelper.getFriendlyNameForUri(
                    getActivity(), getActivity().getPackageManager(), action));
        }

        preference.setOnPreferenceClickListener(this);
        mKeySettings.put(settingsKey, action);
    }

    private String getDescription(String action) {
        if (sFinalActionDialogArray == null || action == null) {
            return null;
        }
        int i = 0;
        for (String actionValue : sFinalActionDialogArray.values) {
            if (action.equals(actionValue)) {
                return sFinalActionDialogArray.entries[i];
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String settingsKey = null;
        int dialogTitle = 0;
        if (preference == mBackPressAction) {
            settingsKey = Settings.System.KEY_BACK_ACTION;
            dialogTitle = R.string.keys_back_press_title;
        } else if (preference == mBackLongPressAction) {
            settingsKey = Settings.System.KEY_BACK_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_back_long_press_title;
        } else if (preference == mBackDoubleTapAction) {
            settingsKey = Settings.System.KEY_BACK_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_back_double_tap_title;
        } else if (preference == mCameraPressAction) {
            settingsKey = Settings.System.KEY_CAMERA_ACTION;
            dialogTitle = R.string.keys_camera_press_title;
        } else if (preference == mCameraLongPressAction) {
            settingsKey = Settings.System.KEY_CAMERA_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_camera_long_press_title;
        } else if (preference == mCameraDoubleTapAction) {
            settingsKey = Settings.System.KEY_CAMERA_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_camera_double_tap_title;
        } else if (preference == mHomePressAction) {
            settingsKey = Settings.System.KEY_HOME_ACTION;
            dialogTitle = R.string.keys_home_press_title;
        } else if (preference == mHomeLongPressAction) {
            settingsKey = Settings.System.KEY_HOME_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_home_long_press_title;
        } else if (preference == mHomeDoubleTapAction) {
            settingsKey = Settings.System.KEY_HOME_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_home_double_tap_title;
        } else if (preference == mMenuPressAction) {
            settingsKey = Settings.System.KEY_MENU_ACTION;
            dialogTitle = R.string.keys_menu_press_title;
        } else if (preference == mMenuLongPressAction) {
            settingsKey = Settings.System.KEY_MENU_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_menu_long_press_title;
        } else if (preference == mMenuDoubleTapAction) {
            settingsKey = Settings.System.KEY_MENU_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_menu_double_tap_title;
        } else if (preference == mAssistPressAction) {
            settingsKey = Settings.System.KEY_ASSIST_ACTION;
            dialogTitle = R.string.keys_assist_press_title;
        } else if (preference == mAssistLongPressAction) {
            settingsKey = Settings.System.KEY_ASSIST_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_assist_long_press_title;
        } else if (preference == mAssistDoubleTapAction) {
            settingsKey = Settings.System.KEY_ASSIST_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_assist_double_tap_title;
        } else if (preference == mAppSwitchPressAction) {
            settingsKey = Settings.System.KEY_APP_SWITCH_ACTION;
            dialogTitle = R.string.keys_app_switch_press_title;
        } else if (preference == mAppSwitchLongPressAction) {
            settingsKey = Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION;
            dialogTitle = R.string.keys_app_switch_long_press_title;
        } else if (preference == mAppSwitchDoubleTapAction) {
            settingsKey = Settings.System.KEY_APP_SWITCH_DOUBLE_TAP_ACTION;
            dialogTitle = R.string.keys_app_switch_double_tap_title;
        }

        if (settingsKey != null) {
            showDialogInner(DLG_SHOW_ACTION_DIALOG, settingsKey, dialogTitle);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSwapVolumeButtons) {
            int value = mSwapVolumeButtons.isChecked()
                    ? (Utils.isTablet(getActivity()) ? 2 : 1) : 0;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SWAP_VOLUME_KEYS_ON_ROTATION, value);
        } else if (preference == mVolumeControlRingStream) {
            int value = mVolumeControlRingStream.isChecked() ? 1 : 0;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_KEYS_CONTROL_RING_STREAM, value);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (!mCheckPreferences) {
            return false;
        }
        if (preference == mEnableCustomBindings) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(), Settings.System.HARDWARE_KEY_REBINDING,
                    value ? 1 : 0);
            return true;
        }
        if (preference == mVolumeKeyCursorControl) {
            handleActionListChange(mVolumeKeyCursorControl, newValue,
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL);
            return true;
        } else if (preference == mVolumeKeyWakeControl) {
            handleSwitchChange(mVolumeKeyWakeControl, newValue,
                    Settings.System.VOLUME_WAKE_SCREEN);
            return true;
        }
        return false;
    }

    private boolean hasHomeKey() {
        Iterator<String> nextAction = mKeySettings.values().iterator();
        while (nextAction.hasNext()){
            String action = nextAction.next();
            if (action != null && action.equals(ActionConstants.ACTION_HOME)) {
                return true;
            }
        }
        return false;
    }

    private void resetToDefault() {
        for (String settingsKey : mKeySettings.keySet()) {
            if (settingsKey != null) {
                Settings.System.putString(getActivity().getContentResolver(),
                settingsKey, null);
            }
        }
        Settings.System.putInt(getContentResolver(),
                Settings.System.HARDWARE_KEY_REBINDING, 1);
        reloadSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void shortcutPicked(String action,
                String description, Bitmap b, boolean isApplication) {
        if (mPendingSettingsKey == null || action == null) {
            return;
        }
        Settings.System.putString(getContentResolver(), mPendingSettingsKey, action);
        reloadSettings();
        mPendingSettingsKey = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ShortcutPickerHelper.REQUEST_PICK_SHORTCUT
                    || requestCode == ShortcutPickerHelper.REQUEST_PICK_APPLICATION
                    || requestCode == ShortcutPickerHelper.REQUEST_CREATE_SHORTCUT) {
                mPicker.onActivityResult(requestCode, resultCode, data);

            }
        } else {
            mPendingSettingsKey = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                    showDialogInner(DLG_RESET_TO_DEFAULT, null, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.shortcut_action_reset)
                .setIcon(com.android.internal.R.drawable.ic_settings_backup_restore)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void showDialogInner(int id, String settingsKey, int dialogTitle) {
        DialogFragment newFragment =
                MyAlertDialogFragment.newInstance(id, settingsKey, dialogTitle);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(
                int id, String settingsKey, int dialogTitle) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            args.putString("settingsKey", settingsKey);
            args.putInt("dialogTitle", dialogTitle);
            frag.setArguments(args);
            return frag;
        }

        HwKeySettings getOwner() {
            return (HwKeySettings) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            final String settingsKey = getArguments().getString("settingsKey");
            int dialogTitle = getArguments().getInt("dialogTitle");
            switch (id) {
                case DLG_SHOW_WARNING_DIALOG:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.attention)
                    .setMessage(R.string.no_home_key)
                    .setPositiveButton(R.string.dlg_ok, null)
                    .create();
                case DLG_SHOW_ACTION_DIALOG:
                    if (sFinalActionDialogArray == null) {
                        return null;
                    }
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(dialogTitle)
                    .setNegativeButton(R.string.cancel, null)
                    .setItems(getOwner().sFinalActionDialogArray.entries,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (getOwner().sFinalActionDialogArray.values[item]
                                    .equals(ActionConstants.ACTION_APP)) {
                                if (getOwner().mPicker != null) {
                                    getOwner().mPendingSettingsKey = settingsKey;
                                    getOwner().mPicker.pickShortcut(getOwner().getId());
                                }
                            } else {
                                Settings.System.putString(getActivity().getContentResolver(),
                                        settingsKey,
                                        getOwner().sFinalActionDialogArray.values[item]);
                                getOwner().reloadSettings();
                            }
                        }
                    })
                    .create();
                case DLG_RESET_TO_DEFAULT:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.shortcut_action_reset)
                    .setMessage(R.string.reset_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getOwner().resetToDefault();
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

    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                        boolean enabled) {
                ArrayList<SearchIndexableResource> result =
                        new ArrayList<SearchIndexableResource>();

                SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.hwkey_settings;
                result.add(sir);

                return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                ArrayList<String> result = new ArrayList<String>();
                return result;
            }
        };

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.VRTOXIN_AOSP;
    }

}
