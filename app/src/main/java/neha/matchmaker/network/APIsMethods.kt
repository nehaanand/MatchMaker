package neha.matchmaker.network

import io.reactivex.Observable
import neha.matchmaker.model.UsersResponse
import retrofit2.http.GET

/**
 * The interface which provides methods to get result of webservices
 */
interface APIsMethods {
    /**
     * Get the users data from the API
     */

    @GET("?results=10")
    fun getUsers(): Observable<UsersResponse>

}