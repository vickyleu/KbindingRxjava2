package com.benny.library.kbinding.viewmodel

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject


/**
 * Created by benny on 11/17/15.
 */

class Property<T>(val defaultValue: T? = null) {
    private var property: BehaviorSubject<T> = if (defaultValue == null) BehaviorSubject.create() else BehaviorSubject.createDefault(defaultValue)

    var value: T?
        get() = property.value
        set(value) {
            if (value != null) property.onNext(value)
        }

    val observable: Observable<T>
        get() {
            ensurePropertyInitialized()
            return property
        }

    val observer: Observer<T>
        get() {
            ensurePropertyInitialized()
            return property
        }

    fun ensurePropertyInitialized() {
        if (property.hasComplete()) property = if (defaultValue == null) BehaviorSubject.create() else BehaviorSubject.createDefault(defaultValue)
    }
}