package com.benny.library.kbinding.adapterview.bindings.utils

import android.support.v7.widget.RecyclerView
import com.benny.library.autoadapter.listener.AdapterPagingListener
import com.benny.library.kbinding.adapterview.bindings.PAGING_LISTENER
import com.benny.library.kbinding.adapterview.bindings.setPagingListener
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.MainThreadDisposable

/**
 * Created by benny on 12/26/15.
 */

@Suppress("UNCHECKED_CAST")
class RecyclerViewPagingOnSubscribe(val view: RecyclerView) : ObservableOnSubscribe<Pair<Int, Any?>> {
    /**
     * Called for each Observer that subscribes.
     * @param e the safe emitter instance, never null
     * @throws Exception on error
     */
    override fun subscribe(e: ObservableEmitter<Pair<Int, Any?>>) {
        val pagingListener = AdapterPagingListener<Any?> { receiver, previous, position ->
            if (e.isDisposed) return@AdapterPagingListener
            e.onNext(Pair(position, previous));
        }

        view.setPagingListener(pagingListener)
        e.setDisposable(object : MainThreadDisposable() {
            override fun onDispose() {
                (view.tag as HashMap<String, Any?>).put(PAGING_LISTENER, null)
            }
        })
    }
}