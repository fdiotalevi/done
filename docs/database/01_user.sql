-- create user table here
CREATE TABLE `user` (
  `email` varchar(256) NOT NULL,
  `firstname` varchar(256) NOT NULL,
  `lastname` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  PRIMARY KEY (`email`)
);

