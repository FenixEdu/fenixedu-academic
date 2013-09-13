<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<bean:define id="schema" name="schema" type="java.lang.String"/>
<fr:view name="delegateBean" schema="<%= schema %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0"/>
		<fr:property name="rowClasses" value="bold,bold,bold,,,,,,"/>
		<fr:property name="columnClasses" value=",width200px nowrap"/>
	</fr:layout>
</fr:view>
