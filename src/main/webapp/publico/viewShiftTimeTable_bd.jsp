<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
	 <br />
	<bean:define id="shiftview" name="shift" />
   		<br/>
		<h2><bean:message key="title.shift.timetable"/><jsp:getProperty name="shiftview" property="nome"/></h2>
		<app:gerarHorario name="lessonList"/>		
		<br />
