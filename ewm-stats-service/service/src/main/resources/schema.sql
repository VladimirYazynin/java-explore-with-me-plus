create table if not exists endpoint_hits
(
    id bigint generated always as identity primary key,
    app varchar not null,
    uri varchar not null,
    ip varchar not null,
    timestamp timestamp without time zone not null
);