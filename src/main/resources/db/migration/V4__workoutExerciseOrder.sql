CREATE TABLE `workout_exercise_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workout` int(11) NOT NULL,
  `exercise` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `edited_at` datetime,
  `created_by` int(11),
  PRIMARY KEY (`id`),
  UNIQUE KEY `workout_exercise_order` (`workout`, `exercise`, `order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;