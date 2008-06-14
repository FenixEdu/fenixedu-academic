<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="title.scientificCouncil.portalTitle" /></em>
<h2><bean:message key="title.scientificJournal.edit" /></h2>
	
<fr:form action="/editScientificJournal.do?method=goToPage">
	<table class="tstyle5 thlight thmiddle mtop05">
		<tr>
			<th>
				<fr:edit id="qq" name="pageContainerBean" type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean"
					layout="tabular" schema="page.goto">
					<fr:destination name="input" path="/editScientificJournal.do?method=goToPage"/>
					<fr:layout>
						<fr:property name="classes" value="tstylenone mvert0"/>
					</fr:layout>
				</fr:edit>
				<fr:edit id="page" name="pageContainerBean" visible="false"/>
			</th>
			<td>
				<html:submit><bean:message key="submit"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<fr:form action="/editScientificJournal.do?method=choose">
	<fr:edit id="pageContainerBean" name="pageContainerBean"
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:destination name="input" path="/editScientificJournal.do?method=prepare"/>
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter mbottom05"/>
			<fr:property name="columnClasses" value="width35em,width5em aright,"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="objectsPerPage" value="20"/>
			<fr:property name="subSchema" value="scientific.journal.merge.list"/>
			<fr:property name="buttonLabel" value="edit"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<p class="mtop2 mbottom025"><em><bean:message key="label.legend"/>:</em></p>
<table class="mtop0">
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #8f613d; background: #c29470; float:left;"></div></td>
	<td><em style="color: #8f613d;"><bean:message key="label.legend.draftScientificJournals"/></em></td>
</tr>
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #3d868f; background: #70b9c2; float:left;"></div></td>
	<td><em style="color: #3d868f;"><bean:message key="label.legend.aprovedScientificJournals"/></em></td>
</tr>
</table>