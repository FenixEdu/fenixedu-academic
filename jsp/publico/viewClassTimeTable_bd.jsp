<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<bean:define id="component" name="siteView" property="component" />
<bean:define id="lessonList" name="component" property="lessons" />
<bean:define id="executionPeriod" name="component" property="executionPeriod" />
<bean:define id="executionYear" name="executionPeriod" property="executionYear" />
	
<h2 class="greytxt">
	<bean:write name="executionPeriod" property="semester" /><bean:message key="label.ordinal.semester.abbr" />
	<bean:write name="executionYear" property="year" />
</h2>

<h2><bean:message key="property.class" />: <bean:write name="className" /></h2>
<app:gerarHorario name="lessonList"/>
