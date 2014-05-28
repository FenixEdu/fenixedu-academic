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
<%@ page isELIgnored="true"%>
<%@page import="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- showGraduationDegreeCurricularPlans.jsp -->

<h2><bean:message key="label.manager.degreeCurricularPlan"
	bundle="MANAGER_RESOURCES" /></h2>
	
<br/>

<logic:present name="degreeCurricularPlans">

<fr:view  name="degreeCurricularPlans" schema="DegreeCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="sortBy" value="degree.name=asc" />
		
		<fr:property name="linkFormat(showPostingRules)"
			value="/postingRules.do?method=showGraduationDegreeCurricularPlanPostingRules&amp;degreeCurricularPlanId=${externalId}" />
		<fr:property name="key(showPostingRules)" value="label.payments.postingRules" />
		<fr:property name="bundle(showPostingRules)" value="MANAGER_RESOURCES" />
		
		
		<fr:property name="linkFormat(showPaymentPlans)"
			value="/postingRules.do?method=showPaymentPlans&amp;degreeCurricularPlanId=${externalId}" />
		<fr:property name="key(showPaymentPlans)" value="label.payments.postingRules.gratuityPaymentPlans" />
		<fr:property name="bundle(showPaymentPlans)" value="MANAGER_RESOURCES" />
					
	</fr:layout>
</fr:view>
</logic:present>
	
<html:link
	action="/postingRules.do?method=prepareCreatePaymentPlan">
	<bean:message key="label.payments.postingRules.createGratuityPaymentPlan" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepareCreateGraduationGratuityPR">
	<bean:message key="label.payments.postingRules.createGraduationGratuityPostingRule" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepareCreateGraduationStandaloneEnrolmentGratuityPR">
	<bean:message key="label.payments.postingRules.createGraduationStandaloneEnrolmentGratuityPostingRule" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepare">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
 