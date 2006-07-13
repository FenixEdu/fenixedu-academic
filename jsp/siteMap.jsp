<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<meta name="keywords" content="ensino, ensino superior, universidade, instituto, ciência, instituto superior técnico, investigação e desenvolvimento" />
		<meta name="description" content="O Instituto Superior Técnico é a maior escola de engenharia, ciência e tecnologia em Portugal." />

		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/logdotist.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />

		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> <bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/>
		</title>
	</head>

	<body>
		<div id="container">

			<h1><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> <bean:message key="site.map.title" bundle="GLOBAL_RESOURCES"/></h1>
			<br/>
			<jsp:include page="i18n.jsp"/>

			<div class="breadcumbs"> </div><br/><br/>

			&nbsp;&nbsp;<A href="loginPage.jsp">Login Page</A><br/><br/>

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
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.MASTER_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.INTEGRATED_MASTER_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.ADVANCED_STUDIES_DIPLOMA.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
			
			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.ADVANCED_FORMATION_DIPLOMA.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>

			<bean:define id="degreeType" type="java.lang.String" toScope="request">
				<%= net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType.SPECIALIZATION_DEGREE.toString() %>
			</bean:define>
			<jsp:include page="degreesTable.jsp"/>
		</div>
	</body>
</html:html>