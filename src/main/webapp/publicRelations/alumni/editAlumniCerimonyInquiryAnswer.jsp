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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.editAlumniCerimonyInquiryAnswer" bundle="APPLICATION_RESOURCES"/>
</h2>


<bean:define id="url">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiryAnswer" property="cerimonyInquiry.externalId"/></bean:define>
<fr:edit id="cerimonyInquiryAnswer" name="cerimonyInquiryAnswer" action="<%= url %>">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
		<fr:slot name="text" key="label.publicRelationOffice.alumniCerimonyInquiry.answer">
			<fr:property name="size" value="40"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>

