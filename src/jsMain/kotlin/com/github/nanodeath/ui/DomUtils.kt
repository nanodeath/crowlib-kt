@file:Suppress("NOTHING_TO_INLINE")

package com.github.nanodeath.ui

import org.w3c.dom.*


//internal inline fun Document.byId(el: String): HTMLElement? = getElementById(el).unsafeCast<HTMLElement?>()
internal inline fun <T : Element> Document.byId(el: String): T {
    return getElementById(el).unsafeCast<T>()
}
internal inline fun Document.byId(el: String): Element = byId<Element>(el)
internal inline fun Document.first(selector: String): HTMLElement? = querySelector(selector).unsafeCast<HTMLElement?>()
internal inline fun Document.all(selector: String): Sequence<HTMLElement> = querySelectorAll(selector).asSequence().filterIsInstance<HTMLElement>()

internal fun NodeList.asSequence(): Sequence<Node?> = sequence {
    for (i in 0 until length) {
        yield(item(i) ?: error("No node at $i"))
    }
}

internal inline fun Node.removeChildNodes() {
    while (true) {
        firstChild?.let { removeChild(it) } ?: break
    }
}

inline fun time(name: String) = console.asDynamic().time(name)
inline fun timeEnd(name: String) = console.asDynamic().timeEnd(name)

internal fun HTMLInputElement.intObservable(): Observable<Int> {
    val initialValue = value.toInt()
    val observable = Observable(initialValue)
    addEventListener("input", { observable.value = value.toInt() })
    observable.subscribe {
        println("observable.subscribe")
        value = it.toString()
    }
    return observable
}

internal fun HTMLSelectElement.observable(): Observable<String> {
    val initialValue = value
    val observable = Observable(initialValue)
    addEventListener("input", { observable.value = value })
    observable.subscribe {
        value = it
    }
    return observable
}