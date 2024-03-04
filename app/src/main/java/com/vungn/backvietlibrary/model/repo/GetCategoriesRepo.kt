package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.CategoryDao
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.CategoryData
import com.vungn.backvietlibrary.model.data.Response
import com.vungn.backvietlibrary.model.service.UserService
import com.vungn.backvietlibrary.model.service.header.XQueryHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetCategoriesRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val service: UserService,
    private val categoryDao: CategoryDao
) : BaseRepo<Response<CategoryData>, List<CategoryEntity>>() {
    override val call: Call<Response<CategoryData>>
        get() {
            val xQueryHeader = XQueryHeader(
                includes = listOf("Books"),
                filters = emptyList(),
                sorts = listOf("Id"),
                page = 1,
                pageSize = 20
            )
            return service.getCategories(xQueryHeader.toJsonString())
        }

    override fun getFromDatabase(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    override fun Response<CategoryData>.toEntity(): List<CategoryEntity> {
        val categories = this.data.items
        val entities = categories.map {
            CategoryEntity(
                id = it.id,
                name = it.name,
                bookIds = it.books?.map { book -> book.id } ?: emptyList())
        }
        return entities
    }

    override suspend fun saveToDatabase(data: Response<CategoryData>) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val entities = data.toEntity()
            categoryDao.insertAll(entities)
        }
    }
}