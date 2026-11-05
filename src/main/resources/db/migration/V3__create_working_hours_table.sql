CREATE TABLE working_hours (
    id UUID PRIMARY KEY,
    barber_id UUID NOT NULL,
    day_of_week INTEGER NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_working_hours_barber FOREIGN KEY (barber_id) REFERENCES users(id)
);