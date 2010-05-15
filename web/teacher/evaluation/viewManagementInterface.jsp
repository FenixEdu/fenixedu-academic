<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.management.title"/>
</h2>

<span class="warning0"><!-- Error messages go here --><html:errors /></span>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<pre><span class="warning0"> <bean:write name="message" /> </span></pre>
</html:messages>


<logic:present name="facultyEvaluationProcessCreationBean">
	<fr:form action="/teacherEvaluation.do?method=createFacultyEvaluationProcess">
		<fr:edit id="facultyEvaluationProcessCreationBean" name="facultyEvaluationProcessCreationBean">
			<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessBean">
				<fr:slot name="title" key="label.teacher.evaluation.facultyEvaluationProcess.title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="40"/>
				</fr:slot>
				<fr:slot name="autoEvaluationIntervalStart" key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationIntervalStart" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="autoEvaluationIntervalEnd" key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationIntervalEnd" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="evaluationIntervalStart" key="label.teacher.evaluation.facultyEvaluationProcess.evaluationIntervalStart" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="evaluationIntervalEnd" key="label.teacher.evaluation.facultyEvaluationProcess.evaluationIntervalEnd" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="allowNoEval" key="label.teacher.evaluation.facultyEvaluationProcess.allowNoEval" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
			<fr:destination name="invalid" path="/teacherEvaluation.do?method=prepareCreateFacultyEvaluationProcess"/>
		</fr:edit>
	
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
		</p>
	</fr:form>
</logic:present>

<logic:present name="facultyEvaluationProcessEditnBean">
	<bean:define id="urlEditInvalid">/teacherEvaluation.do?method=prepareEditFacultyEvaluationProcess&amp;facultyEvaluationProcessOID=<bean:write name="facultyEvaluationProcessEditnBean" property="facultyEvaluationProcess.externalId"/></bean:define>
	<fr:form action="/teacherEvaluation.do?method=editFacultyEvaluationProcess">
		<fr:edit id="facultyEvaluationProcessEditnBean" name="facultyEvaluationProcessEditnBean">
			<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessBean">
				<fr:slot name="title" key="label.teacher.evaluation.facultyEvaluationProcess.title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="40"/>
				</fr:slot>
				<fr:slot name="autoEvaluationIntervalStart" key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationIntervalStart" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="autoEvaluationIntervalEnd" key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationIntervalEnd" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="evaluationIntervalStart" key="label.teacher.evaluation.facultyEvaluationProcess.evaluationIntervalStart" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="evaluationIntervalEnd" key="label.teacher.evaluation.facultyEvaluationProcess.evaluationIntervalEnd" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="allowNoEval" key="label.teacher.evaluation.facultyEvaluationProcess.allowNoEval" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
			<fr:destination name="invalid" path="<%= urlEditInvalid %>"/>
		</fr:edit>
	
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.save.button"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="MESSAGING_RESOURCES" key="messaging.cancel.button"/></html:cancel>
		</p>
	</fr:form>
</logic:present>

