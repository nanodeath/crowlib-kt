package com.github.nanodeath

import kotlin.js.JsExport

@JsExport
data class Path<T>(val nodes: List<T>, val totalDistance: Float) {
    val jsPath = constructQueue<T>().also { q -> nodes.forEach { q.enqueue(it) } }
}