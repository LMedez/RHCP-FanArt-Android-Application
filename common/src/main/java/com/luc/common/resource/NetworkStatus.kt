package com.luc.common.resource

sealed class NetworkStatus <out T> {
    object Loading : NetworkStatus<Nothing>()
    data class Success<out T>(val data: T) : NetworkStatus<T>()
    data class Error<out T>(val exception: Exception?, val message: String) : NetworkStatus<T>()

}