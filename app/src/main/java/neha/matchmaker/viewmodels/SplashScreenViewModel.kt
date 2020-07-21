package neha.matchmaker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import neha.matchmaker.base.BaseViewModel
import neha.matchmaker.model.UsersResponse
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao
import neha.matchmaker.network.APIsMethods
import javax.inject.Inject

class SplashScreenViewModel(private val usersDao: UsersDao) : BaseViewModel() {
    @Inject
    lateinit var apiCall: APIsMethods

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()


    init {
        loadUsers()
    }

    sealed class SplashState {
        class ShadiUsers : SplashState()
    }


    fun loadUsers() {
        apiCall.getUsers().concatMap { apiList ->
            apiList
            Observable.just(apiList)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveUsersListStart() }
                .doOnTerminate { onRetrieveUsersListFinish() }
                .subscribe(
                        { result -> onRetrieveUsersListSuccess(result) },
                        { error -> onRetrieveUsersListError(error) }
                )
    }

    private fun onRetrieveUsersListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null

    }

    private fun onRetrieveUsersListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveUsersListSuccess(userlist: UsersResponse) {

        val list: MutableList<Users> = mutableListOf()
        for (user in userlist.results) {
            val status = ""

            user.id.value = if (user.id.value != null) user.id.value else user.login.username

            val userobj = Users(user.id.value,
                    user.name.title,
                    user.name.first,
                    user.name.last,
                    user.gender,
                    user.picture.large,
                    user.picture.thumbnail,
                    user.dob.age,
                    user.location.city,
                    user.location.state,
                    status
            )
            list.add(userobj)
        }
        Observable.fromCallable { usersDao.deleteAll().toString() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribeWith(object : DisposableObserver<String>() {
                    public override fun onStart() {
                        println("Start!")
                    }

                    override fun onNext(t: String) {
                        Observable.fromCallable {
                            usersDao.insertAll(*list.toTypedArray())
                        }
                                .observeOn(Schedulers.newThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        { result ->
                                            GlobalScope.launch {
                                                delay(1000)
                                                mutableLiveData.postValue(SplashState.ShadiUsers())
                                            }
                                        },
                                        { error -> errorMessage.value = error.toString() }
                                )
                    }

                    override fun onError(t: Throwable) {
                        errorMessage.value = t.toString()
                        t.printStackTrace()
                    }

                    override fun onComplete() {
                        println("Done!")
                    }
                })

    }

    private fun onRetrieveUsersListError(throwable: Throwable) {
        errorMessage.value = "Currently browsing in Offline mode. Please check Internet connection."

        GlobalScope.launch {
            delay(1000)
            mutableLiveData.postValue(SplashState.ShadiUsers())

        }
    }

}