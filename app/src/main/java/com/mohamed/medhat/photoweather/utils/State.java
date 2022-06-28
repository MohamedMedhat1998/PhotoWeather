package com.mohamed.medhat.photoweather.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({State.STATE_NORMAL, State.STATE_LOADING, State.STATE_ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface State {
    int STATE_NORMAL = 0;
    int STATE_LOADING = 1;
    int STATE_ERROR = 2;
}
