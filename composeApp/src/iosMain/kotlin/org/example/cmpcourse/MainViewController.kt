package org.example.cmpcourse

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.example.cmpcourse.di.appModule
import org.example.cmpcourse.multinavigation.MultiNavigationRootComponent
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(appModule)
        }
    }
) {
    val root = remember {
        MultiNavigationRootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}