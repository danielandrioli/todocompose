package com.dboy.todocompose.ui.presentation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class FakeDispatchers : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher() // O StandardTestDispatcher fazia o resultado do teste dar errado.
    override val io: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val unconfined: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}