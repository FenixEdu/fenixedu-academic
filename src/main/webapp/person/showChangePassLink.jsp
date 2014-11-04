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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="org.fenixedu.academic.predicate.AccessControl"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key ="title.person.changepass" /></h2>


<div class="infoop2 mvert15">
<bean:message key="message.change.password" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="APPLICATION_RESOURCES"/>
</div>

<p><html:link href='<%= "https://id.ist.utl.pt/password/index.php?url=https://fenix.tecnico.ulisboa.pt/login&istid=" + AccessControl.getPerson().getUsername() %>' ><bean:message key="link.change.password" bundle="APPLICATION_RESOURCES"/></html:link></p>	
