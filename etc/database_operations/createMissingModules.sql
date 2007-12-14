

-- Estacionamento
insert into CONTENT(OJB_CONCRETE_CLASS,CONTENT_ID,NAME,CREATION_DATE,PREFIX,KEY_AVAILABILITY_POLICY,VISIBLE,EXECUTION_PATH)
values('net.sourceforge.fenixedu.domain.functionalities.Module','452f211a-fada-102a-b5ba-0013d3b09da0','pt14:Estacionamento',NOW(),'/parkingManager',NULL,'1','/parkingManager/index.do');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, EXPRESSION, TARGET_GROUP, KEY_CONTENT) 
select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', 'role(PARKING_MANAGER)','role(PARKING_MANAGER)', C.ID_INTERNAL FROM CONTENT C WHERE CONTENT_ID='452f211a-fada-102a-b5ba-0013d3b09da0';


-- Gestão de website
insert into CONTENT(OJB_CONCRETE_CLASS,CONTENT_ID,NAME,CREATION_DATE,PREFIX,KEY_AVAILABILITY_POLICY,VISIBLE,EXECUTION_PATH)
values('net.sourceforge.fenixedu.domain.functionalities.Module','421011e0-fadc-102a-b5ba-0013d3b09da0','pt17:Gestão do WebSite',NOW(),'/webSiteManager',NULL,'1','/webSiteManager/index.do');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, EXPRESSION, TARGET_GROUP, KEY_CONTENT) 
select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', 'role(WEBSITE_MANAGER)','role(WEBSITE_MANAGER)', C.ID_INTERNAL FROM
CONTENT C WHERE CONTENT_ID='421011e0-fadc-102a-b5ba-0013d3b09da0';

-- Coordenação de Exames AKA Avaliação
insert into CONTENT(OJB_CONCRETE_CLASS,CONTENT_ID,NAME,CREATION_DATE,PREFIX,KEY_AVAILABILITY_POLICY,VISIBLE,EXECUTION_PATH)
values('net.sourceforge.fenixedu.domain.functionalities.Module','e0181ae0-fb8b-102a-b5ba-0013d3b09da0','pt9:Avaliação',NOW(),'/examCoordinatination',NULL,'1','/examCoordination/index.do');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, EXPRESSION, TARGET_GROUP, KEY_CONTENT)
select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', 'role(EXAM_COORDINATOR)','role(EXAM_COORDINATOR)', C.ID_INTERNAL FROM
CONTENT C WHERE CONTENT_ID='e0181ae0-fb8b-102a-b5ba-0013d3b09da0';

-- Academic Admin Office

insert into CONTENT(OJB_CONCRETE_CLASS,CONTENT_ID,NAME,CREATION_DATE,PREFIX,KEY_AVAILABILITY_POLICY,VISIBLE,EXECUTION_PATH)
values('net.sourceforge.fenixedu.domain.functionalities.Module','3d30565c-fb8c-102a-b5ba-0013d3b09da0','pt20:Secretaria Académica',NOW(),'/academicAdminOffice',NULL,'1','/academicAdminOffice/index.do');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, EXPRESSION, TARGET_GROUP, KEY_CONTENT)
select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', 'role(ACADEMIC_ADMINISTRATIVE_OFFICE)','role(ACADEMIC_ADMINISTRATIVE_OFFICE)', C.ID_INTERNAL FROM
CONTENT C WHERE CONTENT_ID='3d30565c-fb8c-102a-b5ba-0013d3b09da0';

update CONTENT C, AVAILABILITY_POLICY AP set C.KEY_AVAILABILITY_POLICY=AP.ID_INTERNAL WHERE AP.KEY_CONTENT=C.ID_INTERNAL;

select @RootModule:=ROOT_DOMAIN_OBJECT.KEY_ROOT_MODULE from ROOT_DOMAIN_OBJECT;

CREATE TEMPORARY TABLE XPTO(CONTENT_ID VARCHAR(255), NODE_ORDER INTEGER);

insert into XPTO(CONTENT_ID, NODE_ORDER) SELECT '452f211a-fada-102a-b5ba-0013d3b09da0', COUNT(ID_INTERNAL)+1 FROM NODE WHERE KEY_PARENT=@RootModule;
insert into XPTO(CONTENT_ID, NODE_ORDER) SELECT '421011e0-fadc-102a-b5ba-0013d3b09da0', COUNT(ID_INTERNAL)+2 FROM NODE WHERE KEY_PARENT=@RootModule;
insert into XPTO(CONTENT_ID, NODE_ORDER) SELECT 'e0181ae0-fb8b-102a-b5ba-0013d3b09da0', COUNT(ID_INTERNAL)+3 FROM NODE WHERE KEY_PARENT=@RootModule;
insert into XPTO(CONTENT_ID, NODE_ORDER) SELECT '3d30565c-fb8c-102a-b5ba-0013d3b09da0', COUNT(ID_INTERNAL)+4 FROM NODE WHERE KEY_PARENT=@RootModule;


insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD,NODE_ORDER,ASCENDING,VISIBLE) SELECT 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode',@RootModule, C.ID_INTERNAL, XPTO.NODE_ORDER, '1', '1' FROM XPTO XPTO, CONTENT C WHERE C.CONTENT_ID=XPTO.CONTENT_ID;

DROP TEMPORARY TABLE XPTO;


-- FIX Grantowner path

update CONTENT SET PREFIX='/grantOwner', EXECUTION_PATH='/grantOwner/index.do' WHERE PREFIX='/granOwner' AND EXECUTION_PATH='/granOwner/index.do';

