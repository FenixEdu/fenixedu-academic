<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.change.areas"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/changeStudentAreas.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showChangeOfStudentAreasConfirmation"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanID" property="studentCurricularPlanID"/>

	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="message.student.enrollment.help"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.specializationAreaID" property="specializationAreaID">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<bean:define id="specializationAreas" name="infoBranches" property="finalSpecializationAreas"/>
					<html:options collection="specializationAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.secondaryAreaID" property="secondaryAreaID">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<bean:define id="secondaryAreas" name="infoBranches" property="finalSecundaryAreas"/>
					<html:options collection="secondaryAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>

	<br/>
	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.student.modify"/>
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.exit"/>
	</html:cancel>			

</html:form>