<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="listThesesActionPath" value="/showDegreeTheses.do" toScope="request"/>
<bean:define id="listThesesContext" value="<%= "degreeID=" + request.getParameter("degreeID") %>" toScope="request"/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<html:link page="<%= "/showDegreeTheses.do?method=showTheses&amp;degreeID=" + request.getAttribute("degreeID").toString() %>">
		<bean:message key="public.degree.information.label.theses"  bundle="PUBLIC_DEGREE_INFORMATION" />
	</html:link>
	&nbsp;&gt;&nbsp;
	<bean:define id="yearId" name="thesis" property="enrolment.executionYear.externalId"/>
	<html:link page="<%= "/showDegreeTheses.do?method=showTheses&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionYearID=" + yearId  %>">
		<fr:view name="thesis" property="enrolment.executionYear.year"/>
	</html:link>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.theses.details"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<h2 class="greytxt mbottom2"><bean:message key="public.degree.information.label.thesis"  bundle="PUBLIC_DEGREE_INFORMATION" /></h2>

<jsp:include flush="true" page="/publico/showThesisDetails.jsp"/>
