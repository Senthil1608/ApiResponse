package com.example.apichecker

import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class FetchUsersTask(private val onResult: (List<User>) -> Unit) : android.os.AsyncTask<Void, Void, List<User>>() {
    override fun doInBackground(vararg params: Void?): List<User> {
        val urlString = "https://reqres.in/api/users?page=1"
        val users = mutableListOf<User>()

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(inputStream)
                val dataArray = jsonObject.getJSONArray("data")

                for (i in 0 until dataArray.length()) {
                    val userObj = dataArray.getJSONObject(i)
                    val firstName = userObj.getString("first_name")
                    val lastName = userObj.getString("last_name")
                    users.add(User(firstName, lastName))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return users
    }

    override fun onPostExecute(result: List<User>) {
        onResult(result)
    }
}
