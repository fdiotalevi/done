CREATE TABLE `done` (
  `id` int primary key auto_increment,
  `email` varchar(256) NOT NULL,
  `text` varchar(4096) NOT NULL,
  `date` char(8) NOT NULL
);
