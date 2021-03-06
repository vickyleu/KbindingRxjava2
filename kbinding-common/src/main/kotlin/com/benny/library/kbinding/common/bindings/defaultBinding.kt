package com.benny.library.kbinding.common.bindings

import android.view.View
import com.benny.library.kbinding.bind.PropertyBinding
import com.benny.library.kbinding.bind.commandBinding
import com.benny.library.kbinding.util.getSubject
import com.benny.library.kbinding.util.getUnitSubject

/**
 * Created by ldy on 16/8/25.
 * Every View have a default subject,here use it
 */
fun View.postDefaultEvent() = getUnitSubject().onNext(Unit)

fun View.defaultCommand(path: String): PropertyBinding = commandBinding(path, getUnitSubject())

fun <T> View.postDefaultEvent(t: T?) = if (t != null) getSubject<T>().onNext(t) else Unit
fun <T> View.defaultArgCommand(path: String): PropertyBinding = commandBinding(path, getSubject<T>())
