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
<%@ page import="net.sourceforge.fenixedu.domain.contacts.PartyContact" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.viewAlumniCerimonyInquiry" bundle="APPLICATION_RESOURCES"/>
</h2>



<fr:view name="cerimonyInquiry">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
		<fr:slot name="description" key="label.publicRelationOffice.alumniCerimonyInquiry.description"/>
		<fr:slot name="begin" key="label.publicRelationOffice.alumniCerimonyInquiry.begin"/>
		<fr:slot name="end" key="label.publicRelationOffice.alumniCerimonyInquiry.end"/>
	</fr:schema>
	<fr:layout name="tabular">		
		<fr:property name="classes" value="tstyle2 thlight thleft"/>
	</fr:layout>
</fr:view>


<h3>
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people" bundle="APPLICATION_RESOURCES"/>
</h3>

<logic:empty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<p>
		<em><bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.none" bundle="APPLICATION_RESOURCES"/></em>
	</p>
</logic:empty>

<bean:define id="cerimonyInquiryId" name="cerimonyInquiry" property="externalId"></bean:define>

<logic:notEmpty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<p class="mvert05">
		<html:link
			page="<%= "/alumniCerimony.do?method=exportInfoToExcel&cerimonyInquiryId=" + cerimonyInquiryId.toString() %>">
			<html:image border="0"
				src="<%= request.getContextPath() + "/images/excel.gif"%>"
				altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
		</html:link>
		<bean:size id="personCount"	name="cerimonyInquiry" property="cerimonyInquiryPerson" />
		<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.count" bundle="APPLICATION_RESOURCES" arg0="<%= personCount.toString() %>" />
		<html:link page="/alumniCerimony.do?method=sendEmail" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="title.sendEmail"/></html:link>
	</p>


	<fr:view name="cerimonyInquiry" property="cerimonyInquiryPerson">
		<fr:schema bundle="APPLICATION_RESOURCES"
			type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
			<fr:slot name="person.username" key="label.username" />
			<fr:slot name="person.name" key="label.name" />
			<fr:slot name="person.defaultPhone.presentationValue" key="label.phone" >
			</fr:slot>
			<fr:slot name="person.email" key="label.email" />
			<fr:slot name="cerimonyInquiryAnswer" layout="null-as-label"
				key="label.publicRelationOffice.alumniCerimony.inquiry.people.answer">
				<fr:property name="label" value=" - " />
				<fr:property name="subLayout" value="values" />
				<fr:property name="subSchema" value="alumni.cerimonyInquiryAnswer" />
			</fr:slot>
			<fr:slot name="comment" key="label.observations"/>
		</fr:schema>
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle1 tdcenter thlight" />
			<fr:property name="sortParameter" value="sortBy"/>
			<fr:property name="sortableSlots" value="person.username, person.name, cerimonyInquiryAnswer" />
            <fr:property name="sortUrl" value='<%= "/alumniCerimony.do?method=viewInquiryPeople&amp;cerimonyInquiryId=" + cerimonyInquiryId.toString() %>'/>
   	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "person.name=asc" : request.getParameter("sortBy") %>"/>
		</fr:layout>
	</fr:view>

	<html:link
		page="<%= "/alumniCerimony.do?method=exportInfoToExcel&cerimonyInquiryId=" + cerimonyInquiryId.toString() %>">
		<html:image border="0"
			src="<%= request.getContextPath() + "/images/excel.gif"%>"
			altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
	</html:link>

</logic:notEmpty>
