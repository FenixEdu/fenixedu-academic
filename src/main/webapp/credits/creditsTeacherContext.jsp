<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<p class="infoselected">
	<b>Ano Lectivo :</b> <bean:write name="creditsView" property="infoCredits.infoExecutionPeriod.name"/>
	<br />
	<br />
	<b><bean:message key="label.teacher" /></b> <bean:write name="creditsView" property="infoCredits.infoTeacher.infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="creditsView" property="infoCredits.infoTeacher.teacherId"/>
</p>