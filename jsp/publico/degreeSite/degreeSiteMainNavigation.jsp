<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject" %>
<%@ page import="net.sourceforge.fenixedu.domain.Degree" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<%
Integer degreeID = (Integer) request.getAttribute("degreeID");
if (degreeID == null) {
	degreeID = Integer.valueOf(request.getParameter("degreeID"));
}

String aditionalParameter = "";
if (request.getAttribute("executionDegreeID") != null) {
    Object executionDegreeID = request.getAttribute("executionDegreeID");
	aditionalParameter = (executionDegreeID != null) ? "&amp;executionDegreeID=" + executionDegreeID.toString() : "";
}

Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeID);
if (degree != null) {
    request.setAttribute("site", degree.getSite());
}

%>

<fr:view name="site" type="net.sourceforge.fenixedu.domain.Site" layout="side-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="<%= request.getContextPath() + "/publico/showDegreeSiteContent.do?method=section" %>"/>
        <fr:property name="contextRelative" value="false"/> <%-- because some parts are implemented with faces --%>
        <fr:property name="contextParam" value="degreeID"/>
    </fr:layout>
</fr:view>
