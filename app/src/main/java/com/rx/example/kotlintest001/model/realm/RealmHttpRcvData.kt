package com.rx.example.kotlintest001.model.realm

import com.rx.example.kotlintest001.model.http.Result
import io.realm.annotations.PrimaryKey

/**
 * Created by Rhpark on 2017-10-30.
 */
public class RealmHttpRcvData {

    @PrimaryKey
    public var count:Int? = null

    public var result: Result? = null

    public constructor(count: Int, result: Result) : super() {
        this.count = count
        this.result = result
    }
    public constructor() : super()
}