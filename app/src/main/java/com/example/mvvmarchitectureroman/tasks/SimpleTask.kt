package com.example.mvvmarchitectureroman.tasks

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

private val executorService = Executors.newCachedThreadPool()
private val handler = Handler(Looper.getMainLooper())

class SimpleTask<T>(
        private val callable: Callable<T>
) : Task<T> {

    private val future: Future<*>
    private var result: Result<T> = PendingResult()

    init {
        future = executorService.submit {
            result = try {
                SuccessResult(callable.call())
            } catch (e: Throwable) {
                ErrorResult(e)
            }
            notifyListeners()
        }
    }

    private var valueCallBack: Callback<T>? = null
    private var errorCallBack: Callback<Throwable>? = null

    override fun onSuccess(callback: Callback<T>): Task<T> {
        this.valueCallBack = callback
        notifyListeners()
        return this
    }

    override fun onError(callback: Callback<Throwable>): Task<T> {
        this.errorCallBack = callback
        notifyListeners()
        return this
    }

    override fun cancel() {
        clear()
        future.cancel(true)
    }

    override fun await(): T {
        future.get()
        val result = this.result
        if (result is SuccessResult) return result.data
        else throw (result as ErrorResult).error
    }

    private fun notifyListeners() {
        handler.post {
            val result = this.result
            val callback = this.valueCallBack
            val errorCallback = this.errorCallBack

            if (result is SuccessResult && callback != null) {
                callback(result.data)
                clear()
            } else if (result is ErrorResult && errorCallback != null) {
                errorCallback.invoke(result.error)
                clear()
            }
        }
    }

    private fun clear() {
        valueCallBack = null
        errorCallBack = null
    }
}