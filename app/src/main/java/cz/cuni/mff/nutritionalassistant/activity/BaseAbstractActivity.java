package cz.cuni.mff.nutritionalassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.data.PersistentStorage;
import cz.cuni.mff.nutritionalassistant.data.PersistentStorageBySharedPrefs;
import cz.cuni.mff.nutritionalassistant.util.MyGson;

public abstract class BaseAbstractActivity extends AppCompatActivity {
    private PersistentStorage storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = new PersistentStorageBySharedPrefs(this);

        storage.load();
    }
}
