-- Procedure để lấy thông tin candidate dựa vào email
DELIMITER //
CREATE PROCEDURE sp_getCandidateByEmail(IN p_email VARCHAR(100))
BEGIN
SELECT * FROM candidate WHERE email = p_email;
END //
DELIMITER ;

-- Procedure để lấy thông tin candidate dựa vào số điện thoại
DELIMITER //
CREATE PROCEDURE sp_getCandidateByPhone(IN p_phone VARCHAR(20))
BEGIN
SELECT * FROM candidate WHERE phone = p_phone;
END //
DELIMITER ;

-- Procedure để đổi mật khẩu dựa vào email
DELIMITER //
CREATE PROCEDURE sp_changePasswordByEmail(
    IN p_email VARCHAR(100),
    IN p_oldPassword VARCHAR(255),
    IN p_newPassword VARCHAR(255),
    OUT p_success BOOLEAN
)
BEGIN
    DECLARE v_count INT;

    -- Kiểm tra email và mật khẩu cũ có khớp không
SELECT COUNT(*) INTO v_count
FROM candidate
WHERE email = p_email AND password = p_oldPassword;

IF v_count > 0 THEN
        -- Nếu khớp, cập nhật mật khẩu mới
UPDATE candidate
SET password = p_newPassword
WHERE email = p_email;

SET p_success = TRUE;
ELSE
        SET p_success = FALSE;
END IF;
END //
DELIMITER ;

-- Procedure để đổi mật khẩu dựa vào số điện thoại
DELIMITER //
CREATE PROCEDURE sp_changePasswordByPhone(
    IN p_phone VARCHAR(20),
    IN p_oldPassword VARCHAR(255),
    IN p_newPassword VARCHAR(255),
    OUT p_success BOOLEAN
)
BEGIN
    DECLARE v_count INT;

    -- Kiểm tra số điện thoại và mật khẩu cũ có khớp không
SELECT COUNT(*) INTO v_count
FROM candidate
WHERE phone = p_phone AND password = p_oldPassword;

IF v_count > 0 THEN
        -- Nếu khớp, cập nhật mật khẩu mới
UPDATE candidate
SET password = p_newPassword
WHERE phone = p_phone;

SET p_success = TRUE;
ELSE
        SET p_success = FALSE;
END IF;
END //
DELIMITER ;

-- Procedure để cập nhật thông tin cá nhân
DELIMITER //
CREATE PROCEDURE sp_updateCandidateInfo(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender VARCHAR(10),
    IN p_description TEXT,
    IN p_dob DATE
)
BEGIN
UPDATE candidate
SET
    name = p_name,
    phone = p_phone,
    experience = p_experience,
    gender = p_gender,
    description = p_description,
    dob = p_dob
WHERE id = p_id;
END //
DELIMITER ;

-- Procedure để lấy thông tin candidate theo ID
DELIMITER //
CREATE PROCEDURE sp_getCandidateById(IN p_id INT)
BEGIN
SELECT * FROM candidate WHERE id = p_id;
END //
DELIMITER ;