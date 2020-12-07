package com.fazemeright.chatbotmetcs622.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

/**
 * Class/Interface can extend to benefit from the basic CRUD operations supported.
 *
 * @param <T> Class/Interface
 */
public interface BaseDao<T> {

  /**
   * Insert an object in the database.
   *
   * @param element the object to be inserted.
   */
  @Insert
  void insert(T element);

  /**
   * Update an object from the database.
   *
   * @param element the object to be updated
   */
  @Update
  void update(T element);

  /**
   * Delete an object from the database.
   *
   * @param element the object to be deleted
   */
  @Delete
  void deleteItem(T element);
}
