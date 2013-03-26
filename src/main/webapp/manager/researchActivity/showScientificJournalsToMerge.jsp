<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="title.scientificJournal.merge" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<p class="mtop2 mbottom05"><strong><bean:message key="label.scientificJournal.merge.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="mergeBean" property="selectedObjects">
	<fr:form action="/mergeScientificJournal.do">
		<html:hidden property="method" value="removeFromMergeList"/>
		<fr:edit id="mergeList" name="mergeBean" 
			type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
			<fr:destination name="input" path="/mergeScientificJournal.do?method=prepare"/>
			<fr:layout name="pages">
				<fr:property name="classes" value="tstyle1 mtop05 mbottom0 width50em"/>
				<fr:property name="columnClasses" value="width35em,width5em"/>
				<fr:property name="subSchema" value="scientific.journal.merge.list"/>
				<fr:property name="paged" value="false"/>
				<fr:property name="buttonLabel" value="button.researchActivity.remove"/>
				<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</fr:layout>
		</fr:edit>
		<table class="tstyle1 mtop0 width50em bgcolor3">
			<tr>
				<td>
					<html:submit onclick="this.form.method.value='prepareResearchActivityMerge';this.form.submit();"><bean:message key="button.merge" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:notEmpty>


<logic:empty name="mergeBean" property="selectedObjects">
	<p class="mtop05">
		<em><bean:message key="label.scientificJournal.merge.no.selected" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>



<p class="mtop2 mbottom05"><strong><bean:message key="label.merge.scientific.journal.completeList" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
	
	
<fr:form action="/mergeScientificJournal.do?method=goToPage">
	<table class="tstyle5 thlight thmiddle mtop05">
		<tr>
			<th>
				<fr:edit id="qq" name="mergeBean"  property="pageContainerBean" type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean"
				layout="tabular" schema="page.goto">
					<fr:destination name="input" path="/mergeScientificJournal.do?method=goToPage"/>
					<fr:layout>
						<fr:property name="classes" value="tstylenone mvert0"/>
					</fr:layout>
				</fr:edit>
				<fr:edit id="page" name="mergeBean" visible="false"/>
			</th>
			<td>
				<html:submit><bean:message key="submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<fr:form action="/mergeScientificJournal.do?method=addToMergeList">
	<fr:edit id="scientificJournalList" name="mergeBean" property="pageContainerBean"
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:destination name="input" path="/mergeScientificJournal.do?method=prepare2"/>
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter mbottom05"/>
			<fr:property name="columnClasses" value="width35em,width5em"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="objectsPerPage" value="20"/>
			<fr:property name="subSchema" value="scientific.journal.merge.list"/>
			<fr:property name="buttonLabel" value="button.researchActivity.add"/>
			<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</fr:layout>
	</fr:edit>
	<fr:edit id="mergeListNotVisible" name="mergeBean" visible="false"/>
</fr:form>



<p class="mtop2 mbottom025"><em><bean:message key="label.legend" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>:</p>
<table class="mtop0">
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #973; background: #ca6; float:left;"></div></td>
	<td><em style="color: #973;"><bean:message key="label.legend.draftScientificJournals" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></td>
</tr>
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #379; background: #6ac; float:left;"></div></td>
	<td><em style="color: #379;"><bean:message key="label.legend.aprovedScientificJournals" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></td>
</tr>
</table>