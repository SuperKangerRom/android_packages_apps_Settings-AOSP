/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.settings.search;

import android.provider.SearchIndexableResource;

import com.android.settings.DataUsageSummary;
import com.android.settings.DateTimeSettings;
import com.android.settings.DevelopmentSettings;
import com.android.settings.DeviceInfoSettings;
import com.android.settings.DisplaySettings;
import com.android.settings.LegalSettings;
import com.android.settings.PrivacySettings;
import com.android.settings.R;
import com.android.settings.ScreenPinningSettings;
import com.android.settings.SecuritySettings;
import com.android.settings.WallpaperTypeSettings;
import com.android.settings.WifiCallingSettings;
import com.android.settings.WirelessSettings;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.applications.AdvancedAppSettings;
import com.android.settings.applications.ManageDefaultApps;
import com.android.settings.bluetooth.BluetoothSettings;
import com.android.settings.deviceinfo.StorageSettings;
import com.android.settings.fuelgauge.BatterySaverSettings;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.inputmethod.InputMethodAndLanguageSettings;
import com.android.settings.location.LocationSettings;
import com.android.settings.location.ScanningSettings;
import com.android.settings.net.DataUsageMeteredSettings;
import com.android.settings.notification.NotificationSettings;
import com.android.settings.notification.OtherSoundSettings;
import com.android.settings.notification.ZenModePrioritySettings;
import com.android.settings.notification.ZenModeSettings;
import com.android.settings.print.PrintSettingsFragment;
import com.android.settings.sim.SimSettings;
import com.android.settings.users.UserSettings;
import com.android.settings.wifi.AdvancedWifiSettings;
import com.android.settings.wifi.SavedAccessPointsWifiSettings;
import com.android.settings.wifi.WifiSettings;
// VRToxin Additions
import com.android.settings.vrtoxin.MainSettings;
import com.android.settings.vrtoxin.HwKeySettings;
import com.android.settings.vrtoxin.InterfaceSettings;
import com.android.settings.vrtoxin.Misc;
import com.android.settings.vrtoxin.StatusBarSettings;
import com.android.settings.vrtoxin.VrtoxinNotifs;
import com.android.settings.vrtoxin.AmbientSettings;

import java.util.Collection;
import java.util.HashMap;

public final class SearchIndexableResources {

    public static int NO_DATA_RES_ID = 0;

    private static HashMap<String, SearchIndexableResource> sResMap =
            new HashMap<String, SearchIndexableResource>();

