<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.concludedMasterDegreeProofList" bundle="ADMIN_OFFICE_RESOURCES"/></h2>

<fr:edit id="degree" name="chooseDegreeAndYearBean"
		 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean"
		 schema="thesis.list.choose.degree">
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

