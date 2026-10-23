CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    loyalty_count INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE services (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_minutes INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE schedule_blocks (
    id UUID PRIMARY KEY,
    barber_id UUID NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    reason VARCHAR(50) NOT NULL,
    CONSTRAINT fk_schedule_block_barber FOREIGN KEY (barber_id) REFERENCES users(id)
);

CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    barber_id UUID NOT NULL,
    service_id UUID NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    time_range tstzrange GENERATED ALWAYS AS (tstzrange(start_time, end_time)) STORED,
    CONSTRAINT fk_appointment_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_appointment_barber FOREIGN KEY (barber_id) REFERENCES users(id),
    CONSTRAINT fk_appointment_service FOREIGN KEY (service_id) REFERENCES services(id),
    CONSTRAINT no_overlapping_appointments EXCLUDE USING GIST (
    barber_id WITH =,
    time_range WITH &&
    ) WHERE (status IN ('SCHEDULED', 'CONFIRMED'))
);

CREATE TABLE visit_history (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    appointment_id UUID NOT NULL UNIQUE,
    barber_id UUID NOT NULL,
    service_id UUID NOT NULL,
    visit_date TIMESTAMP WITH TIME ZONE NOT NULL,
    price_paid NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_history_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_history_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    CONSTRAINT fk_history_barber FOREIGN KEY (barber_id) REFERENCES users(id),
    CONSTRAINT fk_history_service FOREIGN KEY (service_id) REFERENCES services(id)
);