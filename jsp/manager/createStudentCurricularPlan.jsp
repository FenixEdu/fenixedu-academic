<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="link.manager.studentsManagement"/> - <bean:message key="link.manager.studentsManagement.subtitle.createStudentCurricularPlan"/></h2>
<br />

<jsp:include page="studentCurricularPlanHeader.jsp"/>
<br />

<html:form action="/studentsManagement" focus="number">

	<html:hidden property="method" value="createStudentCurricularPlan"/>
	<html:hidden property="page" value="1"/>

	<bean:define id="number" name="studentCurricularPlanForm" property="number" type="java.lang.String"/>
	<html:hidden property="number" value="<%= number %>"/>
	<bean:define id="degreeType" type="java.lang.String" name="studentCurricularPlanForm" property="degreeType"/>
	<html:hidden property="degreeType"/>

	<table>
		<tr>
			<td>
				<html:select property="degreeCurricularPlanId" size="1">
					<html:options collection="degreeCurricularPlans" property="idInternal" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="studentCurricularPlanState" size="1">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<html:text property="startDate" size="10"/>
			</td>
		</tr>
	</table>

	<br/>

	<html:submit property="submit" styleClass="inputbutton"/>

</html:form>