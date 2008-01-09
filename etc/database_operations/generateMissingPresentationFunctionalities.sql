

  -- Department Presentation Functionality
   insert into CONTENT(NAME,TITLE,CREATION_DATE,CONTENT_ID,ENABLED,VISIBLE,EXECUTION_PATH,OJB_CONCRETE_CLASS) VALUES('pt6:Inicio','pt6:Inicio', NOW(),'c150f816-fdde-102a-b5ba-0013d3b09da0',1,1,'/departmentSite.do?method=presentation','net.sourceforge.fenixedu.domain.functionalities.Functionality');

      update NODE N, CONTENT C SET N.NODE_ORDER = N.NODE_ORDER+1 WHERE N.KEY_PARENT=C.ID_INTERNAL AND C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND C.PREFIX='/department';

      insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,VISIBLE,ASCENDING, OJB_CONCRETE_CLASS) values((SELECT ID_INTERNAL FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND PREFIX='/department'), (SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='c150f816-fdde-102a-b5ba-0013d3b09da0'),1,1,1,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode');


  -- ScientificalArea Presentation Functionality

 insert into CONTENT(NAME,TITLE,CREATION_DATE,CONTENT_ID,ENABLED,VISIBLE,EXECUTION_PATH,OJB_CONCRETE_CLASS) VALUES('pt6:Inicio','pt6:Inicio', NOW(),'c156ccf0-fdde-102a-b5ba-0013d3b09da0',1,1,'/viewSite.do?method=presentation','net.sourceforge.fenixedu.domain.functionalities.Functionality');

      update NODE N, CONTENT C SET N.NODE_ORDER = N.NODE_ORDER+1 WHERE N.KEY_PARENT=C.ID_INTERNAL AND C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND C.PREFIX='/scientificArea';

      insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,VISIBLE,ASCENDING, OJB_CONCRETE_CLASS) values((SELECT ID_INTERNAL FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND PREFIX='/scientificArea'), (SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='c156ccf0-fdde-102a-b5ba-0013d3b09da0'),1,1,1,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode');



  -- Research Unit Presentation Functionality 

   insert into CONTENT(NAME,TITLE,CREATION_DATE,CONTENT_ID,ENABLED,VISIBLE,EXECUTION_PATH,OJB_CONCRETE_CLASS) VALUES('pt6:Inicio','pt6:Inicio', NOW(),'c152da32-fdde-102a-b5ba-0013d3b09da0',1,1,'/viewResearchUnitSite.do?method=presentation','net.sourceforge.fenixedu.domain.functionalities.Functionality');

      update NODE N, CONTENT C SET N.NODE_ORDER = N.NODE_ORDER+1 WHERE N.KEY_PARENT=C.ID_INTERNAL AND C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND C.PREFIX='/researchSite';

      insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,VISIBLE,ASCENDING, OJB_CONCRETE_CLASS) values((SELECT ID_INTERNAL FROM CONTENT WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Module' AND PREFIX='/researchSite'), (SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='c152da32-fdde-102a-b5ba-0013d3b09da0'),1,1,1,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode');


 -- PedagogicalCouncil Presentation Functionality

    insert into CONTENT(NAME,TITLE,CREATION_DATE,CONTENT_ID,ENABLED,VISIBLE,EXECUTION_PATH,OJB_CONCRETE_CLASS) VALUES('pt6:Inicio','pt6:Inicio', NOW(),'c15590ce-fdde-102a-b5ba-0013d3b09da0',1,1,'/viewSite.do?method=presentation','net.sourceforge.fenixedu.domain.functionalities.Functionality');

      update NODE N, CONTENT C SET N.NODE_ORDER = N.NODE_ORDER+1 WHERE N.KEY_PARENT=C.ID_INTERNAL AND C.CONTENT_ID='d569fa9c-e701-4891-be2d-6c3463db0312';

      insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,VISIBLE,ASCENDING, OJB_CONCRETE_CLASS) values((select ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='d569fa9c-e701-4891-be2d-6c3463db0312'), (SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='c15590ce-fdde-102a-b5ba-0013d3b09da0'),1,1,1,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode');


 -- Scientific Council Presentation Functionality 

      insert into CONTENT(NAME,TITLE,CREATION_DATE,CONTENT_ID,ENABLED,VISIBLE,EXECUTION_PATH,OJB_CONCRETE_CLASS) VALUES('pt6:Inicio','pt6:Inicio', NOW(),'c15591a0-fdde-102a-b5ba-0013d3b09da0',1,1,'/viewSite.do?method=presentation','net.sourceforge.fenixedu.domain.functionalities.Functionality');


  update NODE N, CONTENT C SET N.NODE_ORDER = N.NODE_ORDER+1 WHERE N.KEY_PARENT=C.ID_INTERNAL AND C.CONTENT_ID='c15591a0-fdde-102a-b5ba-0013d3b09da0';

      insert into NODE(KEY_PARENT,KEY_CHILD,NODE_ORDER,VISIBLE,ASCENDING, OJB_CONCRETE_CLASS) values((SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='07749b24-d2fd-43d2-8254-67dae4aa8492'), (SELECT ID_INTERNAL FROM CONTENT WHERE CONTENT_ID='c15591a0-fdde-102a-b5ba-0013d3b09da0'),1,1,1,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode');

 

