-- Stored Procedures for candidate application functionality

-- 1. Get all active positions (not expired and not deleted)
DELIMITER //
CREATE PROCEDURE sp_GetAllActivePositions()
BEGIN
SELECT * FROM recruitment_position
WHERE name NOT LIKE '%*deleted'
  AND expiredDate >= CURDATE()
ORDER BY id ASC;
END //
DELIMITER ;

-- 2. Create a new application
DELIMITER //
CREATE PROCEDURE sp_CreateApplication(
    IN p_candidateId INT,
    IN p_recruitmentPositionId INT,
    IN p_cvUrl VARCHAR(255),
    OUT p_newId INT
)
BEGIN
INSERT INTO application (
    candidateId,
    recruitmentPositionId,
    cvUrl,
    progress,
    createAt
) VALUES (
             p_candidateId,
             p_recruitmentPositionId,
             p_cvUrl,
             'pending',
             CURRENT_TIMESTAMP
         );

SET p_newId = LAST_INSERT_ID();
END //
DELIMITER ;

-- 3. Get applications by candidate ID
DELIMITER //
CREATE PROCEDURE sp_GetApplicationsByCandidateId(IN p_candidateId INT)
BEGIN
SELECT a.*, rp.name as positionName
FROM application a
         JOIN recruitment_position rp ON a.recruitmentPositionId = rp.id
WHERE a.candidateId = p_candidateId
ORDER BY a.createAt DESC;
END //
DELIMITER ;

-- 4. Get application details
DELIMITER //
CREATE PROCEDURE sp_GetApplicationDetails(IN p_applicationId INT)
BEGIN
SELECT a.*,
       c.name as candidateName, c.email as candidateEmail, c.phone as candidatePhone,
       rp.name as positionName, rp.description as positionDescription
FROM application a
         JOIN candidate c ON a.candidateId = c.id
         JOIN recruitment_position rp ON a.recruitmentPositionId = rp.id
WHERE a.id = p_applicationId;
END //
DELIMITER ;

-- 5. Check if candidate has already applied for a position
DELIMITER //
CREATE PROCEDURE sp_CheckExistingApplication(
    IN p_candidateId INT,
    IN p_positionId INT,
    OUT p_exists BOOLEAN
)
BEGIN
    DECLARE app_count INT;

SELECT COUNT(*) INTO app_count
FROM application
WHERE candidateId = p_candidateId
  AND recruitmentPositionId = p_positionId
  AND destroyAt IS NULL;

IF app_count > 0 THEN
        SET p_exists = TRUE;
ELSE
        SET p_exists = FALSE;
END IF;
END //
DELIMITER ;

-- 6. Get active positions with technology details
DELIMITER //
CREATE PROCEDURE sp_GetActivePositionsWithTechnologies()
BEGIN
SELECT rp.*,
       GROUP_CONCAT(t.name SEPARATOR ', ') as technologies
FROM recruitment_position rp
         LEFT JOIN recruitment_position_technology rpt ON rp.id = rpt.recruitmentPositionId
         LEFT JOIN technology t ON rpt.technologyId = t.id
WHERE rp.name NOT LIKE '%*deleted'
  AND rp.expiredDate >= CURDATE()
GROUP BY rp.id
ORDER BY rp.id ASC;
END //
DELIMITER ;