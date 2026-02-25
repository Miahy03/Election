CREATE DATABASE election_db;

CREATE USER election_db_manager WITH PASSWORD '123456';

GRANT CONNECT ON DATABASE election_db TO election_db_manager;

\c election_db

GRANT CREATE ON SCHEMA public TO election_db_manager;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO election_db_manager;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO election_db_manager;

GRANT ALL PRIVILEGES ON TABLE candidate TO election_db_manager;
GRANT ALL PRIVILEGES ON TABLE voter TO election_db_manager;
GRANT ALL PRIVILEGES ON TABLE vote TO election_db_manager;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO election_db_manager;