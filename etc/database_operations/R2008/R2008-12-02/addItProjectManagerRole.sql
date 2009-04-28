INSERT INTO ROLE VALUES (NULL ,'IT_PROJECTS_MANAGER','/itProjectsManagement','/itProjectIndex.do','portal.itProjectsManager',1,'net.sourceforge.fenixedu.domain.Role', null, null, null, null);

ALTER TABLE PROJECT_ACCESS ADD COLUMN IT_PROJECT  tinyint(1) NOT NULL default '0';
