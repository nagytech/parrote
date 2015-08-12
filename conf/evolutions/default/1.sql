# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bon_mot (
  id                        varchar(40) not null,
  created_on                timestamp,
  created_by_id             varchar(40),
  updated_on                timestamp,
  updated_by_id             varchar(40),
  deleted_on                timestamp,
  deleted_by_id             varchar(40),
  deleted                   boolean,
  user_id                   varchar(40),
  text                      varchar(255),
  constraint pk_bon_mot primary key (id))
;

create table pith (
  id                        varchar(40) not null,
  created_on                timestamp,
  created_by_id             varchar(40),
  updated_on                timestamp,
  updated_by_id             varchar(40),
  deleted_on                timestamp,
  deleted_by_id             varchar(40),
  deleted                   boolean,
  code                      varchar(128),
  constraint uq_pith_code unique (code),
  constraint pk_pith primary key (id))
;

create table session (
  id                        varchar(40) not null,
  user_id                   varchar(40),
  last_access               timestamp,
  constraint pk_session primary key (id))
;

create table user (
  id                        varchar(40) not null,
  created_on                timestamp,
  created_by_id             varchar(40),
  updated_on                timestamp,
  updated_by_id             varchar(40),
  deleted_on                timestamp,
  deleted_by_id             varchar(40),
  deleted                   boolean,
  username                  varchar(64),
  email                     varchar(255),
  password                  varchar(255),
  admin                     boolean,
  banned                    boolean,
  constraint uq_user_username unique (username),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;


create table bon_mot_pith (
  bon_mot_id                     varchar(40) not null,
  pith_id                        varchar(40) not null,
  constraint pk_bon_mot_pith primary key (bon_mot_id, pith_id))
;
alter table bon_mot add constraint fk_bon_mot_createdBy_1 foreign key (created_by_id) references user (id) on delete restrict on update restrict;
create index ix_bon_mot_createdBy_1 on bon_mot (created_by_id);
alter table bon_mot add constraint fk_bon_mot_updatedBy_2 foreign key (updated_by_id) references user (id) on delete restrict on update restrict;
create index ix_bon_mot_updatedBy_2 on bon_mot (updated_by_id);
alter table bon_mot add constraint fk_bon_mot_deletedBy_3 foreign key (deleted_by_id) references user (id) on delete restrict on update restrict;
create index ix_bon_mot_deletedBy_3 on bon_mot (deleted_by_id);
alter table bon_mot add constraint fk_bon_mot_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bon_mot_user_4 on bon_mot (user_id);
alter table pith add constraint fk_pith_createdBy_5 foreign key (created_by_id) references user (id) on delete restrict on update restrict;
create index ix_pith_createdBy_5 on pith (created_by_id);
alter table pith add constraint fk_pith_updatedBy_6 foreign key (updated_by_id) references user (id) on delete restrict on update restrict;
create index ix_pith_updatedBy_6 on pith (updated_by_id);
alter table pith add constraint fk_pith_deletedBy_7 foreign key (deleted_by_id) references user (id) on delete restrict on update restrict;
create index ix_pith_deletedBy_7 on pith (deleted_by_id);
alter table session add constraint fk_session_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_session_user_8 on session (user_id);
alter table user add constraint fk_user_createdBy_9 foreign key (created_by_id) references user (id) on delete restrict on update restrict;
create index ix_user_createdBy_9 on user (created_by_id);
alter table user add constraint fk_user_updatedBy_10 foreign key (updated_by_id) references user (id) on delete restrict on update restrict;
create index ix_user_updatedBy_10 on user (updated_by_id);
alter table user add constraint fk_user_deletedBy_11 foreign key (deleted_by_id) references user (id) on delete restrict on update restrict;
create index ix_user_deletedBy_11 on user (deleted_by_id);



alter table bon_mot_pith add constraint fk_bon_mot_pith_bon_mot_01 foreign key (bon_mot_id) references bon_mot (id) on delete restrict on update restrict;

alter table bon_mot_pith add constraint fk_bon_mot_pith_pith_02 foreign key (pith_id) references pith (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists bon_mot;

drop table if exists bon_mot_pith;

drop table if exists pith;

drop table if exists session;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

