<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement"/> - <bean:message bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement.subtitle.createStudentCurricularPlan"/></h2>
<br />

<jsp:include page="studentCurricularPlanHeader.jsp"/>
<br />

<html:form action="/studentsManagement" focus="number">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudentCurricularPlan"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<bean:define id="number" name="studentCurricularPlanForm" property="number" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" value="<%= number %>"/>
	<bean:define id="degreeType" type="java.lang.String" name="studentCurricularPlanForm" property="degreeType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType"/>

	<table>
		<tr>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanId" property="degreeCurricularPlanId" size="1">
					<html:options collection="degreeCurricularPlans" property="idInternal" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.studentCurricularPlanState" property="studentCurricularPlanState" size="1">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startDate" property="startDate" size="10"/>
			</td>
		</tr>
	</table>

	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="submit" styleClass="inputbutton"/>

</html:form>