<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.regularizations" /></h2>

<logic:present name="regularizationMotives">
	<fr:view name="regularizationMotives" schema="show.regularizationMotives">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder tdleft" />
			<fr:property name="headerClasses" value="acenter" />
		</fr:layout>
	</fr:view>
</logic:present>
