package com.galeeva.jdbc.starter.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDataEntity {

    private Long id;
    private UserEntity usersId;
    private ServiceEntity serviceId;
    private String file;
    private String paperType;
    private Long quantity;
    private MachineEntity machineId;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private String delivery;

    public OrderDataEntity(Long id, UserEntity usersId, ServiceEntity serviceId, String file, String paperType, Long quantity,
                           MachineEntity machineId, String status, BigDecimal totalPrice, LocalDateTime createdAt, LocalDateTime deliveredAt, String delivery) {
        this.id = id;
        this.usersId = usersId;
        this.serviceId = serviceId;
        this.file = file;
        this.paperType = paperType;
        this.quantity = quantity;
        this.machineId = machineId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.delivery = delivery;
    }

    public OrderDataEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUsersId() {
        return usersId;
    }

    public void setUsersId(UserEntity usersId) {
        this.usersId = usersId;
    }

    public ServiceEntity getServiceId() {
        return serviceId;
    }

    public void setServiceId(ServiceEntity serviceId) {
        this.serviceId = serviceId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public MachineEntity getMachineId() {
        return machineId;
    }

    public void setMachineId(MachineEntity machineId) {
        this.machineId = machineId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Override
    public String toString() {
        return "OrderDataEntity{" +
                "id=" + id +
                ", user=" + usersId +
                ", serviceId=" + serviceId +
                ", file='" + file + '\'' +
                ", paperType='" + paperType + '\'' +
                ", quantity=" + quantity +
                ", machineId=" + machineId +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                ", deliveredAt=" + deliveredAt +
                ", delivery='" + delivery + '\'' +
                '}';
    }
}
