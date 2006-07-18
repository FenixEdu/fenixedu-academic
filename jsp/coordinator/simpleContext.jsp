<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
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