package com.example.pr17_aubovsergey_pr_21102;

import static androidx.core.content.ContextCompat.getSystemService;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText etAnimal, etName, etSize, etHeight;
    Button btnInsert, btnRead, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        etAnimal = findViewById(R.id.etAnimal);
        etName = findViewById(R.id.etName);
        etSize = findViewById(R.id.etSize);
        etHeight = findViewById(R.id.etHeight);
        btnInsert = findViewById(R.id.btnInsert);
        btnClear = findViewById(R.id.btnClear);
        btnRead = findViewById(R.id.btnRead);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });
    }

    public void insertData() {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String animal = etAnimal.getText().toString();
        String name = etName.getText().toString();
        double size, height;
        try {
            size = Double.parseDouble(etSize.getText().toString());
            height = Double.parseDouble(etHeight.getText().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("animal", animal);
        cv.put("name", name);
        cv.put("size", size);
        cv.put("height", height);

        // вставляем запись и получаем ее ID
        long rowID = db.insert("mytable", null, cv);
        Log.d("LOG_TAG", "row inserted, ID = " + rowID);
    }

    public void readData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("LOG_TAG", "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if(c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int animalColIndex = c.getColumnIndex("animal");
            int nameColIndex = c.getColumnIndex("name");
            int sizeColIndex = c.getColumnIndex("size");
            int heightColIndex = c.getColumnIndex("height");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("LOG_TAG",
                        "ID = "+ c.getInt(idColIndex) +
                                ", animal = "+ c.getString(animalColIndex) +
                                ", name = "+ c.getString(nameColIndex) +
                                ", size = "+ c.getDouble(sizeColIndex) +
                                ", height = "+ c.getDouble(heightColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while(c.moveToNext());
        } else
            Log.d("LOG_TAG", "0 rows");
        c.close();
    }

    public void clearData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("LOG_TAG", "--- Clear mytable: ---");
        // удаляем все записи
        int clearCount = db.delete("mytable", null, null);
        Log.d("LOG_TAG", "deleted rows count = " + clearCount);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}


class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE mytable ("
                + "id INTEGER PRIMARY KEY, "
                + "animal TEXT, "
                + "name TEXT, "
                + "size REAL, "
                + "height REAL" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
