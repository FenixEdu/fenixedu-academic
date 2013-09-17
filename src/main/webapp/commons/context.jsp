<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<logic:present name="<%= PresentationConstants.EXECUTION_PERIOD %>" scope="request">
	<bean:define id="executionPeriod"
				 name="<%= PresentationConstants.EXECUTION_PERIOD %>"
				 toScope="request"
				 scope="request"/>
	<bean:define id="executionPeriodOID"
				 type="java.lang.String"
				 name="executionPeriod"
				 property="externalId"
				 toScope="request"
				 scope="request"/>
</logic:present>