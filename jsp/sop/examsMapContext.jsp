<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:present name="<%= SessionConstants.INFO_EXECUTION_DEGREE_KEY %>"  >
	<bean:define id="infoDegree" name="<%= SessionConstants.INFO_EXECUTION_DEGREE_KEY %>" property="infoDegreeCurricularPlan.infoDegree"scope="session" />
   	<bean:define id="infoExecutionPeriod" name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" scope="session"/>
	<jsp:getProperty name="infoDegree" property="nome" />
	<br/>
	<jsp:getProperty name="infoExecutionPeriod" property="name"/>
</logic:present>