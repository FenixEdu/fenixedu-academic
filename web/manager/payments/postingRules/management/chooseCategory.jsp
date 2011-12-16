<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

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
	
</logic:present>
