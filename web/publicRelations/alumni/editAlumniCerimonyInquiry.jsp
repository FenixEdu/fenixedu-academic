<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.editAlumniCerimonyInquiry" bundle="APPLICATION_RESOURCES"/>
</h2>

<bean:define id="url">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>

<fr:form action="<%= url %>">
	<fr:edit id="cerimonyInquiry" name="cerimonyInquiry">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
			<fr:slot name="description" key="label.publicRelationOffice.alumniCerimonyInquiry.description">
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="begin" layout="picker" key="label.publicRelationOffice.alumniCerimonyInquiry.begin"/>
			<fr:slot name="end" layout="picker" key="label.publicRelationOffice.alumniCerimonyInquiry.end"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
	
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.alumniCerimonyInquiry.text"/>:</strong></p>
	
	<div class="mtop1">
		<fr:edit id="cerimonyInquiry-text" name="cerimonyInquiry" slot="text" layout="area">
			<fr:layout name="rich-text">
				<fr:property name="safe" value="true" />
		   		<fr:property name="columns" value="70"/>
		   		<fr:property name="rows" value="15"/>
		   		<fr:property name="config" value="intermediateWithBreakLineInsteadOfParagraph" />
			</fr:layout>				
		</fr:edit>
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
		</p>
	</div>
</fr:form>

