package dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.samsungtvcontrol.entity.Brands;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tv_command";
    private static final String BRANDS_TABLE = "brands";
    private static final String BRAND_ID = "id";
    private static final String BRAND_FULL_NAME = "full_name";
    private static final String BRAND_FROM = "brand_from";
    private static final String BRAND_TO = "brand_to";
    private static final String BRAND_STATUS = "status";

    private static final String METHODS_TABLE = "methods";
    private static final String METHOD_ID = "id";
    private static final String METHOD_NAME = "name";
    private static final String METHOD_STATUS = "status";

    private static final String BRANDS_METHODS_TABLE = "brands_methods";
    private static final String BRAND_METHOD_BRAND_ID = "brand_id";
    private static final String BRAND_METHOD_METHOD_ID = "method_id";


    private static final String APP_TABLE = "app";
    private static final String APP_ID = "id";
    private static final String APP_METHOD_ID = "method_id";
    private static final String APP_APP_NAME = "app_name";
    private static final String APP_APP_ID = "app_id";

    private static final String KEYCODE_TABLE = "keycodes";
    private static final String KEYCODE_ID = "id";
    private static final String KEYCODE_CODE = "code";
    private static final String KEYCODE_KEYCODE = "keycode";
    private static final String KEYCODE_METHOD_ID = "method_id";
    private static final String KEYCODE_BUTTON_ID = "button_id";

    private static final String TV_INFO_TABLE = "tv_info";
    private static final String TV_INFO_ID = "id";
    private static final String TV_INFO_NAME = "name";
    private static final String TV_INFO_BRAND_ID = "brand_id";

    private static final String TV_INFO_DETAIL_TABLE = "tv_info_detail";
    private static final String TV_INFO_DETAIL_ID = "id";
    private static final String TV_INFO_DETAIL_TV_INFO_ID = "info_id";
    private static final String TV_INFO_DETAIL_IP = "ip";
    private static final String TV_INFO_DETAIL_UUID = "uuid";
    private static final String TV_INFO_DETAIL_VERSION = "version";
    private static final String TV_INFO_DETAIL_LAST_CONNECT = "last_connect";
    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("DB Manager", "DB Manager");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQueryUser = "CREATE TABLE " + BRANDS_TABLE + " (" +
                BRAND_ID + " integer primary key AUTOINCREMENT, " +
                BRAND_FROM + " TEXT," +
                BRAND_TO + " TEXT," +
                BRAND_STATUS + " TEXT," +
                BRAND_FULL_NAME + " TEXT)";
        db.execSQL(sqlQueryUser);

        String sqlQueryNote = "CREATE TABLE " + METHODS_TABLE + " (" +
                METHOD_ID + " integer primary key AUTOINCREMENT , " +
                METHOD_NAME + " TEXT," +
                METHOD_STATUS + " integer)";
        db.execSQL(sqlQueryNote);

        String sqlQueryChecklist = "CREATE TABLE " + BRANDS_METHODS_TABLE + " (" +
                BRAND_METHOD_BRAND_ID + " integer primary key AUTOINCREMENT , " +
                BRAND_METHOD_METHOD_ID + " integer," +
                BRAND_METHOD_METHOD_ID + " integer)";
        db.execSQL(sqlQueryChecklist);


        String sqlQueryChecklistDetail = "CREATE TABLE " + APP_TABLE + " (" +
                APP_ID + " integer primary key AUTOINCREMENT , " +
                APP_APP_NAME + " TEXT," +
                APP_APP_ID + " TEXT," +
                APP_METHOD_ID + " integer)";
        db.execSQL(sqlQueryChecklistDetail);

        String sqlQueryAtt = "CREATE TABLE " + KEYCODE_TABLE + " (" +
                KEYCODE_ID + " integer primary key AUTOINCREMENT, " +
                KEYCODE_CODE + " TEXT," +
                KEYCODE_KEYCODE + " TEXT," +
                KEYCODE_BUTTON_ID + " integer," +
                KEYCODE_METHOD_ID + " integer)";
        db.execSQL(sqlQueryAtt);

        String sqlQueryCat = "CREATE TABLE " + TV_INFO_TABLE + " (" +
                TV_INFO_ID + " integer primary key AUTOINCREMENT, " +
                TV_INFO_NAME + " TEXT," +
                TV_INFO_BRAND_ID + " integer)";
        db.execSQL(sqlQueryCat);

        String sqlQueryDetail = "CREATE TABLE " + TV_INFO_DETAIL_TABLE + " (" +
                TV_INFO_DETAIL_ID + " integer primary key AUTOINCREMENT, " +
                TV_INFO_DETAIL_TV_INFO_ID + " integer," +
                TV_INFO_DETAIL_IP + " TEXT," +
                TV_INFO_DETAIL_UUID + " TEXT," +
                TV_INFO_DETAIL_VERSION + " TEXT," +
                TV_INFO_DETAIL_LAST_CONNECT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sqlQueryDetail);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BRANDS_TABLE);
        onCreate(db);
        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }

    public void addBrand(Brands b) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BRAND_FROM, b.getBrand_from());
            values.put(BRAND_TO, b.getBrand_to());
            values.put(BRAND_FULL_NAME, b.getFull_name());
            values.put(BRAND_TO, b.getBrand_to());
            values.put(BRAND_STATUS, 1);
