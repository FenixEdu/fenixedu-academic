<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
	 <br />
	<bean:define id="shiftview" name="<%= SessionConstants.SHIFT_VIEW %>" scope="session"/>
   		<br/>
		<h2>Horário do Turno:  <jsp:getProperty name="shiftview" property="nome"/></h2>
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"/>		
		<br />
