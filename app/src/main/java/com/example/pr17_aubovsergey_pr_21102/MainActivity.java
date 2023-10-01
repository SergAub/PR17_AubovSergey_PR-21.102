package com.example.pr17_aubovsergey_pr_21102;

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


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // создаемобъектдляданных
        ContentValues cv = new ContentValues();

        // получаемданныеизполейввода
        String animal = etName.getText().toString();
        String name = etEmail.getText().toString();
        double size = etSize.getText().toString();
        double height = etHeight.getText().toString();

        // подключаемсякБД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // подготовим данные для вставки в виде пар: наименование столбца - значение

        cv.put("animal", animal);
        cv.put("name", name);
        cv.put("size", size);
        cv.put("height", height);

        // вставляем запись и получаем ее ID
        long rowID = db.insert("mytable", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = "+ rowID);
        break;
        caseR.id.btnRead:
        Log.d(LOG_TAG, "--- Rows in mytable: ---");

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if(c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int emailColIndex = c.getColumnIndex("email");

            do{
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = "+ c.getInt(idColIndex) +
                                ", name = "+ c.getString(nameColIndex) +
                                ", email = "+ c.getString(emailColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while(c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        break;
        caseR.id.btnClear:
        Log.d(LOG_TAG, "--- Clear mytable: ---");
        // удаляемвсезаписи
        int clearCount = db.delete("mytable", null, null);
        Log.d(LOG_TAG, "deleted rows count = "+ clearCount);
        break;
    }
    // закрываем подключение к БД
    dbHelper.close();

}
class DBHelper extends SQLiteOpenHelper {

    void publicDBHelper(Context context) {
        // конструкторсуперкласса
        super(context, "myDB", null, 1);
    }

    void publicvoidonCreate(SQLiteDatabasedb) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаемтаблицусполями
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text"+ ");");
    }

    @Override
    publicvoidonUpgrade(SQLiteDatabasedb, intoldVersion, intnewVersion) {

    }
}
