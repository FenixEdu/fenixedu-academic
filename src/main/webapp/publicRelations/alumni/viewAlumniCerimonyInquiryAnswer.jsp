<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.viewAnswer" bundle="APPLICATION_RESOURCES"/></h2>

<p class="mtop15 mbottom05">
	<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer" bundle="APPLICATION_RESOURCES"/>:
	<strong><bean:write name="cerimonyInquiryAnswer" property="text"/></strong>
</p>

<p class="mtop05">
	<bean:message key="label.publicRelationOffice.alumniCerimonyInquiry.answer.count" bundle="APPLICATION_RESOURCES"/>:
	<bean:size id="answers" name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson"/>
	<strong><%= answers %></strong>
</p>

<bean:define id="cerimonyInquiryAnswerOID" name="cerimonyInquiryAnswer" property="externalId"/>
<fr:view name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
		<fr:slot name="person.username" key="label.username"/>
		<fr:slot name="person.name" key="label.name"/>
		<fr:slot name="person.email" key="label.email"/>
		<fr:slot name="person.defaultMobilePhoneNumber" key="label.phone"/>
		<fr:slot name="comment" key="label.observations"/>
	</fr:schema>
	<fr:layout name="tabular-sortable">		
		<fr:property name="classes" value="tstyle1 thlight"/>
		<fr:property name="columnClasses" value="acenter,,"/>
		
		<fr:property name="sortParameter" value="sortBy"/>
        <fr:property name="sortUrl" value="<%= "/alumniCerimony.do?method=viewInquiryAnswer&cerimonyInquiryAnswerId=" + cerimonyInquiryAnswerOID %>" />
	    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "person.name=asc" : request.getParameter("sortBy") %>"/>		
	</fr:layout>
</fr:view>
