package com.example.nerelicions;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

 // Assuming you're using Hawk for data storage

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private EditText titleText, authorText;
    private Button addButton, updateButton, deleteButton;
    private ListView listView;

    private ArrayAdapter<String> adapter;
    private String selectedBookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Hawk (or Paper if you're using it)
        Paper.init(this);

        titleText = findViewById(R.id.titleText);
        authorText = findViewById(R.id.AuthorText);
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        listView = findViewById(R.id.listView);

        // Initialize the adapter with the context, layout, and book titles
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getBooksTitles());

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set an item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected book title from the adapter
                selectedBookTitle = adapter.getItem(position);

                // Read the book from the database using Hawk or Paper
                Book book = Paper.book().read(selectedBookTitle, null); // Adjust based on your storage method

                // If the book is found, fill the input fields with values from the selected book
                if (book != null) {
                    titleText.setText(book.getTitle());
                    authorText.setText(book.getAuthor());
                }
            }
        });

        addButton.setOnClickListener(v -> {
            // Extract data from input fields
            String title = titleText.getText().toString();
            String content = authorText.getText().toString();

            // If both fields are not empty, create a new book object and save it
            if (!title.isEmpty() && !content.isEmpty()) {
                Book book = new Book(title, content);
                Paper.book().write(title, book); // Save using Hawk or adjust for Paper
                updateBookList();
                clearInputs();
            }
        });

        updateButton.setOnClickListener(v -> {
            // If no book is selected, show an error message
            if (selectedBookTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите книгу", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = titleText.getText().toString();
            String content = authorText.getText().toString();

            // If a book is selected, create a new object and save it with the selected title as key
            if (!title.isEmpty() && !content.isEmpty()) {
                Book updatedBook = new Book(title, content);
                Paper.book().write(selectedBookTitle, updatedBook); // Update using Hawk or adjust for Paper
                updateBookList();
                clearInputs();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selectedBookTitle == null) {
                Toast.makeText(MainActivity.this, "Пожалуйста, сначала выберите книгу", Toast.LENGTH_SHORT).show();
                return;
            }

            Paper.book().delete(selectedBookTitle); // Delete using Hawk or adjust for Paper
            updateBookList();
            clearInputs();
        });
    }

    private void updateBookList() {
        adapter.clear();
        adapter.addAll(getBooksTitles());
        adapter.notifyDataSetChanged();
    }

    private List<String> getBooksTitles() {
        return new ArrayList<>(Paper.book().getAllKeys()); // Adjust based on your storage method
    }

    private void clearInputs() {
        titleText.setText("");
        authorText.setText("");
        selectedBookTitle = null;
    }
}