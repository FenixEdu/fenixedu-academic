<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people.title"/>
</h2>

<bean:define id="url">/alumniCerimony.do?method=addPeople&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>
<bean:define id="urlCancel">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>


<div class="infoop2">
<p><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people.fileDescription"/>:</p>
<pre style="font-size: 12px; padding-left: 20px;">
ist2xxxx
ist2xxxx
ist2xxxx
</pre>
</div>


<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.add.inquiry.people.insertFile"/>:</p>

<fr:edit id="usernameFileBean" name="usernameFileBean" action="<%= url %>">
	<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice.UsernameFileBean">
		<fr:slot name="inputStream" key="label.file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="fileNameSlot" value="filename"/>
			<fr:property name="size" value="30"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlCancel %>"/>
</fr:edit>
