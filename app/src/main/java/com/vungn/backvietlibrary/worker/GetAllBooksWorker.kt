package com.vungn.backvietlibrary.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.BookData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo.Callback
import com.vungn.backvietlibrary.model.repo.GetAllBooksRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext

@HiltWorker
class GetAllBooksWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val getAllBooksRepo: GetAllBooksRepo
) : CoroutineWorker(
    appContext,
    workerParams
) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            getAllBooksRepo.execute(object : Callback<Response<BookData>> {
                override fun onSuccess(data: Response<BookData>) {
                    Result.success()
                }

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                    Result.retry()
                }

                override fun onRelease() {}
            }).launchIn(coroutineScopeIO)
            Result.success()
        }
    }
}