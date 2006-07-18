<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><html:errors/></span>

<html:form action="/creditsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="processForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherOID" property="teacherOID"/>
	<table width="100%" cellpadding="5" cellspacing="1">
		<tr>
			<th class="listClasses-header" colspan="2"><b><bean:message key="label.tfc.students.number"/></b></th>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.tfcStudentsNumber" property="tfcStudentsNumber" size="5"/></td>
		</tr>
		<tr>
			<th class="listClasses-header" rowspan="2"><b><bean:message key="label.additional.credits"/></b></th>
			<th class="listClasses-header"><b><bean:message key="label.credits"/></b></th>
			<td class="listClasses" style="text-align:left"><html:text bundle="HTMLALT_RESOURCES" altKey="text.additionalCredits" property="additionalCredits" size="5"/></td>
		</tr>
		<tr>
			<th class="listClasses-header"><b><bean:message key="label.additional.credits.reason"/></b></th>
			<td class="listClasses" style="text-align:left"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.additionalCreditsJustification" property="additionalCreditsJustification" rows="2" cols="60"/></td>
		</tr>
	</table>	
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
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
				<th class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</th>
				<th class="listClasses-header"><bean:message key="label.professorships.name" />
				</th>
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
