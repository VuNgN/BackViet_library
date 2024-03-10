package com.vungn.backvietlibrary.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vungn.backvietlibrary.model.data.BorrowData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.repo.BaseRepo
import com.vungn.backvietlibrary.model.repo.GetAllBorrowsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class GetAllBorrowsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getAllBorrowsRepo: GetAllBorrowsRepo
) : CoroutineWorker(
    appContext,
    workerParams
) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val page = 1
            val pageSite = Int.MAX_VALUE
            getAllBorrowsRepo.execute(page, pageSite, object :
                BaseRepo.Callback<Response<BorrowData>> {
                override fun onSuccess(data: Response<BorrowData>) {
                    Result.success()
                }

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                    Result.retry()
                }

                override fun onRelease() {}
            })
            Result.success()
        }
    }
}