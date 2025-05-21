package org.example.cmpcourse

import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.example.cmpcourse.multinavigation.MultiNavigationRootComponent
import javax.swing.SwingUtilities

fun main() = application {
    val windowState = rememberWindowState()
    val lifecycle = LifecycleRegistry()

    val rootComponent = runOnUiThread {
        MultiNavigationRootComponent(DefaultComponentContext(lifecycle))
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "CMPCourse",
    ) {
        LifecycleController(
            lifecycle,
            windowState = windowState,
            windowInfo = LocalWindowInfo.current
        )
        App(rootComponent)
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    return result as T
}