<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.masterDegreeThesisList" /></h2>

<fr:edit name="chooseDegreeAndYearBean"
		 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean"
		 schema="thesis.list.choose.degree">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<br/><br/>

<logic:present name="masterDegreeThesisCollection" >
	<bean:size name="masterDegreeThesisCollection" id="size" />
	<bean:message key="label.resultsFound" />: <bean:write name="size"/><br/>
	<logic:iterate id="masterDegreeThesis" name="masterDegreeThesisCollection">
		<br/>
		<strong><bean:message key="label.masterDegree.administrativeOffice.number"/>:</strong> <bean:write name="masterDegreeThesis" property="studentCurricularPlan.student.number" /><br/>
		<strong><bean:message key="label.degree.name"/>:</strong> <bean:write name="masterDegreeThesis" property="studentCurricularPlan.degreeCurricularPlan.degree.nome" /><br/>
		
		<logic:notEmpty name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.guiders" >
			<logic:iterate id="guider" name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.guiders">
				<strong><bean:message key="label.guider"/>:</strong> <bean:write name="guider" property="person.name" /><br/>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.guiders" >
			<logic:notEmpty name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.externalGuiders" >
				<logic:iterate id="externalGuider" name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.externalGuiders">
					<strong><bean:message key="label.externalGuider"/>:</strong> <bean:write name="externalGuider" property="person.name" /><br/>
				</logic:iterate>
			</logic:notEmpty>
		</logic:empty>
		
		<strong><bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:</strong> <bean:write name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.dissertationTitle" /><br/>
	</logic:iterate>
</logic:present>
