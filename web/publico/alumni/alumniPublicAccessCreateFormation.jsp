<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<!-- alumniPublicAccessCreateFormation.jsp -->

<%-- <div class="col_right_alumni"><img src="alumni_reg_01.gif" alt="[Image] Alumni" /></div> --%>


<h1>Alumni</h1>

<div class="alumnilogo">

<h2>
	<bean:message key="label.formation" bundle="ALUMNI_RESOURCES" />
	<span class="color777 fwnormal">
		<bean:message key="label.step.3.3" bundle="ALUMNI_RESOURCES" />
	</span>
</h2>


<logic:present name="formationBean" property="alumni.formations">
	<p class="greytxt">Após terminado o curso efectuou algum(s) curso(s) de formação? Se sim adicione uma ou mais formações. Caso contrário escolha 'Prosseguir'.</p>
</logic:present>


<logic:notEmpty name="formationBean" property="alumni.formations">
<p>
	<b>
		Já inseriu <bean:write name="formationBean" property="size" /> registo(s) de formação.
	</b>
</p>
</logic:notEmpty>


<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<style>
div.forminline form {
display: inline !important;
}
div.inputinline input {
display: inline !important;
}
</style>


<div class="forminline">
	<div class="inputinline">

		<fr:form id="reg_form" action="/alumni.do?method=createFormation">
		
			<fieldset style="margin-bottom: 1em; display: inline;">
		
				<legend><bean:message key="label.formation" bundle="ALUMNI_RESOURCES" /></legend>
				
				<bean:define id="typeSchema" name="formationBean" property="alumniFormation.typeSchema" type="java.lang.String" />
				<bean:define id="institutionSchema" name="formationBean" property="alumniFormation.institutionSchema" type="java.lang.String" />
			
				<fr:edit id="formationBean" name="formationBean" visible="false" />
		
				<fr:edit id="alumniFormationDegree" name="formationBean" property="alumniFormation" schema="<%= typeSchema + ".public" %>" >
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft thlight"/>
						<fr:property name="columnClasses" value=",tderror1"/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
					<fr:destination name="updateFormationTypePostback" path="/alumni.do?method=updateAlumniFormationTypePostback"/>
					<fr:destination name="cancel" path="/alumni.do?method=createFormationError" />
				</fr:edit>
			
					<table class="thleft thlight mvert05">
						<tr>
							<th><bean:message key="label.formation.education.area.public" bundle="ALUMNI_RESOURCES"/></th>
						</tr>
						<tr>
							<td>
								<select name="formationEducationArea">
									<logic:iterate id="alumniEducationArea" name="formationBean" property="alumniFormation.allAreas">
										
										<bean:define id="areaIdInternal" name="alumniEducationArea" property="educationArea.idInternal" />
										<bean:define id="areaCode" name="alumniEducationArea" property="codeIndentationValue" type="java.lang.String" />
										<bean:define id="areaDescription" name="alumniEducationArea" property="educationArea.description" type="java.lang.String" />

										<logic:notEmpty name="alumniEducationArea" property="educationArea.childAreas">
											<optgroup label="<%= areaCode + " " + areaDescription %>" >
											</optgroup>
										</logic:notEmpty>
										
										<logic:empty name="alumniEducationArea" property="educationArea.childAreas">
											<logic:equal name="formationEducationArea" value="<%= areaIdInternal.toString() %>">
												<option value="<%= areaIdInternal %>" selected="selected">
													<%= areaCode  + " " + areaDescription %>
												</option>
											</logic:equal>
											<logic:notEqual name="formationEducationArea" value="<%= areaIdInternal.toString() %>">
												<option value="<%= areaIdInternal %>">
													<%= areaCode + " " + areaDescription %>
												</option>
											</logic:notEqual>
										</logic:empty>

									</logic:iterate>
								</select>
							</td>
						</tr>
					</table>

		
				<fr:edit id="alumniFormationInstitution" name="formationBean" property="alumniFormation" schema="<%= institutionSchema + ".public" %>" >
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft thlight"/>
						<fr:property name="columnClasses" value=",tderror1"/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
					<fr:destination name="updateInstitutionTypePostback" path="/alumni.do?method=updateAlumniFormationInfoPostback" />
					<fr:destination name="cancel" path="/alumni.do?method=createFormationError" />
				</fr:edit>

				<fr:edit id="alumniFormationInfo" name="formationBean" property="alumniFormation" schema="alumni.formation.info.public">
					<fr:layout name="tabular-break">
						<fr:property name="classes" value="thleft thlight"/>
						<fr:property name="columnClasses" value=",tderror1"/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
					<fr:destination name="cancel" path="/alumni.do?method=createFormationError" />
					<fr:destination name="invalid" path="/alumni.do?method=createFormationError" />
				</fr:edit>

				
			</fieldset>	
		
			<div>&nbsp;</div>
		
			<p class="dinline">
				<html:submit>
					<bean:message key="label.create.add.formation" bundle="ALUMNI_RESOURCES" />
				</html:submit>
			</p>
		
		</fr:form>
		
		<fr:form id="alumniFormationForm" action="/alumni.do?method=createFormationNext">
			<fr:edit id="formationBean" name="formationBean" visible="false" />
			<p class="dinline">
			<html:submit>
				<bean:message key="label.continue" bundle="ALUMNI_RESOURCES" />
			</html:submit>
			</p>
		</fr:form>

	</div> <!-- inputinline -->
</div> <!-- forminline -->


<logic:present name="formationBean" property="alumni.formations">
	<logic:iterate id="eachFormation" indexId="index" name="formationBean" property="alumni.formations">
		<bean:define id="formationID" name="eachFormation" property="idInternal" />
		<bean:define id="alumniID" name="eachFormation" property="person.student.alumni.idInternal" />
		<fr:view name="eachFormation" layout="tabular" schema="alumni.formation.list">
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="alumni.formation.list"/>
				<fr:property name="classes" value="tstyle2 thlight thleft mtop15"/>
			</fr:layout>
		</fr:view>
		<html:link page="<%= "/alumni.do?method=deleteFormation&formationId=" + formationID + "&alumniId=" + alumniID %>">
			<bean:message key="label.remove" bundle="ALUMNI_RESOURCES"/> 
		</html:link>
	</logic:iterate>
</logic:present>






