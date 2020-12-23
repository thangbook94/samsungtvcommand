package com.example.samsungtvcontrol.tvcommand;

import com.samsung.multiscreen.Search.OnServiceFoundListener;
import com.samsung.multiscreen.Search.OnServiceLostListener;
import com.samsung.multiscreen.Search.OnStartListener;
import com.samsung.multiscreen.Search.OnStopListener;
import com.samsung.multiscreen.Service;

public abstract class SearchListener implements OnStartListener,
        OnStopListener, OnServiceFoundListener, OnServiceLostListener {

    @Override
    public void onLost(Service service) {
    }

    @Override
    public void onFound(Service service) {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onStart() {
    }
}
