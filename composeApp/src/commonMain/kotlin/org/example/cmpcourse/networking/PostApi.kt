package org.example.cmpcourse.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import org.example.cmpcourse.model.PostResponse

class PostApi(private val httpClient: HttpClient) {

    private val baseUrl = "https://jsonplaceholder.typicode.com/posts"

    suspend fun getPost(id: Int): PostResponse = httpClient.get("$baseUrl/$id").body()

    suspend fun getAllPosts(): List<PostResponse> = httpClient.get(baseUrl).body()

    suspend fun createPost(post: PostResponse): PostResponse = httpClient.post(baseUrl) {
        setBody(post)
    }.body()

    suspend fun updatePost(post: PostResponse): PostResponse = httpClient.put("$baseUrl/${post.id}") {
        setBody(post)
    }.body()

    suspend fun patchPost(post: PostResponse): PostResponse = httpClient.patch("$baseUrl/${post.id}") {
        setBody(mapOf("title" to post.title))
    }.body()

    suspend fun deletePost(id: Int): HttpResponse = httpClient.delete("$baseUrl/$id")
}