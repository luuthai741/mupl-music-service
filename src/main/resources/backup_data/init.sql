-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 192.168.1.10    Database: mupl_music
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `mupl_album`
--

DROP TABLE IF EXISTS `mupl_album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_album` (
                              `album_id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                              `artist_id` bigint DEFAULT NULL,
                              `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `released_at` timestamp NULL DEFAULT NULL,
                              PRIMARY KEY (`album_id`),
                              KEY `mupl_album_ibfk_1` (`artist_id`),
                              CONSTRAINT `mupl_album_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `mupl_artist` (`artist_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_album`
--

LOCK TABLES `mupl_album` WRITE;
/*!40000 ALTER TABLE `mupl_album` DISABLE KEYS */;
INSERT INTO `mupl_album` VALUES (2,'Unorthodox Jukebox',12,'2025-02-24 22:29:00','2012-12-07 00:00:00'),(3,'Doo-Wops & Hooligans',12,'2025-02-24 22:29:29','2010-10-04 00:00:00');
/*!40000 ALTER TABLE `mupl_album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_artist`
--

DROP TABLE IF EXISTS `mupl_artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_artist` (
                               `artist_id` bigint NOT NULL AUTO_INCREMENT,
                               `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               `description` text,
                               `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                               `country` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                               `birthday` date DEFAULT NULL,
                               `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`artist_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_artist`
--

LOCK TABLES `mupl_artist` WRITE;
/*!40000 ALTER TABLE `mupl_artist` DISABLE KEYS */;
INSERT INTO `mupl_artist` VALUES (12,'Bruno Mars','Tiểu sử Peter Gene Hernandez (sinh 8 tháng 10 năm 1985), được biết đến với nghệ danh Bruno Mars, là một ca sĩ-nhạc sĩ và nhà sản xuất thu âm người Mỹ. Mars lớn lên trong một gia đình nghệ sĩ ở Honolulu, Hawaii và anh đã bắt đầu ca hát từ khi còn nhỏ. Sau khi biểu diễn nhiều nơi ở quê nhà quê anh thời niên thiếu, anh quyết định theo đuổi sự nghiệp ca hát. Anh đã bắt đầu sản xuất bài hát cho các nghệ sĩ khác, tham gia nhóm sản xuất The Smeezingtons.','Male','United States of America','1985-10-08','2025-02-22 00:15:59'),(13,'Lady Gaga','Lady Gaga chào đời vào ngày 28 tháng 3 năm 1986 tại Yonkers, New York. Thời niên thiếu, GaGa được học tập ở Convent of the Sacred Heart, một trường học nổi tiếng ở Hoa Kỳ. Khi Lady Gaga còn nhỏ, cô thường hát theo những băng cát xét của Michael Jackson hay Cyndi Lauper, đây cũng là hai thần tượng âm nhạc lớn nhất của cô. Cô thích những giai điệu của Rolling Stones hay The Beatles. Không có gì đáng lấy làm lạ khi đứa bé gái gốc Ý sống tại trung tâm New York này lại không trở thành một nhà ca sĩ, nhạc sĩ tài ba - Lady Gaga. Gaga cho biết: \'Tôi rất thích nhạc rock hay pop và cả nhạc kịch. Khi tôi phát hiện ra ban nhạc Queen và David Bowie, tôi đã nảy ra ý tưởng kết hợp hai dòng nhạc này với nhau\'. Cái tên Lady Gaga cũng xuất phát từ ca khúc \'Radio Gaga\' của nhóm rock huyền thoại Queen. Từ năm lên bốn tuổi, GaGa bắt đầu tự học piano và ở tuổi mười ba, cô đã cho ra đời bản hòa tấu piano đầu tay của mình. Năm 14 tuổi, cô đã bắt đầu trình diễn ở các buổi hát tự do. Cũng trong thời gian này, cô bắt đầu xuất hiện trên truyền hình với những vai nhỏ trong các bộ phim như: A Funny Thing Happened on the Way to the Forum, The Government Inspector, Damn Yankees, Guys and Dolls và A Man for All Seasons.','Female','United States of America','1986-03-28','2025-02-22 00:21:10'),(15,'Billie Eilish','Billie Eilish (tên thật là Billie Eilish Pirate Baird O\'Connell), sinh ngày 18/12/2001), là một nữ ca sĩ kiêm nhạc sĩ người Mỹ. Đĩa đơn ra mắt \'Ocean Eyes\' được phát hành trên SoundCloud năm 2016, MV ca khúc được phát hành vào ngày 24 tháng 3 năm 2016, và video vũ đạo phát hành ngày 22 tháng 10 sau đó. Bài hát nhanh chóng được nhận nhiều phản hồi tích cực.','Female','United States of America','2001-12-18','2025-02-22 00:38:12'),(17,'Rihanna','UPDATED: Năm 2005, làng âm nhạc thế giới bắt đầu biết đến cái tên Rihanna. Ngay sau khi phát hành Single ra mắt \'Pon De Replay\', cô gái trẻ Robyn Fenty, sinh tại hòn đảo nhỏ Barbados, đã vụt lên trở thành một siêu sao mới với nghệ danh Rihanna. Chỉ trong vòng 10 năm kể từ khi khởi đầu sự nghiệp ca hát, Rihanna đã trở thành nghệ sĩ solo trẻ tuổi nhất giành nhất 13 single đứng vị trí số 1 của BXH Billboard Hot 100. Trong khoản thời gian này cô cũng đã bán được hơn 54 triệu album và 210 triệu đĩa đơn trên toàn thế giới. Rihanna là nghệ sĩ đã 8 lần đoạt giải Grammy và 12 lần được Billboard Music Awards vinh danh. Thêm vào đó là vô số những giải thưởng âm nhạc lớn nhỏ ở khắc nơi trên thế giới. Bên cạnh nhưng thành tựu trong âm nhạc, Rihanna chứng tỏ mình cũng rất mát tay trong lãnh vực kinh doanh với nhiều nhãn hàng khác nhau dưới tên cô. Quan trọng hơn cả, sức ảnh hưởng văn hóa không thể phủ nhận của Rihanna đã ghi tên cô trở thành một biểu tượng toàn cầu, mặc dù chỉ ở độ tuổi đôi mươi.','Female','United States of America','1988-02-20','2025-02-24 09:57:19'),(18,'The Weeknd','Tiểu sử Abel Tesfaye, được công chúng biết đến nhiều hơn với tên gọi The Weeknd (phát âm giống từ weekend), là một ca sĩ kiêm nhạc sĩ, nhà sản xuất thu âm người Canada. Vào cuối năm 2010, một số bài hát của Tesfaye được tải lên YouTube dưới cái tên nặc danh \'The Weeknd\'. Anh đã phát hành ba bản mix (9 track) trong suốt năm 2011: House of Balloons, Thursday and Echoes of Silence và được giới chuyên môn đánh giá cao. Trong năm kế tiếp, anh phát hành một album tổng hợp mang tên Trilogy, bao gồm các bản mix trước đó đã được chỉnh sửa và ba bài hát mới thêm vào. Hãng Republic Records và XO đã phát hành độc quyền album này. Album thứ hai của Tesfaye, Beauty Behind the Madness trở thành album đầu tiên đạt vị trí quán quân trên bảng xếp hạng Billboard 200, với tốp 5 đĩa đơn chính bao gồm: \'Earned It\', \'The Hills\' và single số một \'Can\'t Feel My Face\'. Cả ba bài hát trên đều lần lượt giữ cả ba vị trí dẫn đầu của Bảng xếp hạng Billboard Hot R&B, khiến Tesfaye trở thành người đầu tiên trong lịch sử đạt được thành tích này.  ','Male','Canada','1990-02-16','2025-02-25 08:34:17');
/*!40000 ALTER TABLE `mupl_artist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_artists_songs`
--

DROP TABLE IF EXISTS `mupl_artists_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_artists_songs` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `artist_id` bigint NOT NULL,
                                      `song_id` bigint NOT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `mupl_artists_songs_ibfk_1` (`artist_id`),
                                      KEY `mupl_artists_songs_ibfk_2` (`song_id`),
                                      CONSTRAINT `mupl_artists_songs_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `mupl_artist` (`artist_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `mupl_artists_songs_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `mupl_song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_artists_songs`
--

LOCK TABLES `mupl_artists_songs` WRITE;
/*!40000 ALTER TABLE `mupl_artists_songs` DISABLE KEYS */;
INSERT INTO `mupl_artists_songs` VALUES (87,13,61),(88,13,62),(92,13,60),(93,12,60);
/*!40000 ALTER TABLE `mupl_artists_songs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_genre`
--

DROP TABLE IF EXISTS `mupl_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_genre` (
                              `genre_id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                              `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_genre`
--

LOCK TABLES `mupl_genre` WRITE;
/*!40000 ALTER TABLE `mupl_genre` DISABLE KEYS */;
INSERT INTO `mupl_genre` VALUES (1,'Pop','2025-02-24 21:15:49'),(2,'Rock','2025-02-24 21:15:56'),(3,'Jazz','2025-02-24 21:16:01'),(4,'Hip hop','2025-02-24 21:16:22'),(5,'Rhythm and blues','2025-02-24 21:16:33'),(7,'Soul','2025-02-24 21:16:51'),(8,'Reggae','2025-02-24 21:16:54'),(9,'Country','2025-02-24 21:16:59'),(10,'Funk','2025-02-24 21:17:05'),(11,'Folk','2025-02-24 21:17:10'),(12,'Disco','2025-02-24 21:17:16'),(13,'Classical','2025-02-24 21:17:21'),(14,'Electronic','2025-02-24 21:17:26'),(15,'Blues','2025-02-24 21:17:31'),(16,'New age','2025-02-24 21:17:37'),(17,'Christian','2025-02-24 21:17:45'),(18,'Traditional','2025-02-24 21:17:52'),(19,'Ska','2025-02-24 21:17:57'),(20,'Indian classical','2025-02-24 21:18:03'),(21,'Metal','2025-02-24 21:18:09'),(22,'Brazilian','2025-02-24 21:18:14'),(23,'Flamenco','2025-02-24 21:18:18'),(24,'Salsa','2025-02-24 21:18:24'),(25,'Merengue','2025-02-24 21:18:29'),(26,'Bachata','2025-02-24 21:18:34');
/*!40000 ALTER TABLE `mupl_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_genres_songs`
--

DROP TABLE IF EXISTS `mupl_genres_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_genres_songs` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `genre_id` bigint NOT NULL,
                                     `song_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `mupl_genres_songs_ibfk_1` (`genre_id`),
                                     KEY `mupl_genres_songs_ibfk_2` (`song_id`),
                                     CONSTRAINT `mupl_genres_songs_ibfk_1` FOREIGN KEY (`genre_id`) REFERENCES `mupl_genre` (`genre_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `mupl_genres_songs_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `mupl_song` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_genres_songs`
--

LOCK TABLES `mupl_genres_songs` WRITE;
/*!40000 ALTER TABLE `mupl_genres_songs` DISABLE KEYS */;
INSERT INTO `mupl_genres_songs` VALUES (87,8,61),(88,8,62),(89,1,62),(90,13,62),(97,8,60),(98,1,60),(99,13,60);
/*!40000 ALTER TABLE `mupl_genres_songs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_lyric`
--

DROP TABLE IF EXISTS `mupl_lyric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_lyric` (
                              `lyric_id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) DEFAULT NULL,
                              `song_id` bigint DEFAULT NULL,
                              PRIMARY KEY (`lyric_id`),
                              UNIQUE KEY `mupl_lyric_unique` (`song_id`),
                              CONSTRAINT `mupl_lyric_mupl_song_FK` FOREIGN KEY (`song_id`) REFERENCES `mupl_song` (`song_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_lyric`
--

LOCK TABLES `mupl_lyric` WRITE;
/*!40000 ALTER TABLE `mupl_lyric` DISABLE KEYS */;
INSERT INTO `mupl_lyric` VALUES (5,'Die with a smile lyric',60),(6,'Abracadabra',61),(8,'Disease lyric',62);
/*!40000 ALTER TABLE `mupl_lyric` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_lyric_detail`
--

DROP TABLE IF EXISTS `mupl_lyric_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_lyric_detail` (
                                     `lyric_detail_id` bigint NOT NULL AUTO_INCREMENT,
                                     `start_time` int NOT NULL,
                                     `lyric` text,
                                     `end_time` int NOT NULL,
                                     `lyric_id` bigint NOT NULL,
                                     PRIMARY KEY (`lyric_detail_id`),
                                     KEY `mupl_lyric_detail_mupl_lyric_FK` (`lyric_id`),
                                     CONSTRAINT `mupl_lyric_detail_mupl_lyric_FK` FOREIGN KEY (`lyric_id`) REFERENCES `mupl_lyric` (`lyric_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_lyric_detail`
--

LOCK TABLES `mupl_lyric_detail` WRITE;
/*!40000 ALTER TABLE `mupl_lyric_detail` DISABLE KEYS */;
INSERT INTO `mupl_lyric_detail` VALUES (3,0,'I, I just woke up from a dream',5,5),(4,5,'Where you and I had to say goodbye',10,5),(5,10,'And I don’t know what it all means',16,5),(6,16,'But since I survived, I realized',21,5),(7,21,'Wherever you go, that’s where I’ll follow',27,5),(8,27,'Nobody’s promised tomorrow',32,5),(9,32,'So I’ma love you every night like it’s the last night',38,5),(10,38,'Like it’s the last night',43,5),(11,43,'If the world was ending',49,5),(12,49,'I’d wanna be next to you',54,5),(13,54,'If the party was over',60,5),(14,60,'And our time on Earth was through',65,5),(15,65,'I’d wanna hold you just for a while',70,5),(16,70,'And die with a smile',76,5),(17,76,'If the world was ending',81,5),(18,81,'I’d wanna be next to you',87,5),(19,87,'Ooh, lost, lost in the words that we scream',92,5),(20,92,'I don’t even wanna do this anymore',98,5),(21,98,'Cause you already know what you mean to me',103,5),(22,103,'And our love’s the only war worth fighting for',109,5),(23,109,'Wherever you go, that’s where I’ll follow',114,5),(24,114,'Nobody’s promised tomorrow',120,5),(25,120,'So I’ma love you every night like it’s the last night',125,5),(26,125,'Like it’s the last night',130,5),(27,130,'If the world was ending',136,5),(28,136,'I’d wanna be next to you',141,5),(29,141,'If the party was over',147,5),(30,147,'And our time on Earth was through',152,5),(31,152,'I’d wanna hold you just for a while',158,5),(32,158,'And die with a smile',163,5),(33,163,'If the world was ending',169,5),(34,169,'I’d wanna be next to you',174,5),(35,174,'Right next to you',180,5),(36,180,'Next to you',185,5),(37,185,'Right next to you',190,5),(38,190,'Oh-oh',196,5),(39,196,'If the world was ending',201,5),(40,201,'I’d wanna be next to you',207,5),(41,207,'If the party was over',212,5),(42,212,'And our time on Earth was through',218,5),(43,218,'I’d wanna hold you just for a while',223,5),(44,223,'And die with a smile',229,5),(45,229,'If the world was ending',234,5),(46,234,'I’d wanna be next to you',240,5),(47,240,'If the world was ending',245,5),(48,245,'I’d wanna be next to you',251,5),(98,0,'Abracadabra, abracadabra',4,6),(99,4,'Abracadabra, abracadabra',9,6),(100,9,'Pay the toll to the angels',13,6),(101,13,'Drawin’ circles in the clouds',18,6),(102,18,'Keep your mind on the distance',22,6),(103,22,'When the devil turns around',27,6),(104,27,'Hold me in your heart tonight',31,6),(105,31,'In the magic of the dark moonlight',36,6),(106,36,'Save me from this empty fight',41,6),(107,41,'In the game of life',45,6),(108,45,'Like a poem said by a lady in red',50,6),(109,50,'You hear the last few words of your life',54,6),(110,54,'With a haunting dance, now you’re both in a trance',59,6),(111,59,'It’s time to cast your spell on the night',63,6),(112,63,'“Abracadabra, amor-oo-na-na',68,6),(113,68,'Abracadabra, morta-oo-ga-ga',72,6),(114,72,'Abracadabra, abra-oo-na-na”',77,6),(115,77,'In her tongue she said, “Death or love tonight”',82,6),(116,82,'Abracadabra, abracadabra',86,6),(117,86,'Abracadabra, abracadabra',91,6),(118,91,'Feel the beat under your feet, the floor’s on fire',95,6),(119,95,'Abracadabra, abracadabra',100,6),(120,100,'Choose the road on the west side',104,6),(121,104,'As the dust flies, watch it burn',109,6),(122,109,'Don’t waste time on a feelin’',114,6),(123,114,'Use your passion, no return',118,6),(124,118,'Hold me in your heart tonight',123,6),(125,123,'In the magic of the dark moonlight',127,6),(126,127,'Save me from this empty fight',132,6),(127,132,'In the game of life',136,6),(128,136,'Like a poem said by a lady in red',141,6),(129,141,'You hear the last few words of your life',145,6),(130,145,'With a haunting dance, now you’re both in a trance',150,6),(131,150,'It’s time to cast your spell on the night',155,6),(132,155,'“Abracadabra, amor-oo-na-na',159,6),(133,159,'Abracadabra, morta-oo-ga-ga',164,6),(134,164,'Abracadabra, abra-oo-na-na”',168,6),(135,168,'In her tongue she said, “Death or love tonight”',173,6),(136,173,'Abracadabra, abracadabra',177,6),(137,177,'Abracadabra, abracadabra',182,6),(138,182,'Feel the beat under your feet, the floor’s on fire',186,6),(139,186,'Abracadabra, abracadabra',191,6),(140,191,'Phantom of the dance floor, come to me',196,6),(141,196,'Sing for me a sinful melody',200,6),(142,200,'Ah, ah, ah',205,6),(143,205,'Ah, ah, ah',209,6),(144,209,'“Abracadabra, amor-oo-na-na',214,6),(145,214,'Abracadabra, morta-oo-ga-ga',218,6),(146,218,'Abracadabra, abra-oo-na-na”',223,6),(200,0,'(Ah)',2,8),(201,2,'(Ah)',5,8),(202,5,'There are no more tears to cry',9,8),(203,9,'I heard you beggin’ for life',13,8),(204,13,'Runnin’ out of medicine',17,8),(205,17,'You’re worse than you’ve ever been',21,8),(206,21,'(Ah-ah) Screamin’ for me, baby',25,8),(207,25,'(Ah-ah) Like you’re gonna die',29,8),(208,29,'(Ah-ah) Poison on the inside',33,8),(209,33,'I could be your antidote tonight',37,8),(210,37,'(Ah-ah) Screamin’ for me, baby',41,8),(211,41,'(Ah-ah) Like you’re gonna die',45,8),(212,45,'(Ah-ah) Poison on the inside',49,8),(213,49,'I could be your antidote tonight',53,8),(214,53,'I could play the doctor, I can cure your disease',58,8),(215,58,'If you were a sinner, I could make you believe',63,8),(216,63,'Lay you down like one, two, three',67,8),(217,67,'Eyes roll back in ecstasy',71,8),(218,71,'I can smell your sickness, I can cure ya (Cure)',75,8),(219,75,'Cure your disease',79,8),(220,79,'You’re so tortured when you sleep',83,8),(221,83,'Plagued with all your memories',87,8),(222,87,'You reach out, and no one’s there',91,8),(223,91,'Like a god without a prayer',95,8),(224,95,'(Ah-ah) Screamin’ for me, baby',99,8),(225,99,'(Ah-ah) Like you’re gonna die',103,8),(226,103,'(Ah-ah) Poison on the inside',107,8),(227,107,'I could be your antidote tonight',111,8),(228,111,'I could play the doctor, I can cure your disease',116,8),(229,116,'If you were a sinner, I could make you believe',121,8),(230,121,'Lay you down like one, two, three',125,8),(231,125,'Eyes roll back in ecstasy',129,8),(232,129,'I can smell your sickness, I can cure ya (Cure)',133,8),(233,133,'Cure your disease',137,8),(234,137,'(Ah)',140,8),(235,140,'(Ah) Cure your disease',144,8),(236,144,'(Ah)',148,8),(237,148,'I can smell your sickness, I can cure ya',152,8),(238,152,'Bring me your desire, I can cure your disease',157,8),(239,157,'If you were a sinner, I could make you believe',162,8),(240,162,'Lay you down like one, two, three',166,8),(241,166,'Eyes roll back in ecstasy',170,8),(242,170,'I know all your secrets, I can cure ya, oh',174,8),(243,174,'Cure your disease',178,8),(244,178,'(Ah) Cure your disease',182,8),(245,182,'(Ah) Cure ya',186,8),(246,186,'(Ah)',190,8),(247,190,'I can smell your sickness, I can cure ya',194,8),(248,194,'I can cure your disease',198,8),(249,198,'(Ah) Cure your disease',202,8),(250,202,'(Ah) Cure your disease',206,8),(251,206,'(Ah) Ooh',210,8),(252,210,'(Ah) Ooh',214,8);
/*!40000 ALTER TABLE `mupl_lyric_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mupl_song`
--

DROP TABLE IF EXISTS `mupl_song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mupl_song` (
                             `song_id` bigint NOT NULL AUTO_INCREMENT,
                             `title` varchar(255) NOT NULL,
                             `album_id` bigint DEFAULT NULL,
                             `image_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                             `released_at` date DEFAULT NULL,
                             `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `song_path` varchar(100) DEFAULT NULL,
                             `duration` int DEFAULT NULL,
                             `is_free_to_play` tinyint(1) DEFAULT '0',
                             `lyric_id` bigint DEFAULT NULL,
                             PRIMARY KEY (`song_id`),
                             KEY `mupl_song_mupl_lyric_FK` (`lyric_id`),
                             KEY `mupl_song_ibfk_1` (`album_id`),
                             CONSTRAINT `mupl_song_ibfk_1` FOREIGN KEY (`album_id`) REFERENCES `mupl_album` (`album_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mupl_song`
--

LOCK TABLES `mupl_song` WRITE;
/*!40000 ALTER TABLE `mupl_song` DISABLE KEYS */;
INSERT INTO `mupl_song` VALUES (60,'Die with a smile',NULL,'60/28_02_2025_4ae3ed7e-3ac7-4a96-bd78-4b74c65cc3bb.jpg','2024-08-20','2025-02-27 08:56:06','2025-02-27 08:56:06','60/28_02_2025_a4cda8fc-b498-4ccb-913d-087954e899db.mp3',251,1,NULL),(61,'Abracadabra',NULL,'61/27_02_2025_5a5c4be7-ab30-401a-94f3-9ee9d9c374b9.jpg','2024-08-20','2025-02-27 20:53:27','2025-02-27 20:53:27','61/27_02_2025_ef998e08-4ac0-4e8a-95be-0d9ba031387c.mp3',223,1,NULL),(62,'Disease',NULL,'62/27_02_2025_a43f21c6-55ae-4e94-bb1e-a5d682dfad5d.jpg','2024-08-20','2025-02-27 20:56:47','2025-02-27 20:56:47','62/27_02_2025_d50ebfe0-0cbf-4cf6-bbb9-a9f1b50980d7.mp3',230,1,NULL);
/*!40000 ALTER TABLE `mupl_song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'mupl_music'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-28 22:10:20
