update USER set USER_U_ID = 'ist24661' where USER_U_ID like 'ist10482704';
update LOGIN_ALIAS set ALIAS = 'ist24661' where ALIAS like 'ist10482704';
update IDENTIFICATION set IS_PASS_IN_KERBEROS = 0 where ID_INTERNAL = (select KEY_LOGIN from LOGIN_ALIAS where ALIAS like 'ist24661');