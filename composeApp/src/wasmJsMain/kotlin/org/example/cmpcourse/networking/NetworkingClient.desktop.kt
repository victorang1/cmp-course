package org.example.cmpcourse.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual fun getHttpClient(): HttpClient = createHttpClient(Js)