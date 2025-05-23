package org.example.cmpcourse.multinavigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.navigate
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.example.cmpcourse.common.componentScope
import org.example.cmpcourse.model.Todo
import org.example.cmpcourse.repository.AppRepository
import org.example.cmpcourse.repository.TodoRepository
import org.example.cmpcourse.settings.AppSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.random.Random

class MultiNavigationRootComponent(context: ComponentContext) : ComponentContext by context {

    private val _initialConfiguration: MutableStateFlow<RootConfiguration> = MutableStateFlow(
        RootConfiguration.Auth
    )

    val navigation = StackNavigation<RootConfiguration>()

    init {
        val email = AppSettings.getString(AppSettings.EMAIL, "")
        if (email.isNotBlank()) {
            _initialConfiguration.value = RootConfiguration.Main
        } else {
            _initialConfiguration.value = RootConfiguration.Auth
        }
    }

    val childStack = childStack(
        source = navigation,
        initialConfiguration = _initialConfiguration.value,
        childFactory = ::createRootChild,
        serializer = RootConfiguration.serializer(),
        key = "MultiNavigation",
        handleBackButton = true
    )

    private fun createRootChild(
        configuration: RootConfiguration,
        context: ComponentContext
    ): RootChild {
        return when (configuration) {
            is RootConfiguration.Auth -> {
                val authComponent = AuthComponent(
                    context = context,
                    onAuthSuccess = {
                        navigation.replaceAll(RootConfiguration.Main)
                    }
                )
                RootChild.Auth(authComponent)
            }

            is RootConfiguration.Main -> {
                val mainComponent = MainComponent(
                    context = context,
                    logout = {
                        navigation.replaceAll(RootConfiguration.Auth)
                    }
                )
                RootChild.Main(mainComponent)
            }
        }
    }

    @Serializable
    sealed class RootConfiguration {
        @Serializable
        data object Auth : RootConfiguration()

        @Serializable
        data object Main : RootConfiguration()
    }

    sealed class RootChild {
        class Auth(val component: AuthComponent) : RootChild()
        class Main(val component: MainComponent) : RootChild()
    }
}

class AuthComponent(context: ComponentContext, private val onAuthSuccess: () -> Unit) :
    ComponentContext by context {

    val navigation = StackNavigation<AuthScreen>()

    val childStack = childStack(
        source = navigation,
        initialConfiguration = AuthScreen.Welcome,
        childFactory = ::createAuthChild,
        serializer = AuthScreen.serializer()
    )

    private fun createAuthChild(screen: AuthScreen, context: ComponentContext): AuthChild {
        return when (screen) {
            is AuthScreen.Welcome -> {
                val component = WelcomeComponent(
                    context = context,
                    navigateToRegister = {
                        navigation.pushNew(AuthScreen.Register)
                    },
                    navigateToLogin = {
                        navigation.pushNew(AuthScreen.Login)
                    }
                )
                AuthChild.Welcome(component)
            }

            is AuthScreen.Register -> {
                val component = RegisterComponent(
                    context = context,
                    onBack = {
                        navigation.pop()
                    },
                    navigateToMain = {
                        onAuthSuccess()
                    }
                )
                AuthChild.Register(component)
            }

            is AuthScreen.Login -> {
                val component = LoginComponent(
                    context = context,
                    onBack = {
                        navigation.pop()
                    },
                    navigateToMain = {
                        onAuthSuccess()
                    }
                )
                AuthChild.Login(component)
            }
        }
    }

    @Serializable
    sealed class AuthScreen {
        @Serializable
        data object Welcome : AuthScreen()

        @Serializable
        data object Register : AuthScreen()

        @Serializable
        data object Login : AuthScreen()
    }

    sealed class AuthChild {
        class Welcome(val component: WelcomeComponent) : AuthChild()
        class Register(val component: RegisterComponent) : AuthChild()
        class Login(val component: LoginComponent) : AuthChild()
    }
}

class WelcomeComponent(
    context: ComponentContext,
    private val navigateToRegister: () -> Unit,
    private val navigateToLogin: () -> Unit
) : ComponentContext by context {

    fun goToRegister() {
        navigateToRegister()
    }

    fun goToLogin() {
        navigateToLogin()
    }
}

class RegisterComponent(
    context: ComponentContext,
    private val onBack: () -> Unit,
    private val navigateToMain: () -> Unit
) : ComponentContext by context {

    fun goBack() {
        onBack()
    }

    fun register() {
        // register

        navigateToMain()
    }
}

