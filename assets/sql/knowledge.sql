-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: knowledge_base
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lecturer_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt9v45o9358384n2veilnl2sq0` (`lecturer_id`),
  CONSTRAINT `FKt9v45o9358384n2veilnl2sq0` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'java',' \"Learn the fundamentals of Java programming, including object-oriented principles, data structures, and building scalable applications.',1),(2,'Algorithms and Data Structures','\"Explore the design, analysis, and implementation of algorithms, and learn to organize and manipulate data efficiently with various data structures.\"',4),(3,'Operating Systems','\"Understand the core concepts of operating systems, including process management, memory allocation, file systems, and concurrency.\"',2),(4,' Artificial Intelligence','\"Dive into the principles and techniques behind intelligent systems, including machine learning, natural language processing, and decision-making algorithms.\"',3),(5,'New Course','New Dewscription',2),(6,'gsdgds','gdsgsg',1),(7,'try','try2',2),(8,'Graphics','This course will include materials about computer graphics',2),(9,'gbbtt','bg',4),(10,'njk','mklml',3);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturers`
--

DROP TABLE IF EXISTS `lecturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp8mocspquan2i6fjx36qghvct` (`user_id`),
  CONSTRAINT `FKp8mocspquan2i6fjx36qghvct` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturers`
--

LOCK TABLES `lecturers` WRITE;
/*!40000 ALTER TABLE `lecturers` DISABLE KEYS */;
INSERT INTO `lecturers` VALUES (1,'Shai',1),(2,'Boris',2),(3,'Shnaps',3),(4,'Alba',4),(5,' aviya yech',28);
/*!40000 ALTER TABLE `lecturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material-history`
--

DROP TABLE IF EXISTS `material-history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material-history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `material_id` int NOT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKno6728ju65oq0fm4ighp2jspd` (`user_id`),
  KEY `FK7v7ot9x1l9tjlgwoer2huqwaa` (`material_id`),
  CONSTRAINT `FK7v7ot9x1l9tjlgwoer2huqwaa` FOREIGN KEY (`material_id`) REFERENCES `materials` (`id`),
  CONSTRAINT `FKno6728ju65oq0fm4ighp2jspd` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material-history`
--

