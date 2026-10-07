CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    original_text TEXT NOT NULL,
    corrected_text TEXT,
    language VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX idx_tasks_status_created_at ON tasks(status, created_at);
