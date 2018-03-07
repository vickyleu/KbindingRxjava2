package com.benny.library.kbinding.bind

import com.benny.library.kbinding.converter.EmptyTwoWayConverter
import com.benny.library.kbinding.converter.TwoWayConverter
import com.benny.library.kbinding.viewmodel.Property
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate


/**
 * Created by benny on 11/17/15.
 */

class TwoWayPropertyBinding<T, R>(key: String, val observable: Observable<T>, val observer: Consumer<in T>, converter: TwoWayConverter<T, R> = EmptyTwoWayConverter()) : PropertyBinding() {
    var key: String = key
        private set

    val converter: TwoWayConverter<T, R> = converter

    fun prefix(prefix: String): TwoWayPropertyBinding<T, R> {
        if (!prefix.isEmpty()) key = "$prefix.$key"
        return this
    }

    fun bindTo(property: Property<R?>): Disposable {
        val cs = CompositeDisposable()
        val breaker = CircleBreaker<T>()
        cs.add(observable.filter(breaker).map {
            converter.convert(it)
        }.doOnSubscribe { LogBind(key, "TwoWay") }
                .doOnDispose { LogUnbind(key, "TwoWay") }
//                .doOnUnsubscribe { LogUnbind(key, "TwoWay") }
                .subscribe({
                    property.observer.onNext(it)
                })
        )
        cs.add(property.observable.map { converter.convertBack(it) }.filter(breaker)
                .doOnSubscribe { LogBind(key, "TwoWay") }
                .doOnDispose { LogUnbind(key, "TwoWay") }
//                .doOnUnsubscribe { LogUnbind(key, "TwoWay") }
                .subscribe(observer)
        )
        return cs
    }

    private class CircleBreaker<T> : Predicate<T> {
        /**
         * Test the given input value and return a boolean.
         * @param t the value
         * @return the boolean result
         * @throws Exception on error
         */
        override fun test(t: T): Boolean {
            if (last != null && t.toString() == last!!.toString()) return false
            last = t
            return true
        }

        private var last: T? = null
    }
}