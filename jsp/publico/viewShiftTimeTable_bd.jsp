<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
	 <br />
	<bean:define id="shiftview" name="shift" />
   		<br/>
		<h2>Horário do Turno:  <jsp:getProperty name="shiftview" property="nome"/></h2>
		<app:gerarHorario name="lessonList"/>		
		<br />
