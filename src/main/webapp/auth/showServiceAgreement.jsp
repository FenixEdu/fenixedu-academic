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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="org.joda.time.DateTime"%>

<style>
@media print
{    
    .no-print, .no-print *
    {
        display: none !important;
    }
}
</style>
<bean:define id="now" type="java.lang.String">
	<%= new DateTime().toString("yyyy/MM/dd HH:mm") %>
</bean:define>

<bean:define id="checksum" name="serviceAgreementChecksum" scope="request" type="java.lang.String"/>

<bean:message key="oauthapps.label.terms.generated.date" arg0="<%= now  %>" bundle="APPLICATION_RESOURCES"/></br>
<bean:message key="oauthapps.label.terms.checksum" arg0="<%=  request.getAttribute("serviceAgreementChecksum").toString() %>" bundle="APPLICATION_RESOURCES"/></br>
</br>
<a class="no-print" href="#" onclick="javascript:window.print();return false;">
	<bean:message key="print" bundle="APPLICATION_RESOURCES"/>
</a>

<div style="margin: 8%;align:center;">
	<bean:write name="serviceAgreement" filter="false"/>
</div>