package com.galeeva.jdbc.starter;

import com.galeeva.jdbc.starter.dao.OrderDataDao;
import com.galeeva.jdbc.starter.entity.MachineEntity;
import com.galeeva.jdbc.starter.entity.OrderDataEntity;
import com.galeeva.jdbc.starter.entity.ServiceEntity;
import com.galeeva.jdbc.starter.entity.UserEntity;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
//        var orderDataFilter = new OrderDataFilter(3, 2);
//        List<OrderDataEntity> orderDataEntities = OrderDataDao.getInstance().findAll(orderDataFilter);
//        System.out.println(orderDataEntities);
    }

    private static void updateTest() {
        OrderDataDao orderDataDao = OrderDataDao.getInstance();
        Optional<OrderDataEntity> maybeOrderData = orderDataDao.findById(2L);
        System.out.println(maybeOrderData);

        maybeOrderData.ifPresent(orderData -> {
            orderData.setTotalPrice(BigDecimal.valueOf(188.5));
            orderDataDao.update(orderData);
        });
    }

    private static void deleteTest() {
        OrderDataDao orderDataDao = OrderDataDao.getInstance();
        saveTest();
        boolean deleteResult = orderDataDao.delete(1L);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        OrderDataDao orderDataDao = OrderDataDao.getInstance();
        var orderData = new OrderDataEntity();
        var service = new ServiceEntity();
        var user = new UserEntity();
        var machine = new MachineEntity();
        service.setId(56);
        orderData.setFile("image.jpg");
        orderData.setPaperType("mat");
        orderData.setQuantity(25L);
        machine.setId(5);
        orderData.setStatus("upload");
        orderData.setTotalPrice(BigDecimal.TEN);
        orderData.setCreatedAt(LocalDateTime.of(2022, 01, 25, 12, 12, 12));
        orderData.setDeliveredAt(LocalDateTime.of(2022, 10, 15, 5, 35, 25));
        orderData.setDelivery("By postman");
        OrderDataEntity savedOrderData = orderDataDao.save(orderData);
        System.out.println(savedOrderData);
    }
}
