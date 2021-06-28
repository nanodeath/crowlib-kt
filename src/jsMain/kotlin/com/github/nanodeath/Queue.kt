package com.github.nanodeath

actual fun <T> constructQueue(): Queue<T> = JsArray()