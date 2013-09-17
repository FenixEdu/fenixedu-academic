<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<html:xhtml/>

<bean:define id="component" name="siteView" property="component" />
<bean:define id="lessonList" name="component" property="lessons" />
<bean:define id="executionPeriod" name="component" property="infoExecutionPeriod" />
<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear" />
	
<h2 class="greytxt">
	<bean:write name="executionYear" property="year" />,
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.semester.abbr" />
	<bean:write name="executionPeriod" property="semester" />
</h2>

<h2><bean:message key="property.class" />: <bean:write name="className" /></h2>
<app:gerarHorario name="lessonList"/>
