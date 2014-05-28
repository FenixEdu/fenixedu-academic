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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<html:xhtml/>

<h3>
	<bean:message key="link.inquiries.define.response.period" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>

<fr:form action="/defineResponsePeriods.do?method=define">
	<fr:edit id="inquiryResponsePeriod" name="definitionPeriodBean" schema="net.sourceforge.fenixedu.domain.inquiry.SelectInquiryResponsePeriod.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
			<fr:destination name="postBack" path="/defineResponsePeriods.do?method=prepare"/>
			<fr:destination name="cancel" path="/defineResponsePeriods.do?method=prepare"/>
	   	</fr:layout>	    	
	</fr:edit>
	<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
		<p><span class="success0"><bean:write name="message" /></span></p>
	</html:messages>
	<html:messages id="message" bundle="INQUIRIES_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	<logic:present name="inquiryDoesntExist">
		<br/>
		<p class="warning0"><bean:message key="message.inquiry.doesntExistForSpecifiedParameters" bundle="INQUIRIES_RESOURCES"/></p>
		<br/>
	</logic:present>
	<logic:notPresent name="inquiryDoesntExist">
		<fr:edit id="inquiryResponsePeriodMessage" name="definitionPeriodBean" schema="net.sourceforge.fenixedu.domain.inquiry.InquiryResponsePeriodMessage.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
	 			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:destination name="postBack" path="/defineResponsePeriods.do?method=changeLanguage"/>
				<fr:destination name="cancel" path="/defineResponsePeriods.do?method=prepare"/>
		   	</fr:layout>	    	
		</fr:edit>
	</logic:notPresent>
	<html:submit />
</fr:form>