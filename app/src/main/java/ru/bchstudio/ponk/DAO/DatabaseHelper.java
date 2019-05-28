package ru.bchstudio.ponk.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作管理工具类
 * <p>
 * 我们需要自定义一个类继承自ORMlite给我们提供的OrmLiteSqliteOpenHelper，创建一个构造方法，重写两个方法onCreate()和onUpgrade()
 * 在onCreate()方法中使用TableUtils类中的createTable()方法初始化数据表
 * 在onUpgrade()方法中我们可以先删除所有表，然后调用onCreate()方法中的代码重新创建表
 * <p>
 * 我们需要对这个类进行单例，保证整个APP中只有一个SQLite Connection对象
 * <p>
 * 这个类通过一个Map集合来管理APP中所有的DAO，只有当第一次调用这个DAO类时才会创建这个对象（并存入Map集合中）
 * 其他时候都是直接根据实体类的路径从Map集合中取出DAO对象直接调用
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // 数据库名称
    public static final String DATABASE_NAME = "mydb.db";

    // 本类的单例实例
    private static DatabaseHelper instance;

    // 存储APP中所有的DAO对象的Map集合
    private Map<String, Dao> maps = new HashMap<>();

    // 获取本类单例对象的方法
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

    // 私有的构造方法
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * 获得数据库的访问对象
     * @param cls
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class cls) throws SQLException {
        Dao dao = null;
        String className = cls.getSimpleName();//通过反射获得类名
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
     * 关闭所有操作
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
        // 完成对数据库的创建 以及表的建立
        try {
            TableUtils.createTableIfNotExists(connectionSource,User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,User.class,true);
        }catch (Exception e){

        }
    }
}
