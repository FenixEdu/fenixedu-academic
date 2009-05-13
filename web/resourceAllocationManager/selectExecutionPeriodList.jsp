<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:select bundle="HTMLALT_RESOURCES" property="index" size="1">
	<html:options property="value" labelProperty="label" collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>" />
</html:select>
