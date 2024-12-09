package com.example.nerelicions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductSize;
        ImageView ivProductImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductSize = itemView.findViewById(R.id.tvProductSize);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }

    // Конструктор адаптера
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Создание ViewHolder для каждого элемента списка
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    // Привязка данных к элементам ViewHolder
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductSize.setText("Размер: " + product.getSize());

        // Загружаем изображение с пути, сохраненного в базе данных
        if (product.getImageUri() != null) {
            Glide.with(context).load(product.getImageUri()).into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground); // Или другая заглушка
        }
    }

    // Возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return productList.size();
    }

}
