DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT (
                      id INT AUTO_INCREMENT  PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      description VARCHAR(50) NOT NULL,
                      price FLOAT
);