<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>

	<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
		<html:link page="/candidateSection.do"><bean:message key="link.coordinator.candidate"/></html:link>
		<br/>
		<html:link page="/studentSection.do"><bean:message key="link.coordinator.student"/></html:link>
		<br/>
	</logic:equal>
	<logic:notEqual name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
		<html:link forward="equivalenceForCoordinator"><bean:message key="link.coordinator.equivalence"/></html:link>
		<br/>
		<br/>
	</logic:notEqual>
	<html:link forward="backForCoordinator"><bean:message key="link.coordinator.back"/></html:link>
	<br/>
	<br/>
</logic:present>