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
<%@page import="org.fenixedu.academic.domain.Installation"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key ="link.title.irsDeclaration" /></h2>

<p><em><bean:message key="message.irs.declaration.not.available" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" arg1="<%= Installation.getInstance().getInstituitionalEmailAddress("da") %>" bundle="APPLICATION_RESOURCES" /></em></p>
