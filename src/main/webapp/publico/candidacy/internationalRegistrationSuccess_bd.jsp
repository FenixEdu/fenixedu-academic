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
<%@page import="org.fenixedu.academic.FenixEduAcademicConfiguration"%>
<%@page import="org.fenixedu.academic.domain.Installation"%>
<%@page import="org.fenixedu.academic.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="java.util.Locale"%>

<html:xhtml/>

<h2><bean:message key ="title.register.user" bundle="APPLICATION_RESOURCES"/></h2>

<div class="infoop2 mvert15">
	<bean:define id="username" name="person" property="username" type="java.lang.String"/>
	<p><bean:message key="message.register.success" bundle="APPLICATION_RESOURCES" arg0="<%= username %>" /></p>

</div>



