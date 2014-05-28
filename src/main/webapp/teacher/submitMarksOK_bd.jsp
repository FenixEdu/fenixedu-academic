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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<bean:define id="submitMarksComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteSubmitMarks"/>
<logic:greaterThan name="submitMarksComponent" property="submited" value="0">
	<p><h2><bean:message key="label.submitMarksNumber.submit" />
	<bean:write name="submitMarksComponent" property="submited"/>
	<bean:message key="label.submitMarksNumber.marks" /></h2></p>
</logic:greaterThan>

<logic:messagesPresent>
	<p><h2><bean:message key="label.errors.notSubmited"/><br/><br/>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>


