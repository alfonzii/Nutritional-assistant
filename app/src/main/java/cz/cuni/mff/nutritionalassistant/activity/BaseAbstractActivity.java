package cz.cuni.mff.nutritionalassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.util.MyGson;

public abstract class BaseAbstractActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_FILE = "cz.cuni.mff.nutritionalassistant";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataHolder dataHolder = DataHolder.getInstance();

        if (!dataHolder.isInitialized()) {
            SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

            String json = mPreferences.getString(DataHolder.class.getName(), "");
            // first run if equals ""
            if (!json.equals("")) {
                DataHolder.setInstance(MyGson.PolymorphicGson.getInstance().fromJson(json, DataHolder.class));
            }
            DataHolder.getInstance().setInitialized(true);
        }
    }
}
