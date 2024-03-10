package com.vungn.backvietlibrary.db.data

import androidx.room.Embedded
import androidx.room.Relation
import com.vungn.backvietlibrary.db.entity.BorrowDetailEntity
import com.vungn.backvietlibrary.db.entity.BorrowEntity

data class BorrowItemWithDetails(
    @Embedded val borrowItem: BorrowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "borrowId"
    )
    val borrowDetails: List<BorrowDetailEntity>
)