class LoginComponent(
    context: ComponentContext,
    private val onBack: () -> Unit,
    private val navigateToMain: () -> Unit
) : ComponentContext by context {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    fun goBack() {
        onBack()
    }

    fun updateEmail(newEmail: String) {
        _email.update { newEmail }
    }

    fun login() {
        val currentEmail = _email.value
        AppSettings.putString(
            AppSettings.EMAIL,
            currentEmail
        )

        navigateToMain()
    }
}

class MainComponent(context: ComponentContext, private val logout: () -> Unit) :
    ComponentContext by context {
    val navigation = StackNavigation<MainConfiguration>()

    sealed interface MainComponentEffect {
        data object AddEditTodoSuccess : MainComponentEffect
    }

    private val _uiEffect: MutableSharedFlow<MainComponentEffect> = MutableSharedFlow()

    private val scope = componentScope()

    val childStack = childStack(
        source = navigation,
        initialConfiguration = MainConfiguration.MainTabs,
        childFactory = ::createMainChild,
        serializer = MainConfiguration.serializer()
    )

    private fun createMainChild(
        configuration: MainConfiguration,
        context: ComponentContext
    ): MainChild {
        return when (configuration) {
            is MainConfiguration.MainTabs -> {
                val component = MainTabsComponent(
                    context = context,
                    effect = _uiEffect,
                    navigateToDetail = {
                        navigation.pushNew(MainConfiguration.Detail)
                    },
                    logout = logout,
                    navigateToAddEditTodo = { todo ->
                        navigation.pushNew(
                            MainConfiguration.AddEditTodo(todo)
                        )
                    }
                )
                MainChild.MainTabs(component)
            }

            is MainConfiguration.Detail -> {
                val component = DetailComponent(
                    context = context,
                    onBack = {
                        navigation.pop()
                    }
                )
                MainChild.Detail(component)
            }

            is MainConfiguration.AddEditTodo -> {
                val component = AddEditTodoComponent.factory(
                    context = context,
                    todo = configuration.todo,
                    onComplete = {
                        navigation.pop()
                        scope.launch {
                            _uiEffect.emit(MainComponentEffect.AddEditTodoSuccess)
                        }
                    }
                )
                MainChild.AddEditTodo(component)
            }
        }
    }

    @Serializable
    sealed class MainConfiguration {
        @Serializable
        data object MainTabs : MainConfiguration()

        @Serializable
        data object Detail : MainConfiguration()

        @Serializable
        data class AddEditTodo(val todo: Todo?) : MainConfiguration()
    }

    sealed class MainChild {
        class MainTabs(val component: MainTabsComponent) : MainChild()
        class Detail(val component: DetailComponent) : MainChild()
        class AddEditTodo(val component: AddEditTodoComponent) : MainChild()
    }
}

class MainTabsComponent(
    context: ComponentContext,
    private val effect: MutableSharedFlow<MainComponent.MainComponentEffect>,
    private val navigateToDetail: () -> Unit,
    private val navigateToAddEditTodo: (todo: Todo?) -> Unit,
    private val logout: () -> Unit
) : ComponentContext by context {
    enum class Tab {
        HOME, PROFILE
    }

    private val selectedTab = SlotNavigation<Tab>()

    val child = childSlot(
        source = selectedTab,
        serializer = null,
        initialConfiguration = {
            Tab.HOME
        },
        childFactory = ::createMainTabsChild,
        key = "MainTabs"
    )

    fun selectTab(tab: Tab) {
        selectedTab.navigate { tab }
    }

    private fun createMainTabsChild(tab: Tab, context: ComponentContext): MainTabsChild {
        return when (tab) {
            Tab.HOME -> {
                val component = HomeComponent.factory(
                    context = context,
                    effect = effect,
                    navigateToDetail = {
                        navigateToDetail()
                    },
                    navigateToAddEditTodo = { todo ->
                        navigateToAddEditTodo(todo)
                    }
                )
                MainTabsChild.Home(
                    component
                )
            }

            Tab.PROFILE -> {
                val component = ProfileComponent(
                    context = context,
                    logout = logout
                )
                MainTabsChild.Profile(
                    component
                )
            }
        }
    }

    sealed class MainTabsChild {
        class Home(val component: HomeComponent) : MainTabsChild()
        class Profile(val component: ProfileComponent) : MainTabsChild()
    }
}

