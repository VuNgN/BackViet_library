package com.vungn.backvietlibrary.model.repo

import com.vungn.backvietlibrary.db.dao.CategoryDao
import com.vungn.backvietlibrary.db.entity.CategoryEntity
import com.vungn.backvietlibrary.di.CoroutineScopeIO
import com.vungn.backvietlibrary.model.data.CategoryResponse
import com.vungn.backvietlibrary.model.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

class GetCategoriesRepo @Inject constructor(
    @CoroutineScopeIO private val coroutineScopeIO: CoroutineScope,
    private val service: UserService,
    private val categoryDao: CategoryDao
) : BaseRepo<CategoryResponse, List<CategoryEntity>>() {
    override val call: Call<CategoryResponse>
        get() = service.getCategories()

    override fun getFromDatabase(): Flow<List<CategoryEntity>> = flow { }

    override fun CategoryResponse.toEntity(): List<CategoryEntity> {
        val categories = this.data.items
        val entities = categories.map {
            CategoryEntity(id = it.id, name = it.name, bookIds = it.books.map { book -> book.id })
        }
        return entities
    }

    override suspend fun saveToDatabase(data: CategoryResponse) {
        coroutineScopeIO.launch(Dispatchers.IO) {
            val entities = data.toEntity()
            categoryDao.insertAll(entities)
        }
    }
}