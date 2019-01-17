package com.project.stephencao.myasynctask02.bean;

public class NewsBean {
    private String name;
    private String picSmall;
    private String description;

    public NewsBean(String name, String picSmall, String description) {
        this.name = name;
        this.picSmall = picSmall;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "name='" + name + '\'' +
                ", picSmall='" + picSmall + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
