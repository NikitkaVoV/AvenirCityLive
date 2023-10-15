-- Database configuration

-- Create tables

create table permissions
(
    id   serial primary key,
    name varchar(255) not null
        constraint uk_permissions_n unique

);

create table roles
(
    id   serial primary key,
    name varchar(255) not null
        constraint uk_roles_n unique
);

create table users
(
    id         serial primary key,
    blocked    boolean not null,
    disabled   boolean not null,
    email      varchar(255),
    name       varchar(255),
    password   varchar(255),
    patronymic varchar(255),
    surname    varchar(255),
    uuid       uuid    not null
        constraint uk_users_uuid unique
);

create table assigned_permissions
(
    user_id       integer not null,
    permission_id integer not null
        constraint fk_assigned_permissions_p references permissions,
    primary key (user_id, permission_id)
);

create table refresh_tokens
(
    id                  serial primary key,
    access_expiry_date  timestamp    not null,
    refresh_expiry_date timestamp    not null,
    access_token_hash   varchar(255) not null,
    refresh_token_hash  varchar(255) not null,
    user_id             integer      not null
        constraint fk_refresh_tokens_u references users
);


create table assigned_roles
(
    user_id integer not null
        constraint fk_assigned_roles_u references users,
    role_id integer not null
        constraint fk_assigned_roles_r references roles,
    primary key (user_id, role_id)
);

create table role_permissions
(
    role_id       integer not null
        constraint fk_role_permissions_r references roles,
    permission_id integer not null
        constraint fk_role_permissions_p references permissions,
    primary key (role_id, permission_id)
);

create table link_social_network_tokens
(
    id          serial primary key,
    expiry_date timestamp,
    token       varchar(255),
    user_id     integer not null
        constraint fk_link_social_network_tokens_u references users
);

create table linked_social_networks
(
    id                     serial primary key,
    additional_information varchar(255),
    enabled_mailing        boolean      not null,
    social_network_type    varchar(255) not null,
    user_social_network_id varchar(255) not null,
    user_id                integer      not null
        constraint fk_linked_social_networks_u references users
);

create table login_histories
(
    id            serial primary key,
    date_and_time timestamp not null,
    user_id       integer   not null
        constraint fk_login_histories_u references users
);

create table temp_users
(
    id           serial primary key,
    email        varchar(255) not null,
    expiry_date  timestamp    not null,
    password     varchar(255) not null,
    redirect_url varchar(500) not null,
    token        varchar(255) not null
);

create table mail_changes
(
    id           serial primary key,
    email        varchar(255) not null,
    expiry_date  timestamp    not null,
    redirect_url varchar(500) not null,
    token        varchar(255) not null,
    user_id      integer      not null
        constraint fk_mail_changes_u references users
);

create table redirect_urls
(
    id       serial primary key,
    url      varchar(500) not null
        constraint uk_redirect_urls_u unique,
    url_hash varchar(255) not null

);


-- Insert Data

CREATE OR REPLACE FUNCTION insert_role_permission(role_name VARCHAR, permission_name VARCHAR)
    RETURNS VOID AS
$$
BEGIN
    INSERT INTO role_permissions (role_id, permission_id)
    SELECT roles.id, permissions.id
    FROM roles,
         permissions
    WHERE roles.name = role_name
      AND permissions.name = permission_name;
END;
$$ LANGUAGE plpgsql;


-- Вставка записей в таблицу roles
INSERT INTO roles (name)
VALUES ('user'),
       ('admin'),
       ('unverified'),
       ('verified');

-- Вставка записей в таблицу permissions
INSERT INTO permissions (name)
VALUES ('permissions.me'),
       ('profile.me'),
       ('profile.updating.name'),
       ('profile.updating.mail'),
       ('profile.updating.password'),
       ('profile.updating.remove_linked_network'),
       ('profile.updating.mailing'),
       ('profile.updating.linked_schedule'),

       ('schedule.updating.replacement'),
       ('schedule.updating.standard'),

       ('verified.add'),
       ('verified.remove'),
       ('verified.find'),

       ('role.manager.available'),
       ('role.manager.view'),
       ('role.manager.add'),
       ('role.manager.remove')
;


-- Вставка записей в таблицу role_permissions с использованием имен ролей и разрешений
SELECT insert_role_permission('user', 'permissions.me');

SELECT insert_role_permission('user', 'profile.me');
SELECT insert_role_permission('user', 'profile.updating.mail');
SELECT insert_role_permission('user', 'profile.updating.password');
SELECT insert_role_permission('user', 'profile.updating.remove_linked_network');
SELECT insert_role_permission('user', 'profile.updating.mailing');

SELECT insert_role_permission('unverified', 'profile.updating.name');
SELECT insert_role_permission('verified', 'profile.updating.linked_schedule');

SELECT insert_role_permission('admin', 'verified.find');
SELECT insert_role_permission('admin', 'role.manager.available');
SELECT insert_role_permission('admin', 'role.manager.view');
SELECT insert_role_permission('admin', 'role.manager.add');
SELECT insert_role_permission('admin', 'role.manager.remove');

INSERT INTO users (blocked, disabled, email, name, password, patronymic, surname, uuid)
VALUES (false, false, 'admin@admin.com', 'NameAdmin', '$2a$10$pKh97WFo02/88VLjBHjUW.9YmHa.iWL2o/l9.Uz3j1UOFY6W7OWmK',
        'PatronymicAdmin', 'SurnameAdmin', 'db3f0c3f-a9e8-415c-81d3-4b82db1e3569');

INSERT INTO assigned_roles (user_id, role_id)
values (1, (SELECT id FROM roles WHERE name = 'user')),
       (1, (SELECT id FROM roles WHERE name = 'admin')),
       (1, (SELECT id FROM roles WHERE name = 'verified'))
;



