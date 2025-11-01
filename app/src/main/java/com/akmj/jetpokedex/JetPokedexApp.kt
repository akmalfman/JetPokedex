package com.akmj.jetpokedex

import android.app.Application
import com.couchbase.lite.CouchbaseLite

class JetPokedexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CouchbaseLite.init(this)
    }
}
