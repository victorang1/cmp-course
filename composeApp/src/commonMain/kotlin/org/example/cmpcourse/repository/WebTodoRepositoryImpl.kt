package org.example.cmpcourse.repository

import org.example.cmpcourse.model.Todo

class WebTodoRepositoryImpl : TodoRepository {
    override suspend fun getAll(): List<Todo> {
        return emptyList()
    }

    override suspend fun add(todo: Todo) {

    }

    override suspend fun update(todo: Todo) {

    }

    override suspend fun delete(id: String) {

    }
}