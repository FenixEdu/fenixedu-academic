<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><html:errors/></span>

<html:form action="/creditsManagement">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="processForm"/>
	<html:hidden property="teacherOID"/>
	<table width="100%" cellpadding="5" cellspacing="1">
		<tr>
			<td class="listClasses-header" colspan="2"><b><bean:message key="label.tfc.students.number"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text property="tfcStudentsNumber" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses-header" rowspan="2"><b><bean:message key="label.additional.credits"/></b></td>
			<td class="listClasses-header"><b><bean:message key="label.credits"/></b></td>
			<td class="listClasses" style="text-align:left"><html:text property="additionalCredits" size="5"/></td>
		</tr>
		<tr>
			<td class="listClasses-header"><b><bean:message key="label.additional.credits.reason"/></b></td>
			<td class="listClasses" style="text-align:left"><html:textarea property="additionalCreditsJustification" rows="2" cols="60"/></td>
		</tr>
	</table>	
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<br />
	<br />
	<bean:define id="link">
		/executionCourseShiftsPercentageManager.do?method=show&amp;teacherOID=<bean:write name="creditsView" property="infoCredits.infoTeacher.idInternal"/>
	</bean:define>
	<logic:empty name="creditsView" property="infoProfessorshipList">
		<span class="error"><bean:message key="message.teacher.no.professorship"/></span>
	</logic:empty>
	<logic:notEmpty name="creditsView" property="infoProfessorshipList">
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</td>
				<td class="listClasses-header"><bean:message key="label.professorships.name" />
				</td>
			</tr>
			<logic:iterate id="professorship" name="creditsView" property="infoProfessorshipList">
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</html:form>
