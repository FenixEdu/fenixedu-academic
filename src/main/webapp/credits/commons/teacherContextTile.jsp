<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<tiles:importAttribute />
<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> 
	<bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherId" name="infoTeacher" property="teacherId"/>
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> 
	<bean:write name="teacherId"/>
</p>
