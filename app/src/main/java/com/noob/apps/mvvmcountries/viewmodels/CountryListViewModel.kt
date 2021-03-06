package com.noob.apps.mvvmcountries.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noob.apps.mvvmcountries.interfaces.NetworkResponseCallback
import com.noob.apps.mvvmcountries.models.Country
import com.noob.apps.mvvmcountries.repositories.CountriesRepository
import com.noob.apps.mvvmcountries.utils.NetworkHelper

class CountryListViewModel : ViewModel() {
    private var mList: MutableLiveData<List<Country>> =
        MutableLiveData<List<Country>>().apply { value = emptyList() }
    var mShowProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    private var mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    private var mShowApiError: MutableLiveData<Boolean> = MutableLiveData()
    private var mRepository = CountriesRepository.getInstance()

    fun getCountriesList() = mList

    fun fetchCountriesFromServer(context: Context, forceFetch : Boolean): MutableLiveData<List<Country>> {
        if (NetworkHelper.isOnline(context)) {
            mShowProgressBar.value = true
            mList = mRepository.getCountries(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = true
                }

                override fun onNetworkSuccess() {
                    mShowProgressBar.value = false
                }
            }, forceFetch)
        } else {
            mShowNetworkError.value = true
        }
        return mList
    }

    fun onRefreshClicked(view : View){
        fetchCountriesFromServer(view.context, true)
    }
}