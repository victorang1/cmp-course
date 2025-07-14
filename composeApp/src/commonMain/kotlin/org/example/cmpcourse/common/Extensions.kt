package org.example.cmpcourse.common

import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

fun LifecycleOwner.componentScope(): CoroutineScope {
    val scope = CoroutineScope(Dispatchers.Main.immediate)
    lifecycle.doOnDestroy(scope::cancel)

    return scope
}