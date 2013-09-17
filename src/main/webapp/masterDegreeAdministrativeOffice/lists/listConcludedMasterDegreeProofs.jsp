<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.concludedMasterDegreeProofList" bundle="ADMIN_OFFICE_RESOURCES"/></h2>

<fr:edit id="degree" name="chooseDegreeAndYearBean"
		 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean"
		 schema="proofs.list.choose.degree">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>
	
<br/><br/>
	
<logic:present name="chooseDegreeAndYearBean" property="degree" >
	<fr:view name="chooseDegreeAndYearBean" property="masterDegreeProofs" schema="thesis.list.showProofs">
		<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>	
	</fr:view>
</logic:present>

