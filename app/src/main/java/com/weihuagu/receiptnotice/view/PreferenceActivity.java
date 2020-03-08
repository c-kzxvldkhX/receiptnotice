package com.weihuagu.receiptnotice.view;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.weihuagu.receiptnotice.util.AuthorityUtil;
import com.weihuagu.receiptnotice.MainApplication;
import com.weihuagu.receiptnotice.R;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();
    }


    public static class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (s.equals("isaccessibilityservice") && sharedPreferences.getBoolean(s, false) == true) {
                if (! AuthorityUtil.isAccessibilitySettingsOn(MainApplication.getAppContext()))
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

            }
        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }


    }


}
