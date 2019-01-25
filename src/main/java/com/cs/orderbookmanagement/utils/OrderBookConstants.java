package com.cs.orderbookmanagement.utils;

import java.util.HashMap;
import java.util.Map;

public interface OrderBookConstants {

    static final Map<String, String> errorMap = new HashMap<>();



    static String STATUS = "status/";
    static final String OPEN = "Open";
    static final String CLOSE = "Close";
    static final String UTF_8 = "UTF-8";
    static final String VALID = "Valid";
    static final String INVALID = "InValid";
    static final String OPENED = "Opened";
    static final String CLOSED = "Closed";
    static final String OBMS_ORD_BOOK_CLOSED_INSTID = "Order book is closed for instrumentid";
    static String OBMS_INVL_CMD = "OBMS-INVL-CMD";
    static final String LIMIT_ORDER = "LimitOrder";
    static final String MARKET_ORDER = "MarketOrder";
    static final String ORDERBOOKMGMNT = "/orderbook/";
    static String OBMS_INVL_INST_ID = "OBMS-INVL-INTRUMENT_ID";
    static String INSTRUMENT_ID_NOT_FOUND = "InstrumentId Not Found.";
    static String ERROR_IN_ADDING_INSTRUMENT = "Error in adding instrument.";
    static String INVALID_COMMAND = "Invalid command. Only OPEN & CLOSE are valid command types.";
}
