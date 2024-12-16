package com.bizilabs.streeek.lib.presentation.helpers

import android.app.Application
import com.bizilabs.streeek.feature.authentication.authenticationModule
import com.bizilabs.streeek.feature.landing.landingModule
import com.bizilabs.streeek.feature.setup.setupModule
import com.bizilabs.streeek.feature.tabs.tabsModule
import com.bizilabs.streeek.lib.domain.workers.SyncContributionsWork
import com.bizilabs.streeek.lib.domain.workers.SyncDailyContributionsWork
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun Application.initKoin(vararg modules: Module) {
    startKoin {
        androidContext(androidContext = this@initKoin)
        workManagerFactory()
        loadKoinModules(PresentationModule)
        loadKoinModules(modules.asList())
    }
}

val PresentationModule = module {
    includes(
        landingModule, authenticationModule,
        setupModule, tabsModule
    )
    workerOf(::SyncContributionsWork)
    workerOf(::SyncDailyContributionsWork)
}
