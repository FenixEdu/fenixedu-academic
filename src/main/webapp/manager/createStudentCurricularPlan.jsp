<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

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
					<html:options collection="degreeCurricularPlans" property="externalId" labelProperty="label"/>
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