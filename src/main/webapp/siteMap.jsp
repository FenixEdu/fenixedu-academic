<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.nio.charset.Charset"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html xhtml="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="keywords" content="<bean:message key="meta.keywords" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="GLOBAL_RESOURCES"/>" />
		<meta name="description" content="<bean:message key="meta.description" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="GLOBAL_RESOURCES"/>" />

		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/logdotist.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />

		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/><bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/>
		</title>
	</head>

	<body>
		<div id="container">

			<h1><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/><bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/></h1>
			<br/>
			<jsp:include page="i18n.jsp"/>

			<div class="breadcumbs"> </div><br/><br/>

			&nbsp;&nbsp;<html:link href="loginPage.jsp">Login Page</html:link>
<%--
			&nbsp;&nbsp;<A href="#degrees">Degrees</A>
			&nbsp;&nbsp;<A href="#units">Units</A>
			&nbsp;&nbsp;<A href="#people">People</A>
			&nbsp;&nbsp;<A href="#infrastructures">Infra-structures</A>
--%>
			<hr style="margin-top: 2em; margin-bottom: 2em"/>

<%--
			<a name="degrees"></a>
			<h2>&nbsp;&nbsp;&nbsp;&nbsp;Degrees</h2>
			<hr style="margin-top: 2em; margin-bottom: 2em"/>

			<a name="units"></a>
			<h2>&nbsp;&nbsp;&nbsp;&nbsp;Units</h2>
			<hr style="margin-top: 2em; margin-bottom: 2em"/>

			<a name="people"></a>
			<h2>&nbsp;&nbsp;&nbsp;&nbsp;People</h2>
			<hr style="margin-top: 2em; margin-bottom: 2em"/>

			<a name="infrastructures"></a>
			<h2>&nbsp;&nbsp;&nbsp;&nbsp;Infra-structures</h2>
			<hr style="margin-top: 2em; margin-bottom: 2em"/>
--%>

			<!-- PRE-BOLONHA DEGREES -->
			<h2><bean:message key="old.degrees" bundle="GLOBAL_RESOURCES"/></h2>
			<bean:define id="renderBolonha" type="java.lang.String" toScope="request" value="false"/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>

			<hr style="margin-top: 2em; margin-bottom: 2em"/>

			<!-- BOLONHA DEGREES -->
			<h2><bean:message key="bolonha.degrees" bundle="GLOBAL_RESOURCES"/></h2>
			<bean:define id="renderBolonha" type="java.lang.String" toScope="request" value="true"/>
						
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_MASTER_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_SPECIALIZATION_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
		</div>
	</body>
</html:html>