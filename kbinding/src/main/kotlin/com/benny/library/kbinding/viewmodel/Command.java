package com.benny.library.kbinding.viewmodel;

import io.reactivex.functions.Consumer;

/**
 * Created by benny on 12/16/15.
 */

public interface Command<T> {
    void execute(T param, Consumer<? super Boolean> canExecute);
}
