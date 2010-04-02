<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer" bundle="APPLICATION_RESOURCES"/>
	:
	<bean:write name="cerimonyInquiryAnswer" property="text"/>
</h2>

<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer.count" bundle="APPLICATION_RESOURCES"/>
:
<bean:size id="answers" name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson"/>
<%= answers %>

<fr:view name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
		<fr:slot name="person.username" key="label.username"/>
		<fr:slot name="person.name" key="label.name"/>
		<fr:slot name="person.email" key="label.email"/>
	</fr:schema>
	<fr:layout name="tabular">		
		<fr:property name="classes" value="plist mtop05 width100pc"/>
	</fr:layout>
</fr:view>
