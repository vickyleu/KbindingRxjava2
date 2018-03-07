package com.benny.library.kbinding.adapterview.bindings.utils

import android.support.v7.widget.RecyclerView
import android.widget.AdapterView
import com.benny.library.kbinding.adapterview.bindings.setOnItemClickListener
import com.benny.library.kbinding.adapterview.extensions.Preconditions
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.MainThreadDisposable


/**
 * Created by benny on 2/3/16.
 */

class RecyclerViewItemClickOnSubscribe(val view: RecyclerView) : ObservableOnSubscribe<Int> {
    /**
     * Called for each Observer that subscribes.
     * @param e the safe emitter instance, never null
     * @throws Exception on error
     */
    override fun subscribe(e: ObservableEmitter<Int>) {
        Preconditions.checkUiThread(e)
        val listener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (!e.isDisposed) {
                e.onNext(position)
            }
        }
        view.setOnItemClickListener(listener)
        e.setDisposable(object : MainThreadDisposable() {
            override fun onDispose() {
                view.setOnItemClickListener(null)
            }
        })
    }
}