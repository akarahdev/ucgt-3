CREATE DATABASE IF NOT EXISTS ucgt3;
USE ucgt3;

CREATE TABLE IF NOT EXISTS inventory_slots (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player UUID,
    item VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_player_slots ON inventory_slots (player);