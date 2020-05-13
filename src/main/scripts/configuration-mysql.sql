
-- run mysql in a docker file
-- docker run -p 3306:3306 --name mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -v [pwd-mount-dir]:/var/lib/mysql -d mysql:5.7

-- Create Databases
CREATE DATABASE sfg_dev;
CREATE DATABASE sfg_prod;

-- Create database service accounts
CREATE USER 'sfg_dev_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'sfg_prod_user'@'localhost' IDENTIFIED BY 'password';
CREATE USER 'sfg_dev_user'@'%' IDENTIFIED BY 'password';
CREATE USER 'sfg_prod_user'@'%' IDENTIFIED BY 'password';

-- Database grants
GRANT SELECT , INSERT , DELETE , UPDATE ON sfg_dev.* to 'sfg_dev_user'@'localhost';
GRANT SELECT , INSERT , DELETE , UPDATE ON sfg_prod.* to 'sfg_prod_user'@'localhost';
GRANT SELECT , INSERT , DELETE , UPDATE ON sfg_dev.* to 'sfg_dev_user'@'%';
GRANT SELECT , INSERT , DELETE , UPDATE ON sfg_prod.* to 'sfg_prod_user'@'%';
