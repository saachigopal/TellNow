DROP TRIGGER IF EXISTS after_update__on_answer_increment_reward_points;
DELIMITER //
CREATE TRIGGER after_update__on_answer_increment_reward_points
    AFTER UPDATE ON `answer_reward` FOR EACH ROW
    BEGIN
    DECLARE points_value INTEGER;
     SELECT COUNT(*) INTO points_value FROM reward_points WHERE (profile_id = NEW.profile_id AND topic_id = NEW.topic_id);
     IF points_value > 0 THEN
	UPDATE reward_points SET points = points + (NEW.value - OLD.value) WHERE (profile_id = NEW.profile_id AND topic_id = NEW.topic_id);
     ELSE
	INSERT INTO reward_points(profile_id, topic_id, points) VALUES (NEW.profile_id, NEW.topic_id, NEW.value);
     END IF;
    END//
DELIMITER ;