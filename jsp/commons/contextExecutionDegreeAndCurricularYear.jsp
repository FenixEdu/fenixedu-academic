<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoExecutionPeriod" %>

<jsp:include page="context.jsp"/>

<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request">
	<bean:define id="executionDegree"
				 name="<%= SessionConstants.EXECUTION_DEGREE %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionDegreeOID"
				 type="java.lang.Integer"
				 name="executionDegree"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>

<logic:present name="<%= SessionConstants.CURRICULAR_YEAR %>" scope="request">
	<bean:define id="curricularYear"
				 name="<%= SessionConstants.CURRICULAR_YEAR %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="curricularYearOID"
				 type="java.lang.Integer"
				 name="curricularYear"
				 property="idInternal"
				 toScope="request"
				 scope="request"/>
</logic:present>