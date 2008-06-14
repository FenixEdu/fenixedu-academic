<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizes"/></h2>
	
<fr:view name="prize" schema="prize.view.details">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
	<fr:destination name="view.publication" path="/showResearchResult.do?method=showPublication&resultId=${idInternal}"/> 
	<fr:destination name="view.patent" path="/showResearchResult.do?method=showPatent&resultId=${idInternal}"/> 
</fr:view>