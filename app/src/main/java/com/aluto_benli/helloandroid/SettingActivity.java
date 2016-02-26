package com.aluto_benli.helloandroid;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit(); // Note: `R.id.content` without `android` got error

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Use PreferenceFragment
     */
    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
