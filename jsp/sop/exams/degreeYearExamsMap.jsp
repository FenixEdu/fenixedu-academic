<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<h2><bean:message key="title.exams.calendar"/></h2>
<br/>

<bean:define id="infoExamsMaps" name="<%=SessionConstants.INFO_EXAMS_MAP%>"/>
<logic:iterate id ="infoExamsMap" name="infoExamsMaps">
	<app:generateNewExamsMap name="infoExamsMap" user="sop" mapType="DegreeAndYear"/>
</logic:iterate>