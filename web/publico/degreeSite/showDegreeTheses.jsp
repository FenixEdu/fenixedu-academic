<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="listThesesActionPath" value="/showDegreeTheses.do" toScope="request"/>
<bean:define id="listThesesContext" value="<%= "degreeID=" + request.getParameter("degreeID") %>" toScope="request"/>
<bean:define id="listThesesSchema" value="degree.thesis.list.filter" toScope="request"/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString() %>"><bean:write name="degree" property="sigla"/></html:link>
	</logic:present>
	&nbsp;&gt;&nbsp;
	<bean:message key="public.degree.information.label.theses"  bundle="PUBLIC_DEGREE_INFORMATION" />
</div>

<!-- COURSE NAME -->
<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
</h1>

<h2 class="greytxt"><bean:message key="public.degree.information.label.theses"  bundle="PUBLIC_DEGREE_INFORMATION" /></h2>

<jsp:include flush="true" page="/publico/showTheses.jsp"/>
