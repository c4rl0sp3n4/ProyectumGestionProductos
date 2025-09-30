# CRUD Reactivo (Spring Boot + WebFlux)

La clase principal que se levanta es ProyectumGestionProductosApplication.java que se encuentra en el pakage com.proyectum.gestion.productos

Implementa un API REST que permita crear, listar, actualizar y eliminar productos.
Cada producto debe tener: id, nombre, precio, stock.

http://localhost:8080/api/productos

[{"id":1,"nombre":"Laptop","precio":1200.00,"stock":10},{"id":2,"nombre":"Mouse","precio":25.00,"stock":3},{"id":3,"nombre":"Teclado","precio":75.00,"stock":2},{"id":4,"nombre":"Monitor","precio":300.00,"stock":7}]


http://localhost:8080/api/productos/1
{"id":1,"nombre":"Laptop","precio":1200.00,"stock":10}


## Persistencia (Spring Data)

Usa una base en memoria (H2) para almacenar los productos.

http://localhost:8080/api/productos

[{"id":1,"nombre":"Laptop","precio":1200.00,"stock":10},{"id":2,"nombre":"Mouse","precio":25.00,"stock":3},{"id":3,"nombre":"Teclado","precio":75.00,"stock":2},{"id":4,"nombre":"Monitor","precio":300.00,"stock":7}]


Implementa una consulta personalizada para listar los productos con stock < 5.

http://localhost:8080/api/productos/bajo-stock

[{"id":2,"nombre":"Mouse","precio":25.00,"stock":3},{"id":3,"nombre":"Teclado","precio":75.00,"stock":2}]



## Consumo de servicio externo (Spring Cloud WebClient)

Simula el consumo de un servicio externo (por ejemplo, una API pública de tipo de cambio).
Expone un endpoint adicional que devuelva el precio de los productos convertidos a otra moneda.

http://localhost:8080/api/productos/converted/EUR

[{"id":1,"name":"Laptop","originalPrice":1200.00,"originalCurrency":"USD","convertedPrice":1023.60,"targetCurrency":"EUR"},{"id":2,"name":"Mouse","originalPrice":25.00,"originalCurrency":"USD","convertedPrice":21.33,"targetCurrency":"EUR"},{"id":3,"name":"Teclado","originalPrice":75.00,"originalCurrency":"USD","convertedPrice":63.98,"targetCurrency":"EUR"},{"id":4,"name":"Monitor","originalPrice":300.00,"originalCurrency":"USD","convertedPrice":255.90,"targetCurrency":"EUR"}]

http://localhost:8080/api/productos/converted/XYZ
Moneda no soportada: XYZ

## Prueba automatizada

Incluye al menos 1 test unitario o de integración usando JUnit 5 y/o Mockito.

[INFO] [1;32mTests run: [0;1;32m2[m, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 21.66 s -- in com.proyectum.gestion.test.[1mProductoIntegracionTest[m

[INFO] Running com.proyectum.gestion.test.[1mProductServiceTest[m
[INFO] [1;32mTests run: [0;1;32m2[m, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.406 s -- in com.proyectum.gestion.test.[1mProductServiceTest




