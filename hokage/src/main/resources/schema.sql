USE anime_db;
DROP TABLE anime;
CREATE TABLE anime (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       anime_name VARCHAR(100),
                       rating FLOAT,
                       genre VARCHAR(50),
                       no_of_ep INT,
                       type VARCHAR(20)
);

