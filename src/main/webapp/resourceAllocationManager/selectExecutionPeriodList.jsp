<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:select bundle="HTMLALT_RESOURCES" property="index" size="1">
	<html:options property="value" labelProperty="label" collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>" />
</html:select>
