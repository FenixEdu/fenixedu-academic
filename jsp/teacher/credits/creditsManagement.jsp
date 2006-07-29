<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/creditsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="processForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherOID" property="teacherOID"/>
	<h2 class="infoop"><bean:write name="infoCreditsTeacher" property="infoTeacher.infoPerson.nome"/></h2>
	<table cellpadding="5" cellspacing="1">
		<tr>
			<td class="listClasses" colspan="2"><b><bean:message key="label.tfc.students.number"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.tfcStudentsNumber" property="tfcStudentsNumber" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses" rowspan="2"><b><bean:message key="label.additional.credits"/></b></td>
			<td class="listClasses"><b><bean:message key="label.credits"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.additionalCredits" property="additionalCredits" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses"><b><bean:message key="label.additional.credits.reason	"/></b></td>
			<td class="listClasses"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.additionalCreditsJustification" property="additionalCreditsJustification" rows="2" cols="50"/></td>
		</tr>
	</table>	
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<bean:define id="link">
		/executionCourseShiftsPercentageManager.do?method=show&amp;teacherOID=<bean:write name="infoCreditsTeacher" property="infoTeacher.idInternal"/>
	</bean:define>
	<tiles:insert definition="teacher-professor-ships">
		<tiles:put name="title" value="label.professorships"/>
		<tiles:put name="link" value="<%= link %>"/>
	</tiles:insert>	
</html:form>
