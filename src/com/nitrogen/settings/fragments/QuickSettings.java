package com.nitrogen.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.SearchIndexableResource;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import java.util.List;
import java.util.ArrayList;

import com.nitrogen.settings.preferences.CustomSeekBarPreference;
import com.nitrogen.settings.preferences.Utils;

public class QuickSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener, Indexable {

    private static final String OMNI_QS_PANEL_BG_ALPHA = "qs_panel_bg_alpha";
    private static final String QS_TILE_STYLE = "qs_tile_style";

    private CustomSeekBarPreference mQsPanelAlpha;
    private ListPreference mQsTileStyle;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.nitrogen_settings_quicksettings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mQsPanelAlpha = (CustomSeekBarPreference) findPreference(OMNI_QS_PANEL_BG_ALPHA);
        int qsPanelAlpha = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_QS_PANEL_BG_ALPHA, 255, UserHandle.USER_CURRENT);
        mQsPanelAlpha.setValue(qsPanelAlpha);
        mQsPanelAlpha.setOnPreferenceChangeListener(this);

         mQsTileStyle = (ListPreference) findPreference(QS_TILE_STYLE);
         int qsTileStyle = Settings.System.getIntForUser(resolver,
               Settings.System.QS_TILE_STYLE, 0,
	       UserHandle.USER_CURRENT);
         int valueIndex = mQsTileStyle.findIndexOfValue(String.valueOf(qsTileStyle));
         mQsTileStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
         mQsTileStyle.setSummary(mQsTileStyle.getEntry());
         mQsTileStyle.setOnPreferenceChangeListener(this);
        }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mQsPanelAlpha) {
            int bgAlpha = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.OMNI_QS_PANEL_BG_ALPHA, bgAlpha,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsTileStyle) {
            int qsTileStyleValue = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(resolver, Settings.System.QS_TILE_STYLE,
                    qsTileStyleValue, UserHandle.USER_CURRENT);
            mQsTileStyle.setSummary(mQsTileStyle.getEntries()[qsTileStyleValue]);
       }

        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NITROGEN_SETTINGS;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();

                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.nitrogen_settings_quicksettings;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
