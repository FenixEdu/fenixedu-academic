<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.scientificJournal.merge" /></h2>

<div class="infoop2">
	<p><bean:message key="label.merge.scientific.journal.instructions" /></p>
</div>

<p class="mtop15 mbottom05"><strong>Lista de revistas seleccionadas:</strong></p>
<fr:form action="/mergeScientificJournal.do?method=chooseScientificJournal">
	<fr:edit id="mergeList" name="mergeList" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 mtop05"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="scientific.journal.merge.list.full"/>
			<fr:property name="buttonLabel" value="button.researchActivity.choose"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<p class="mtop1 mbottom05"><strong><bean:message key="label.merge.scientific.journal.new.details" />:</strong></p>
<div class="forminline dinline">
	<fr:form action="/mergeScientificJournal.do">
		<html:hidden property="method" value="mergeResearchActivity"/>
		<fr:edit schema="scientific.journal.new.properties" id="scientificJournal" name="mergeList" type="net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean">
			<fr:destination name="invalid" path="/mergeScientificJournal.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="submit"/></html:submit>
	</fr:form>
	<fr:form action="/mergeScientificJournal.do?method=back">
		<fr:edit id="mergeListNotVisible" name="mergeList" visible="false"/>
		<html:submit><bean:message key="return"/></html:submit>
	</fr:form>
</div>