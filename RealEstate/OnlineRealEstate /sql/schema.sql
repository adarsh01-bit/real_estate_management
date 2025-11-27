DROP DATABASE IF EXISTS orems;
CREATE DATABASE orems;
USE orems;
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('tenant','manager','admin') NOT NULL DEFAULT 'tenant',
  fullname VARCHAR(150) NOT NULL
);

CREATE TABLE properties (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  address VARCHAR(255),
  rent DECIMAL(10,2) NOT NULL,
  manager_id INT,
  is_available BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE applications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  property_id INT NOT NULL,
  tenant_id INT NOT NULL,
  message TEXT,
  status ENUM('pending','accepted','rejected') DEFAULT 'pending',
  applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
  FOREIGN KEY (tenant_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE rental_agreements (
  id INT AUTO_INCREMENT PRIMARY KEY,
  property_id INT NOT NULL,
  tenant_id INT NOT NULL,
  manager_id INT NOT NULL,
  start_date DATE,
  end_date DATE,
  rent DECIMAL(10,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
  FOREIGN KEY (tenant_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Sample admin (password: admin123) -- hashed value below is for bcrypt 'admin123'
-- If you prefer to insert plain text and let app hash on first register, comment this out.
INSERT INTO users (username, password, role, fullname) VALUES
('admin', '$2a$10$u1s1iYVQ2NQvFhF2q6x2ke6yQZzR9pHzYlHc6E/0c9Qk4Zr6H9b2G', 'admin', 'Site Administrator');


SELECT id, username, role FROM users;
DELETE FROM users WHERE username='admin';
INSERT INTO users (username, password, role, fullname)
VALUES
('admin', '$2a$10$ThgKU4thsor8AODQL/Sk7Ouy3e/r0m4GTRch8zk2dFFVVRXaU7e6q
', 'admin', 'Site Admin');
SELECT username, password, role FROM users;
UPDATE users 
SET password = '$2a$10$oiA1JJYma4SSA6NLGarxvuGa/psz4iXGDssnnw3C8Qbmj0VwmHoPW'
WHERE username = 'admin';
USE orems;
SELECT id, username, role, fullname FROM users;
SELECT User, Host, plugin FROM mysql.user;
SELECT * FROM users;
SELECT id, username, password, role FROM users;
USE orems;
DROP DATABASE IF EXISTS orems;
CREATE DATABASE orems;
USE orems;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('tenant','manager','admin') NOT NULL DEFAULT 'tenant',
  fullname VARCHAR(150) NOT NULL
);

CREATE TABLE properties (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  address VARCHAR(255),
  rent DECIMAL(10,2) NOT NULL,
  manager_id INT,
  is_available BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE applications (
  id INT AUTO_INCREMENT PRIMARY KEY,
  property_id INT NOT NULL,
  tenant_id INT NOT NULL,
  message TEXT,
  status ENUM('pending','accepted','rejected') DEFAULT 'pending',
  applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
  FOREIGN KEY (tenant_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE rental_agreements (
  id INT AUTO_INCREMENT PRIMARY KEY,
  property_id INT NOT NULL,
  tenant_id INT NOT NULL,
  manager_id INT NOT NULL,
  start_date DATE,
  end_date DATE,
  rent DECIMAL(10,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
  FOREIGN KEY (tenant_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (username, password, role, fullname) VALUES
('admin', '$2a$10$u1s1iYVQ2NQvFhF2q6xe6yQZzR9pHZrYHc6E/0c9Qk4Zr6H9b2G', 'admin', 'Site Administrator');
USE orems;
SELECT id, username, role FROM users;
USE orems;

UPDATE users
SET password = '$2a$10$0vvI4ZP3QVdOE1oOwZ9rCuFV5HkfgYQFSL5PSJHcVYwQ1zFimTzi.'
WHERE username = 'admin';
