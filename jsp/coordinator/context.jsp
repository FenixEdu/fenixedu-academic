<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
	<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
		<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
		<bean:define id="candidates" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT %>" scope="session"/>
		<bean:message key="label.masterDegree.coordinator.selectedDegree"/> 
		<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
		<br>
		<bean:message key="label.masterDegree.coordinator.executionYear"/> 
		<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
		<br>
		<bean:message key="label.masterDegree.coordinator.candidates"/> 
		<bean:write name="candidates" />
	</logic:present>

