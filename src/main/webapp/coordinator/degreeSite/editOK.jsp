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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<html:xhtml/>

<h2>
	<bean:message key="link.coordinator.degreeSite.management"/>
</h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p><bean:message key="text.coordinator.degreeSite.editOK"/>
<br />
<br />
<a href="${master_degree.degree.site.fullPath}" target="_blank"><bean:message key="link.coordinator.degreeSite.management"/></a>
