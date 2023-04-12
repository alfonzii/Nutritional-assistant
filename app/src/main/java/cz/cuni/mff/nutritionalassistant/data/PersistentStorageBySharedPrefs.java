package cz.cuni.mff.nutritionalassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import cz.cuni.mff.nutritionalassistant.util.MyGson;

import static android.content.Context.MODE_PRIVATE;

public final class PersistentStorageBySharedPrefs implements PersistentStorage {

    public static final String SHARED_PREFERENCES_FILE = "cz.cuni.mff.nutritionalassistant";
    private Context context;

    public PersistentStorageBySharedPrefs(Context context) {
        this.context = context;
    }

    @Override
    public void save() {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        String json = MyGson.PolymorphicGson.getInstance().toJson(DataHolder.getInstance());
        preferencesEditor.putString(DataHolder.class.getName(), json);
        preferencesEditor.apply();
    }

    @Override
    public void load() {
        DataHolder dataHolder = DataHolder.getInstance();

        if (!dataHolder.isInitialized()) {
            SharedPreferences mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

            String json = mPreferences.getString(DataHolder.class.getName(), "");
            // first run if equals ""
            if (!json.equals("")) {
                DataHolder.setInstance(MyGson.PolymorphicGson.getInstance().fromJson(json, DataHolder.class));
            }
            DataHolder.getInstance().setInitialized(true);
        }
    }
}
