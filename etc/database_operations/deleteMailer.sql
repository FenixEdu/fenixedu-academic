delete from PERSON_ROLE where PERSON_ROLE.KEY_PERSON=(select ID_INTERNAL from PERSON where PERSON.USERNAME="mailer");
delete from PERSON where PERSON.USERNAME="mailer";