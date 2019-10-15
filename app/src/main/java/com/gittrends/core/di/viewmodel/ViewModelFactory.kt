package com.gittrends.core.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gittrends.core.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
@Suppress("UNCHECKED_CAST")
class ViewModelFactory
@Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.get() as T
    }
}