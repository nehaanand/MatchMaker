package neha.matchmaker.view.adapter

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
import neha.matchmaker.model.UsersGridItemModel
import neha.matchmaker.model.database.Users
import neha.matchmaker.model.database.UsersDao


class ShadiUsersAdapter : BaseAdapter {
    private lateinit var usersList: List<Users>
    var status: String = ""

    var usersDao: UsersDao


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

        var inflator = parent?.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


        var binding: neha.matchmaker.databinding.GriditemShadiUsersBinding = inflate(inflator, neha.matchmaker.R.layout.griditem_shadi_users, parent, false)
        val viewModel = UsersGridItemModel()

        viewModel.bind(usersList[position])
        binding.viewModel = viewModel


        binding.btnAccept.setOnClickListener {

            onRowUpdate(position, "Accepted")


        }
        binding.btnDecline.setOnClickListener {

            onRowUpdate(position, "Declined")


        }

        fun onUserUpdateScucess() {
            Log.d("success", "success")
            usersList[position].status == "Accepted"
            this.updateUsersList(usersList)
        }


        return binding.root


    }


    fun onRowUpdate(position: Int, userStatus: String) {
        Observable.fromCallable { usersDao?.updateUser(usersList[position].id, userStatus) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())

                .subscribe(
                        { result ->
                            Observable.empty<String>()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            { result -> Log.d("abbc", "hjsjf") },
                                            { error -> Log.d("error", "error") },
                                            {
                                                Log.d("success", "success")
                                                usersList[position].status = userStatus
                                                updateUsersList(usersList)
                                            }
                                    )
                        },
                        { error -> (Log.d("erroraccept", error.toString())) }
                )


    }

}
