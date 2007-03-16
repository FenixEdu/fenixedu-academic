<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form action="/createStudent.do">

	<h3 class="mtop15 mbottom025"><bean:message key="label.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:edit id="executionDegree"
			 name="executionDegreeBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean"
			 schema="choose.executionDegree">
		<fr:destination name="degreePostBack" path="/createStudent.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/createStudent.do?method=chooseDegreeCurricularPlanPostBack"/>	
		<fr:destination name="invalid" path="/createStudent.do?method=chooseExecutionDegreeInvalid"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<logic:present name="executionDegreeBean" property="executionDegree">
		<h3 class="mtop1 mbottom025"><bean:message key="label.ingression.short" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:edit name="ingressionInformationBean" id="chooseIngression" schema="ingression.information" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean">				 
			<fr:destination name="agreementPostBack" path="/createStudent.do?method=chooseAgreementPostBack"/>
			<fr:destination name="ingressionPostBack" path="/createStudent.do?method=chooseIngressionPostBack"/>
			<fr:destination name="entryPhasePostBack" path="/createStudent.do?method=chooseEntryPhasePostBack"/>
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</logic:present>		
	
</fr:form>

<logic:present name="choosePersonBean">
	<fr:form action="/createStudent.do?method=choosePerson">
		<fr:edit id="executionDegree" name="executionDegreeBean" visible="false"  />
		<fr:edit name="ingressionInformationBean" id="chooseIngression" visible="false" />
		<h3 class="mtop1 mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:edit id="choosePerson" name="choosePersonBean" schema="choose.person" type="net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean">				 
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<p>
			<html:submit><bean:message key="button.continue" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>		
		</p>
	</fr:form>
</logic:present>	