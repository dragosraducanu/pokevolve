package com.dragos.testing

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxTestSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }

                base.evaluate()
            }
        }

    companion object {
        private val SCHEDULER_INSTANCE = Schedulers.trampoline()
    }
}