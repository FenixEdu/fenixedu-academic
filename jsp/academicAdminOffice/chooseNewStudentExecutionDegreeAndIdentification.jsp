<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<h2><strong><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<fr:form >	
	<fr:edit id="executionDegree"
			 name="executionDegreeBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean"
			 schema="choose.executionDegree">
		<fr:destination name="degreePostBack" path="/createStudent.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/createStudent.do?method=chooseDegreeCurricularPlanPostBack"/>	
		<fr:destination name="invalid" path="/createStudent.do?method=chooseExecutionDegreeInvalid"/>
		<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<logic:present name="executionDegreeBean" property="executionDegree">
		<br/>
		<fr:edit name="ingressionInformationBean" id="chooseIngression" schema="ingression.information" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean">				 
			<fr:destination name="ingressionPostBack" path="/createStudent.do?method=chooseIngressionPostBack"/>
			<fr:destination name="entryPhasePostBack" path="/createStudent.do?method=chooseEntryPhasePostBack"/>
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
	</logic:present>		
	
</fr:form >

<logic:present name="choosePersonBean">
	<fr:form action="/createStudent.do?method=choosePerson">			
		<fr:edit id="executionDegree" name="executionDegreeBean" visible="false"  />
		<fr:edit name="ingressionInformationBean" id="chooseIngression" visible="false" />
		<br/>
		<fr:edit id="choosePerson" name="choosePersonBean" schema="choose.person" type="net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean">				 
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<br/>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>		
	</fr:form>
</logic:present>	