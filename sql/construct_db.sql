-- create_tables.sql
-- Creates the simple todo schema for CSCE548 Project 1
-- Run while connected to the server. Adjust the database name if needed.

CREATE DATABASE IF NOT EXISTS csce548_project1
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE csce548_project1;

DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- USERS: very simple user table
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- CATEGORIES: labels for tasks
CREATE TABLE categories (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- TASKS: each task belongs to a user and a category
CREATE TABLE tasks (
  task_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  category_id INT NOT NULL,
  description VARCHAR(255) NOT NULL,
  completed BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_tasks_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT
) ENGINE=InnoDB;