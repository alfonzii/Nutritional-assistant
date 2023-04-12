package cz.cuni.mff.nutritionalassistant.data;

import android.content.Context;
import android.content.SharedPreferences;

import cz.cuni.mff.nutritionalassistant.util.MyGson;

import static android.content.Context.MODE_PRIVATE;

public final class PersistentStorageBySharedPrefs implements PersistentStorage {

    private static final String SHARED_PREFERENCES_FILE = "cz.cuni.mff.nutritionalassistant";
    private Context context;
    private DataHolder dataHolder;

    public PersistentStorageBySharedPrefs(Context context) {
        this.context = context;
        dataHolder = DataHolder.getInstance();
    }

    @Override
    public void save() {
        SharedPreferences mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        String json = MyGson.PolymorphicGson.getInstance().toJson(dataHolder);
        preferencesEditor.putString(DataHolder.class.getName(), json);
        preferencesEditor.apply();
    }

    @Override
    public void load() {

        if (!dataHolder.isInitialized()) {
            SharedPreferences mPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

            String json = mPreferences.getString(DataHolder.class.getName(), "");
            // first run if equals ""
            if (!json.equals("")) {
                DataHolder.setInstance(MyGson.PolymorphicGson.getInstance().fromJson(json, DataHolder.class));
            }
            dataHolder.setInitialized(true);
        }
    }
}
