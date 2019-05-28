package ru.bchstudio.ponk.DAO.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @DatabaseTable(tableName = "user_info") 配置数据库的表名称 为 user_info
 */

@DatabaseTable(tableName = "user_info")
public class User {
    /**
     * 声明主键
     */
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "desc")
    private String desc;

    public User(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public User(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
