<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<h3>
	Processos de Auditoria criados
</h3>

<logic:notEmpty name="executionCoursesAudits">
	<fr:view name="executionCoursesAudits">
		<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit">
			<fr:slot name="executionCourse.name" key="label.executionCourse.name" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="teacherAuditor.person.name" key="teacher.docente" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="studentAuditor.person.name" key="student" bundle="APPLICATION_RESOURCES"/>
			<fr:slot name="approvedByTeacher" key="label.inquiry.audit.approvedByTeacher" layout="boolean-icon">
				<fr:property name="nullAsFalse" value="true"/>
			</fr:slot>
			<fr:slot name="approvedByStudent" key="label.inquiry.audit.approvedByStudent" layout="boolean-icon">
				<fr:property name="nullAsFalse" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,,acenter, acenter, "/>
			<fr:property name="linkFormat(view)" value="/qucAudit.do?method=viewProcessDetails&executionCourseAuditOID=${externalId}"/>
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="executionCoursesAudits">
	<em>Não existem processos criados.</em>
</logic:empty>

<h3>
	Procurar UC Execução para processo de Auditoria
</h3>

<logic:present name="executionCourseSearchBean">
	<fr:form action="/qucAudit.do?method=searchExecutionCourse">
		
		<fr:edit id="executionCourseSearchBean" name="executionCourseSearchBean">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.ExecutionCourse1st2ndCycleSearchBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="executionPeriod" key="label.executionPeriod" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA$ExecutionSemesterQucProvider" />
					<fr:property name="format" value="${semester}º Semestre ${executionYear.year}" />
				</fr:slot>
				<fr:slot name="executionDegree" key="label.executionDegree" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionDegree1stAnd2ndCycleProviderForExecutionCourseSearchBean" />
					<fr:property name="format" value="${presentationName}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
	        	<fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.submit"/></html:submit>
	</fr:form>
	
	<logic:present name="coursesToAuditAndObserve">		
		<p>Disciplinas para Auditoria e/ou em Observação</p>
		<logic:notEmpty name="coursesToAuditAndObserve">
			<table class="tstyle1">
				<tr>
					<th><bean:message key="label.executionCourse.name" bundle="APPLICATION_RESOURCES"/></th>
					<th></th>
				</tr>
				<logic:iterate id="entrySet" name="coursesToAuditAndObserve">
					<tr>
						<td>
							<bean:write name="entrySet" property="key.nome"/>
							<em>
								(<logic:iterate indexId="iter" id="executionDegree" name="entrySet" property="value" type="net.sourceforge.fenixedu.domain.ExecutionDegree">
									<logic:notEqual name="iter" value="0">
									, 
									</logic:notEqual>
									<bean:write name="executionDegree" property="degree.sigla"/>
								</logic:iterate>)
							</em>
						</td>
						<td>
							<bean:define id="executionCourse" name="entrySet" property="key" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
							<html:link page="/qucAudit.do?method=prepareSelectPersons" paramId="executionCourseOID" 
																paramName="executionCourse" paramProperty="externalId">
								<bean:message key="label.choose" bundle="APPLICATION_RESOURCES"/>
							</html:link>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="coursesToAuditAndObserve">
			<em>Não existem</em>
		</logic:empty>
	</logic:present>
	<logic:present name="executionCourses">
		<p>Disciplinas do curso seleccionado</p>
		<fr:view name="executionCourses">
			<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
				<fr:slot name="nome" key="label.executionCourse.name"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
    	    	<fr:property name="columnClasses" value=",,noborder"/>
				<fr:property name="linkFormat(view)" value="/qucAudit.do?method=prepareSelectPersons&executionCourseOID=${externalId}"/>
				<fr:property name="key(view)" value="label.choose" />
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>
