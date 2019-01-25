package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.models.OrderBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderBookRepositoryTests {

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Test
    public void testFindOrderDetailsByInstrumentId() {
        OrderBook orderBook = new OrderBook();
        OrderBook savedOrderBook = orderBookRepository.save(orderBook);
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(savedOrderBook.getInstrumentId());
        assertThat(orderBookHolder.isPresent());
        assertThat(orderBookHolder.get().getInstrumentId()).isEqualTo(savedOrderBook.getInstrumentId());
    }

}
