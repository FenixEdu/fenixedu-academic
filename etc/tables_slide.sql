drop table if exists objects;
create table objects(uri blob not null, primary key uriIndex (uri(255)), 
  classname blob);
  
drop table if exists children;
create table children(uri blob, childuri blob);

drop table if exists links;
create table links(link blob, linkto blob);

drop table if exists permissions;
create table permissions(object blob, revisionnumber varchar(20), 
  subject blob, action blob, inheritable int, negative int);
  
drop table if exists locks;
create table locks(id blob, object blob, subject blob, type blob, 
  expirationdate varchar(15), inheritable int, xexclusive int);
  
drop table if exists revisions;
create table revisions(uri blob not null, primary key uriIndex 
  (uri(255)), isversioned int, initialrevision varchar(10) );
  
drop table if exists workingrevision;
create table workingrevision(uri blob, baserevision varchar(20), xnumber 
  varchar(20) );
drop table if exists latestrevisions;
create table latestrevisions(uri blob, branchname blob, xnumber 
  varchar(20) );
  
drop table if exists branches;
create table branches(uri blob, xnumber varchar(20), childnumber 
  varchar(20) );
  
drop table if exists revision;
create table revision(uri blob, xnumber varchar(20), branchname blob);

drop table if exists label;
create table label(uri blob, xnumber varchar(20), label blob);

drop table if exists property;
create table property(uri blob, xnumber varchar(20), name blob, value 
  blob, namespace blob, type varchar(100), protected int);
  
drop table if exists revisioncontent;
create table revisioncontent(uri blob, xnumber varchar(20), content longblob);