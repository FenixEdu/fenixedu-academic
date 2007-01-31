<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<html:xhtml/>

<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<ul class="mtop2 printhidden">
		<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
</logic:present>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<%-- Foto --%>
<div style="float: right;" class="printhidden">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<%-- Registration Details --%>
<logic:notPresent name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>
<logic:present name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<%-- Registration Average and Curricular Year calculations --%>
<logic:notPresent role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<logic:equal name="registration" property="degreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree" value="true">

		<script language="JavaScript">	
		function check(e,v){
			if (e.className == "dnone")
		  	{
			  e.className = "dblock";
			  v.value = "-";
			}
			else {
			  e.className = "dnone";
		  	  v.value = "+";
			}
		}
		</script>

	<div class="mtop1 mbottom15">

	<p class="mbottom05">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<a href="#" class="dnone" id="instructionsButton" onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;">Consultar Média</a>
	</p>

	<div id="instructions" class="dblock mvert0">

	<div class="infoop3">
	
		<logic:equal name="registration" property="concluded" value="true">
			<p class="mvert05"><strong><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="final.degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:write name="registration" property="average"/></p>
		</logic:equal>
		
		<logic:equal name="registration" property="concluded" value="false">
			<%
				final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
				request.setAttribute("studentCurriculum", studentCurriculum);
			
				final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(null);
				request.setAttribute("totalEctsCredits", totalEctsCredits);
				
				final double average = studentCurriculum.getRoundedAverage(null, true);
				request.setAttribute("average", average);
			
				final int curricularYear = studentCurriculum.calculateCurricularYear(null);
				request.setAttribute("curricularYear", curricularYear);
			
				final double sumPiCi = studentCurriculum.getSumPiCi(null);
				request.setAttribute("sumPiCi", sumPiCi);
			
				final double sumPi = studentCurriculum.getSumPi(null);
				request.setAttribute("sumPi", sumPi);
			%>
			<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="average"/></b></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
			<p class="mtop1 mbottom05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:write name="curricularYear"/></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
		</logic:equal>
	</div>
	</div>
	</div>

	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dinline";
	</script>

	</logic:equal>
</logic:notPresent>





<%--
	<div class="infoop3 mtop2">
		<logic:notEmpty name="executionYear">
			<p class="mvert05"><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05">Os cálculos acima baseam-se nas informações na tabela em baixo.</p>
	
			<p class="mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
		
			<p class="mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b></p>
		</logic:notEmpty>
		<logic:empty name="executionYear">
			<bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="all.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<logic:equal name="registration" property="concluded" value="false">
				<p class="mvert05"><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert05">Os cálculos acima baseam-se nas informações na tabela em baixo.</p>
		
				<p class="mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
			
				<p class="mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b></p>
			</logic:equal>
			<logic:equal name="registration" property="concluded" value="true">
				<bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>

				<p class="mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="registration" property="average"/></b></p>
			</logic:equal>
		</logic:empty>
		<logic:notEmpty name="executionYear">
			<bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>.
		</logic:notEmpty>
	</div>
--%>










<%-- Choose Student Curricular Plan form --%>
<html:form action="<%="/viewCurriculum.do?method=prepare&registrationOID=" + registration.getIdInternal()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<p class="mbottom025 printhidden"><strong><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" />:</strong></p>
	<table class="tstyle4 thright thlight mtop025">
		<tr>
			<th><bean:message key="label.studentCurricularPlan.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="studentCPID" onchange='this.form.submit();'> 
					<html:options collection="scpsLabelValueBeanList" property="value" labelProperty="label" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="select" onchange='this.form.submit();' >
					<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<th><bean:message key="organize.by" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="groups" onclick='this.form.submit();'/><bean:message key="groups" bundle="BOLONHA_MANAGER_RESOURCES"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="executionYears" onclick='this.form.submit();'/><bean:message key="label.execution.year" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>
	</table>
</html:form>


<%-- Show Student Curricular Plans --%>
<logic:empty name="selectedStudentCurricularPlans">
	<p>
		<span class="warning0">
			<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</p>
</logic:empty>

