<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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

<fr:view name="cerimonyInquiryAnswer" property="cerimonyInquiryPerson">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson">
		<fr:slot name="person.username" key="label.username"/>
		<fr:slot name="person.name" key="label.name"/>
		<fr:slot name="person.email" key="label.email"/>
		<fr:slot name="comment" key="label.observations"/>
	</fr:schema>
	<fr:layout name="tabular">		
		<fr:property name="classes" value="tstyle1 thlight"/>
		<fr:property name="columnClasses" value="acenter,,"/>
	</fr:layout>
</fr:view>
