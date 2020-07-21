package neha.matchmaker.view.adapter

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.DataBindingUtil.inflate
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import neha.matchmaker.model.GridItemModel
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao

class UsersAdapter : BaseAdapter {
    private lateinit var usersList: List<Users>
    var status: String = ""
    var usersDao: UsersDao
    val errorMessage: MutableLiveData<String> = MutableLiveData()


    constructor(usersDao: UsersDao) : super() {
        this.usersDao = usersDao
    }

    override fun getCount(): Int {
        return if (::usersList.isInitialized) usersList.size else 0
    }

    override fun getItem(position: Int): Any {
        return usersList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateUsersList(usersList: List<Users>) {
        this.usersList = usersList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = parent?.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: neha.matchmaker.databinding.GriditemBinding = inflate(inflator, neha.matchmaker.R.layout.griditem, parent, false)
        val viewModel = GridItemModel()
        viewModel.bind(usersList[position])
        binding.viewModel = viewModel
        binding.btnAccept.setOnClickListener {
            onRowUpdate(position, "Accepted")
        }
        binding.btnDecline.setOnClickListener {
            onRowUpdate(position, "Declined")
        }
        return binding.root
    }


    fun onRowUpdate(position: Int, userStatus: String) {
        Observable.fromCallable { usersDao?.updateUser(usersList[position].id, userStatus) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {
                    Observable.empty<String>()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { Log.d("Testing", "No output") },
                        { errorMessage.value="error while updating user" },
                        {
                            usersList[position].status = userStatus
                            updateUsersList(usersList)
                        }
                    )
                },
                { error -> errorMessage.value=error.toString()}
            )
    }

}
