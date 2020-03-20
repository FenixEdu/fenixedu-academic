<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="java.util.Locale"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<% 
		Locale locale = I18N.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<% } %>
	<% 
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkDefault %>'><bean:write name="application.name"/> </a> &gt;
	<% } else { %>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= applicationInformationLinkEnglish %>'><bean:write name="application.name"/> </a> &gt;
	<% } %>
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:write name="application.name" bundle="APPLICATION_RESOURCES"/></h1>

<em><bean:message key="message.application.submission.period.ended" bundle="CANDIDATE_RESOURCES"/>.</em>

<p class="mtop15">
	<bean:message key="message.any.question.application.submission" bundle="CANDIDATE_RESOURCES"/>.
</p>

<div class="mtop15" id="contacts"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>
