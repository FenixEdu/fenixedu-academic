ALTER TABLE PROJECT_ACCESS ADD COLUMN COST_CENTER tinyint(1) NOT NULL default '0' AFTER END_DATE;

INSERT INTO role VALUES (NULL ,'INSTITUCIONAL_PROJECTS_MANAGER','/institucionalProjectsManagement','/institucionalProjectIndex.do','portal.institucionalProjectsManager',null);