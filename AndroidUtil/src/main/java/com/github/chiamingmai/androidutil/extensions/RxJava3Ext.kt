@file:JvmName("RxJava3Ext")

package com.github.chiamingmai.androidutil.extensions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

//region Single
/** RxJava Single 執行於 CPU Thread */
fun <T : Any> Single<T>.executeOnCPUThread(): Single<T> =
    subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Single 執行於 IO Thread */
fun <T : Any> Single<T>.executeOnIOThread(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Single 執行於 Main Thread */
fun <T : Any> Single<T>.executeOnMainThread(): Single<T> =
    subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
//endregion

//region Completable
/** RxJava Completable 執行於 CPU Thread */
fun Completable.executeOnCPUThread(): Completable =
    subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Completable 執行於 IO Thread */
fun Completable.executeOnIOThread(): Completable =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Completable 執行於 Main Thread */
fun Completable.executeOnMainThread(): Completable =
    subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
//endregion

//region Observable
/** RxJava Observable 執行於 CPU Thread */
fun <T : Any> Observable<T>.executeOnCPUThread(): Observable<T> =
    subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Observable 執行於 IO Thread */
fun <T : Any> Observable<T>.executeOnIOThread(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Observable 執行於 Main Thread */
fun <T : Any> Observable<T>.executeOnMainThread(): Observable<T> =
    subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
//endregion

//region Flowable
/** RxJava Flowable 執行於 CPU Thread */
fun <T : Any> Flowable<T>.executeOnCPUThread(): Flowable<T> =
    subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Flowable 執行於 IO Thread */
fun <T : Any> Flowable<T>.executeOnIOThread(): Flowable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/** RxJava Flowable 執行於 Main Thread */
fun <T : Any> Flowable<T>.executeOnMainThread(): Flowable<T> =
    subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
//endregion
