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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiryRegistry" property="registration.degree.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiryRegistry" property="executionCourse.nome" /></td>
	</tr>
</table>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.notAnswer" bundle="INQUIRIES_RESOURCES"/></span></h3>

<p><span class="warning0"><bean:message key="label.inquiries.notAnswer.irreversibleAction" bundle="INQUIRIES_RESOURCES"/></span></p>

<p><bean:message key="label.inquiries.notAnswer.reasons" bundle="INQUIRIES_RESOURCES"/>:</p>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<div class="forminline dinline">
	<div class="relative">
		<html:form action="/studentInquiry.do">
			<html:hidden property="method" value="justifyNotAnswered"/>
			<html:hidden property="inquiryRegistryID" />
			
			<p class="mvert05"><html:radio property="notAnsweredJustification" value="UNIT_NOT_ATTENDED" onclick="document.getElementById('textArea').readOnly=true"/><strong><bean:message key="label.inquiries.notAnswer.reasons.unitNotAttended" bundle="INQUIRIES_RESOURCES"/></strong></p>
			<p class="mvert05"><html:radio property="notAnsweredJustification" value="LOW_ASSIDUITY" onclick="document.getElementById('textArea').readOnly=true"/><strong><bean:message key="label.inquiries.notAnswer.reasons.lowAssiduity" bundle="INQUIRIES_RESOURCES"/></strong></p>			
			<p class="mvert05"><html:radio property="notAnsweredJustification" value="OTHER" onclick="document.getElementById('textArea').readOnly=false"/><strong><bean:message key="label.inquiries.notAnswer.reasons.other" bundle="INQUIRIES_RESOURCES"/></strong></p>
			<p class="mtop15 mbottom05"><bean:message key="label.inquiries.notAnswer.reasons.otherJustification" bundle="INQUIRIES_RESOURCES"/>:</p>
			<bean:define id="readonly" value="true"/>
			<logic:present name="textAreaReadOnly">
				<bean:define id="readonly" value="false"/>	
			</logic:present>
			<html:textarea styleId="textArea" property="notAnsweredOtherJustification" cols="60" rows="4" readonly="<%= Boolean.valueOf(readonly) %>"/>
			<br/><br/><br/>
			<html:submit styleClass="bright"><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</html:form>
		<html:form action="/studentInquiry.do">
			<html:hidden property="method" value="showCoursesToAnswer"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</html:form>		
	</div>
</div>