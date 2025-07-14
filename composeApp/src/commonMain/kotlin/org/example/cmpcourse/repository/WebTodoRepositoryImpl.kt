package org.example.cmpcourse.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.cmpcourse.model.Todo

class WebTodoRepositoryImpl(
    private val settings: Settings
) : TodoRepository {

    private val key = "todo_list"
    private val json = Json { ignoreUnknownKeys = true }

    private suspend fun loadTodos(): MutableList<Todo> {
        return withContext(Dispatchers.Default) {
            val storedTodos = settings.getString(key, "")
            try {
                json.decodeFromString(storedTodos)
            } catch (ex: Exception) {
                mutableListOf<Todo>()
            }
        }
    }

    override suspend fun getAll(): List<Todo> {
        return loadTodos()
    }

    override suspend fun add(todo: Todo) {
        val currentTodos = loadTodos()
        currentTodos.add(todo)
        saveTodo(currentTodos)
    }

    private fun saveTodo(todos: MutableList<Todo>) {
        val jsonTodos = json.encodeToString(todos)
        settings.putString(key, jsonTodos)
    }

    override suspend fun update(todo: Todo) {
        val currentTodos = loadTodos()
        val index = currentTodos.indexOfFirst { t -> t.id == todo.id}
        if (index != -1) {
            currentTodos[index] = todo
            saveTodo(currentTodos)
        }
    }

    override suspend fun delete(id: String) {
        val todos = loadTodos()
        val newTodos = todos.filter { t -> t.id != id }.toMutableList()
        saveTodo(newTodos)
    }
}