package neha.matchmaker.model


import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UsersResponse(

        @SerializedName("info")
        val info: Info,
        @SerializedName("results")
        val results: List<Result>
)