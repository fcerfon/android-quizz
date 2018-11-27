package om.superquizz.diginamic.superquizz.ui.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import om.superquizz.diginamic.superquizz.R;

public class SettingsFragment extends Fragment {

    SharedPreferences mSettings;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch sw = v.findViewById(R.id.galamadriabouyak);

        try {
            mSettings = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        }   catch (Exception e) {
                Log.e("settings", e.getMessage());
        }
        boolean actualValue = mSettings.getBoolean("save_answer", true);
        sw.setChecked(actualValue);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences mSettings = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("save_answer", isChecked);
                editor.apply();
            }
        });

        return v;
    }
}
