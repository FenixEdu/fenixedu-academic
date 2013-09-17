<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.payments.postingRules.category"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<p>
	<html:link
		action="/postingRules.do?method=managePostGraduationRules">
		<bean:message	key="label.payments.postingRules.category.postGraduate"
			bundle="MANAGER_RESOURCES" />
	</html:link>
</p>
<p>
	<html:link
		action="/postingRules.do?method=manageGraduationRules">
		<bean:message	key="label.payments.postingRules.category.graduation"
			bundle="MANAGER_RESOURCES" />
	</html:link>
</p>

<p>
	<html:link
		action="/postingRules.do?method=showInsurancePostingRules">
		<bean:message key="label.payments.postingRules.category.insurance"
			bundle="MANAGER_RESOURCES" />
	</html:link></td>	
</p>

<p>
	<html:link
		action="/postingRules.do?method=showFCTScolarshipPostingRules">
		Bolsas da FCT
	</html:link></td>	
</p>
