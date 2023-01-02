
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`
(
    `id`         INT NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(50),
    `last_name`  VARCHAR(50),
    `email`      VARCHAR(100),
    `salary`     DOUBLE,
    PRIMARY KEY (`id`)
);
