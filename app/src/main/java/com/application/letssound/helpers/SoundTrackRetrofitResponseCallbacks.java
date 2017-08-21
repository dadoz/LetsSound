package com.application.letssound.helpers;

import java.util.ArrayList;

public interface SoundTrackRetrofitResponseCallbacks {
    void onObservableSuccess(ArrayList<Object> list);
    void onObservableSuccess(Object obj);
    void onObservableError(String type);
}
