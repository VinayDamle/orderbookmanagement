package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.models.Order;
import com.cs.orderbookmanagement.models.OrderDao;
import com.cs.orderbookmanagement.models.OrderDetails;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderDetailsRepositoryTests {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Test
    public void testSaveOrderBook() {
        OrderDetails orderDetails = getTestOrderDetails();
        int instrumentId = orderDetails.getOrder().getInstrumentId();
        assertThat(orderDetailsRepository.save(orderDetails).getOrder().getInstrumentId()).isEqualTo(instrumentId);
    }

    @Test
    public void testFindAllOrderDetailsByOrderInstrumentId() {
        OrderDetails orderDetails = getTestOrderDetails();
        int instrumentId = orderDetails.getOrder().getInstrumentId();
        assertThat(orderDetailsRepository.save(orderDetails).getOrder().getInstrumentId()).isEqualTo(instrumentId);

        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
        assertThat(orderDetailsList).isNotNull();
        assertThat(orderDetailsList.size()).isEqualTo(1);
    }

    public OrderDetails getTestOrderDetails() {
        OrderDetails orderDetails = new OrderDetails();
        OrderDao order = new OrderDao(10, "", 1, 100);
        orderDetails.setOrder(order);
        return orderDetails;
    }
}
