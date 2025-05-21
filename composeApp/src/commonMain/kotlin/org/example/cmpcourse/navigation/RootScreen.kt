package org.example.cmpcourse.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun RootScreen(component: RootComponent) {
    val childStack = component.childStack.subscribeAsState()

    Children(stack = childStack.value) { child ->
        when (val ch = child.instance) {
            is RootComponent.RootChild.ScreenA -> ScreenA(ch.component)
            is RootComponent.RootChild.ScreenB -> ScreenB(ch.component)
        }
    }
}