    static {
        sResMap.put(WifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WifiSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(AdvancedWifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AdvancedWifiSettings.class.getName()),
                        R.xml.wifi_advanced_settings,
                        AdvancedWifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(SavedAccessPointsWifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SavedAccessPointsWifiSettings.class.getName()),
                        R.xml.wifi_display_saved_access_points,
                        SavedAccessPointsWifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(BluetoothSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(BluetoothSettings.class.getName()),
                        NO_DATA_RES_ID,
                        BluetoothSettings.class.getName(),
                        R.drawable.ic_settings_bluetooth));

        sResMap.put(SimSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SimSettings.class.getName()),
                        NO_DATA_RES_ID,
                        SimSettings.class.getName(),
                        R.drawable.ic_sim_sd));

        sResMap.put(DataUsageSummary.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DataUsageSummary.class.getName()),
                        NO_DATA_RES_ID,
                        DataUsageSummary.class.getName(),
                        R.drawable.ic_settings_data_usage));

        sResMap.put(DataUsageMeteredSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DataUsageMeteredSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DataUsageMeteredSettings.class.getName(),
                        R.drawable.ic_settings_data_usage));

        sResMap.put(WirelessSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WirelessSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WirelessSettings.class.getName(),
                        R.drawable.ic_settings_more));

        sResMap.put(DisplaySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DisplaySettings.class.getName()),
                        NO_DATA_RES_ID,
                        DisplaySettings.class.getName(),
                        R.drawable.ic_settings_display));

        sResMap.put(WallpaperTypeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WallpaperTypeSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WallpaperTypeSettings.class.getName(),
                        R.drawable.ic_settings_display));

        sResMap.put(NotificationSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NotificationSettings.class.getName()),
                        NO_DATA_RES_ID,
                        NotificationSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(OtherSoundSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(OtherSoundSettings.class.getName()),
                        NO_DATA_RES_ID,
                        OtherSoundSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(ZenModeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ZenModeSettings.class.getName()),
                        NO_DATA_RES_ID,
                        ZenModeSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(ZenModePrioritySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ZenModePrioritySettings.class.getName()),
                        R.xml.zen_mode_priority_settings,
                        ZenModePrioritySettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(StorageSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(StorageSettings.class.getName()),
                        NO_DATA_RES_ID,
                        StorageSettings.class.getName(),
                        R.drawable.ic_settings_storage));

        sResMap.put(PowerUsageSummary.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PowerUsageSummary.class.getName()),
                        R.xml.power_usage_summary,
                        PowerUsageSummary.class.getName(),
                        R.drawable.ic_settings_battery));

        sResMap.put(BatterySaverSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(BatterySaverSettings.class.getName()),
                        R.xml.battery_saver_settings,
                        BatterySaverSettings.class.getName(),
                        R.drawable.ic_settings_battery));

        sResMap.put(AdvancedAppSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AdvancedAppSettings.class.getName()),
                        R.xml.advanced_apps,
                        AdvancedAppSettings.class.getName(),
                        R.drawable.ic_settings_applications));

        sResMap.put(ManageDefaultApps.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ManageDefaultApps.class.getName()),
                        NO_DATA_RES_ID,
                        ManageDefaultApps.class.getName(),
                        R.drawable.ic_settings_applications));

        sResMap.put(UserSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(UserSettings.class.getName()),
                        NO_DATA_RES_ID,
                        UserSettings.class.getName(),
                        R.drawable.ic_settings_multiuser));

        sResMap.put(LocationSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LocationSettings.class.getName()),
                        R.xml.location_settings,
                        LocationSettings.class.getName(),
                        R.drawable.ic_settings_location));

        sResMap.put(ScanningSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ScanningSettings.class.getName()),
                        R.xml.location_scanning,
                        ScanningSettings.class.getName(),
                        R.drawable.ic_settings_location));

        sResMap.put(SecuritySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SecuritySettings.class.getName()),
                        NO_DATA_RES_ID,
                        SecuritySettings.class.getName(),
                        R.drawable.ic_settings_security));

        sResMap.put(ScreenPinningSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ScreenPinningSettings.class.getName()),
                        NO_DATA_RES_ID,
                        ScreenPinningSettings.class.getName(),
                        R.drawable.ic_settings_security));

        sResMap.put(InputMethodAndLanguageSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(InputMethodAndLanguageSettings.class.getName()),
                        NO_DATA_RES_ID,
                        InputMethodAndLanguageSettings.class.getName(),
                        R.drawable.ic_settings_language));

        sResMap.put(PrivacySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PrivacySettings.class.getName()),
                        NO_DATA_RES_ID,
                        PrivacySettings.class.getName(),
                        R.drawable.ic_settings_backup));

        sResMap.put(DateTimeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DateTimeSettings.class.getName()),
                        R.xml.date_time_prefs,
                        DateTimeSettings.class.getName(),
                        R.drawable.ic_settings_date_time));

        sResMap.put(AccessibilitySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AccessibilitySettings.class.getName()),
                        NO_DATA_RES_ID,
                        AccessibilitySettings.class.getName(),
                        R.drawable.ic_settings_accessibility));

        sResMap.put(PrintSettingsFragment.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PrintSettingsFragment.class.getName()),
                        NO_DATA_RES_ID,
                        PrintSettingsFragment.class.getName(),
                        R.drawable.ic_settings_print));

        sResMap.put(DevelopmentSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DevelopmentSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DevelopmentSettings.class.getName(),
                        R.drawable.ic_settings_development));

        sResMap.put(DeviceInfoSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DeviceInfoSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DeviceInfoSettings.class.getName(),
                        R.drawable.ic_settings_about));

        sResMap.put(LegalSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LegalSettings.class.getName()),
                        NO_DATA_RES_ID,
                        LegalSettings.class.getName(),
                        R.drawable.ic_settings_about));

        sResMap.put(WifiCallingSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WifiCallingSettings.class.getName()),
                        R.xml.wifi_calling_settings,
                        WifiCallingSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(MainSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(MainSettings.class.getName()),
                        NO_DATA_RES_ID,
                        MainSettings.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(InterfaceSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(InterfaceSettings.class.getName()),
                        NO_DATA_RES_ID,
                        InterfaceSettings.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(StatusBarSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(StatusBarSettings.class.getName()),
                        NO_DATA_RES_ID,
                        StatusBarSettings.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(HwKeySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(HwKeySettings.class.getName()),
                        NO_DATA_RES_ID,
                        HwKeySettings.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(Misc.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(Misc.class.getName()),
                        NO_DATA_RES_ID,
                        Misc.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(VrtoxinNotifs.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(VrtoxinNotifs.class.getName()),
                        NO_DATA_RES_ID,
                        VrtoxinNotifs.class.getName(),
                        R.drawable.ic_settings_vrtoxin));

        sResMap.put(AmbientSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AmbientSettings.class.getName()),
                        NO_DATA_RES_ID,
                        AmbientSettings.class.getName(),
                        R.drawable.ic_settings_display));
    }

    private SearchIndexableResources() {
    }

    public static int size() {
        return sResMap.size();
    }

    public static SearchIndexableResource getResourceByName(String className) {
        return sResMap.get(className);
    }

    public static Collection<SearchIndexableResource> values() {
        return sResMap.values();
    }
}
