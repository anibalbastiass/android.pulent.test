package com.anibalbastias.android.pulentapp.base.api.domain.base.executor

import io.reactivex.Scheduler

interface APIPostExecutionThread {
    val scheduler: Scheduler
}
