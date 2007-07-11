<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionCourse">
	<h3>
		<bean:write name="executionCourse" property="nome"/>
		(<bean:write name="executionCourse" property="sigla"/>)
	</h3>
</logic:present>

<logic:present name="executionPeriod">
	<p>
		<bean:write name="executionPeriod" property="infoExecutionYear.year"/> - 
		<bean:write name="executionPeriod" property="name"/>
	</p>
</logic:present>
