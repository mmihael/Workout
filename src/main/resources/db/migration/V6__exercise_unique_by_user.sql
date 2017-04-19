ALTER TABLE `exercise` DROP KEY `name`;
ALTER TABLE `exercise` ADD UNIQUE `created_by_name` (`created_by`, `name`);