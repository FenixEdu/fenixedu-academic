<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:present name="executionDegree">
	<table>
		<tr>
			<td colspan="2">
				<strong>
					<bean:write name="<%= SessionConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>
					em
					<bean:write name="<%= SessionConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				</strong>
			</td>
			</tr>
			<tr>
				<td>
					<strong>
						<bean:write name="<%= SessionConstants.CURRICULAR_YEAR %>" property="year"/> Ano
					</strong>
				</td>
				<td>
				<strong>
					<bean:write name="<%= SessionConstants.EXECUTION_PERIOD %>" property="name"/>
					-
					<bean:write name="<%= SessionConstants.EXECUTION_PERIOD %>" property="infoExecutionYear.year"/>
				</strong>				
				</td>
			</tr>
		</table>
</logic:present>
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="nome"/>
</logic:present>