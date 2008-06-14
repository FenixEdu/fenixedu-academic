<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html xhtml="true" locale="true">
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
				<%= net.sourceforge.fenixedu.domain.degree.DegreeType.BOLONHA_PHD_PROGRAM.toString() %>
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