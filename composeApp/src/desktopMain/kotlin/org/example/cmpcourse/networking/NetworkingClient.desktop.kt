package org.example.cmpcourse.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun getHttpClient(): HttpClient = createHttpClient(CIO)