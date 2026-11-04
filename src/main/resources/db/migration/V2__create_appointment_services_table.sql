CREATE TABLE appointment_services (
    appointment_id UUID NOT NULL,
    service_id UUID NOT NULL,
    PRIMARY KEY (appointment_id, service_id),
    CONSTRAINT fk_app_services_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    CONSTRAINT fk_app_services_service FOREIGN KEY (service_id) REFERENCES barber_service(id)
);