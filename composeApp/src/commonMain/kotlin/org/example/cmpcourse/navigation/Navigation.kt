package org.example.cmpcourse.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable

class RootComponent(context: ComponentContext) : ComponentContext by context {

    val navigation = StackNavigation<RootScreen>()

    val childStack = childStack(
        source = navigation,
        serializer = RootScreen.serializer(),
        initialConfiguration = RootScreen.ScreenA,
        childFactory = ::createRootChild,
        key = "Root"
    )

    fun createRootChild(screen: RootScreen, context: ComponentContext): RootChild {
        return when (screen) {
            is RootScreen.ScreenA -> {
                val screenAComponent = ScreenAComponent(
                    context = context,
                    navigateToScreenB = {
                        navigation.pushNew(RootScreen.ScreenB("I am from Screen A"))
                    }
                )
                RootChild.ScreenA(screenAComponent)
            }

            is RootScreen.ScreenB -> {
                val screenBComponent = ScreenBComponent(context, screen.text)
                RootChild.ScreenB(screenBComponent)
            }
        }
    }

    @Serializable
    sealed class RootScreen {
        @Serializable
        data object ScreenA : RootScreen()
        @Serializable
        data class ScreenB(val text: String) : RootScreen()
    }

    sealed class RootChild {
        class ScreenA(val component: ScreenAComponent) : RootChild()
        class ScreenB(val component: ScreenBComponent) : RootChild()
    }
}

class ScreenAComponent(context: ComponentContext, val navigateToScreenB: () -> Unit) : ComponentContext by context {

    fun goToScreenB() {
        navigateToScreenB()
    }
}

class ScreenBComponent(context: ComponentContext, val text: String) : ComponentContext by context {

}