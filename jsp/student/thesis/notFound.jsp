<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.submission"/></h2>

<p>
	<logic:present name="noEnrolment">
		<em><bean:message key="label.student.thesis.notFound"/></em>
	</logic:present>
	<logic:present name="noThesis">
		<em><bean:message key="label.student.thesis.jury.notFound"/></em>
	</logic:present>
</p>

<logic:notEmpty name="proposal">
	<h3><bean:message key="title.student.thesis.proposal"/></h3>
	
	<fr:view name="proposal" schema="thesis.proposal.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
