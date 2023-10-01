package com.example.pr17_aubovsergey_pr_21102;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

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
    EditText etAnimal, etName, etSize, etHeight, etID;
    Button btnInsert, btnRead, btnClear, btnDelete, btnUpdate;

    String animal, name, id;
    double size, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        etAnimal = findViewById(R.id.etAnimal);
        etName = findViewById(R.id.etName);
        etSize = findViewById(R.id.etSize);
        etHeight = findViewById(R.id.etHeight);
        etID = findViewById(R.id.etID);

        btnInsert = findViewById(R.id.btnInsert);
        btnClear = findViewById(R.id.btnClear);
        btnRead = findViewById(R.id.btnRead);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    public void insertData() {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        animal = etAnimal.getText().toString();
        name = etName.getText().toString();

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

    @SuppressLint("RestrictedApi")
    public void updateData() {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        id = etID.getText().toString();

        if (id.equalsIgnoreCase("")) {
        }

        Log.d(LOG_TAG, "--- Update mytable: ---");
        // подготовим значения для обновления
        cv.put("animal", animal);
        cv.put("name", name);
        cv.put("size", size);
        cv.put("height", height);
        // обновляемпо id
        int updCount = db.update("mytable", cv, "id = ?",
                new String[] { id });
        Log.d(LOG_TAG, "updated rows count = "+ updCount);


    }

    @SuppressLint("RestrictedApi")
    public void deleteData(){

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        id = etID.getText().toString();

        if(id.equalsIgnoreCase("")) {

        }

        Log.d(LOG_TAG, "--- Delete from mytable: ---");
        // удаляемпо id
        int delCount = db.delete("mytable", "id = "+ id, null);
        Log.d(LOG_TAG, "deleted rows count = "+ delCount);


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
