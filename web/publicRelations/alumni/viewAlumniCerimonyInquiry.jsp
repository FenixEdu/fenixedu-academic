<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
			<fr:property name="classes" value="plist mtop05 width100pc"/>
		</fr:layout>
	</fr:view>
	<div>
		<strong>
			<bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.alumniCerimonyInquiry.text"/>:
		</strong>
		<br>
		<bean:write name="cerimonyInquiry" property="text" filter="false"/>
	</div>
	<html:link page="/alumniCerimony.do?method=editInquiry" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.edit.inquiry"/></html:link>

<br/>
<br/>

<h3>
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people" bundle="APPLICATION_RESOURCES"/>
</h3>
<logic:empty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<blockquote>
		<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.none" bundle="APPLICATION_RESOURCES"/>
	</blockquote>
</logic:empty>
<logic:notEmpty name="cerimonyInquiry" property="cerimonyInquiryPerson">
	<blockquote>
		<bean:size id="personCount" name="cerimonyInquiry" property="cerimonyInquiryPerson"/>
		<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.people.count" bundle="APPLICATION_RESOURCES" arg0="<%= personCount.toString() %>"/>
		<html:link page="/alumniCerimony.do?method=viewInquiryPeople" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.view"/></html:link>
	</blockquote>
</logic:notEmpty>
<p>
	<html:link page="/alumniCerimony.do?method=prepareAddPeople" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people"/></html:link>
</p>

<h3>
	<bean:message key="label.publicRelationOffice.viewAlumniCerimonyInquiry.answers" bundle="APPLICATION_RESOURCES"/>
</h3>
<logic:empty name="cerimonyInquiry" property="cerimonyInquiryAnswer">
	<blockquote>
		<bean:message key="label.publicRelationOffice.alumniCerimony.inquiry.answers.none" bundle="APPLICATION_RESOURCES"/>
	</blockquote>
</logic:empty>
<logic:notEmpty name="cerimonyInquiry" property="cerimonyInquiryAnswer">
	<table>
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
<p>
	<html:link page="/alumniCerimony.do?method=addInquiryAnswer" paramId="cerimonyInquiryId" paramName="cerimonyInquiry" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.answer"/></html:link>
</p>
