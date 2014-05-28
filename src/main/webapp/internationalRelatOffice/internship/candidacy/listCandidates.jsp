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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.apache.struts.action.ActionMessages"%><html:xhtml />

<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jquery/jquery-ui.js"></script>
<link href="${pageContext.request.contextPath}/CSS/jqTheme/ui.all.css" rel="stylesheet">

<h2><bean:message key="label.internationalrelations.internship.candidacy.title"
	bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<span class="error0"><html:errors bundle="INTERNATIONAL_RELATIONS_OFFICE"/></span>    

<logic:present name="search">
	<fr:form action="/internship/internshipCandidacy.do">
		<fr:edit id="search" name="search" schema="internship.candidates.search">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mbottom05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
			</fr:layout>
			<fr:destination name="sessionPostback" path="/internship/internshipCandidacy.do?method=sessionPostback"/>
		</fr:edit>
		<input type="hidden" name="method" />
		<p class="mtop05 mbottom15"><html:submit onclick="this.form.method.value='searchCandidates';">
			<bean:message bundle="COMMON_RESOURCES" key="button.search" />
		</html:submit>
        <html:submit onclick="this.form.method.value='exportToCandidatesToXls';">
            <bean:message bundle="COMMON_RESOURCES" key="link.export.xls" />
        </html:submit></p>
	</fr:form>
</logic:present>

<html:messages id="message" message="true" bundle="INTERNATIONAL_RELATIONS_OFFICE">
	<p><span class="warning0"><bean:write name="message" /></span></p>
</html:messages>

<logic:notEmpty name="candidates">
    <bean:size id="count" name="candidates"/>
    <bean:define id="countStr"><bean:write name="count"/></bean:define>
	<bean:message key="label.internationalrelations.internship.candidacy.count"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" arg0="<%=countStr%>" />

	<fr:view name="candidates" schema="internship.candidates.list">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight" />
            <fr:property name="columnClasses" value=",,,,,,,nowrap" />
			<fr:property name="rowClasses" value="" />
            <fr:property name="link(view)"
                value="/internship/internshipCandidacy.do?method=candidateView" />
            <fr:property name="key(view)"
                value="link.view" />
            <fr:property name="bundle(view)" value="COMMON_RESOURCES" />
            <fr:property name="param(view)" value="candidacy.OID" />
            <fr:property name="order(view)" value="1" />
            <fr:property name="link(edit)"
                value="/internship/internshipCandidacy.do?method=prepareCandidacyEdit" />
            <fr:property name="key(edit)"
                value="link.edit" />
            <fr:property name="bundle(edit)" value="COMMON_RESOURCES" />
            <fr:property name="param(edit)" value="candidacy.OID" />
            <fr:property name="order(edit)" value="2" />
            <fr:property name="link(delete)"
                value="/internship/internshipCandidacy.do?method=prepareCandidacyDelete" />
            <fr:property name="key(delete)"
                value="link.remove" />
            <fr:property name="bundle(delete)" value="COMMON_RESOURCES" />
            <fr:property name="param(delete)" value="candidacy.OID" />
            <fr:property name="order(delete)" value="2" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
