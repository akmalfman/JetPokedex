package com.akmj.jetpokedex.data.local


import android.content.Context
import com.akmj.jetpokedex.domain.model.User
import com.couchbase.lite.*

class UserDatabase(context: Context) {

    private val database = Database("users", DatabaseConfiguration())

    fun registerUser(user: User): Boolean {
        if (getUserByEmail(user.email) != null) return false

        val doc = MutableDocument()
            .setString("id", user.id)
            .setString("username", user.username)
            .setString("email", user.email)
            .setString("password", user.password)

        database.save(doc)
        return true
    }

    fun loginUser(email: String, password: String): User? {
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(database))
            .where(
                Expression.property("email").equalTo(Expression.string(email))
                    .and(Expression.property("password").equalTo(Expression.string(password)))
            )

        val result = query.execute().allResults()
        if (result.isEmpty()) return null

        val data = result[0].getDictionary("users")!!
        return User(
            id = data.getString("id")!!,
            username = data.getString("username")!!,
            email = data.getString("email")!!,
            password = data.getString("password")!!
        )
    }

    fun getUserByEmail(email: String): User? {
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(database))
            .where(Expression.property("email").equalTo(Expression.string(email)))

        val result = query.execute().allResults()
        if (result.isEmpty()) return null

        val data = result[0].getDictionary("users")!!
        return User(
            id = data.getString("id")!!,
            username = data.getString("username")!!,
            email = data.getString("email")!!,
            password = data.getString("password")!!
        )
    }
}