class HomeComponent(
    context: ComponentContext,
    private val effect: MutableSharedFlow<MainComponent.MainComponentEffect>,
    private val appRepository: AppRepository,
    private val todoRepository: TodoRepository,
    private val navigateToDetail: () -> Unit,
    private val navigateToAddEditTodo: (todo: Todo?) -> Unit
) : ComponentContext by context {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _todos: MutableStateFlow<List<Todo>> = MutableStateFlow(emptyList())
    val todo: StateFlow<List<Todo>> = _todos

    private val scope = componentScope()

    companion object : KoinComponent {
        fun factory(
            context: ComponentContext,
            effect: MutableSharedFlow<MainComponent.MainComponentEffect>,
            navigateToDetail: () -> Unit,
            navigateToAddEditTodo: (todo: Todo?) -> Unit
        ): HomeComponent {
            return HomeComponent(
                context = context,
                effect = effect,
                navigateToDetail = navigateToDetail,
                appRepository = get(),
                todoRepository = get(),
                navigateToAddEditTodo = navigateToAddEditTodo
            )
        }
    }

    init {
        val currentEmail = appRepository.getLoggedInEmail()
        fetchTodos()
        _email.update { currentEmail }
        scope.launch {
            effect.collect { ef ->
                when (ef) {
                    is MainComponent.MainComponentEffect.AddEditTodoSuccess -> {
                        fetchTodos()
                    }
                }
            }
        }
    }

    fun goToDetail() {
        navigateToDetail()
    }

    private fun fetchTodos() {
        scope.launch {
            val newTodos = todoRepository.getAll()
            _todos.update { newTodos }
        }
    }

    fun goToAddEditTodo(todo: Todo) {
        navigateToAddEditTodo(todo)
    }

    fun addTodo() {
        navigateToAddEditTodo(null)
    }

    fun onCheckChange(todo: Todo, newChecked: Boolean) {
        scope.launch {
            todoRepository.update(
                todo.copy(
                    isDone = if (newChecked) 1 else 0
                )
            )
            fetchTodos()
        }
    }

    fun delete(todo: Todo) {
        scope.launch {
            todoRepository.delete(todo.id)
            fetchTodos()
        }
    }
}

class ProfileComponent(context: ComponentContext, private val logout: () -> Unit) :
    ComponentContext by context {

    fun doLogout() {
        AppSettings.logout()
        logout()
    }
}

class DetailComponent(context: ComponentContext, private val onBack: () -> Unit) :
    ComponentContext by context {

    fun goBack() {
        onBack()
    }
}

class AddEditTodoComponent(
    context: ComponentContext,
    private val todo: Todo? = null,
    private val todoRepository: TodoRepository,
    private val onComplete: () -> Unit
) : ComponentContext by context {

    private val _uiState: MutableStateFlow<AddEditTodoUiState> = MutableStateFlow(AddEditTodoUiState())
    val uiState: StateFlow<AddEditTodoUiState> = _uiState.asStateFlow()

    private val scope = componentScope()

    companion object : KoinComponent {
        fun factory(
            context: ComponentContext,
            todo: Todo? = null,
            onComplete: () -> Unit
        ): AddEditTodoComponent {
            return AddEditTodoComponent(
                context = context,
                todo = todo,
                todoRepository = get(),
                onComplete = onComplete
            )
        }
    }

    init {
        if (todo != null) {
            _uiState.update { uiState ->
                uiState.copy(
                    id = todo.id,
                    title = todo.title,
                    isDone = todo.isDone,
                    idEnabled = false
                )
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.UpdateID -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        id = event.id
                    )
                }
            }

            is AddEditTodoEvent.UpdateTitle -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        title = event.title
                    )
                }
            }

            is AddEditTodoEvent.OnIsDoneCheckedChange -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isDone = if (event.checked) 1L else 0L
                    )
                }
            }

            is AddEditTodoEvent.Submit -> {
                scope.launch {
                    val uiState = _uiState.value
                    val newTodo = Todo(
                        id = uiState.id,
                        title = uiState.title,
                        isDone = uiState.isDone
                    )
                    if (todo != null) {
                        todoRepository.update(newTodo)
                    } else {
                        todoRepository.add(newTodo)
                    }
                    onComplete()
                }
            }
        }
    }

    sealed interface AddEditTodoEvent {
        data class UpdateID(val id: String) : AddEditTodoEvent
        data class UpdateTitle(val title: String) : AddEditTodoEvent
        data class OnIsDoneCheckedChange(val checked: Boolean) : AddEditTodoEvent
        data object Submit : AddEditTodoEvent
    }

    data class AddEditTodoUiState(
        val id: String = "",
        val title: String = "",
        val isDone: Long = 0,
        val idEnabled: Boolean = true
    )
}

