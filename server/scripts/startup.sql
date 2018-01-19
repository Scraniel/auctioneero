CREATE DATABASE auctioneero;

USE auctioneero;

CREATE TABLE user (
	id 				CHAR(36) 		NOT NULL,
	user_name		VARCHAR(20) 	NULL,
	
	PRIMARY KEY (id)
);

CREATE TABLE auction_item (
	id 				CHAR(36) 		NOT NULL,
	name 			VARCHAR(20) 	NULL,
	description 	VARCHAR(128) 	NULL,
	owner_id		CHAR(36) 		NOT NULL,
	high_bidder_id 	CHAR(36) 		NULL,
	category		VARCHAR(20) 	NULL,
	expiry			TIMESTAMP 		NOT NULL, 
	
	PRIMARY KEY (id),
	CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES user(id),
	CONSTRAINT fk_high_bidder FOREIGN KEY (high_bidder_id) REFERENCES user(id)
);