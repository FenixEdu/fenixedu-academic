<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message key="label.publicRelationOffice.alumniCerimony.inquiries" bundle="APPLICATION_RESOURCES"/>
</h2>

<p>
	<html:link page="/alumniCerimony.do?method=createNewInquiry">+ <bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.create.new.inquiry"/></html:link>
</p>

<logic:empty name="cerimonyInquirySet">
	<p class="mtop1"><em><bean:message key="label.publicRelationOffice.alumniCerimony.inquiries.none" bundle="APPLICATION_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="cerimonyInquirySet">
	<fr:view name="cerimonyInquirySet">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
			<fr:slot name="description" key="label.publicRelationOffice.alumniCerimonyInquiry.description"/>
			<fr:slot name="begin" key="label.publicRelationOffice.alumniCerimonyInquiry.begin"/>
			<fr:slot name="end" key="label.publicRelationOffice.alumniCerimonyInquiry.end"/>
		</fr:schema>
		<fr:layout name="tabular">		
			<fr:property name="classes" value="tstyle1"/>

			<fr:property name="link(view)" value="/alumniCerimony.do?method=viewInquiry"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="externalId/cerimonyInquiryId"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(delete)" value="/alumniCerimony.do?method=deleteInquiry"/>
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="param(delete)" value="externalId/cerimonyInquiryId"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>