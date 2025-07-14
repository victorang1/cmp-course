package org.example.cmpcourse.multinavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue;
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun MultiNavigationRootScreen(component: MultiNavigationRootComponent) {
    val childStack = component.childStack.subscribeAsState()

    Children(stack = childStack.value) { child ->
        when (val ch = child.instance) {
            is MultiNavigationRootComponent.RootChild.Auth -> MultiNavigationAuthScreen(ch.component)
            is MultiNavigationRootComponent.RootChild.Main -> MultiNavigationMainScreen(ch.component)
        }
    }
}

@Composable
fun MultiNavigationAuthScreen(component: AuthComponent) {
    val childStack = component.childStack.subscribeAsState()

    Children(stack = childStack.value) { child ->
        when (val ch = child.instance) {
            is AuthComponent.AuthChild.Welcome -> MultiNavigationWelcomeScreen(ch.component)
            is AuthComponent.AuthChild.Register -> MultiNavigationRegisterScreen(ch.component)
            is AuthComponent.AuthChild.Login -> MultiNavigationLoginScreen(ch.component)
        }
    }
}

@Composable
fun MultiNavigationWelcomeScreen(component: WelcomeComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            component.goToRegister()
        }) {
            Text(text = "Register")
        }
        Button(onClick = {
            component.goToLogin()
        }) {
            Text(text = "Login")
        }
    }
}

@Composable
fun MultiNavigationRegisterScreen(component: RegisterComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            component.register()
        }) {
            Text(text = "Register")
        }
    }
}

@Composable
fun MultiNavigationLoginScreen(component: LoginComponent) {
    val email by component.email.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { newEmail ->
                component.updateEmail(newEmail)
            }
        )
        Button(onClick = {
            component.login()
        }) {
            Text(text = "Login")
        }
    }
}

@Composable
fun MultiNavigationMainScreen(component: MainComponent) {
    val childStack = component.childStack.subscribeAsState()

    Children(stack = childStack.value) { child ->
        when (val ch = child.instance) {
            is MainComponent.MainChild.MainTabs -> MultiNavigationMainTabsScreen(ch.component)
            is MainComponent.MainChild.Detail -> MultiNavigationDetailScreen(ch.component)
        }
    }
}

@Composable
fun MultiNavigationMainTabsScreen(component: MainTabsComponent) {
    val childSlot = component.child.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = childSlot.value.child?.instance is MainTabsComponent.MainTabsChild.Home,
                    onClick = {
                        component.selectTab(MainTabsComponent.Tab.HOME)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Home")
                    }
                )
                NavigationBarItem(
                    selected = childSlot.value.child?.instance is MainTabsComponent.MainTabsChild.Profile,
                    onClick = {
                        component.selectTab(MainTabsComponent.Tab.PROFILE)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Profile")
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val ch = childSlot.value.child?.instance) {
                is MainTabsComponent.MainTabsChild.Home -> MultiNavigationHomeScreen(ch.component)
                is MainTabsComponent.MainTabsChild.Profile -> MultiNavigationProfileScreen(ch.component)
                else -> {}
            }
        }
    }
}

@Composable
fun MultiNavigationHomeScreen(component: HomeComponent) {
    val email by component.email.collectAsStateWithLifecycle()
    val todos by component.todo.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                component.addTodo()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            items(todos) { todo ->
                Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = todo.id)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = todo.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        Checkbox(
                            checked = todo.isDone == 1L,
                            onCheckedChange = {
                                //
                            }
                        )
                    }
                }
            }
        }
    }

//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = email
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = {
//            component.goToDetail()
//        }) {
//            Text(text = "Go To Detail")
//        }
//    }
}

@Composable
fun MultiNavigationProfileScreen(component: ProfileComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile Screen")
        Button(onClick = {
            component.doLogout()
        }) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun MultiNavigationDetailScreen(component: DetailComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            component.goBack()
        }) {
            Text(text = "Go Back!")
        }
    }
}