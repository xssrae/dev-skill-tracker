CREATE TABLE IF NOT EXISTS skill_events (
    event_id UUID PRIMARY KEY,
    user_id TEXT NOT NULL,
    repository TEXT NOT NULL,
    project_name TEXT NOT NULL,
    event_type TEXT NOT NULL,
    technologies TEXT NOT NULL,
    progress_delta INTEGER NOT NULL,
    completed_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS technology_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id TEXT NOT NULL,
    technology TEXT NOT NULL,
    completed_items INTEGER NOT NULL DEFAULT 0,
    progress_points INTEGER NOT NULL DEFAULT 0,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT technology_progress_user_technology_unique UNIQUE (user_id, technology)
);

CREATE INDEX IF NOT EXISTS technology_progress_user_idx
    ON technology_progress (user_id, progress_points DESC);
