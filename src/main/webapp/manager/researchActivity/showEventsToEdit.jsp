<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="title.event.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
	
<fr:form action="/editEvent.do?method=goToPage">
	<table class="tstyle5 thlight thmiddle mtop05">
		<tr>
			<th>
				<fr:edit id="qq" name="pageContainerBean" type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean"
					layout="tabular" schema="page.goto">
					<fr:destination name="input" path="/editEvent.do?method=goToPage"/>
					<fr:layout>
						<fr:property name="classes" value="tstylenone mvert0"/>
					</fr:layout>
				</fr:edit>
				<fr:edit id="page" name="pageContainerBean" visible="false"/>
			</th>
			<td>
				<html:submit><bean:message key="submit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<fr:form action="/editEvent.do?method=choose">
	<fr:edit id="pageContainerBean" name="pageContainerBean"
		type="net.sourceforge.fenixedu.dataTransferObject.PageContainerBean">
		<fr:destination name="input" path="/editEvent.do?method=prepare"/>
		<fr:layout name="pages">
			<fr:property name="classes" value="tstyle1 thcenter mbottom05"/>
			<fr:property name="columnClasses" value="width35em,width5em"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="objectsPerPage" value="20"/>
			<fr:property name="subSchema" value="event.merge.list"/>
			<fr:property name="buttonLabel" value="edit"/>
			<fr:property name="bundle" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</fr:layout>
	</fr:edit>
</fr:form>