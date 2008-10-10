<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>"  >
	<bean:define id="infoDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree"scope="request" />
   	<bean:define id="infoExecutionPeriod" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request"/>
	<jsp:getProperty name="infoDegree" property="degree.presentationName" />
	<br/>
	<jsp:getProperty name="infoExecutionPeriod" property="name"/> -
	<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
</logic:present>