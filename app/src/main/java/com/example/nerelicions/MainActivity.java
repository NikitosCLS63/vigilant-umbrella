package com.example.nerelicions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etProductName;
    private EditText etProductSize; // Добавляем поле для размер
    private ImageView ivProductImage;
    private Button btnChooseImage, btnAddProduct, btnDeleteAllProducts;
    private RecyclerView recyclerView;

    private Uri imageUri;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etProductName = findViewById(R.id.etProductName);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnDeleteAllProducts = findViewById(R.id.btnDeleteAllProducts);

        etProductSize = findViewById(R.id.etProductSize);


        recyclerView = findViewById(R.id.recyclerView);


        databaseHelper = new DatabaseHelper(this);
        productList = databaseHelper.getAllProducts();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        btnChooseImage.setOnClickListener(v -> openFileChooser());
        btnAddProduct.setOnClickListener(v -> addProduct());
        btnDeleteAllProducts.setOnClickListener(v -> deleteAllProducts());

        checkPermissions();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProductImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProduct() {
        String productName = etProductName.getText().toString();
        String productSize = etProductName.getText().toString();
        if (productName.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Введите название и выберите изображение", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            String imagePath = saveImageToInternalStorage(bitmap, "product_" + System.currentTimeMillis());

            if (imagePath != null) {
                databaseHelper.addProduct(productName, imageUri.toString(), productSize);
                productList.add(new Product(productName, imageUri,  productSize));
                productAdapter.notifyDataSetChanged();
                etProductName.setText("");
                etProductSize.setText("");
                ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
                imageUri = null;
            } else {
                Toast.makeText(this, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private String saveImageToInternalStorage(Bitmap bitmap, String imageName) {
        File directory = getDir("images", Context.MODE_PRIVATE);
        File imagePath = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return imagePath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteAllProducts() {
        databaseHelper.deleteAllProducts();
        productList.clear();
        productAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Все товары удалены", Toast.LENGTH_SHORT).show();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }
}
