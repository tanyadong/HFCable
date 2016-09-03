package com.hbhongfei.hfcable.pojo;

/**
 * Created by 苑雪元 on 2016/8/2.
 */
public class TypeInfo {
    protected String Id;
    protected String name;
    protected boolean isChoosed;
    private boolean isEdtor;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public boolean isEdtor() {
        return isEdtor;
    }

    public void setEdtor(boolean edtor) {
        isEdtor = edtor;
    }

    public TypeInfo(String id, String name, boolean isChoosed, boolean isEdtor) {
        Id = id;
        this.name = name;
        this.isChoosed = isChoosed;
        this.isEdtor = isEdtor;
    }

    public TypeInfo(String id, String name) {
        Id = id;
        this.name = name;
    }

    public TypeInfo() {
    }
}
