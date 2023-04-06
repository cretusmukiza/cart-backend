-- -------------------------------------------------------------
-- TablePlus 4.8.7(448)
--
-- https://tableplus.com/
--
-- Database: retail
-- Generation Time: 2023-04-06 13:24:30.2200
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `cancelled_orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `reason` varchar(512) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnscikbe458ds3du99x0ffe3e5` (`order_id`),
  CONSTRAINT `FKnscikbe458ds3du99x0ffe3e5` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `discounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_code` varchar(255) NOT NULL,
  `discount_type` varchar(255) DEFAULT NULL,
  `discount_value` decimal(7,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_amount` decimal(7,2) DEFAULT NULL,
  `final_amount` decimal(7,2) DEFAULT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `total_amount` decimal(7,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `discount_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9cbujec8nd37w1mny5qar3uv8` (`discount_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  CONSTRAINT `FK9cbujec8nd37w1mny5qar3uv8` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_amount` decimal(7,2) DEFAULT NULL,
  `final_amount` decimal(7,2) DEFAULT NULL,
  `grand_total` decimal(7,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `product_attributes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attribute_key` varchar(255) NOT NULL,
  `attribute_value` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcex46yvx4g18b2pn09p79h1mc` (`product_id`),
  CONSTRAINT `FKcex46yvx4g18b2pn09p79h1mc` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

CREATE TABLE `product_inventories` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `reorder_level` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK53md215ibhaod6dw7af2qussf` (`product_id`),
  CONSTRAINT `FK53md215ibhaod6dw7af2qussf` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `product_prices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo21ew0lemtpkoyly3vm1mq925` (`product_id`),
  CONSTRAINT `FKo21ew0lemtpkoyly3vm1mq925` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(512) NOT NULL,
  `image` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `product_code` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_inventory_id` bigint(20) DEFAULT NULL,
  `product_price_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr66smyvnm5r9a7xnkh141oiv1` (`product_inventory_id`),
  KEY `FKlg8tcox3inixi7oj2y8mhwb4t` (`product_price_id`),
  CONSTRAINT `FKlg8tcox3inixi7oj2y8mhwb4t` FOREIGN KEY (`product_price_id`) REFERENCES `product_prices` (`id`),
  CONSTRAINT `FKr66smyvnm5r9a7xnkh141oiv1` FOREIGN KEY (`product_inventory_id`) REFERENCES `product_inventories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `products_discounts` (
  `products_id` bigint(20) NOT NULL,
  `discounts_id` bigint(20) NOT NULL,
  PRIMARY KEY (`products_id`,`discounts_id`),
  KEY `FKlcgo0my2tiqurc6yba0rmcteo` (`discounts_id`),
  CONSTRAINT `FKlcgo0my2tiqurc6yba0rmcteo` FOREIGN KEY (`discounts_id`) REFERENCES `discounts` (`id`),
  CONSTRAINT `FKn0w5tdco3l6lhp4d2w7bqxttg` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `shopping_cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `discount_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `shopping_cart_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK73tub1bs11btfwuxomhl5lhm9` (`discount_id`),
  KEY `FKgbo9dbc0drcea1m4u8112oxip` (`product_id`),
  KEY `FKbujx77cqv0w1v7v9cw6md956s` (`shopping_cart_id`),
  CONSTRAINT `FK73tub1bs11btfwuxomhl5lhm9` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`id`),
  CONSTRAINT `FKbujx77cqv0w1v7v9cw6md956s` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts` (`id`),
  CONSTRAINT `FKgbo9dbc0drcea1m4u8112oxip` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

CREATE TABLE `shopping_carts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3iw2988ea60alsp0gnvvyt744` (`user_id`),
  CONSTRAINT `FK3iw2988ea60alsp0gnvvyt744` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `user_sessions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `token` varchar(512) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8klxsgb8dcjjklmqebqp1twd5` (`user_id`),
  CONSTRAINT `FK8klxsgb8dcjjklmqebqp1twd5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

INSERT INTO `cancelled_orders` (`id`, `created_at`, `reason`, `updated_at`, `order_id`) VALUES
(1, '2023-04-06 10:21:49.820314', 'Order delayed', '2023-04-06 10:21:49.820354', 1);

INSERT INTO `discounts` (`id`, `active`, `created_at`, `discount_code`, `discount_type`, `discount_value`, `updated_at`) VALUES
(1, 1, '2023-04-06 10:02:49.027182', 'FSZ-9GP-NSX-R', 'AMOUNT', 10.00, '2023-04-06 10:02:49.027210');

INSERT INTO `order_items` (`id`, `created_at`, `discount_amount`, `final_amount`, `price`, `quantity`, `total_amount`, `updated_at`, `discount_id`, `product_id`, `order_id`) VALUES
(1, '2023-04-06 10:20:22.436494', 170.00, 14280.00, 850.00, 17, 14450.00, '2023-04-06 10:20:22.436504', 1, 1, 1);

INSERT INTO `orders` (`id`, `created_at`, `discount_amount`, `final_amount`, `grand_total`, `status`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 10:20:22.431934', 170.00, 14280.00, 14450.00, 'CREATED', '2023-04-06 10:20:22.443051', 1);

INSERT INTO `product_attributes` (`id`, `attribute_key`, `attribute_value`, `created_at`, `updated_at`, `product_id`) VALUES
(1, 'RAM', '16GB', '2023-04-06 10:01:12.170183', '2023-04-06 10:01:12.170188', 1),
(2, 'Storage', '512GB', '2023-04-06 10:01:12.172981', '2023-04-06 10:01:12.172985', 1);

INSERT INTO `product_inventories` (`id`, `created_at`, `quantity`, `reorder_level`, `updated_at`, `product_id`) VALUES
(1, '2023-04-06 10:01:12.137350', 589260, 100, '2023-04-06 10:04:12.899325', 1);

INSERT INTO `product_prices` (`id`, `created_at`, `price`, `updated_at`, `product_id`) VALUES
(1, '2023-04-06 10:01:12.151107', 850.00, '2023-04-06 10:04:43.798744', 1);

INSERT INTO `products` (`id`, `created_at`, `description`, `image`, `name`, `product_code`, `updated_at`, `product_inventory_id`, `product_price_id`) VALUES
(1, '2023-04-06 10:01:12.165554', 'Lenovo 2023 ThinkPad E15 Gen 4 High Performance Business Laptop: AMD Ryzen 5 5625U Hex-Core, 40GB RAM, 1TB NVMe SSD, 15.6 FHD 1920x1080 IPS Display, Win 10 Pro, Silver', 'https://www.amazon.com/Lenovo-ThinkPad-Performance-Business-Laptop', 'Lenovo 2023 ThinkPad E15', 'W1O3-83GT-MY', '2023-04-06 10:01:12.165561', 1, 1);

INSERT INTO `shopping_cart_items` (`id`, `created_at`, `quantity`, `updated_at`, `discount_id`, `product_id`, `shopping_cart_id`) VALUES
(2, '2023-04-06 10:18:05.939747', 17, '2023-04-06 10:18:05.939768', 1, 1, 1);

INSERT INTO `shopping_carts` (`id`, `created_at`, `expires_at`, `status`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 10:11:54.543364', '2023-04-07 10:18:05.903336', 'ACTIVE', '2023-04-06 10:20:22.441614', 1);

INSERT INTO `user_sessions` (`id`, `created_at`, `expires_at`, `token`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 10:09:14.080022', '2023-04-07 10:09:13.986580', 'eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODA4NjIxNTMsInVzZXJJZCI6MSwiaWF0IjoxNjgwNzc1NzUzfQ.ALjf8xnQyX7uzilw3o85iP_noeNoThoydBl7KWn4PErTKHzq82yDionJ2KwTiioHINIDnErAWVjDX7UwsXlcMw', '2023-04-06 10:09:14.080045', 1);

INSERT INTO `users` (`id`, `created_at`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `updated_at`) VALUES
(1, '2023-04-06 10:00:33.168719', 'cretus@gmail.com', 'cillum eiusmod', 'minimum', '$2a$10$aeT.MPwJlH4uOx9bX2yENOP.SeiOCq7l1mGMtrJnCarjG1mlj8W2q', '+255757807834', '2023-04-06 10:00:33.168751');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;