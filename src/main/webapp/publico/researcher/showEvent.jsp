<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.event"/></h2>
 
<fr:view name="event" layout="tabular-nonNullValues" schema="presentEvent">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="event" property="articles">
	<p class="mbottom05"><bean:message key="label.conferenceArticleList" bundle="RESEARCHER_RESOURCES"/>:</p>
	<fr:view name="event" property="articles" schema="presentArticlesInEvent">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="event" property="articles">
	<bean:message key="label.noArticlesAssociated" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>