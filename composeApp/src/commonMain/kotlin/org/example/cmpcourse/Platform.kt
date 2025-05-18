package org.example.cmpcourse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform