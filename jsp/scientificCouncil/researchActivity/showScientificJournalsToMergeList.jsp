<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.scientificJournal.merge" /></h2>
<br />
<br />

<fr:form action="/mergeScientificJournal.do?method=chooseScientificJournal">
	<fr:edit id="mergeList" name="mergeList" 
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter mbottom05"/>
			<fr:property name="paged" value="false"/>
			<fr:property name="subSchema" value="scientific.journal.merge.list.full"/>
			<fr:property name="buttonLabel" value="button.researchActivity.choose"/>
		</fr:layout>
	</fr:edit>
</fr:form>
<br />
<br />
<fr:form action="/mergeScientificJournal.do">
	<html:hidden property="method" value="mergeResearchActivity"/>
	<fr:edit schema="scientific.journal.new.properties" id="scientificJournal" name="mergeList" type="net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean">
		<fr:destination name="invalid" path="/mergeScientificJournal.do?method=invalid"/>
		<fr:destination name="cancel" path="/mergeScientificJournal.do?method=back"/>
		<fr:layout name="tabular">
		</fr:layout>
	</fr:edit>
	<br />
	<br />
	<html:submit><bean:message key="submit"/></html:submit>
	<html:cancel><bean:message key="return"/></html:cancel>
</fr:form>