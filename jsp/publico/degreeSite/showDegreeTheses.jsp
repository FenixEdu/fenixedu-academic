<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

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

<logic:notEmpty name="years">
	<logic:iterate id="executionYear" name="years" type="net.sourceforge.fenixedu.domain.ExecutionYear">
		<h2 class="greytxt mtop2">
			<fr:view name="executionYear" property="year"/>
		</h2>
		
		<ul style="width: 600px;">
			<logic:iterate id="thesis" name="theses" property="<%= executionYear.getYear() %>" scope="request">
				<bean:define id="thesisId" name="thesis" property="idInternal"/>
				<li>
		 			<fr:view name="thesis" layout="nonNullValues" schema="result.publication.presentation.Thesis">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
		 				<fr:destination name="view.publication" path="<%= "/showDegreeTheses.do?method=showResult&amp;thesisID=" + thesisId + "&amp;degreeID=" + request.getParameter("degreeID")%>"/>
		 			</fr:view> (<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId="+ thesisId %>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
				</li>
			</logic:iterate>
		</ul>
	</logic:iterate>
</logic:notEmpty>

<logic:empty name="years">
	<em>
		<bean:message key="public.degree.information.label.theses.empty.message" bundle="PUBLIC_DEGREE_INFORMATION"/>
	</em>
</logic:empty>
