package org.example.cmpcourse.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun getHttpClient(): HttpClient = createHttpClient(Darwin)