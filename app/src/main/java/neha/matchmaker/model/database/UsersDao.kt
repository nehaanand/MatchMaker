package neha.matchmaker.model.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
@Dao
interface UsersDao {
    @get:Query("SELECT * FROM users")
    val all: List<Users>

    @Insert
    @JvmSuppressWildcards
    fun insertAll(vararg users: Users)

    @Query("UPDATE users SET status = :status WHERE id = :id")
    fun updateUser(id: String, status: String): Int

    @Query("DELETE FROM users")
    fun deleteAll()
}