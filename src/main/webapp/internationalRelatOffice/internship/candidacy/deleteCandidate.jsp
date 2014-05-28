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

<html:xhtml />

<h2><bean:message key="label.internationalrelations.internship.candidacy.remove.title"
	bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<logic:present name="candidacy">
	<fr:form action="/internship/internshipCandidacy.do">
		<bean:define id="number" name="candidacy" property="studentNumber" />
		<bean:define id="name" name="candidacy" property="name" />
		<p><bean:message key="label.internationalrelations.internship.candidacy.remove.question"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" arg0="<%=number.toString()%>" arg1="<%=name.toString()%>" /></p>
		<fr:edit name="candidacy" visible="false">
		</fr:edit>
		<input type="hidden" name="method" />
		<p><html:submit onclick="this.form.method.value='candidateDelete';">
			<bean:message bundle="COMMON_RESOURCES" key="button.yes" />
		</html:submit> <html:cancel onclick="this.form.method.value='prepareCandidates';">
			<bean:message bundle="COMMON_RESOURCES" key="button.no" />
		</html:cancel></p>
	</fr:form>
</logic:present>