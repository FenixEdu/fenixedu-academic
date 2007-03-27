<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.event.merge" /></h2>

<div class="infoop2">
	<p><bean:message key="label.merge.event.instructions" /></p>
</div>

<p class="mtop15 mbottom05"><strong><bean:message key="label.merge.event.selected" />:</strong></p>
<fr:form action="/mergeEvents.do?method=chooseEvent">
	<fr:edit id="mergeList" name="mergeList" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 mtop05"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="event.merge.list.full"/>
			<fr:property name="buttonLabel" value="button.researchActivity.choose"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<p class="mtop1 mbottom05"><strong><bean:message key="label.merge.event.new.details" />:</strong></p>
<div class="forminline dinline">
	<fr:form action="/mergeEvents.do">
		<html:hidden property="method" value="mergeResearchActivity"/>
		<fr:edit schema="event.new.properties" id="researchActivity" name="mergeList" type="net.sourceforge.fenixedu.dataTransferObject.MergeEventPageContainerBean">
			<fr:destination name="invalid" path="/mergeEvents.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="submit"/></html:submit>
	</fr:form>
	<fr:form action="/mergeEvents.do?method=back">
		<fr:edit id="mergeListNotVisible" name="mergeList" visible="false"/>
		<html:submit><bean:message key="return"/></html:submit>
	</fr:form>
</div>