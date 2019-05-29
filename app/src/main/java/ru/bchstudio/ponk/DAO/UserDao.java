package ru.bchstudio.ponk.DAO;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import ru.bchstudio.ponk.DAO.entities.User;


public class UserDao {
    private static final String TAG = "UserDao";
    private Dao<User,Integer> dao;

    public UserDao(Context context){
        DatabaseHelper helper = DatabaseHelper.getInstance(context);

        try {
            dao = helper.getDao(User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user){
        try{
            dao.create(user);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "addUser: "+e.getMessage());
        }
    }

    public void updateUser(User user) {
        try {
            dao.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * update user_info set name=XX  where id = XX
     *
     * @param user
     * @param id
     */
    public void updateUserByID(User user, Integer id) {
        try {
            dao.updateId(user, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据
     * update user_info set name=XX   where id = XX
     *
     * @param user
     */
    public void updateUserByBuilder(User user) {
        try {
            UpdateBuilder builder = dao.updateBuilder();
            builder.updateColumnValue("name", user.getName())
                    .where().eq("id",1);
            builder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个user对象
     * @param user
     */
    public void deleteUser(User user) {
        try {
            dao.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除users
     * @param users
     */
    public void deleteMulUser(List<User> users){
        try{
            dao.delete(users);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据传入的id集合 删除数据
     * @param ids
     */
    public void deleteUserByIDs(List<Integer> ids){
        try{
            dao.deleteIds(ids);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * select * from user_info
     * 从 user_info 表中 获取全部的
     * @return
     */
    public List<User> listAll(){
        try {
            return dao.queryForAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 条件查询
     * select * from user_info where (age>23 and name like ?) and XXX
     *
     * @return
     */
    public List<User> getUsersByName(String name){
        List<User> list = null;
        QueryBuilder<User, Integer> queryBuilder = dao.queryBuilder();
        Where<User,Integer> where = queryBuilder.where();
        try {
            where.eq("name",name);
//            where.and();
//            where.eq("desc","Hong Kong");
            where.prepare();
            //select * from user_info where name ='刘德华' and desc='香港'
            list = queryBuilder.query();
            //list = userDao.queryBuilder().where().eq("name", "刘德华").and().eq("desc", "香港").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<User> queryBuilder_2() {
        List<User> list = null;

        QueryBuilder<User, Integer> queryBuilder = dao.queryBuilder();
        Where<User, Integer> where = queryBuilder.where();
        try {
            list = where.or(where.and(where.eq("name", "jack"), where.eq("desc", "湖北")),
                    where.and(where.eq("name", "张学友"), where.eq("id", "1")))
                    .query();

            where.and(where.in("name","jack","刘德华"),where.eq("name","jack"));
            // where (name in("jack","刘德华")) and name = "jack"
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
