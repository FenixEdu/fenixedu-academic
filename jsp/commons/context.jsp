<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoExecutionPeriod" %>

<logic:present name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request">
	<bean:define id="executionPeriod"
				 name="<%= SessionConstants.EXECUTION_PERIOD %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionPeriodOID"
				 type="java.lang.Integer"
				 name="executionPeriod"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>

	######### ExecutionPeriodOID= 
	<bean:write name="executionPeriodOID"/>

</logic:present>