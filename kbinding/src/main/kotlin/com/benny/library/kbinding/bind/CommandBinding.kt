package com.benny.library.kbinding.bind

import com.benny.library.kbinding.viewmodel.Command
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by benny on 11/17/15.
 */

class CommandBinding<T>(key: String, val trigger: Observable<T>, val canExecute: Consumer<in Boolean> = Consumer {}) : PropertyBinding() {
    var key: String = key
        private set

    fun prefix(prefix: String): CommandBinding<T> {
        if (!prefix.isEmpty()) key = "$prefix.$key"
        return this
    }

    fun bindTo(command: Command<T>): Disposable {
        return trigger
                .doOnSubscribe { LogBind(key, "Command") }
                .doOnDispose {LogUnbind(key, "Command")}
//                .doOnUnsubscribe { LogUnbind(key, "Command") }
                .subscribe { it -> command.execute(it, canExecute) }
    }
}
