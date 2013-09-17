<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

	
<logic:present name="executionDegree">
	<table>
		<tr>
			<td colspan="2">
				<strong>
					<bean:write name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.degreeType"/>
					em
					<bean:write name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				</strong>
			</td>
			</tr>
			<tr>
				<td>
					<strong>
						<bean:write name="<%= PresentationConstants.CURRICULAR_YEAR %>" property="year"/> Ano
					</strong>
				</td>
				<td>
				<strong>
					<bean:write name="<%= PresentationConstants.EXECUTION_PERIOD %>" property="name"/>
					-
					<bean:write name="<%= PresentationConstants.EXECUTION_PERIOD %>" property="infoExecutionYear.year"/>
				</strong>				
				</td>
			</tr>
		</table>
</logic:present>
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="nome"/>
</logic:present>