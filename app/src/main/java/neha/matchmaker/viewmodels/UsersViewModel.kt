package neha.matchmaker.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import neha.matchmaker.base.BaseViewModel
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao
import neha.matchmaker.view.adapter.UsersAdapter

class UsersViewModel(private val usersDao: UsersDao) : BaseViewModel() {
    val userListAdapter: UsersAdapter = UsersAdapter(usersDao)

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val noDataFound: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadUsers() }

    private var subscription: Disposable? = null

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    private fun loadUsers() {


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


        if(userlist.isEmpty()){
            noDataFound.value=View.VISIBLE
        }
        else {
            noDataFound.value=View.GONE

            userListAdapter.updateUsersList(userlist)
            loadingVisibility.value = View.GONE
        }

    }

    private fun onRetrieveEpisodesListError(throwable: Throwable) {
        errorMessage.value = throwable.toString()
    }
}