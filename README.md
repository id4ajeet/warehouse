# Getting Started

This software hold articles, and the articles contain an identification number, a name and available stock. It is be
possible to load articles into the software from a file. The warehouse software also have products, products are made of
different articles. Products have a name, price and a list of articles of which they are made from with a quantity. The
products can also be loaded from a file.

The warehouse has the following functionality;

* Get all products and quantity of each that is an available with the current inventory
* Remove(Sell) a product and update the inventory accordingly
* Get the Articles
* Delete any article
* Get the Products
* Delete any Product
* Load Articles from file
* Load Products from file

### Software Specification

Project built using

- java 11.0.10 (adoptopenjdk-11.jdk)
- maven 3.8.1
- spring boot 2.5.2
- spring-boot-devtools 2.5.2
- lombok 1.18.20
- swagger springfox 3.0.0
- Docker Compose (for postgres db)
- postgres

### Build

To build the application go to the project directory and run maven install. This will execute tests as well

```shell
cd warehouse
mvn clean install
```

To only run **tests**

```shell
cd warehouse 
mvn test
```

### Run the application

Application needs Postgres database, for this purpose docker-compose.yml is used

- Build the Database (Dockerfile for DB can be located at `src/main/docker/db/Dockerfile`)

```shell
cd warehouse 
docker-compose build db
```

it will use init.d/init_db.sql file to create the schema

- Build the application using maven command `mvn clean install`

- Start the database and application together

```shell
cd warehouse  
docker-compose up
```

**Note**: in docker-compose application port 8080 is mapped to 8888, So all the apis will be accessible at port 8888

open browser and access http://localhost:8888/actuator/health

- Application can be started using java command also, But start the database first to avoid failures.

```shell
docker-compose up db

java -jar target/mancala-game-0.0.1-SNAPSHOT.jar
```

- To Stop the docker-compose

```shell
docker-compose down
```

### Load data from file

Application property `warehouse.fs.listener.directory.name=inventory` is used to monitor directory `inventory` (in
project root)

- All files with prefix `inventory` will be used to load Articles, it can be changed using
  property `warehouse.fs.listener.inventoryFile.prefix`
- All files with prefix `product` will be used to load Products, it can be changed using
  property `warehouse.fs.listener.productFile.prefix`
- Sample Articles file `src/test/resources/samples/inventory-input.json`
- Sample Products file `src/test/resources/samples/products-input.json`

Copy files to `inventory` directory to load them in DB. `inventory` directory monitoring properties are

- `warehouse.fs.listener.directory.pollInterval`:4000
- `warehouse.fs.listener.directory.quietPeriod`:3000

**Note**: hidden files or file without matching prefix will be skipped from processing.

### Database tables

- `article`(id, name, stock) : to hold the articles
- `product`(id, name, price) : to hold the articles
- `product_composition`(id, product_id, article_id, article_quantity) : to hold the articles of a product

### API Details

| Http Method   | URI                            | Description                                                       |
| ------------- | ------------------------------ | ----------------------------------------------------------------- |
| GET           | /v1/purchase/products          | load all products based on quantity of articles in stock          |
| POST          | /v1/purchase/products          | sell multiple products if available and updates articles in stock |
|               |                                |                                                                   |
| GET           | /v1/stock/articles             | load all articles                                                 |
| GET           | /v1/stock/articles/{id}        | load single article                                               |
| DELETE        | /v1/stock/articles/{id}        | delete single article                                             |
|               |                                |                                                                   |
| GET           | /v1/definition/products        | load all products                                                 |
| GET           | /v1/definition/products/{name} | load single product                                               |
| DELETE        | /v1/definition/products/{name} | delete single product                                             |

#### Purchase APIs

- API `GET /v1/purchase/products` can be used to load available products in stock

```shell
curl -i --location --request GET 'http://localhost:8888/v1/purchase/products'
```
Response Example:
```json
[
  {
    "name": "Table Fan",
    "price": "50.0",
    "quantity": 10,
    "contain_articles": [
      {
        "art_id": "5",
        "amount_of": "1"
      }
    ]
  },
  {
    "name": "Window",
    "price": "250.0",
    "quantity": 4,
    "contain_articles": [
      {
        "art_id": "6",
        "amount_of": "2"
      },
      {
        "art_id": "8",
        "amount_of": "1"
      }
    ]
  }
]
```

- API `POST /v1/purchase/products` can be used to buy products in stock

```shell
curl -i --location --request POST 'http://localhost:8888/v1/purchase/products' \
--header 'Content-Type: application/json' \
--data '[    
    {
        "name": "Door",
        "quantity": "3"
    },
    {
        "name": "Dinning Table",
        "quantity": "1"
    }
]'
```
Success Response Example:
```json
{
  "totalAmount": "1050.75"
}
```
Failure Response Example:
```json
{
    "message": "Stock not available for article[6] - Door Nob"
}
```

### Swagger API documentation

After server startup

- Swagger api-docs in json can be accessed at http://localhost:8888/v2/api-docs
- Swagger UI can be accessed at http://localhost:8888/swagger-ui/#/

### Actuator Health API
Health API is available at path `/actuator/health` 

```shell
curl -i --location --request GET 'http://localhost:8888/actuator/health'
```

### Further Improvements

- Security implementation
- Pagination in APIs
- Spring boot actuator for metrics
- Component testing (maybe using cucumber)
- Caching implementation
- APIs to load Articles and Products