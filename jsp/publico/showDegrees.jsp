<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>


<h2 align="left"><bean:message key="title.degrees"/></h2>
<ul>
	<logic:iterate id="infoExecutionDegree" name="degreesList" indexId="index">		
		<bean:define id="degreeID" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal" />
		<li>
			<html:link page="<%= "/showDegreeCurricularPlan.do?executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeCurricularPlanId=" + pageContext.findAttribute("degreeID").toString() %>">
				<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.sigla" />&nbsp;
				<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
			</html:link>
		</li>
	</logic:iterate>
</ul>

