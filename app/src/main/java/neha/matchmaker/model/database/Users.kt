package neha.matchmaker.model.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Class which provides a model for users
 * @constructor Sets all properties of the users
 */
@Entity
data class Users(
        @field:PrimaryKey
        val id: String,
        val title: String,
        val first: String,
        val last: String,
        val gender: String,
        val picture: String,
        val age: String,
        val city: String,
        val state: String,
        var status: String

)