package neha.matchmaker.viewmodel.shadiusers

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import neha.matchmaker.base.BaseViewModel
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao
import neha.matchmaker.network.APIsMethods
import neha.matchmaker.view.adapter.ShadiUsersAdapter
import javax.inject.Inject

class ShadiUsersViewModel(private val usersDao:UsersDao) : BaseViewModel() {
    @Inject
    lateinit var apiCall: APIsMethods
     val userListAdapter: ShadiUsersAdapter = ShadiUsersAdapter(usersDao)

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadEpisodes() }

    private var subscription: Disposable?=null

    init {
        loadEpisodes()
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    private fun loadEpisodes() {


            Observable.fromCallable { usersDao.all }
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

    private fun onRetrieveEpisodesListSuccess(userlist: List<Users>) {
        Log.d("fdh",userlist.toString());


        userListAdapter.updateUsersList(userlist)
        loadingVisibility.value = View.GONE

        Log.d("fdh","jkds");
    }

    private fun onRetrieveEpisodesListError(throwable: Throwable) {
        errorMessage.value = throwable.toString()
    }
}