create table thing
(
id text not null,
name text not null
);

create table other_thing
(
id text not null,
name text not null,
thing_id text not null
)