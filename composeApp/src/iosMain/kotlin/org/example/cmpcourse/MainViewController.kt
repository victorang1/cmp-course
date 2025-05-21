package org.example.cmpcourse

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.example.cmpcourse.multinavigation.MultiNavigationRootComponent

fun MainViewController() = ComposeUIViewController {
    val root = remember {
        MultiNavigationRootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}