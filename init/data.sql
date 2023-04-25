-- -------------------------------------------------------------
-- TablePlus 4.8.7(448)
--
-- https://tableplus.com/
--
-- Database: retail
-- Generation Time: 2023-04-06 16:25:43.7680
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `discounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_code` varchar(255) NOT NULL,
  `discount_type` varchar(255) DEFAULT NULL,
  `discount_value` decimal(7,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_amount` decimal(16,2) DEFAULT NULL,
  `final_amount` decimal(16,2) DEFAULT NULL,
  `price` decimal(16,2) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `total_amount` decimal(16,2) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_amount` decimal(16,2) DEFAULT NULL,
  `final_amount` decimal(16,2) DEFAULT NULL,
  `grand_total` decimal(16,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

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
  `price` decimal(16,2) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

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

INSERT INTO `discounts` (`id`, `active`, `created_at`, `discount_code`, `discount_type`, `discount_value`, `updated_at`) VALUES
(1, 0, '2023-04-06 12:45:29.884958', 'QH3-HC0-0J6-Y', 'PERCENT', 10.00, '2023-04-06 13:14:36.308440'),
(2, 1, '2023-04-06 12:51:10.610543', 'YCI-4R0-64T-A', 'AMOUNT', 50.00, '2023-04-06 12:51:10.610614');

INSERT INTO `order_items` (`id`, `created_at`, `discount_amount`, `final_amount`, `price`, `quantity`, `total_amount`, `updated_at`, `discount_id`, `product_id`, `order_id`) VALUES
(1, '2023-04-06 12:48:58.182018', 20400.00, 183600.00, 850.00, 240, 204000.00, '2023-04-06 12:48:58.182025', 1, 1, 1),
(2, '2023-04-06 12:58:46.721047', 17000.00, 153000.00, 850.00, 200, 170000.00, '2023-04-06 12:58:46.721053', 1, 1, 2),
(3, '2023-04-06 12:58:46.723163', 10000.00, 160000.00, 850.00, 200, 170000.00, '2023-04-06 12:58:46.723167', 2, 1, 2),
(4, '2023-04-06 13:01:29.567829', 17000.00, 153000.00, 850.00, 200, 170000.00, '2023-04-06 13:01:29.567837', 1, 1, 3),
(5, '2023-04-06 13:01:29.570369', 10000.00, 160000.00, 850.00, 200, 170000.00, '2023-04-06 13:01:29.570376', 2, 1, 3),
(6, '2023-04-06 13:04:59.101000', 17000.00, 153000.00, 850.00, 200, 170000.00, '2023-04-06 13:04:59.101007', 1, 1, 4),
(7, '2023-04-06 13:04:59.104237', 10000.00, 160000.00, 850.00, 200, 170000.00, '2023-04-06 13:04:59.104242', 2, 1, 4),
(8, '2023-04-06 13:15:59.474406', NULL, 170000.00, 850.00, 200, 170000.00, '2023-04-06 13:15:59.474412', NULL, 1, 5),
(9, '2023-04-06 13:15:59.477708', 12500.00, 200000.00, 850.00, 250, 212500.00, '2023-04-06 13:15:59.477713', 2, 1, 5);

INSERT INTO `orders` (`id`, `created_at`, `discount_amount`, `final_amount`, `grand_total`, `status`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 12:48:58.178655', 20400.00, 183600.00, 204000.00, 'CREATED', '2023-04-06 12:48:58.193491', 1),
(2, '2023-04-06 12:58:46.717287', 27000.00, 313000.00, 340000.00, 'CREATED', '2023-04-06 12:58:46.734883', 1),
(3, '2023-04-06 13:01:29.563909', 27000.00, 313000.00, 340000.00, 'CREATED', '2023-04-06 13:01:29.585930', 1),
(4, '2023-04-06 13:04:59.091831', 27000.00, 313000.00, 340000.00, 'CREATED', '2023-04-06 13:04:59.108059', 1),
(5, '2023-04-06 13:15:59.465389', 12500.00, 370000.00, 382500.00, 'CREATED', '2023-04-06 13:15:59.482262', 1);

INSERT INTO `product_attributes` (`id`, `attribute_key`, `attribute_value`, `created_at`, `updated_at`, `product_id`) VALUES
(1, 'RAM', '40GB', '2023-04-06 12:42:35.955010', '2023-04-06 12:42:35.955017', 1),
(2, 'Storage', '512GB', '2023-04-06 12:42:35.958699', '2023-04-06 12:42:35.958705', 1);

INSERT INTO `product_inventories` (`id`, `created_at`, `quantity`, `reorder_level`, `updated_at`, `product_id`) VALUES
(1, '2023-04-06 12:42:35.900311', 22670, 2000, '2023-04-06 13:15:59.418675', 1);

INSERT INTO `product_prices` (`id`, `created_at`, `price`, `updated_at`, `product_id`) VALUES
(1, '2023-04-06 12:42:35.930650', 850.00, '2023-04-06 12:44:53.004220', 1);

INSERT INTO `products` (`id`, `created_at`, `description`, `image`, `name`, `product_code`, `updated_at`, `product_inventory_id`, `product_price_id`) VALUES
(1, '2023-04-06 12:42:35.947535', 'Lenovo 2023 ThinkPad E15 Gen 4 High Performance Business Laptop: AMD Ryzen 5 5625U Hex-Core, 40GB RAM,', 'https://www.amazon.com/Lenovo-ThinkPad-Performance-Business-Laptop', 'Lenovo 2023 ThinkPad E15', '1BV4-V1KZ-IH', '2023-04-06 12:42:35.947559', 1, 1);

INSERT INTO `shopping_cart_items` (`id`, `created_at`, `quantity`, `updated_at`, `discount_id`, `product_id`, `shopping_cart_id`) VALUES
(1, '2023-04-06 12:46:31.545607', 240, '2023-04-06 12:46:31.545612', 1, 1, 1),
(2, '2023-04-06 12:52:03.801245', 200, '2023-04-06 12:52:03.801269', 2, 1, 1),
(3, '2023-04-06 12:57:59.815488', 200, '2023-04-06 12:57:59.815495', 1, 1, 2),
(4, '2023-04-06 12:58:11.435820', 200, '2023-04-06 12:58:11.435833', 2, 1, 2),
(5, '2023-04-06 13:09:16.388286', 200, '2023-04-06 13:09:16.388292', 1, 1, 3),
(6, '2023-04-06 13:09:36.653724', 250, '2023-04-06 13:09:36.653734', 2, 1, 3);

INSERT INTO `shopping_carts` (`id`, `created_at`, `expires_at`, `status`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 12:46:31.542697', '2023-04-07 12:52:03.765893', 'CHECKOUT', '2023-04-06 12:54:23.518460', 1),
(2, '2023-04-06 12:57:59.800595', '2023-04-07 12:58:11.313403', 'CHECKOUT', '2023-04-06 13:04:59.174438', 1),
(3, '2023-04-06 13:09:16.373812', '2023-04-07 13:09:36.609249', 'ACTIVE', '2023-04-06 13:09:36.663234', 1);

INSERT INTO `user_sessions` (`id`, `created_at`, `expires_at`, `token`, `updated_at`, `user_id`) VALUES
(1, '2023-04-06 12:39:17.664681', '2023-04-07 12:39:17.540242', 'eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODA4NzExNTcsInVzZXJJZCI6MSwiaWF0IjoxNjgwNzg0NzU3fQ.KlQtq79-3mZvcME1PzW6ngUjjgCiYCrKKRKQMsVjDPTmU3AZ-MC7jHPjTGFkOwdTTIkwYWUx5IJFVAl2_V4KgQ', '2023-04-06 12:39:17.664691', 1);

INSERT INTO `users` (`id`, `created_at`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `updated_at`) VALUES
(1, '2023-04-06 12:38:51.907369', 'cretus@gmail.com', 'dolore aliquip aliqua', 'veniam proident', '$2a$10$LclHasFlJFQE.CZqR0A.XO0Ypu/pRZBqp3d/gLY2wBGVW6kH74l7O', 'voluptate laborum sit', '2023-04-06 12:38:51.907393');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;