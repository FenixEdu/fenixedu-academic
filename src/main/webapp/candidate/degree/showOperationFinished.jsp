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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message  key="label.candidacy.operation.finished.title"/></h2>
<hr/>
<bean:define id="candidacyID" name="candidacy" property="externalId" />
<bean:define id="operationQualifiedName" name="operation" property="type.qualifiedName" />

<br/>


<bean:define id="translatedQualifiedName">
	<bean:message name="operationQualifiedName" bundle="ENUMERATION_RESOURCES"/>
</bean:define>
<bean:message  key="label.candidacy.operation.finished.message" arg0="<%=translatedQualifiedName.toString()%>"/>
<br/>
<br/>

<logic:present name="aditionalInformation">
	<strong><bean:write name="aditionalInformation"/></strong> <br/><br/>
</logic:present>


<bean:message key="label.candidacy.operation.finished.viewCandidacyState.message"/>
<html:link action="<%="/degreeCandidacyManagement.do?method=showCandidacyDetails&candidacyID=" + candidacyID%>">
	<bean:message  key="link.candidacy.here"/>
</html:link>

