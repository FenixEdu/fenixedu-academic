<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p class="infoselected">
	<b>Ano Lectivo :</b> <bean:write name="creditsView" property="infoCredits.infoExecutionPeriod.name"/>
	<br />
	<br />
	<b><bean:message key="label.teacher" /></b> <bean:write name="creditsView" property="infoCredits.infoTeacher.infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="creditsView" property="infoCredits.infoTeacher.teacherNumber"/>
</p>