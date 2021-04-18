package br.edu.ufrn.android_ecommerce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()) :
        RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ProductAdapter.ViewHolder, position: Int) {
        viewHolder.bindProduct(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindProduct(product: Product) {
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = "$${product.price.toString()}"
            Picasso.get().load("http://192.168.1.4:3333"+product.image).fit().into(itemView.findViewById<ImageView>(R.id.product_image))
        }
    }
}