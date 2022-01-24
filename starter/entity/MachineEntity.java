package com.galeeva.jdbc.starter.entity;

public class MachineEntity {

    private Integer id;
    private String model;
    private String type;

    public MachineEntity(Integer id, String model, String type) {
        this.id = id;
        this.model = model;
        this.type = type;
    }

    public MachineEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MachineEntity{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
