CREATE TABLE special_schedules (
   id UUID PRIMARY KEY,
   barber_id UUID NOT NULL,
   exception_date DATE NOT NULL,
   is_closed BOOLEAN DEFAULT FALSE NOT NULL,
   start_time TIME,
   end_time TIME,
   description VARCHAR(100),
   CONSTRAINT fk_special_schedule_barber FOREIGN KEY (barber_id) REFERENCES users(id)
);