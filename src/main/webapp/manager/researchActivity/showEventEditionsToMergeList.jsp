<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="title.event.edition.merge" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<div class="infoop2">
	<p><bean:message key="label.merge.event.edition.instructions" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></p>
</div>

<p class="mtop2 mbottom05"><strong><bean:message key="label.merge.event.edition.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
<fr:form action="/mergeEventEditions.do?method=chooseEventEdition">
	<fr:edit id="mergeList" name="mergeList" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="event.edition.merge.list.full"/>
			<fr:property name="buttonLabel" value="button.researchActivity.choose"/>
			<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</fr:layout>
	</fr:edit>
</fr:form>


<p class="mtop15 mbottom05"><strong><bean:message key="label.merge.event.edition.new.details" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
<div class="forminline dinline">
	<fr:form action="/mergeEventEditions.do">
		<html:hidden property="method" value="mergeResearchActivity"/>
		<fr:edit schema="event.edition.new.properties" id="researchActivity" name="mergeList" type="net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean">
			<fr:destination name="invalid" path="/mergeEventEditions.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
	</fr:form>
	<fr:form action="/mergeEventEditions.do?method=back">
		<fr:edit id="mergeListNotVisible" name="mergeList" visible="false"/>
		<html:submit><bean:message key="return" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
	</fr:form>
</div>