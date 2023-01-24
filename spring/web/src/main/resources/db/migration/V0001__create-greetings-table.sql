create table greeting
(
    id        int         not null primary key generated always as identity,
    message   longvarchar not null,
    recipient longvarchar not null
)