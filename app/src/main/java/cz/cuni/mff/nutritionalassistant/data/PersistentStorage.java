package cz.cuni.mff.nutritionalassistant.data;

import android.content.Context;

public interface PersistentStorage {
    // Save DataHolder state
    public void save();

    // Load DataHolder state
    public void load();
}
