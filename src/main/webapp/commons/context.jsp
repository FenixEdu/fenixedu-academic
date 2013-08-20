<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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