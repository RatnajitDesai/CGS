package com.darpg33.hackathon.cgs.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.darpg33.hackathon.cgs.R;
import com.darpg33.hackathon.cgs.Utils.Fields;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private SettingsViewModel settingsViewModel;


    //widgets
    private TextView mSelectedLanguage;
    private MaterialButton mSelectLanguage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        mSelectedLanguage = root.findViewById(R.id.selectedLanguage);
        mSelectLanguage = root.findViewById(R.id.btnSelectLanguage);

        SharedPreferences preferences = getActivity().getSharedPreferences(Fields.SHARED_PREF_SETTINGS, Context.MODE_PRIVATE);
        String locale = preferences.getString(Fields.SHARED_PREF_SETTINGS_LOCALE, Locale.getDefault().getLanguage());

        switch (locale) {

            case "mr": {
                mSelectedLanguage.setText(getString(R.string.marathi));
                break;
            }

            case "en": {
                mSelectedLanguage.setText(getString(R.string.english));
                break;
            }
        }

        mSelectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] languages = {"English", "Marathi"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select language");
                builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {
                            case 0: {
                                setAppLocale(Locale.getDefault().getLanguage());
                                break;
                            }
                            case 1: {
                                setAppLocale("mr");
                                break;
                            }

                        }
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();


            }
        });


        return root;

    }


    private void setAppLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(locale));
        resources.updateConfiguration(configuration, metrics);
        Log.d(TAG, "setAppLocale: :" + locale);


//        //shared preferences
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Fields.SHARED_PREF_SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putString(Fields.SHARED_PREF_SETTINGS_LOCALE, locale);
        editor.apply();

        getActivity().recreate();

    }

}
