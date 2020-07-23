package com.pangaea.taskflow.state;

import android.os.AsyncTask;

public class ModelAsyncTask<D, T> extends AsyncTask<T, Void, T> {

    public interface ModelAsyncListener<D, T> {
        void onExecute(D dao, T obj);
        //void onFinished(T t);
    }

    private final ModelAsyncListener taskListener;
    private final D dao;

    public ModelAsyncTask(D dao, ModelAsyncListener listener) {
        this.dao = dao;
        this.taskListener = listener;
    }

    @Override
    protected T doInBackground(final T... params) {
        if(this.taskListener != null) {
            taskListener.onExecute(dao, params[0]);
        }
        return params[0];
    }

//    @Override
//    protected void onPostExecute(final T result) {
//        super.onPostExecute(result);
//        if(this.taskListener != null) {
//            this.taskListener.onFinished(result);
//        }
//    }
}