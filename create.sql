create database CDSHOP; 
use CDSHOP;

CREATE TABLE customer(
    cust_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    cust_name VARCHAR(100) NOT NULL,
    cd_id INT , 
    CONSTRAINT pk_customer PRIMARY KEY(cust_id) 
);

CREATE TABLE cd(
    id INT NOT NULL UNIQUE AUTO_INCREMENT,
    cd_name VARCHAR(100) NOT NULL,
    artist VARCHAR(100) NOT NULL, 
    release_year INTEGER NOT NULL,
    issuedby INT, 
    CONSTRAINT pk_cd PRIMARY KEY(id) 
);

CREATE TABLE issuer(
    issuer_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    issuer_name VARCHAR(100) NOT NULL,
    issuer_password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_issuer PRIMARY KEY(issuer_id) 
);

CREATE TABLE super_admin(
    admin_id INT NOT NULL UNIQUE AUTO_INCREMENT,
    admin_name VARCHAR(100) NOT NULL,
    admin_password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_super_admin PRIMARY KEY(admin_id) 
);