<logic:present name="facultyEvaluationProcess">
	<h3 class="mbottom05">
		<fr:view name="facultyEvaluationProcess" property="title"/>
	</h3>
	<table class="tstyle2 thlight thleft">
		<tr>
			<th>
				<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" bundle="RESEARCHER_RESOURCES"/>
			</th>
			<td>
				<fr:view name="facultyEvaluationProcess" property="autoEvaluationInterval.start"/>
				-
				<fr:view name="facultyEvaluationProcess" property="autoEvaluationInterval.end"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.evaluationInterval" bundle="RESEARCHER_RESOURCES"/>
			</th>
			<td>
				<fr:view name="facultyEvaluationProcess" property="evaluationInterval.start"/>
				-
				<fr:view name="facultyEvaluationProcess" property="evaluationInterval.end"/>
			</td>
		</tr>
	</table>
	
	
	<ul>
		<li>
			<html:link page="/teacherEvaluation.do?method=prepareEditFacultyEvaluationProcess" paramId="facultyEvaluationProcessOID" paramName="facultyEvaluationProcess" paramProperty="OID">
				<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.edit" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/teacherEvaluation.do?method=prepareUploadEvaluators" paramId="facultyEvaluationProcessOID" paramName="facultyEvaluationProcess" paramProperty="OID">
				<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.upload" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	
	
	<p class="mtop15 mbottom05">
		<bean:size id="count" name="facultyEvaluationProcess" property="teacherEvaluationProcess"/>
		<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.count" bundle="RESEARCHER_RESOURCES"/>:
		<%= count %>
	</p>
	<p class="mvert05">
		<bean:define id="autoEvaluatedCount" name="facultyEvaluationProcess" property="autoEvaluatedCount"/>
		<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.autoEvaluation.locked.count" bundle="RESEARCHER_RESOURCES"/>:
		<%= autoEvaluatedCount %>
	</p>
	<p class="mtop05 mbottom15">
		<bean:define id="evaluatedCount" name="facultyEvaluationProcess" property="evaluatedCount"/>
		<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.evaluation.locked.count" bundle="RESEARCHER_RESOURCES"/>:
		<%= evaluatedCount %>
	</p>
	
	
	<logic:notEmpty name="facultyEvaluationProcess" property="sortedTeacherEvaluationProcess">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.evaluee" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.evaluator" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.teacherEvaluationProcess.coevaluators" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="teacherEvaluationProcess" name="facultyEvaluationProcess" property="sortedTeacherEvaluationProcess">
				<tr>
					<td>
						<bean:write name="teacherEvaluationProcess" property="evaluee.name"/>
					</td>
					<td>
						<logic:present name="teacherEvaluationProcess" property="evaluator">
							<bean:write name="teacherEvaluationProcess" property="evaluator.name"/>
						</logic:present>
					</td>
					<td>
						<bean:write name="teacherEvaluationProcess" property="coEvaluatorsAsString"/>
					</td>
					<td>
						<html:link page="/teacherEvaluation.do?method=viewEvaluation" paramId="evalueeOID" paramName="teacherEvaluationProcess" paramProperty="evaluee.externalId">
							<bean:message key="label.view" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	
	
</logic:present>

<logic:present name="facultyEvaluationProcessSet">
	<p class="mtop15">
		<html:link page="/teacherEvaluation.do?method=prepareCreateFacultyEvaluationProcess">
			<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.create" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</p>
	<logic:empty name="facultyEvaluationProcessSet">
		<p class="mtop1">
			<em>
				<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.none" bundle="RESEARCHER_RESOURCES"/>
			</em>
		</p>
	</logic:empty>
	<logic:notEmpty name="facultyEvaluationProcessSet">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.process" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
					<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.evaluationInterval" bundle="RESEARCHER_RESOURCES"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="facultyEvaluationProcess" name="facultyEvaluationProcessSet">
			<tr>
				<td>
					<fr:view name="facultyEvaluationProcess" property="title"/>
				</td>
				<td>
					<fr:view name="facultyEvaluationProcess" property="autoEvaluationInterval.start"/>
					-
					<fr:view name="facultyEvaluationProcess" property="autoEvaluationInterval.end"/>
				</td>
				<td>
					<fr:view name="facultyEvaluationProcess" property="evaluationInterval.start"/>
					-
					<fr:view name="facultyEvaluationProcess" property="evaluationInterval.end"/>
				</td>
				<td>
					<html:link page="/teacherEvaluation.do?method=viewFacultyEvaluationProcess" paramId="facultyEvaluationProcessOID" paramName="facultyEvaluationProcess" paramProperty="OID">
						<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.view" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
					<logic:present role="MANAGER">
						<html:link page="/teacherEvaluation.do?method=deleteFacultyEvaluationProcess" paramId="facultyEvaluationProcessOID" paramName="facultyEvaluationProcess" paramProperty="OID">
							<bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</logic:present>
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>

<logic:present name="fileUploadBean">
	<h3>
		<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.add.list.of.evaluees" bundle="RESEARCHER_RESOURCES"/>
	</h3>
	<p class="infoop">
		<bean:message key="label.teacher.evaluation.facultyEvaluationProcess.add.list.of.evaluees.instruction" bundle="RESEARCHER_RESOURCES"/>
	</p>
	<bean:define id="urlUploadInvalid">/teacherEvaluation.do?method=viewFacultyEvaluationProcess&facultyEvaluationProcessOID=<bean:write name="fileUploadBean" property="facultyEvaluationProcess.externalId"/></bean:define>
	<fr:edit id="fileUploadBean" name="fileUploadBean" action="/teacherEvaluation.do?method=uploadEvaluators">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FileUploadBean">
			<fr:slot name="inputStream" key="label.file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="30"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlUploadInvalid %>"/>
	</fr:edit>
</logic:present>