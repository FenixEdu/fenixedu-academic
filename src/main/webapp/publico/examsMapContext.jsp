<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exeDegree" name="component" property="infoExecutionDegree" />
<logic:present name="exeDegree"  >
	<bean:define id="infoDegree" name="exeDegree" property="infoDegreeCurricularPlan.infoDegree" />
	<jsp:getProperty name="infoDegree" property="degree.presentationName" />
	<br/>
	<bean:write name="<%= PresentationConstants.EXECUTION_PERIOD%>" property="name" scope="request"/>
</logic:present>