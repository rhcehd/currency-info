package dev.rhcehd123.config

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.config.AbstractProjectConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
object ProjectConfig: AbstractProjectConfig() {
    private val testDispatcher = StandardTestDispatcher()

    override suspend fun beforeProject() {
        super.beforeProject()
        setupLiveData()
        Dispatchers.setMain(testDispatcher)
    }

    override suspend fun afterProject() {
        super.afterProject()
        resetLiveData()
        Dispatchers.resetMain()
    }

    private fun setupLiveData() {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    private fun resetLiveData() {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}