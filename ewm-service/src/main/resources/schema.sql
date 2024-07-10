DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;

CREATE TABLE IF NOT EXISTS categories (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat NUMERIC,
    lon NUMERIC
);

CREATE TABLE IF NOT EXISTS events
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    annotation          VARCHAR(2000) NOT NULL,
    category_id         BIGINT NOT NULL,
    confirmed_Requests  BIGINT,
    create_date         TIMESTAMP WITHOUT TIME ZONE,
    description         VARCHAR(7000),
    event_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id        BIGINT NOT NULL,
    location_id         BIGINT,
    paid                BOOLEAN,
    participant_limit   INTEGER DEFAULT 0,
    published_date      TIMESTAMP WITHOUT TIME ZONE,
    request_moderation  BOOLEAN DEFAULT true,
    status              VARCHAR(200),
    title               VARCHAR(120) NOT NULL,
    CONSTRAINT          fk_event_to_user     FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT          fk_event_to_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT          fk_event_to_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

