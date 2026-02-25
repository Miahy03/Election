-- Tables
CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE voter (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TYPE vote_type AS ENUM ('VALID', 'BLANK', 'NULL');

CREATE TABLE vote (
    id SERIAL PRIMARY KEY,
    candidate_id INT REFERENCES candidate(id),
    voter_id INT NOT NULL REFERENCES voter(id),
    vote_type vote_type NOT NULL
);

-- Données initiales
INSERT INTO candidate (name) VALUES
('Alice'),
('Bob'),
('Charlie');

INSERT INTO voter (name) VALUES
('Voter1'),
('Voter2'),
('Voter3'),
('Voter4'),
('Voter5'),
('Voter6');

INSERT INTO vote (candidate_id, voter_id, vote_type) VALUES
(1, 1, 'VALID'),
(1, 2, 'VALID'),
(2, 3, 'VALID'),
(2, 4, 'BLANK'),
(NULL, 5, 'BLANK'),
(3, 6, 'NULL');