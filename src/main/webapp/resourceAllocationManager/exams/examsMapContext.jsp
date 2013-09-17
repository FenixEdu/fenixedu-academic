<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>"  >
	<bean:define id="infoDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree"scope="request" />
   	<bean:define id="infoExecutionPeriod" name="<%= PresentationConstants.EXECUTION_PERIOD %>" scope="request"/>
	<jsp:getProperty name="infoDegree" property="degree.presentationName" />
	<br/>
	<jsp:getProperty name="infoExecutionPeriod" property="name"/> -
	<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
</logic:present>