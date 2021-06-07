package com.paint.randompeoplek.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class OnTimeLiveData<T> : MutableLiveData<T>() {

    private var isUpdateSent = true

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, { t ->
            if (!isUpdateSent) {
                isUpdateSent = true
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        isUpdateSent = false
        super.setValue(t)
    }

}