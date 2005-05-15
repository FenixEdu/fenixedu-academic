<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<html:html locale="true">
	<HEAD>
		<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<META name="keywords" content="ensino, ensino superior, universidade, instituto, ciência, instituto superior técnico, investigação e desenvolvimento" />
		<META name="description" content="O Instituto Superior Técnico é a maior escola de engenharia, ciência e tecnologia em Portugal." />

		<LINK rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/logdotist.css" />
		<LINK rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />

		<TITLE>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> <bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/>
		</TITLE>
	</HEAD>

	<BODY>
		<DIV id="container">

			<H1><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> <bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/></H1>
			<BR/>

			<jsp:include page="i18n.jsp"/>

			<DIV class="breadcumbs"> </DIV><BR/><BR/>

			&nbsp;&nbsp;<A href="loginPage.jsp">Login Page</A><BR/><BR/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request"><%= net.sourceforge.fenixedu.domain.degree.DegreeType.DEGREE.toString() %></bean:define>
			<jsp:include page="degreesTable.jsp"/>
			<BR/>
			<bean:define id="degreeType" type="java.lang.String" toScope="request"><%= net.sourceforge.fenixedu.domain.degree.DegreeType.MASTER_DEGREE.toString() %></bean:define>
			<jsp:include page="degreesTable.jsp"/>

		</DIV>
	</BODY>
</html:html>