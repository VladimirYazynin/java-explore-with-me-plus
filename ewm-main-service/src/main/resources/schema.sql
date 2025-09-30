create table if not exists users
(
    id bigint generated always as identity primary key,
    email varchar not null,
    name varchar not null
);

create table if not exists categories
(
    id bigint generated always as identity primary key,
    name varchar not null
);

create table if not exists locations
(
    id bigint generated always as identity primary key,
    lat float not null,
    lon float not null
);

create table if not exists events
(
    id bigint generated always as identity primary key,
    annotation varchar not null,
    category_id bigint not null references categories (id) on delete cascade,
    confirmed_requests int,
    created_on timestamp without time zone not null,
    description varchar not null,
    event_date timestamp without time zone not null,
    user_id bigint not null references users (id) on delete cascade,
    location_id bigint not null references locations (id) on delete cascade,
    paid bool,
    participant_limit int,
    published_on timestamp without time zone,
    request_moderation bool,
    title varchar not null,
    state varchar not null,
    views int
);

create table if not exists participation_requests
(
    id bigint generated always as identity primary key,
    created timestamp without time zone not null,
    event_id bigint not null references events (id) on delete cascade,
    user_id bigint not null references users (id) on delete cascade,
    status varchar not null
);

create table if not exists compilations
(
    id bigint generated always as identity primary key,
    pinned bool not null,
    title varchar not null
);

create table if not exists compilations_events
(
    events_id bigint not null references events (id) on delete cascade ,
    compilation_id bigint not null references compilations (id) on delete cascade,
    primary key (events_id, compilation_id)
);
