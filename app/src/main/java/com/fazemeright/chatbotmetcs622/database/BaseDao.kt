package com.fazemeright.chatbotmetcs622.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Class/Interface can extend to benefit from the basic CRUD operations supported.
 *
 * @param <T> Class/Interface
</T> */
interface BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param element the object to be inserted.
     */
    @Insert
    fun insert(element: T)

    /**
     * Update an object from the database.
     *
     * @param element the object to be updated
     */
    @Update
    fun update(element: T)

    /**
     * Delete an object from the database.
     *
     * @param element the object to be deleted
     */
    @Delete
    fun deleteItem(element: T)
}