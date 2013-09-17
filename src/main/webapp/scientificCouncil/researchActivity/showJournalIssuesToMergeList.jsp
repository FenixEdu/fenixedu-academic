<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.journal.issue.merge" /></h2>

<div class="infoop2">
	<p><bean:message key="label.merge.journal.issue.instructions" /></p>
</div>

<p class="mtop2 mbottom05"><strong><bean:message key="label.merge.journal.issue.selected" />:</strong></p>
<fr:form action="/mergeJournalIssues.do?method=chooseJournalIssue">
	<fr:edit id="mergeList" name="mergeList" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="journal.issue.merge.list.full"/>
			<fr:property name="buttonLabel" value="button.researchActivity.choose"/>
		</fr:layout>
	</fr:edit>
</fr:form>


<p class="mtop15 mbottom05"><strong><bean:message key="label.merge.journal.issue.new.details" />:</strong></p>
<div class="forminline dinline">
	<fr:form action="/mergeJournalIssues.do">
		<html:hidden property="method" value="mergeResearchActivity"/>
		<fr:edit schema="journal.issue.new.properties" id="researchActivity" name="mergeList" type="net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean">
			<fr:destination name="invalid" path="/mergeJournalIssues.do?method=invalid"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="submit"/></html:submit>
	</fr:form>
	<fr:form action="/mergeJournalIssues.do?method=back">
		<fr:edit id="mergeListNotVisible" name="mergeList" visible="false"/>
		<html:submit><bean:message key="return"/></html:submit>
	</fr:form>
</div>