<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoShiftsPercentageList" name="infoShiftPercentageList" scope="request"/>

<html:form action="/executionCourseShiftsPercentageManager">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="executionCourseInternalCode"/>
	
	<logic:iterate id="infoShiftPercentage" name="infoShiftsPercentageList" indexId="index">
		<bean:write name="infoShiftPercentage" property="shift.nome"/>
		<bean:define id="availablePercentage" name="infoShiftPercentage" property="availablePercentage" />
		<html:multibox property="shiftProfessorships">
			<bean:write name="infoShiftPercentage" property="shift.idInternal"/>
		</html:multibox>

		<html:text property="shiftProfessorshipsPercentages" size="4" value="<%= availablePercentage.toString() %>" />

		<br />
	</logic:iterate>
	<html:submit value="Ola"/>
</html:form>