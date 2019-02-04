package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@JsonIgnoreProperties
final public class OrderRequest {

    private BigDecimal price;

    private Integer quantity;

    private Long instrumentId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate entryDate;

    public OrderRequest(Integer quantity, LocalDate entryDate, Long instrumentId, BigDecimal price) {
        if (quantity != null) {
            this.quantity = new Integer(quantity);
        } else {
            this.quantity = null;
        }
        if (instrumentId != null) {
            this.instrumentId = new Long(instrumentId);
        } else {
            this.instrumentId = null;
        }
        if (price != null) {
            this.price = new BigDecimal(price.doubleValue());
        } else {
            this.price = null;
        }
        if (entryDate != null) {
            this.entryDate = LocalDate.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth());
        } else {
            this.entryDate = null;
        }
    }

    public BigDecimal getPrice() {
        if (price == null) {
            return price;
        }
        return new BigDecimal(price.doubleValue());
    }

    public Integer getQuantity() {
        if (quantity == null) {
            return quantity;
        }
        return new Integer(quantity);
    }

    public Long getInstrumentId() {
        if (instrumentId == null) {
            return instrumentId;
        }
        return new Long(instrumentId);
    }

    public LocalDate getEntryDate() {
        if (entryDate == null) {
            return entryDate;
        }
        return LocalDate.of(entryDate.getYear(), entryDate.getMonth(), entryDate.getDayOfMonth());
    }

    @Override
    public String toString() {
        return "Order{" +
                ", price=" + price +
                "quantity=" + quantity +
                ", entryDate=" + entryDate +
                ", instrumentId=" + instrumentId +
                '}';
    }

}