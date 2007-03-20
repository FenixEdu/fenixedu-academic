<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.event.merge" /></h2>
<br />
<br />

<strong><bean:message key="label.event.merge.selected" /></strong>
<logic:notEmpty name="mergeBean" property="selectedObjects">
	<fr:form action="/mergeEvents.do">
		<html:hidden property="method" value="removeFromMergeList"/>
		<fr:edit id="mergeList" name="mergeBean" 
			type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
			<fr:destination name="input" path="/mergeEvents.do?method=prepare"/>
			<fr:layout name="pages">
				<fr:property name="classes" value="tstyle1 thcenter tdcenter mbottom05"/>
				<fr:property name="subSchema" value="event.merge.list"/>
				<fr:property name="paged" value="false"/>
				<fr:property name="buttonLabel" value="button.researchActivity.remove"/>
			</fr:layout>
		</fr:edit>
		<br />
		<html:submit onclick="this.form.method.value='prepareResearchActivityMerge';this.form.submit();"><bean:message key="button.merge"/></html:submit>
	</fr:form>
</logic:notEmpty>
<logic:empty name="mergeBean" property="selectedObjects">
	<br />
	<bean:message key="label.event.merge.no.selected"/>
</logic:empty>

<br />
<br />
<fr:form action="/mergeEvents.do?method=goToPage">
	<table>
		<tr>
			<td>
				<fr:edit id="qq" name="mergeBean"  property="pageContainerBean" type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean"
				layout="tabular" schema="page.goto"/>
				<fr:edit id="page" name="mergeBean" visible="false"/>
			</td>
			<td>
				<html:submit></html:submit>
			</td>
		</tr>
	</table>
</fr:form>
<br />
<fr:form action="/mergeEvents.do?method=addToMergeList">
	<fr:edit id="scientificJournalList" name="mergeBean" property="pageContainerBean"
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:destination name="input" path="/mergeEvents.do?method=prepare2"/>
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter mbottom05"/>
			<fr:property name="objectsPerPage" value="20"/>
			<fr:property name="subSchema" value="event.merge.list"/>
			<fr:property name="buttonLabel" value="button.researchActivity.add"/>
			<fr:property name="sortBy" value="number"/>
		</fr:layout>
	</fr:edit>
	<fr:edit id="mergeListNotVisible" name="mergeBean" visible="false"/>
</fr:form>