package neha.matchmaker.model

import android.arch.lifecycle.MutableLiveData
import neha.matchmaker.base.BaseViewModel
import neha.matchmaker.model.database.Users

class GridItemModel: BaseViewModel() {
    private val userName = MutableLiveData<String>()
    private val userGender = MutableLiveData<String>()
    private val userImage = MutableLiveData<String>()
    private val userImageThumbnail = MutableLiveData<String>()
    private val userAge= MutableLiveData<String>()
    private val userCity= MutableLiveData<String>()
    private val userStatus= MutableLiveData<String>()

    fun bind(list: Users){
        userName.value = list.title +" "+list.first+" "+list.last
        userGender.value = list.gender
        userImage.value = list.picture
        userImageThumbnail.value = list.pictureThumbnail
        userAge.value = list.age+" yrs"
        userCity.value = list.city+", "+list.state
        userStatus.value = list.status
    }

    fun getUserName():MutableLiveData<String>{
        return userName
    }

    fun getUserGender():MutableLiveData<String>{
        return userGender
    }

    fun getUserStatus():MutableLiveData<String>{
        return userStatus
    }


    fun getUserImage():MutableLiveData<String>{
        return userImage
    }

    fun getUserImageThumbnail():MutableLiveData<String>{
        return userImage
    }

    fun getUserAge():MutableLiveData<String>{
        return userAge
    }

    fun getUserCity():MutableLiveData<String>{
        return userCity
    }
}