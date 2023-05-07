package com.pangaea.taskflow.ui.shared.viewmodels;

import java.util.List;
import java.util.function.Consumer;

import androidx.lifecycle.LiveData;

public interface ItemViewModel<M> {
    LiveData<List<M>> getModel();
    void update(M task);
    void delete(M task);
}