<logic:notEmpty name="selectedStudentCurricularPlans">
	<bean:define id="organizedBy" name="organizedBy" scope="request" type="java.lang.String" />
	<bean:define id="enrolmentStateSelectionType" name="enrolmentStateSelectionType" scope="request" type="java.lang.Integer" />	
	<logic:iterate id="studentCurricularPlan" name="selectedStudentCurricularPlans" indexId="index">
		
		<logic:greaterThan name="index" value="0">
			<div class="mvert3"></div>
		</logic:greaterThan>

		<bean:define id="dateFormated">
			<dt:format pattern="dd.MM.yyyy">
				<bean:write name="studentCurricularPlan" property="startDate.time"/>
			</dt:format>
		</bean:define>

		<div class="mvert2 mtop0">
			<p class="mvert05">
				<strong>
					<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:message bundle="ENUMERATION_RESOURCES" name="studentCurricularPlan" property="degreeType.name"/>
				<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/> 
				<bean:write name="studentCurricularPlan" property="degree.name"/>,
				<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/>
				<logic:present name="studentCurricularPlan" property="specialization">
					- <bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
				</logic:present>
			</p>
			<logic:present name="studentCurricularPlan" property="branch">
				<p class="mvert05">
					<strong>
						Grupo: 
					</strong> 
					<bean:write name="studentCurricularPlan" property="branch.name"/>
				</p>
			</logic:present>
			<p class="mvert05">
				<strong>
					<bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:write name="dateFormated"/>
			</p>
		</div>



<%-- inline styles to remove --%>
<style type="text/css">
<%--
.col01 { background: #efe; }
.col02 { background: #eff; }
.col03 { background: #ffe; }
.col04 { background: #eef; }
.col05 { background: #fee; }
.col06 { background: #fde; }
.col07 { background: #efd; }
.col08 { background: #dfe; }
.col09 { background: #edf; }
.col10 { background: #def; }
.col11 { background: #fed; }
.col12 { background: #fcd; }
--%>

.bgcolor01 { background: #fdfdfa; }
.bgcolor02 { background: #fdfdfa; }
.bgcolor03 { background: #fdfdfa; }
.bgcolor04 { background: #fdfdfa; }
.bgcolor05 { background: #fdfdfa; }
.bgcolor06 { background: #fdfdfa; }
.bgcolor07 { background: #fdfdfa; }
.bgcolor08 { background: #fdfdfa; }
.bgcolor09 { background: #fdfdfa; }
.bgcolor10 { background: #f9f9f5; }
.bgcolor11 { background: #fdfdfa; }
.bgcolor12 { background: #fdfdfa; }
.bgcolor13 { background: #fdfdfa; }

</style>


		<bean:define id="initialWidth" value="800px"/>
		<bean:define id="widthDecreasePerLevel" value="10"/>
		<bean:define id="tablesClasses" value="showinfo3 mvert0 prtwidth100pc"/>
		<bean:define id="groupRowClasses" value="bgcolor2"/>
		<bean:define id="groupNameClasses" value="aleft"/>
		<bean:define id="enrolmentClasses" value="
			<!-- Código e Disciplina --> 		col01 bgcolor04 aleft,
			<!-- Curso  --> 					col02 bgcolor05 width5em acenter,
			<!-- Opcional  -->					col03 bgcolor08 width5em acenter,
			<!-- Caixa  -->						col04 bgcolor13 width2em acenter printhidden,
			<!-- Reprovado Não Avaliado  -->	col05 bgcolor09 width8em acenter,
			<!-- Nota  -->						col06 bgcolor10 width2em acenter,
			<!-- Peso  -->						col07 bgcolor11 color888 width2em acenter,
			<!-- ECTS  -->						col08 bgcolor12 color888 width2em acenter,
			<!-- Época Normal -->				col09 bgcolor03 width1p5em acenter,
			<!-- Ano -->						col10 bgcolor06 width5em acenter,
			<!-- Semestre -->					col11 bgcolor07 width5em acenter,
			<!-- Data do Exame -->				col12 bgcolor02 width6em acenter,
			<!-- Pessoa Responsável Nota -->	col13 bgcolor01 width3em acenter
		"/>

		<fr:edit name="studentCurricularPlan">
			<fr:layout>
				<fr:property name="organizedBy" value="<%=organizedBy%>"/>
				<fr:property name="initialWidth" value="<%=initialWidth%>"/>
				<fr:property name="widthDecreasePerLevel" value="<%=widthDecreasePerLevel%>"/>
				<fr:property name="tablesClasses" value="<%=tablesClasses%>"/>
				<fr:property name="groupRowClasses" value="<%=groupRowClasses%>"/>
				<fr:property name="groupNameClasses" value="<%=groupNameClasses%>"/>
				<fr:property name="enrolmentClasses" value="<%=enrolmentClasses%>"/>
				<fr:property name="enrolmentStateSelectionType" value="<%=enrolmentStateSelectionType.toString()%>"/>
			</fr:layout>
		</fr:edit>

	</logic:iterate>


<p class="mtop2 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
<div style="width: 250px; float: left;">
    <e:labelValues id="enrolmentEvaluationTypes" enumeration="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType" />
	<logic:iterate id="enrolmentEvaluationType" name="enrolmentEvaluationTypes" type="LabelValueBean">
		<p class="mvert05"><em><bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue() + ".acronym"%>" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue()%>" bundle="ENUMERATION_RESOURCES"/></em></p>
	</logic:iterate>
</div>
<div class="cboth"></div>

</logic:notEmpty>
