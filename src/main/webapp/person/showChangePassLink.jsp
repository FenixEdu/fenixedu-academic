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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key ="title.person.changepass" /></h2>


<div class="infoop2 mvert15">
<bean:message key="message.change.password" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="APPLICATION_RESOURCES"/>
</div>

<p><html:link href='<%= "https://id.ist.utl.pt/password/index.php?url=https://fenix.tecnico.ulisboa.pt/privado&istid=" + AccessControl.getPerson().getIstUsername() %>' ><bean:message key="link.change.password" bundle="APPLICATION_RESOURCES"/></html:link></p>	
