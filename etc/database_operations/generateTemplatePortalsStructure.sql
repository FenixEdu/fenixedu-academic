
-- Homepage

select @Homepage:= C.ID_INTERNAL FROM CONTENT C, META_DOMAIN_OBJECT MDO WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.homepage.Homepage';

delete FROM NODE WHERE KEY_PARENT = @Homepage;

insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, ASCENDING, VISIBLE) select 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', @Homepage, C.ID_INTERNAL, '1','1','1' FROM CONTENT C WHERE C.EXECUTION_PATH='/viewHomepage.do?method=show';

update CONTENT C1, CONTENT C2 SET C1.KEY_INITIAL_CONTENT=C2.ID_INTERNAL WHERE C2.EXECUTION_PATH='/viewHomepage.do?method=show' AND C1.ID_INTERNAL=@Homepage;

-- Research

select @Research :=  C.ID_INTERNAL FROM CONTENT C, META_DOMAIN_OBJECT MDO WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ResearchUnitSite';

insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, ASCENDING, VISIBLE) select 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', @Research, C.ID_INTERNAL, '1','1','1' FROM CONTENT C WHERE C.CONTENT_ID='c152da32-fdde-102a-b5ba-0013d3b09da0';

update CONTENT C1, CONTENT C2 SET C1.KEY_INITIAL_CONTENT=C2.ID_INTERNAL WHERE C2.CONTENT_ID='c152da32-fdde-102a-b5ba-0013d3b09da0' AND C1.ID_INTERNAL=@Research;

-- Scientific Area

select @ScientificArea :=  C.ID_INTERNAL FROM CONTENT C, META_DOMAIN_OBJECT MDO WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ScientificAreaSite';

insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, ASCENDING, VISIBLE) select 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', @ScientificArea, C.ID_INTERNAL, '1','1','1' FROM CONTENT C WHERE C.CONTENT_ID='c156ccf0-fdde-102a-b5ba-0013d3b09da0';

update CONTENT C1, CONTENT C2 SET C1.KEY_INITIAL_CONTENT=C2.ID_INTERNAL WHERE C2.CONTENT_ID='c156ccf0-fdde-102a-b5ba-0013d3b09da0' AND C1.ID_INTERNAL=@ScientificArea;

-- Scientific Council

select @ScientificCouncil :=  C.ID_INTERNAL FROM CONTENT C, META_DOMAIN_OBJECT MDO WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.ScientificCouncilSite';

insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, ASCENDING, VISIBLE) select 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', @ScientificCouncil, C.ID_INTERNAL, '1','1','1' FROM CONTENT C WHERE C.CONTENT_ID='c15591a0-fdde-102a-b5ba-0013d3b09da0';

update CONTENT C1, CONTENT C2 SET C1.KEY_INITIAL_CONTENT=C2.ID_INTERNAL WHERE C2.CONTENT_ID='c15591a0-fdde-102a-b5ba-0013d3b09da0' AND C1.ID_INTERNAL=@ScientificCouncil;

-- Pedagogical Council

select @PedagogicalCouncil :=  C.ID_INTERNAL FROM CONTENT C, META_DOMAIN_OBJECT MDO WHERE C.OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal' AND C.KEY_META_DOMAIN_OBJECT=MDO.ID_INTERNAL AND MDO.TYPE='net.sourceforge.fenixedu.domain.PedagogicalCouncilSite';

insert into NODE(OJB_CONCRETE_CLASS, KEY_PARENT, KEY_CHILD, NODE_ORDER, ASCENDING, VISIBLE) select 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode', @PedagogicalCouncil, C.ID_INTERNAL, '1','1','1' FROM CONTENT C WHERE C.CONTENT_ID='c15590ce-fdde-102a-b5ba-0013d3b09da0';

update CONTENT C1, CONTENT C2 SET C1.KEY_INITIAL_CONTENT=C2.ID_INTERNAL WHERE C2.CONTENT_ID='c15590ce-fdde-102a-b5ba-0013d3b09da0' AND C1.ID_INTERNAL=@PedagogicalCouncil;