//            if (getUserByPhoneNumber(b.getPhoneNumber()) != null) {
//                db.update(USER_TABLE_NAME, values, USER_ID + "=?", new String[]{String.valueOf(b.getId())});
//            } else
            db.insert(BRANDS_TABLE, null, values);
            db.close();
        } catch (Exception e) {
            Log.v("error", e.toString());
        }
    }

    public Brands getBrandById(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(BRANDS_TABLE, new String[]{BRAND_ID, BRAND_FULL_NAME
                            , BRAND_FROM, BRAND_TO, BRAND_STATUS}, BRAND_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            else
                return null;
            Brands b = new Brands(cursor.getInt(0), cursor.getString(1),
                    cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
            cursor.close();
            db.close();
            return b;
        } catch (Exception e) {
            Log.v("error", e.toString());
            return null;
        }
    }

    public int updateBrand(Brands b) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BRAND_ID, b.getId());
            values.put(BRAND_FULL_NAME, b.getFull_name());
            values.put(BRAND_FROM, b.getBrand_from());
            values.put(BRAND_TO, b.getBrand_to());
            values.put(BRAND_STATUS, b.getStatus());
            return db.update(BRANDS_TABLE, values, BRAND_ID + "=?", new String[]{String.valueOf(b.getId())});
        } catch (Exception e) {
            Log.v("error", e.toString());
            return 0;
        }
    }

    public List<Brands> getAllBrand() {
        try {
            List<Brands> listBrand = new ArrayList<>();
            String selectQuery = "SELECT  * FROM " + BRANDS_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Brands b = new Brands();
                    b.setId(cursor.getInt(0));
                    b.setFull_name(cursor.getString(1));
                    b.setBrand_from(cursor.getInt(2));
                    b.setBrand_to(cursor.getInt(3));
                    b.setStatus(cursor.getInt(4));
                    listBrand.add(b);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return listBrand;
        } catch (Exception e) {
            Log.v("error", e.toString());
            return new ArrayList<>();
        }
    }

//    public List<Note> getNodeByUserId(int userId) {
//        try {
//            SQLiteDatabase db = this.getReadableDatabase();
//            List<Note> nodes = new ArrayList<>();
//            Cursor cursor = db.query(NOTE_TABLE_NAME, new String[]{NOTE_ID,
//                            NOTE_PASSWORD, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATED,
//                            NOTE_LAST_MODIFIED, NOTE_TIMER, NOTE_STATUS, NOTE_USER_ID, NOTE_CAT_NAME},
//                    NOTE_USER_ID + "=?" + " AND " + NOTE_STATUS + "= 1 ",
//                    new String[]{String.valueOf(userId)}, null, null, NOTE_LAST_MODIFIED + " DESC", null);
//            if (cursor != null)
//                cursor.moveToFirst();
//            else
//                return new ArrayList<>();
//            do {
//                Note note = new Note();
//                note.setId(cursor.getInt(0));
//                note.setPassword(cursor.getString(1));
//                note.setTitle(cursor.getString(2));
//                note.setContent(cursor.getString(3));
//                note.setCreated(cursor.getString(4));
//                note.setLastModified(cursor.getString(5));
//                note.setTimer(cursor.getString(6));
//                note.setStatus(cursor.getInt(7));
//                note.setUserId(cursor.getInt(8));
//                note.setCatName(cursor.getString(9));
//                nodes.add(note);
//            } while (cursor.moveToNext());
//            cursor.close();
//            db.close();
//            return nodes;
//        } catch (Exception e) {
//            Log.v("error", e.toString());
//            return new ArrayList<>();
//        }
//    }

}
