package com.pangaea.taskflow.ui.shared.viewmodels;

import java.util.List;
import androidx.lifecycle.LiveData;

public interface ItemViewModel<M> {
    LiveData<List<M>> getModel();
    void insert(M task);
    void update(M task);
    void delete(M task);
}
