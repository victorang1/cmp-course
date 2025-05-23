package org.example.cmpcourse.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.cmpcourse.TodoDatabase
import org.example.cmpcourse.model.Todo

class TodoRepositoryImpl(
    private val todoDatabase: TodoDatabase
) : TodoRepository {

    private val queries = todoDatabase.todoQueries

    override suspend fun getAll(): List<Todo> {
        return withContext(Dispatchers.Default) {
            queries.selectAll().executeAsList().map { row ->
                Todo(
                    id = row.id,
                    title = row.title,
                    isDone = row.isDone
                )
            }
        }
    }

    override suspend fun add(todo: Todo) {
        withContext(Dispatchers.Default) {
            queries.insert(todo.id, todo.title, todo.isDone)
        }
    }

    override suspend fun update(todo: Todo) {
        withContext(Dispatchers.Default) {
            queries.update(todo.title, todo.isDone, todo.id)
        }
    }

    override suspend fun delete(id: String) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}