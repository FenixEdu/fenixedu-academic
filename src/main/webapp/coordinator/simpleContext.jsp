<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
				<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
					<p>
						<strong><bean:message key="label.masterDegree.coordinator.selectedDegree"/></strong> 
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
						<br />
						<strong><bean:message key="label.masterDegree.coordinator.curricularPlan"/></strong>
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.name" />
					</p>
					<logic:present name="infoCurricularCourse">
						<strong><bean:message key="property.aula.disciplina" />:</strong>
						<bean:write name="infoCurricularCourse" property="name"/>
					</logic:present>
				</logic:present>
		</td>
	</tr>
</table>