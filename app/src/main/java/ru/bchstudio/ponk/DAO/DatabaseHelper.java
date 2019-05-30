package ru.bchstudio.ponk.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ru.bchstudio.ponk.DAO.entities.User;
import ru.bchstudio.ponk.DAO.entities.Weather;

/**
 * Класс инструмента управления операциями с базой данных
   *
   * Нам нужно настроить класс, который наследует от OrmLiteSqliteOpenHelper, предоставляемого ORMlite, создать конструктор, переопределить два метода onCreate () и onUpgrade ()
   * Используйте метод createTable () в классе TableUtils для инициализации таблицы данных в методе onCreate ().
   * В методе onUpgrade () мы можем сначала удалить все таблицы, а затем вызвать код в методе onCreate () для воссоздания таблицы.
   *
   * Нам нужно сделать синглтон для этого класса, чтобы гарантировать, что во всем приложении есть только один объект SQLite Connection.
   *
   * Этот класс управляет всеми DAO в приложении через коллекцию Map.Этот объект будет создан только при первом вызове класса DAO (и сохранен в коллекции Map).
   * В других случаях объект DAO вызывается напрямую из коллекции Map напрямую в соответствии с путем к классу сущностей.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Имя базы данных
    private static final String DATABASE_NAME = "ponk.db";
    // Версия базы данных, изменить при изменении схемы
    private static final int DATABASE_VERSION = 6;


    // Единственный экземпляр класса
    private static DatabaseHelper instance;

    // Хранит коллекцию карт всех объектов DAO в приложении
    private Map<String, Dao> maps = new HashMap<>();

    // Возвращает инстанс синглтона
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    // Приватный конструктор
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG, "Подключена база данных "+ DATABASE_NAME);

    }

    /**
     * Возвращает объект доступа к БД
     * @param cls
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class cls) throws SQLException {
        Dao dao = null;
        String className = cls.getSimpleName();
        if (maps.containsKey(className)) {
            dao = maps.get(className);
        }
        if (dao == null) {
            dao = super.getDao(cls);
            maps.put(className, dao);
        }
        return dao;
    }

    /**
     * Завершаем все операции
     */
    public void close(){
        super.close();
        for(String key:maps.keySet()){
            Dao dao = maps.get(key);
            dao = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // Создаем таблицы
        Log.e(TAG, "Создаем таблицы");
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.e(TAG, "Версия базы данных изменилась. Удаляем таблицы.");
        try {
            TableUtils.dropTable(connectionSource,User.class,true);
            TableUtils.dropTable(connectionSource,Weather.class,true);

        }catch (Exception e){
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }
}
