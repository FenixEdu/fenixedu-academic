<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="net.sourceforge.fenixedu.domain.phd.PhdProgram" %>

<h2><bean:message key="label.manager.degreeCurricularPlan" bundle="MANAGER_RESOURCES" /></h2>
<br/>

<fr:view name="degreeCurricularPlans" schema="DegreeCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="sortBy" value="degree.name=asc" />
		<fr:property name="linkFormat(view)" value="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules&amp;degreeCurricularPlanId=${externalId}" />
		<fr:property name="key(view)" value="label.view" />
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
	</fr:layout>
</fr:view>

<h2><bean:message key="label.manager.degreeCurricularPlan" bundle="MANAGER_RESOURCES" /></h2>
<br/>

<fr:view name="phdPrograms">
	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgram.class.getName() %>">
		<fr:slot name="name" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="sortBy" value="name=asc" />
		
		<fr:link 	label="label.view,APPLICATION_RESOURCES" 
					link="/phdPostingRules.do?method=showPhdProgramPostingRules&amp;phdProgramId=${externalId}" 
					name="view" />
	</fr:layout>		
</fr:view>

<html:link
	action="<%="/postingRules.do?method=prepare"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
