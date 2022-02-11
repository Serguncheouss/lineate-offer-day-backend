CREATE TABLE license
(
    id           BIGSERIAL PRIMARY KEY,
    recording_id BIGINT REFERENCES recording (id),
    start_time   TIMESTAMP,
    end_time     TIMESTAMP,
    cost         NUMERIC(38,2),
    company_id   BIGINT REFERENCES company (id)
);

INSERT INTO license (recording_id, start_time, end_time, cost, company_id) VALUES
(1, '2020-02-23', '2025-02-23', 12.5, 1),
(2, '2019-01-12', '2021-02-23', 5.3, 2),
(3, '2021-05-05', '2023-02-23', 121.45, 3);
commit;