ALTER TABLE special_schedules RENAME COLUMN exception_date TO start_date;
ALTER TABLE special_schedules ADD COLUMN end_date DATE;
UPDATE special_schedules SET end_date = start_date WHERE end_date IS NULL;
ALTER TABLE special_schedules ALTER COLUMN end_date SET NOT NULL;