<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.event.edition.merge" /></h2>


<p class="mtop2 mbottom05"><strong><bean:message key="label.eventEdition.merge.selected" /></strong></p>
<logic:notEmpty name="mergeBean" property="selectedObjects">
	<fr:form action="/mergeEventEditions.do">
		<html:hidden property="method" value="removeFromMergeList"/>
		<fr:edit id="mergeList" name="mergeBean" 
			type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
			<fr:destination name="input" path="/mergeEventEditions.do?method=prepare"/>
			<fr:layout name="pages">
				<fr:property name="classes" value="tstyle1 mtop05 mbottom0 width50em"/>
				<fr:property name="columnClasses" value="width35em,width5em"/>
				<fr:property name="subSchema" value="event.edition.merge.list"/>
				<fr:property name="paged" value="false"/>
				<fr:property name="buttonLabel" value="button.researchActivity.remove"/>
			</fr:layout>
		</fr:edit>
		<table class="tstyle1 mtop0 width50em bgcolor3">
			<tr>
				<td>
					<html:submit onclick="this.form.method.value='prepareResearchActivityMerge';this.form.submit();"><bean:message key="button.merge"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:notEmpty>


<logic:empty name="mergeBean" property="selectedObjects">
	<p class="mtop05">
		<em><bean:message key="label.eventEdition.merge.no.selected"/></em>
	</p>
</logic:empty>



<p class="mtop2 mbottom05"><strong><bean:message key="label.merge.eventEdition.completeList"/>:</strong></p>
	
	
<fr:form action="/mergeEventEditions.do?method=addToMergeList">
	<fr:edit id="scientificJournalList" name="mergeBean" property="pageContainerBean"
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:destination name="input" path="/mergeEventEditions.do?method=prepare2"/>
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter mbottom05"/>
			<fr:property name="columnClasses" value="width35em,width5em"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="subSchema" value="event.edition.merge.list"/>
			<fr:property name="buttonLabel" value="button.researchActivity.add"/>
			<fr:property name="paged" value="false"/>
		</fr:layout>
	</fr:edit>
	<fr:edit id="mergeListNotVisible" name="mergeBean" visible="false"/>
</fr:form>
<br/>
<br/>
<fr:form action="/editEventMergeEventEditions.do?method=prepare">
	<html:submit><bean:message key="button.back"/></html:submit>
</fr:form>

