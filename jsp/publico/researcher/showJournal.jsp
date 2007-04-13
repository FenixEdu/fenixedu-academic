<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.journal"/></h2>
	
<fr:view name="journal" layout="tabular-nonNullValues" schema="presentJournal">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thright thlight thtop"/>
		<fr:property name="rowClasses" value="tdbold,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="journal" property="articles">
<p class="mbottom05"><bean:message key="label.articleList" bundle="RESEARCHER_RESOURCES"/>:</p>
<fr:view name="journal" property="articles" schema="presentArticlesInJournal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
		<fr:property name="rowClasses" value=",bgcolorfafafa"/>
		<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>
		<fr:property name="sortBy" value="volume, number"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="journal" property="articles">
	<bean:message key="label.noArticlesAssociated" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>