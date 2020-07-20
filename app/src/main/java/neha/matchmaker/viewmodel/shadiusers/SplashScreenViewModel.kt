package neha.matchmaker.viewmodel.shadiusers

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
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
    val errorClickListener = View.OnClickListener { loadUsers() }

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
            //get data from randomuser.me host
            apiList
            Observable.just(apiList)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveEpisodesListStart() }
                .doOnTerminate { onRetrieveEpisodesListFinish() }

                .subscribe(
                        { result -> onRetrieveEpisodesListSuccess(result) },
                        { error -> onRetrieveEpisodesListError(error) }
                )
    }

    private fun onRetrieveEpisodesListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null

    }

    private fun onRetrieveEpisodesListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveEpisodesListSuccess(userlist: UsersResponse) {
        Log.d("fdh", "hii")


        var list: MutableList<Users> = mutableListOf()
        for (user in userlist.results) {
            var status: String = ""

            if (user.id.value == null) {
                var userobj = Users(user.login.username
                        , user.name.title, user.name.first, user.name.last,
                        user.gender, user.picture.large, user.dob.age,
                        user.location.city, user.location.state, status)
                list.add(userobj)

            } else {
                var userobj = Users(user.id.value
                        , user.name.title, user.name.first, user.name.last,
                        user.gender, user.picture.large, user.dob.age,
                        user.location.city, user.location.state, status)
                list.add(userobj)

            }
        }
        val disposable = Observable.fromCallable { usersDao.deleteAll().toString() }
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
                                        { error -> (Log.d("error", error.toString())) }
                                )
                    }

                    override fun onError(t: Throwable) {
                        println("Error!")

                        t.printStackTrace()
                    }

                    override fun onComplete() {
                        println("Done!")
                    }
                })


    }

    private fun onRoomDBInsertionSuccess() {

    }

    private fun onRetrieveEpisodesListError(throwable: Throwable) {
        errorMessage.value = "Currently browsing in Offline mode. Please check Internet connection."

        GlobalScope.launch {
            delay(1000)
            mutableLiveData.postValue(SplashState.ShadiUsers())

        }
    }

    private fun onRoomDBInsertionError(throwable: Throwable) {
        errorMessage.value = throwable.toString()
    }
}