CREATE DATABASE  IF NOT EXISTS `db_springboot_backend_technical_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_springboot_backend_technical_test`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: db_springboot_backend_technical_test
-- ------------------------------------------------------
-- Server version	8.0.37-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `edad` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `created_at` varchar(45) DEFAULT NULL,
  `clienteId` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `estado` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`clienteId`),
  UNIQUE KEY `cliente_emaill_UNIQUE` (`email`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (17,'Jose','Lema','25','098254999','Otavalo sn y principal','jlema1233@gmail.com','2024-05-13','123456789','1234',1),(20,'Juan','Osoroio','31','09685479632','13 junio y Equinoccial','josorio@hotmail.com','2024-05-13','1856479621','1245',1);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuentas`
--

DROP TABLE IF EXISTS `cuentas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentas` (
  `nro_cuenta` int NOT NULL,
  `tipo` varchar(45) NOT NULL,
  `saldo` double NOT NULL,
  `estado` tinyint NOT NULL,
  `id_cliente` int NOT NULL,
  PRIMARY KEY (`nro_cuenta`),
  KEY `fk_cuentas_1_idx` (`id_cliente`),
  CONSTRAINT `fk_cuentas_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentas`
--

LOCK TABLES `cuentas` WRITE;
/*!40000 ALTER TABLE `cuentas` DISABLE KEYS */;
INSERT INTO `cuentas` VALUES (478758,'ahorro',1925,1,17),(495878,'Ahorros',150,1,20),(585525,'Ahorros',2170,0,20),(585545,'Corriente',1000,1,17);
/*!40000 ALTER TABLE `cuentas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos`
--

DROP TABLE IF EXISTS `movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos` (
  `id_movimiento` int NOT NULL AUTO_INCREMENT,
  `tipo_movimiento` varchar(45) NOT NULL,
  `saldo` varchar(45) NOT NULL,
  `valor` varchar(45) NOT NULL,
  `fecha` datetime NOT NULL,
  `nro_cuenta` int NOT NULL,
  PRIMARY KEY (`id_movimiento`),
  KEY `fk_movimientos_1_idx` (`nro_cuenta`),
  CONSTRAINT `fk_movimientos_1` FOREIGN KEY (`nro_cuenta`) REFERENCES `cuentas` (`nro_cuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos`
--

LOCK TABLES `movimientos` WRITE;
/*!40000 ALTER TABLE `movimientos` DISABLE KEYS */;
INSERT INTO `movimientos` VALUES (7,'Retiro','1925.0','-75.0','2024-05-13 00:00:00',478758),(9,'Deposito','150.0','150.0','2024-05-13 00:00:00',495878),(15,'Retiro','1960.0','-540.0','2024-07-07 00:00:00',585525),(16,'Retiro','1420.0','-540.0','2024-07-07 00:00:00',585525),(17,'Deposito','2170.0','750.0','2024-07-07 00:00:00',585525),(18,'Deposito','5670.0','3500.0','2024-07-07 00:00:00',585525),(19,'Retiro','2170.0','-3500.0','2024-07-07 00:00:00',585525);
/*!40000 ALTER TABLE `movimientos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-07 17:07:29
