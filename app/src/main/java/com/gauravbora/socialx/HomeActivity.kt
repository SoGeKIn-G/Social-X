package com.gauravbora.socialx

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.gauravbora.socialx.Modal.NewsData
import com.gauravbora.socialx.adapter.HomeAdapter
import com.gauravbora.socialx.databinding.ActivityHomeBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var madapter: HomeAdapter
    private lateinit var newsList: ArrayList<NewsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterlist(newText)
                return true
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchNews()
        madapter = HomeAdapter()
        binding.recyclerView.adapter = madapter
    }

    private fun filterlist(newText: String?) {
        val filteredlist = ArrayList<NewsData>()
        for(item in newsList){
            if (newText != null) {
                if (item.title.lowercase(Locale.getDefault()).contains(newText.lowercase(Locale.getDefault()))) {
                    filteredlist.add(item)
                }
            }
        }
        if (filteredlist.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }
        else{
            madapter.setfilteredlist(filteredlist)
        }
    }

    private fun fetchNews() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=abea6802adae48e5865e243f9db14a94"
        val jsonObjectRequest = @SuppressLint("SimpleDateFormat") object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                newsList = ArrayList<NewsData>()
                for (news in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(news)


                    // published at
                    val publishedAt = newsJsonObject.getString("publishedAt")
                    var ago : CharSequence? = null
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    sdf.timeZone = TimeZone.getTimeZone("GMT")
                    try {
                        val time = sdf.parse(publishedAt)?.time
                        val now = System.currentTimeMillis()
                        ago = DateUtils.getRelativeTimeSpanString(
                            time!!,
                            now,
                            DateUtils.MINUTE_IN_MILLIS
                        )
                    }catch (e:ParseException){
                        e.printStackTrace()
                    }


                    // source
                    val source = newsJsonObject.getJSONObject("source")
                    val name = source.getString("name")
                    val newsData = NewsData(
                        ago.toString(),
                        name,
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsList.add(newsData)
                }
                madapter.updatedNews(newsList)
            },
            {

            }
        )
        // overriding function to add Headers.
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}