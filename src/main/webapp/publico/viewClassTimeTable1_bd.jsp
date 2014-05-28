<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType"%>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" />

<bean:define id="institutionUrl" type="java.lang.String">
	<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
</bean:define>

<div class="breadcumbs mvert0">
   <a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> 
   <bean:define id="institutionUrlTeaching" type="java.lang.String"><%= institutionUrl %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES" /></bean:define> 
    &nbsp;&gt;&nbsp;
    <a href="<%= institutionUrlTeaching %>"><bean:message key="public.degree.information.label.education" bundle="PUBLIC_DEGREE_INFORMATION" /></a> 
    <bean:define id="degreeType" name="<%= PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN %>" property="infoDegree.degreeType" /> 
    <bean:define id="infoDegreeCurricularPlan" name="<%= PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN %>" />
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()%>"> <bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" /> </html:link> 
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  %>"> <bean:write name="infoDegreeCurricularPlan" property="name" /> </html:link> 
    &nbsp;&gt;&nbsp; 
    <html:link page="<%= "/chooseContextDANew.do?method=nextPagePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" +  pageContext.findAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>"> <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.classes" /> </html:link> 
    &nbsp;&gt;&nbsp;<fr:view name="schoolClass" property="nome"/>
</div>



<h1>
	<bean:write name="infoDegreeCurricularPlan" property="infoDegree.degree.presentationName" />
</h1>

<bean:define id="component" name="siteView" property="component" />
<bean:define id="execPeriod" name="component"
	property="infoExecutionPeriod" />

<h2><span class="greytxt"> <bean:write name="execPeriod"
	property="infoExecutionYear.year" />, <bean:message
	bundle="PUBLIC_DEGREE_INFORMATION"
	key="public.degree.information.label.semester.abbr" /> <bean:write
	name="execPeriod" property="semester" /> </span></h2>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="<%="hidden." + PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>"
	value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

<bean:define id="lessonList" name="component" property="lessons" />

<app:gerarHorario name="lessonList" />

