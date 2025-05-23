package org.example.cmpcourse.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.cmpcourse.model.Todo

class WebTodoRepositoryImpl(
    private val settings: Settings
) : TodoRepository {

    companion object {
        private const val KEY = "todo_list"
    }

    private val json = Json { ignoreUnknownKeys = true }

    private fun loadTodos(): List<Todo> {
        val storedListInString = settings.getString(KEY, "")
        return try {
            json.decodeFromString(storedListInString)
        } catch (ex: Exception) {
            emptyList<Todo>()
        }
    }

    private suspend fun saveTodo(newTodos: List<Todo>) {
        withContext(Dispatchers.Default) {
            val encodedList = json.encodeToString(newTodos)
            settings.putString(KEY, encodedList)
        }
    }

    override suspend fun getAll(): List<Todo> {
        return loadTodos()
    }

    override suspend fun add(todo: Todo) {
        val currentTodos = loadTodos().toMutableList()
        currentTodos.add(todo)
        saveTodo(currentTodos)
    }

    override suspend fun update(todo: Todo) {
        val currentTodos = loadTodos().toMutableList()
        val index = currentTodos.indexOfFirst { t -> t.id == todo.id }
        if (index != -1) {
            currentTodos[index] = todo
            saveTodo(currentTodos)
        }
    }

    override suspend fun delete(id: String) {
        val currentTodos = loadTodos()
        val updatedTodos = currentTodos.filter { t -> t.id != id }
        saveTodo(updatedTodos)
    }
}