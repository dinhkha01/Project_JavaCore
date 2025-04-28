-- Stored procedure to find all candidates
DELIMITER //
CREATE PROCEDURE proc_candidates_findAll()
BEGIN
    SELECT * FROM candidate;
END //
DELIMITER ;

-- Stored procedure to find a candidate by ID
DELIMITER //
CREATE PROCEDURE proc_candidates_findById(IN candidate_id INT)
BEGIN
    SELECT * FROM candidate WHERE id = candidate_id;
END //
DELIMITER ;

-- Stored procedure to create a new candidate
DELIMITER //
CREATE PROCEDURE proc_candidates_create(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender VARCHAR(10),
    IN p_status VARCHAR(20),
    IN p_description TEXT,
    IN p_dob DATE
)
BEGIN
    -- Đặt các giá trị mặc định cho các trường không được cung cấp
    SET p_phone = IFNULL(p_phone, '');
    SET p_experience = IFNULL(p_experience, 0);
    SET p_gender = IFNULL(p_gender, '');
    SET p_status = IFNULL(p_status, 'active');
    SET p_description = IFNULL(p_description, '');

    INSERT INTO candidate(name, email, password, phone, experience, gender, status, description, dob)
    VALUES(p_name, p_email, p_password, p_phone, p_experience, p_gender, p_status, p_description, p_dob);
END //
DELIMITER ;

-- Stored procedure to update a candidate
DELIMITER //
CREATE PROCEDURE proc_candidates_update(
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender VARCHAR(10),
    IN p_status VARCHAR(20),
    IN p_description TEXT,
    IN p_dob DATE,
    IN p_id INT
)
BEGIN
    UPDATE candidate
    SET name = p_name,
        email = p_email,
        password = p_password,
        phone = p_phone,
        experience = p_experience,
        gender = p_gender,
        status = p_status,
        description = p_description,
        dob = p_dob
    WHERE id = p_id;
END //
DELIMITER ;

-- Stored procedure to delete a candidate
DELIMITER //
CREATE PROCEDURE proc_candidates_delete(IN candidate_id INT)
BEGIN
    DELETE FROM candidate WHERE id = candidate_id;
END //
DELIMITER ;

-- Stored procedure to find a candidate by email
DELIMITER //
CREATE PROCEDURE proc_candidates_findByEmail(IN p_email VARCHAR(100))
BEGIN
    SELECT * FROM candidate WHERE email = p_email;
END //
DELIMITER ;

-- Stored procedure to find a candidate by email and password
DELIMITER //
CREATE PROCEDURE proc_candidates_findByEmailAndPassword(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT * FROM candidate WHERE email = p_email AND password = p_password;
END //
DELIMITER ;