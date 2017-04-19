ALTER TABLE `workout` DROP KEY `name`;
ALTER TABLE `workout` ADD UNIQUE `created_by_name` (`created_by`, `name`);