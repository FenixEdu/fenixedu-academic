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
	</blockquote>
	<fr:view name="cerimonyInquiry" property="cerimonyInquiryPerson">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
			<fr:slot name="person.username" key="label.username"/>
			<fr:slot name="person.name" key="label.name"/>
			<fr:slot name="person.email" key="label.email"/>
		</fr:schema>
		<fr:layout name="tabular">		
			<fr:property name="classes" value="plist mtop05 width100pc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