LOCK TABLES `material-history` WRITE;
/*!40000 ALTER TABLE `material-history` DISABLE KEYS */;
INSERT INTO `material-history` VALUES (28,23,12,'2024-12-18 09:08:43'),(29,23,12,'2024-12-18 09:08:18'),(30,23,1,'2024-12-30 23:33:41'),(31,23,1,'2024-12-18 09:14:09'),(32,24,1,'2024-12-22 20:13:17'),(33,23,1,'2024-12-24 17:04:24'),(34,24,1,'2024-12-24 21:45:55'),(35,24,8,'2024-12-24 21:46:01'),(36,24,1,'2024-12-24 21:50:07'),(37,22,1,'2024-12-24 23:21:29'),(38,22,1,'2024-12-24 23:21:31'),(39,22,9,'2024-12-24 23:21:59'),(40,6,1,'2024-12-27 01:45:09'),(41,4,1,'2024-12-29 22:43:07'),(42,4,8,'2024-12-29 22:43:21'),(43,23,11,'2024-12-30 23:33:50');
/*!40000 ALTER TABLE `material-history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materials`
--

DROP TABLE IF EXISTS `materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materials` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `type_id` int DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `tag_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtli10b9smqwj2u6yvplk32q9c` (`type_id`),
  KEY `FK32wqk9p2efffrkb1l6yvkysou` (`user_id`),
  KEY `FKdky0bj42nucpuas522aj6yoc` (`course_id`),
  KEY `FKo0t4n0a954iqubuy60r3yeuie` (`tag_id`),
  CONSTRAINT `FK32wqk9p2efffrkb1l6yvkysou` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKdky0bj42nucpuas522aj6yoc` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  CONSTRAINT `FKo0t4n0a954iqubuy60r3yeuie` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  CONSTRAINT `FKtli10b9smqwj2u6yvplk32q9c` FOREIGN KEY (`type_id`) REFERENCES `types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materials`
--

LOCK TABLES `materials` WRITE;
/*!40000 ALTER TABLE `materials` DISABLE KEYS */;
INSERT INTO `materials` VALUES (1,'temp',1,'ב-Java, כדי לכתוב קוד דקלרטיבי (declarative), אנו מתמקדים במה צריך לעשות במקום איך לעשות זאת. זה נעשה בדרך כלל באמצעות שימוש ב-Streams וב-APIs ברמה גבוהה יותר המאפשרים לנו להימנע מלולאות ידניות ושינויים ישירים.',1,'2024-12-10 23:40:09',1,'.stream()',1),(4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'dkjv',1,'m v,mfd',10,'2024-12-17 22:56:00',1,'mkf ',1),(9,'ram',1,'ram and aviya checking',12,'2024-12-18 01:17:08',1,'ram1',2),(10,'tyrt',2,'njhnbhj',13,'2024-12-18 02:24:49',1,'hjbjh',1),(11,'jkjnccxvkj n',2,'xnckv jnxc',22,'2024-12-18 08:11:13',1,'ncvkjnk',2),(12,'New PPT',1,'NNNN',23,'2024-12-18 08:39:45',8,'This is my new PPT',1),(13,'fv,',2,'kmbflgm b',24,'2024-12-22 20:13:46',1,'fv,fl',2),(14,'ldmkmvd',1,'dmklmv',24,'2024-12-24 21:40:09',1,'km ck',3),(15,'tryyyy',3,'mdvkm',24,'2024-12-24 21:40:29',1,'dvjjkmndjvk',2),(16,'mkfv',1,'fvm k',24,'2024-12-27 01:51:28',1,'fmvk',2),(17,'mkfmbkfmgb',1,'mvkmkbvm',24,'2024-12-27 01:54:18',1,'vkmk',3),(18,'dvir',2,'m k',24,'2024-12-27 01:57:53',1,'fm vkfd',2),(19,'lior',1,'kkk',24,'2024-12-27 02:00:38',1,'kkk',2),(20,',',1,' ',24,'2024-12-27 02:01:19',1,',',1),(21,'חרקמהלח',2,'דלחהמלחקה',24,'2024-12-27 02:23:23',1,'הלחה',2),(22,'fv dfm bm,',2,' m ,',24,'2024-12-27 02:41:06',1,'v c bf,m',3);
/*!40000 ALTER TABLE `materials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `from_user_id` int DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ym9ojpy2t5aytdw25r4hsn2s` (`from_user_id`),
  CONSTRAINT `FK6ym9ojpy2t5aytdw25r4hsn2s` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,15,'hi','2024-12-30 02:30:55'),(26,23,'היי','2024-12-30 02:31:14'),(27,23,'מה קורה?','2024-12-30 02:31:31'),(28,23,'החח','2024-12-30 02:32:02'),(29,23,'היי','2024-12-30 02:33:49'),(30,23,'hi hi','2024-12-30 02:36:41'),(31,23,'try','2024-12-30 02:36:53'),(32,23,'היייי','2024-12-30 02:38:34'),(33,23,'פליז תעבוד','2024-12-30 02:41:30'),(34,23,'מההה','2024-12-30 02:42:07'),(35,23,'vhh','2024-12-30 02:43:51'),(36,23,'היי','2024-12-30 02:45:28'),(37,23,'היי','2024-12-30 02:45:50'),(38,23,'כי זה סטרינג','2024-12-30 02:47:14'),(39,23,'היי','2024-12-30 02:51:45'),(40,23,'היי','2024-12-30 02:52:43'),(41,23,'טטטטט','2024-12-30 02:52:51'),(42,23,'אאאא','2024-12-30 02:53:27'),(43,23,'rooooo','2024-12-30 02:56:34'),(44,23,'rammm','2024-12-30 02:58:00'),(45,23,'ram2222\n','2024-12-30 02:59:02'),(46,23,'nv bzdr','2024-12-30 03:00:07'),(47,23,'מה נסגר','2024-12-30 03:02:11');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `from_user_id` int DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `time_stamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32miscyb8msuwy1yn4a1pqpuf` (`from_user_id`),
  KEY `FKnbsay6xj4ansg9vt922bj7rm6` (`course_id`),
  CONSTRAINT `FK32miscyb8msuwy1yn4a1pqpuf` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnbsay6xj4ansg9vt922bj7rm6` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,21,2,'exercise','to do declerative','2024-12-30 02:05:50');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_history`
--

DROP TABLE IF EXISTS `query_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `query_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `query` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc47eow13hku0x7rgpo03jnd3f` (`user_id`),
  CONSTRAINT `FKc47eow13hku0x7rgpo03jnd3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_history`
--

LOCK TABLES `query_history` WRITE;
/*!40000 ALTER TABLE `query_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recovery`
--

DROP TABLE IF EXISTS `recovery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recovery` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `time_stamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recovery`
--

LOCK TABLES `recovery` WRITE;
/*!40000 ALTER TABLE `recovery` DISABLE KEYS */;
INSERT INTO `recovery` VALUES (1,'Title','Body','byhyhzql@gmail.com','2024-12-11 10:08:05'),(2,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-11 10:16:36'),(3,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-11 10:19:02'),(4,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-17 22:56:00'),(5,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-18 01:17:08'),(6,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-18 02:24:49'),(7,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-18 08:11:13'),(8,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-18 08:39:45'),(9,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-22 20:13:46'),(10,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-24 21:40:09'),(11,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-24 21:40:29'),(12,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 01:51:28'),(13,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 01:54:18'),(14,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 01:57:53'),(15,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 02:00:38'),(16,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 02:01:19'),(17,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 02:23:23'),(18,'My Title','לשחזור הסיסמא הכנס את הקודMy OTP','shaigivati464@gmail.com','2024-12-27 02:41:06');
/*!40000 ALTER TABLE `recovery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Student'),(2,'Lecturer'),(3,'Admin');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,'אלגו'),(2,'מתמטיקה'),(3,'ג\'אווה');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `types`
--

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;
INSERT INTO `types` VALUES (1,'מצגת'),(2,'תרגיל'),(3,'פתרון'),(4,'אחר');
/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `pass_recovery` varchar(255) DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'aviyay','avi123','Shai','yechezkel','avi@.com','0505887223','',NULL,1),(2,'aviyaY','25D55AD283AA400AF464C76D713C07AD','Boris','2','aviyay@edu.aac.ac.il','0505454545','147739',NULL,1),(3,'avi','25F9E794323B453885F5181F1B624D0B','Shnaps','yh','aviyay@aviya.com','0506565456','',NULL,1),(4,'aviii','202CB962AC59075B964B07152D234B70','aviy','avi','aviyay@edu.aac.ac.i','0505478787','',NULL,1),(5,'shaig','FE8C078136ECF7132909C98F53B8131B','Shai','Givati','shaig@gmail.com','0504730464','',NULL,1),(6,'avi123','81DC9BDB52D04DC20036DBD8313ED055','avi','yech','avi@jc','0505698741','',NULL,1),(9,'aviyay1','25F9E794323B453885F5181F1B624D0B','aviya','yech','aviya@1234','0505445454','',NULL,1),(10,'gabi1','5F4DCC3B5AA765D61D8327DEB882CF99','gabi','choen','gabi@123','0504789654','',NULL,1),(11,'avi33333','d','d','d','aviyay@edddru.aac.ac.i','0505898745',NULL,NULL,NULL),(12,'ram','44F437CED647EC3F40FA0841041871CD','avirrrrrrr','yech','avi@jc1333err','0505874545','',NULL,1),(13,'jv dnh','dm vmnd ','nd vcn','dvnjd','dnckjs','0505478965',NULL,NULL,NULL),(14,'fmvmv','fjn vfjnv','nfv njfv ','jfvnjfv',' vnjkfv','0505478787',NULL,NULL,NULL),(15,'avi1235','A210284D0F9FE9ADB1E3FB287B0FEFU8','aviA@11111','yech','avi@jcr','0546547874','',NULL,1),(16,'fvmlfk','fv fjv ','fmv  v','njkfv ndf','fvkjfv','0505478987',NULL,NULL,NULL),(17,'rrrrr','A210284D0F9FE9ADB1E3FB287B0FEFA8','aviA@11111','yech','avi@jcqqq','0505893047','',NULL,2),(18,'aviA#11111','4488CB579378E3910D67F7935D3AB181','aviA#11111','yech','avi@j222ljg','0505893040','',NULL,1),(19,'sssss','4488CB579378E3910D67F7935D3AB181','aviA#11111','yech','avi@j222ljg5','0505893071','',NULL,1),(20,'sssssf','941B0B623815D4D4043B2F6FF4DB9C34','gabiA2#','choen','gabi@123k','0505893047','',NULL,1),(21,'aviWw33333','8680B9411A3743F2AEECD9F3345F7D34','aviWw3333344','yecjj','avi@d.com','0505893055','',NULL,1),(22,'aviyasA@555','59750B9988ADD64D5DBB8B45F62FF84C','aviyasA@555','yechezkel','aviyay@edu.aacss.ac.il','0508741414','',NULL,1),(23,'shaig2','723766BA76CB5C96F2A731DE4298C323','Shai','Givati','shai@g.com','0543298199','455660',NULL,1),(24,'davidE','1B94C117E589BB6F20D4E8C682E00C66','davidD111$','even','david@k.com','0525964484','',NULL,1),(25,'ramRevivo1','8DAB45789BE027AF4B31F06A10E9F4FE',' ramRevivo1@','ramRevivo1@','ramevivo1@g.com','0502132933','',NULL,1),(26,'ramRevivo2','C9D1B4539B3619B0F8CB7F622D48C3ED','ramRevivo1@','ramRevivo1@','ramevivo1s@g.com','0528975647','',NULL,1),(27,'aviya9','58467A11EA1EA8C4605D4CB9F7D36E1B',' aviya','yech','aviyay@edu.aacf.ac.il','0504787878','',NULL,2),(28,'aviya19','61A668818B4D4568C42E4D7F3127E65A',' aviya','yech','aviyay@edu.aac2f.ac.il2','0505893041','',NULL,2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-31 22:00:01
