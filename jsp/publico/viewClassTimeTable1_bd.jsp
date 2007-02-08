<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType"%>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<bean:define id="institutionUrl" type="java.lang.String">
	<bean:message key="institution.url" bundle="GLOBAL_RESOURCES" />
</bean:define>

<div class="breadcumbs mvert0">
   <a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> 
   <bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES" /><bean:message key="link.institution" bundle="GLOBAL_RESOURCES" /></bean:define> 
    &nbsp;&gt;&nbsp;
    <a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a> 
    <bean:define id="degreeType" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>" property="infoDegree.tipoCurso" /> 
    <bean:define id="infoDegreeCurricularPlan"name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLAN %>" />
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()%>"> <bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" /> </html:link> 
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  %>"> <bean:write name="infoDegreeCurricularPlan" property="name" /> </html:link> 
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/chooseContextDANew.do?method=nextPagePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" +  pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>"> <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes" /> </html:link> 
    &nbsp;&gt;&nbsp;<bean:write name="className" />
</div>



<h1>
	<bean:message bundle="ENUMERATION_RESOURCES"
		name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso.name" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION"
		key="public.degree.information.label.in" /> <bean:write
		name="infoDegreeCurricularPlan" property="infoDegree.nome" />
</h1>

<bean:define id="component" name="siteView" property="component" />
<bean:define id="execPeriod" name="component"
	property="infoExecutionPeriod" />

<h2><span class="greytxt"> <bean:write name="execPeriod"
	property="infoExecutionYear.year" />, <bean:message
	bundle="PUBLIC_DEGREE_INFORMATION"
	key="public.degree.information.label.semester.abbr" /> <bean:write
	name="execPeriod" property="semester" /> </span></h2>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.<%SessionConstants.EXECUTION_PERIOD_OID%>" property="<%SessionConstants.EXECUTION_PERIOD_OID%>"
	value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

<bean:define id="lessonList" name="component" property="lessons" />

<h2><bean:message bundle="PUBLIC_DEGREE_INFORMATION"
	key="public.degree.information.title.class.timetable" /><bean:write
	name="className" /></h2>
<app:gerarHorario name="lessonList" />
