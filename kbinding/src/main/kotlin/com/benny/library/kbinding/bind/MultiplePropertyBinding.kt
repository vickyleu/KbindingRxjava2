package com.benny.library.kbinding.bind

import com.benny.library.kbinding.converter.OneWayConverter
import com.benny.library.kbinding.viewmodel.Property
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by benny on 11/17/15.
 */

class MultiplePropertyBinding<T>(keys: List<String>, val observer: Consumer<in T>, val oneTime: Boolean, val multipleConverter: OneWayConverter<Array<Any?>, T>) : PropertyBinding() {
    var keys: List<String> = keys
        private set

    fun bindTo(properties: List<Property<*>>): Disposable {
        val ob = Observable.combineLatest(properties.map { property -> property.observable }, { multipleConverter.convert(it) })
                .doOnSubscribe { LogBind(keys, "OneWay") }
                .doOnDispose { LogUnbind(keys, "OneWay") }
//                .doOnUnsubscribe { LogUnbind(keys, "OneWay") }
        return if (!oneTime) ob.subscribe(observer) else ob.take(1).subscribe(observer)
    }

    fun prefix(prefix: String): MultiplePropertyBinding<T> {
        if (!prefix.isEmpty()) keys = keys.map { it -> "$prefix.$it" }
        return this
    }
}