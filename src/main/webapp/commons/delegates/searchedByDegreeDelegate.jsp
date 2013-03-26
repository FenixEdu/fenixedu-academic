<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="schema" name="schema" type="java.lang.String"/>
<fr:view name="delegateBean" schema="<%= schema %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0"/>
		<fr:property name="columnClasses" value=",width200px nowrap"/>
	</fr:layout>
</fr:view>