<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
				<strong><bean:message key="label.guider"/>:</strong> <bean:write name="guider" property="person.nome" /><br/>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.guiders" >
			<logic:notEmpty name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.externalGuiders" >
				<logic:iterate id="externalGuider" name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.externalGuiders">
					<strong><bean:message key="label.externalGuider"/>:</strong> <bean:write name="externalGuider" property="person.nome" /><br/>
				</logic:iterate>
			</logic:notEmpty>
		</logic:empty>
		
		<strong><bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:</strong> <bean:write name="masterDegreeThesis" property="activeMasterDegreeThesisDataVersion.dissertationTitle" /><br/>
	</logic:iterate>
</logic:present>
