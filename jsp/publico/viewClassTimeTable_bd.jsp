<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
	 <br /> 

	 <bean:define id="component" name="siteView" property="component" />
	 <bean:define id="lessonList" name="component" property="lessons" />
	
   		<br/>
		<h2><bean:message key="title.class.timetable" /><bean:write name="className" /></h2>
		<app:gerarHorario name="lessonList"/>
		<br />