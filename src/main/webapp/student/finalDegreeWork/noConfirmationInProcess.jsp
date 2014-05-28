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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<html:xhtml/>

<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
<h2><bean:message key="title.finalDegreeWork.attribution"/></h2>

<html:form action="/finalDegreeWorkAttribution">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmAttribution"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID" size="1"
				 onchange='this.form.method.value=\'prepare\';this.form.page.value=\'0\';this.form.submit();'>
		<html:option value=""><!-- w3c complient--></html:option>
		<html:options property="externalId"
					  labelProperty="nextYearsYearString"
					  collection="executionYears" />
	</html:select>
	<br/>
	<br/>
</html:form>

<p><em><!-- Error messages go here --><bean:message key="error.message.NoConfirmationInProcessException"/></em></p>
