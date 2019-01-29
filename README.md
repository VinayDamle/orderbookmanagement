# orderbookmanagement
Prerequisites
-------------
Must have java 8 and maven configured

Swagger Details
---------------
http://localhost:9090/swagger-ui.html#

Steps to run
------------

1. Unzip the project folder

Follow below when project is imported on IDE

2. Import the project into intellij IDE (can be imported to eclipse as well)
3. Open terminal and execute mvn clean install
4. If the project is imported into eclipse then perform right click pom.xml and say run as maven. In the dialog box maven goals can be provided (clean install)
5. All the test results can be seen on console

or

Follow below if project is not imported on IDE
2. Open command prompt and goto directory and look for pom.xml
3. mvn clean install
4. all the test results can be seen on console


==================================================================================================================================================================

SOAP UI project for following APIs
----------------------------------

Open Book
---------
POST http://localhost:9090/orderbook/{instrumentId}/status HTTP/1.1
Accept-Encoding: gzip,deflate
Content-Type: application/json
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==

Close Book
----------
POST http://localhost:9090/orderbook/{instrumentId}/status HTTP/1.1
Accept-Encoding: gzip,deflate
Content-Type: application/json
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==

Add Order
---------
POST http://localhost:9090/orderbook/{instrumentId}/order HTTP/1.1
Accept-Encoding: gzip,deflate
Content-Type: application/json
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==

Execute Order
-------------
POST http://localhost:9090/orderbook/{instrumentId}/execute HTTP/1.1
Accept-Encoding: gzip,deflate
Content-Type: application/json
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==

Get Order State by orderId
--------------------------
GET http://localhost:9090/orderbook/statstics/order/{orderId}/orderState HTTP/1.1
Accept-Encoding: gzip,deflate
Authorizarion: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==
Content-Type: application/json

Get Order detail statstics
--------------------------
GET http://localhost:9090/orderbook/statstics HTTP/1.1
Accept-Encoding: gzip,deflate
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==
Content-Type: application/json

Get Order detail statstics (incloudes valid/invalid order counts)
------------------------------------------------------------------
GET http://localhost:9090/orderbook/statstics?fetchValidInvalidRecords=true HTTP/1.1
Accept-Encoding: gzip,deflate
Authorization: Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==
Content-Type: application/json

Added SOAP UI project with Order Book test suite
------------------------------------------------


Enhancement
-----------
1. Crud for Instrument API
2. Link OrderBook with Instrument
3. Validation to perforn any opertation for non-existing instrument

========================================
UserName : OrderBookUser01
Password : OrderBookUser01
