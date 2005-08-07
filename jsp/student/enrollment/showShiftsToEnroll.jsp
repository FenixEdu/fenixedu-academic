<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<span class="error"><html:errors/></span>
<bean:define id="hoursPattern">HH : mm</bean:define>

<bean:define id="infoLessons" name="infoLessons"/>
<bean:define id="studentId" name="studentId"/>
<bean:define id="classId" name="classId"/>

<bean:define id="infoClasslessons" name="infoClasslessons"/>	
		
<h2 class="redtxt" style="text-align:left"><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome"/></h2>

<div class="px9"><bean:message key="message.shif.type.help" /></div>
<app:gerarHorario name="infoClasslessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
	application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" action="add"/>
	
<br/><br/>
	
<app:gerarHorario name="infoLessons" type="<%= TimeTableType.SHIFT_ENROLLMENT_TIMETABLE %>" studentID="<%= studentId.toString() %>"
	application="<%= request.getContextPath() %>" classID="<%= classId.toString() %>" action="remove"/>
