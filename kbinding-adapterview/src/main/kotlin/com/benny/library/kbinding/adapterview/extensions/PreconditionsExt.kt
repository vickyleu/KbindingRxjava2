package com.benny.library.kbinding.adapterview.extensions

import android.os.Looper
import io.reactivex.ObservableEmitter

/**
 * Created by vicky on 2018.03.06.
 *
 * @Author vicky
 * @Date 2018年03月06日  13:48:02
 * @ClassName 这里输入你的类名(或用途)
 */
class Preconditions {
    companion object {
        fun checkUiThread(e: ObservableEmitter<*>) {
            if (Looper.getMainLooper() != Looper.myLooper()) {
                e.onError(IllegalStateException(
                        "Must be called from the main thread. Was: " + Thread.currentThread()))
            }
        }
    }
}
