package br.edu.ufrn.android_ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: APIService
    private lateinit var productAdapter: ProductAdapter

    private var products = listOf<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolBar))
        apiService = APIConfig.getRetrofitClient(this).create(APIService::class.java)

        val swipeRefreshLayout =  findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.design_default_color_primary))
        swipeRefreshLayout.isRefreshing = true

        findViewById<RecyclerView>(R.id.products_recyclerView).layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        getProducts()

    }

    fun getProducts() {
        apiService.getProducts().enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                print(t.message)
                Log.d("Data error", t.message)
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).isRefreshing = false
                Log.d("SUCCESS", response.code().toString())
                if(response.code() == 200) {
                    products = response.body()!!
                    for (product in products) {
                        Log.d("SUCCESS", product.image)
                    }
                }

                productAdapter = ProductAdapter(this@MainActivity, products)

                findViewById<RecyclerView>(R.id.products_recyclerView).adapter = productAdapter
                productAdapter.notifyDataSetChanged()
            }
        })
    }
}