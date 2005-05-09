<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:form action="/studentsManagement" focus="number">

	<html:hidden property="method" value="show"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="selectedStudentCurricularPlanId" value="0"/>

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="property.student.number"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.student.degreeType"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses">
				<html:text property="number" size="5" onchange="this.form.submit();"/>
			</td>
			<td class="listClasses">
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.DegreeType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="degreeType" size="1" onchange="this.form.submit();">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="infoStudentCurricularPlans">
			<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans" length="1">
				<tr>
					<td colspan="2" class="listClasses">
						<bean:define id="email" name="infoStudentCurricularPlan" property="infoStudent.infoPerson.email" type="java.lang.String"/>
						<a href="mailto:<%= email %>">
							<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
						</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>

</html:form>