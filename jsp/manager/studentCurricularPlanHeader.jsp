<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:form action="/studentsManagement" focus="number">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="show"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedStudentCurricularPlanId" property="selectedStudentCurricularPlanId" value="0"/>

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="property.student.number"/>
			</th>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="property.student.degreeType"/>
			</th>
		</tr>
		<tr>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number" size="5" onchange="this.form.submit();"/>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
			<td class="listClasses">
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType" size="1" onchange="this.form.submit();">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>
		</tr>
	</table>

</html:form>