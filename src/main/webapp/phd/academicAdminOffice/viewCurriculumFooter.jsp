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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@page import="org.apache.struts.util.LabelValueBean"%>

<div class="print_fsize08">
	<p class="mtop2 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
	<div style="width: 350px; float: left;">
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.minCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.creditsConcluded" bundle="APPLICATION_RESOURCES"/></em></p>	
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.approvedCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.maxCredits" bundle="APPLICATION_RESOURCES"/></em></p>
	    <e:labelValues id="enrolmentEvaluationTypes" enumeration="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType" />
		<logic:iterate id="enrolmentEvaluationType" name="enrolmentEvaluationTypes" type="LabelValueBean">
			<p class="mvert05"><em><bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue() + ".acronym"%>" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue()%>" bundle="ENUMERATION_RESOURCES"/></em></p>
		</logic:iterate>
	</div>
</div>
