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
	<bean:message key="label.publicRelationOffice.viewAlumniCerimonyInquiry" bundle="APPLICATION_RESOURCES"/>
</h2>


<p>
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.description" bundle="APPLICATION_RESOURCES"/>
</p> 



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


<p class="mtop05"><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.alumniCerimonyInquiry.text"/>:</strong></p>


<logic:notEmpty name="cerimonyInquiry" property="text">
	<div style="border: 1px solid #ddd; padding: 0 10px; background: #fafafa;">
		<bean:write name="cerimonyInquiry" property="text" filter="false"/>
	</div>
</logic:notEmpty>


<logic:empty name="cerimonyInquiry" property="text">
	<p class="mvert05"><em><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.alumniCerimonyInquiry.text.notDefined"/></em></p>
</logic:empty>


<p><html:link page="/alumniCerimony.do?method=editInquiry" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.edit.inquiry"/></html:link></p>




<h3 class="mtop2">
	<bean:message key="label.publicRelationOffice.viewAlumniCerimonyInquiry.answers" bundle="APPLICATION_RESOURCES"/>
</h3>

<logic:empty name="cerimonyInquiry" property="cerimonyInquiryAnswer">
	<p>
		<em><bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.answers.none" bundle="APPLICATION_RESOURCES"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="cerimonyInquiry" property="cerimonyInquiryAnswer">
	<table class="tstyle1 tdcenter thlight mtop0 mbottom05">
		<tr>
			<th>
				<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer" bundle="APPLICATION_RESOURCES"/>
			</th>
			<th>
				<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer.count" bundle="APPLICATION_RESOURCES"/>
			</th>
			<th>
			</th>
		</tr>
		<logic:iterate id="cerimonyInquiryAnswer" name="cerimonyInquiry" property="orderedCerimonyInquiryAnswer">
			<tr>
				<td>
					<bean:write name="cerimonyInquiryAnswer" property="text"/>
				</td>
				<td>
					<bean:size id="answers" name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson"/>
					<%= answers %>
				</td>
				<td>
					<html:link page="/alumniCerimony.do?method=viewInquiryAnswer" paramId="cerimonyInquiryAnswerId" paramName="cerimonyInquiryAnswer" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.view"/></html:link>
					|
					<html:link page="/alumniCerimony.do?method=editInquiryAnswer" paramId="cerimonyInquiryAnswerId" paramName="cerimonyInquiryAnswer" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.edit"/></html:link>
					|
					<html:link page="/alumniCerimony.do?method=deleteInquiryAnswer" paramId="cerimonyInquiryAnswerId" paramName="cerimonyInquiryAnswer" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.delete"/></html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<p class="mtop05">
	<html:link page="/alumniCerimony.do?method=addInquiryAnswer" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId">+ <bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.answer"/></html:link>
</p>

<p>
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.answers.allow.observations" bundle="APPLICATION_RESOURCES"/>?
	<strong>
		<logic:equal name="cerimonyInquiry" property="allowComments" value="true">
			<bean:message key="label.yes" bundle="APPLICATION_RESOURCES"/>
		</logic:equal>
		<logic:notEqual name="cerimonyInquiry" property="allowComments" value="true">
			<bean:message key="label.no" bundle="APPLICATION_RESOURCES"/>
		</logic:notEqual>
	</strong>
	<html:link page="/alumniCerimony.do?method=toggleObservationFlag" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.change"/>
	</html:link>
</p>


<h3 class="mtop2">
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people" bundle="APPLICATION_RESOURCES"/>
</h3>

<logic:empty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<p>
		<em><bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.none" bundle="APPLICATION_RESOURCES"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<p>
		<bean:define id="cerimonyInquiryId" name="cerimonyInquiry" property="externalId"/>
		<html:link
			page="<%= "/alumniCerimony.do?method=exportInfoToExcel&cerimonyInquiryId=" + cerimonyInquiryId.toString() %>">
			<html:image border="0"
				src="<%= request.getContextPath() + "/images/excel.gif"%>"
				altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
		</html:link>
		<bean:size id="personCount" name="cerimonyInquiry" property="cerimonyInquiryPerson"/>
		<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.count" bundle="APPLICATION_RESOURCES" arg0="<%= personCount.toString() %>"/>
		<html:link page="/alumniCerimony.do?method=viewInquiryPeople" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.view"/></html:link>
	</p>
</logic:notEmpty>
<p>
	<html:link page="/alumniCerimony.do?method=prepareAddPeople" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId">+ <bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people"/></html:link>
</p>