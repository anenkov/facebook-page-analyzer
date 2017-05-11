# create table Data
# (
#   userId varchar(255) not null
#     primary key,
#   data varchar(1024) null,
#   constraint DataPK
#   unique (userId)
# )
# ;
#
# create table UserConnection
# (
#   userId varchar(255) not null,
#   providerId varchar(255) not null,
#   providerUserId varchar(255) not null,
#   rank int not null,
#   displayName varchar(255) null,
#   profileUrl varchar(512) null,
#   imageUrl varchar(512) null,
#   accessToken varchar(1024) not null,
#   secret varchar(255) null,
#   refreshToken varchar(255) null,
#   expireTime bigint null,
#   primary key (userId, providerId, providerUserId),
#   constraint UserConnectionRank
#   unique (userId, providerId, rank)
# )
# ;
#
# create table UserProfile
# (
#   userId varchar(255) not null
#     primary key,
#   email varchar(255) null,
#   firstName varchar(255) null,
#   lastName varchar(255) null,
#   name varchar(255) null,
#   username varchar(255) null,
#   constraint UserProfilePK
#   unique (userId)
# )
# ;
#
# create table authorities
# (
#   username varchar(50) not null,
#   authority varchar(50) not null,
#   constraint ix_auth_username
#   unique (username, authority)
# )
# ;
#
# create table users
# (
#   username varchar(50) not null
#     primary key,
#   password varchar(500) not null,
#   enabled tinyint(1) not null
# )
# ;
#
# alter table authorities
#   add constraint fk_authorities_users
# foreign key (username) references facebook_page_analyzer.users (username)
# ;
#
