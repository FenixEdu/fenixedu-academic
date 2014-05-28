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
	<bean:message key="label.publicRelationOffice.editAlumniCerimonyInquiry" bundle="APPLICATION_RESOURCES"/>
</h2>

<bean:define id="url">/alumniCerimony.do?method=viewInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>
<bean:define id="urlEdit">/alumniCerimony.do?method=editInquiry&amp;cerimonyInquiryId=<bean:write name="cerimonyInquiry" property="externalId"/></bean:define>

<fr:form action="<%= url %>">
	<fr:edit id="cerimonyInquiry" name="cerimonyInquiry">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry">
			<fr:slot name="description" key="label.publicRelationOffice.alumniCerimonyInquiry.description"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="size" value="50"/>
			</fr:slot>
			<fr:slot name="begin" layout="picker" key="label.publicRelationOffice.alumniCerimonyInquiry.begin"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			<fr:slot name="end" layout="picker" key="label.publicRelationOffice.alumniCerimonyInquiry.end"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= urlEdit %>"/>
	</fr:edit>
	
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.publicRelationOffice.alumniCerimonyInquiry.text"/>:</strong></p>
	
	<div class="mtop1">
		<fr:edit id="cerimonyInquiry-text" name="cerimonyInquiry" slot="text" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:layout name="rich-text">
				<fr:property name="safe" value="true" />
		   		<fr:property name="columns" value="70"/>
		   		<fr:property name="rows" value="15"/>
		   		<fr:property name="config" value="intermediateWithBreakLineInsteadOfParagraph" />
			</fr:layout>
			<fr:destination name="invalid" path="<%= urlEdit %>"/>
		</fr:edit>
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
		</p>
	</div>
</fr:form>

