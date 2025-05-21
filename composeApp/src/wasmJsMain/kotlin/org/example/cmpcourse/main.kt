package org.example.cmpcourse

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.browser.document
import org.example.cmpcourse.multinavigation.MultiNavigationRootComponent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponent = MultiNavigationRootComponent(
        DefaultComponentContext(lifecycle)
    )
    ComposeViewport(document.body!!) {
        App(rootComponent)
    }
}