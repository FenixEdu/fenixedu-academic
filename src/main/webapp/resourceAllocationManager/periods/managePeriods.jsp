<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<jsp:include page="managePeriodsScripts.jsp"/>

		<header class="col-md-8">
			<h3><bean:message key="label.occupation.period.management" bundle="SOP_RESOURCES" />
			<span class="small"><bean:write name="managementBean" property="executionYear.year"/> - <a href="#" data-toggle="modal" data-target="#change-year-modal">
				  <bean:message key="label.change" bundle="APPLICATION_RESOURCES" /></a>
			</span>
			<h3>
		</header>
			<span class="col-md-4">
			<logic:equal value="false" name="managementBean" property="hasNewObject">
				<fr:form action="/periods.do?method=addNewPeriod">
					<fr:edit name="managementBean" visible="false" />
					<html:submit value="Criar novo Período"/>
				</fr:form>
			</logic:equal>
			</span>
		
		<logic:messagesPresent message="true" property="error">
			<br />
			<html:messages id="messages" message="true" bundle="SOP_RESOURCES" property="error">
				<div class="error2"><bean:write name="messages" /></div>
			</html:messages>
			<br />
		</logic:messagesPresent>
		
		<logic:messagesPresent message="true" property="success">
			<br />
			<html:messages id="messages" message="true" bundle="SOP_RESOURCES" property="success">
				<div class="success2"><bean:write name="messages" /></div>
			</html:messages>
			<br />
		</logic:messagesPresent>
		
		
		<div class="all">
		<div class="col-md-8">
				
			<section id="periods">
			

				
				<br />
			
				<div id="periods-filters">
					<span class="classes">
						<input type="checkbox" name="periods-classes" value="" id="periods-classes" checked>
						<label for="periods-classes"><bean:message key="label.occupation.period.type.LESSONS" bundle="SOP_RESOURCES"/> </label>
					</span>
					<span class="exams">
						<input type="checkbox" name="periods-exams" value="" id="periods-exams" checked>
						<label for="periods-exams"><bean:message key="label.occupation.period.type.EXAMS" bundle="SOP_RESOURCES"/></label>
					</span>
					<span class="grades">
						<input type="checkbox" name="periods-grades" value="" id="periods-grades" checked>
						<label for="periods-grades"><bean:message key="label.occupation.period.type.GRADE_SUBMISSION" bundle="SOP_RESOURCES"/></label>
					</span>
					<span class="special-exams">
						<input type="checkbox" name="periods-specialExams" value="" id="periods-specialExams" checked>
						<label for="periods-specialExams"><bean:message key="label.occupation.period.type.EXAMS_SPECIAL_SEASON" bundle="SOP_RESOURCES"/></label>
					</span>
					<span class="special-grades">
						<input type="checkbox" name="periods-specialGrades" value="" id="periods-specialGrades" checked>
						<label for="periods-specialGrades"><bean:message key="label.occupation.period.type.GRADE_SUBMISSION_SPECIAL_SEASON" bundle="SOP_RESOURCES"/></label>
					</span>
					<span class="extraordinary-exams">
						<input type="checkbox" name="periods-extraordinaryExams" value="" id="periods-extraordinaryExams" checked>
						<label for="periods-extraordinaryExams"><bean:message key="label.occupation.period.type.EXAMS_EXTRAORDINARY_SEASON" bundle="SOP_RESOURCES"/></label>
					</span>
					<span class="extraordinary-grades">
						<input type="checkbox" name="periods-extraordinaryGrades" value="" id="periods-extraordinaryGrades" checked>
						<label for="periods-extraordinaryGrades"><bean:message key="label.occupation.period.type.GRADE_SUBMISSION_EXTRAORDINARY_SEASON" bundle="SOP_RESOURCES"/></label>
					</span>
				</div>
				
				<br />
				
				
				
				<div id="periodList">

				<script type="text/javascript">
				
					var selectedTypeValue = {};
				
				</script>
			
				<logic:iterate id="period" name="managementBean" property="periods">

				<bean:define id="id" name="period" property="id" type="java.lang.Integer" />				
				
				<bean:define id="periodType" name="period" property="occupationPeriodType"/>
				
				<bean:define id="newObject" name="period" property="newObject" type="java.lang.Boolean"/>
				
				<div class="editar-periodo">
				<fr:form action="/periods.do?method=managePeriods">
				
				<fr:edit id="managementBean" name="managementBean" visible="false" />
				
				<html:hidden bundle="HTMLALT_RESOURCES" property="periodId" value="<%= id.toString() %>" styleClass="idClass"/>
				
				<html:hidden bundle="HTMLALT_RESOURCES" property="intervals" value="" styleClass="intervals" />

				<html:hidden bundle="HTMLALT_RESOURCES" property="courses" value="" styleClass="courses" />
				
				<script type="text/javascript">
					
					 <%= "selectedTypeValue[" + id + "] = '" + periodType + "';" %>
				
				</script>
				
				<bean:size id="size" name="period" property="references"/>
				
				<div id="period" class="period first-period <%= periodType %> <%= newObject ? "newObject" : "" %>">
					<header id="header">
						<h4><bean:write name="period" property="name"/> <span class="small"><bean:write name="period" property="datesString"/> - 
						<bean:message key="label.Total" bundle="APPLICATION_RESOURCES"/>: <bean:write name="size"/> <bean:message key="label.degrees" bundle="APPLICATION_RESOURCES"/></span>
						</h4>
						<span class="saveButton"><i> - <bean:message key="label.occupation.period.unsavedChanges" bundle="SOP_RESOURCES"/></i></span>
						<a href="javascript:void(0);" class="edit-period"><img src="${pageContext.request.contextPath}/images/iconEditOff.png" /> <bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></a>
					</header>
					
					<div class="period-edit">
						<fr:edit name="period">
							<fr:schema type="org.fenixedu.academic.dto.resourceAllocationManager.OccupationPeriodBean" bundle="SOP_RESOURCES">
							<fr:slot name="occupationPeriodType" key="label.occupation.period.type">
										<fr:property name="defaultOptionHidden" value="true"/>
										<fr:property name="includedValues" value="${managementBean.availablePeriodTypes}"/>
										<fr:property name="onChange" 
											value="<%= "changeType($(this), " + id + ");"%>"/>
									</fr:slot>
									<fr:slot name="semester" layout="menu-select">
										<fr:property name="from" value="possibleSemesters" />
										<fr:property name="defaultText" value="Época Especial" />
									</fr:slot>
							</fr:schema>
						</fr:edit>
						<fieldset class="datas">
							<% boolean first = true; %>
							<logic:iterate id="interval" name="period" property="intervals" type="org.joda.time.Interval">
								<div class="date">
									<div class="data-bloco">
										<label for="data-inicio"><bean:message key="label.beginDate" bundle="APPLICATION_RESOURCES"/></label>
										<input type="text" name="data-inicio" class="startDate" value="<%= interval.getStart().toString("dd/MM/yyyy") %>" readonly="readonly">
									</div>
									<div class="data-bloco">
										<label for="data-fim"><bean:message key="label.endDate" bundle="APPLICATION_RESOURCES"/></label>
										<input type="text" name="data-fim" class="endDate" value="<%= interval.getEnd().toString("dd/MM/yyyy") %>" readonly="readonly">
									</div>
									<span class="symbol add-date"><img src="${pageContext.request.contextPath}/images/iconCreatePeriod.png"/></span>
									<span class="<%="symbol remove-date " + (first ? "hide" : "") %>"><img src="${pageContext.request.contextPath}/images/iconRemovePeriod.png"/></span>
								</div>
								<% first = false; %>
							</logic:iterate>
						</fieldset>
						<fieldset class="botoes">
							<html:submit value="<%= newObject ? "Criar" : "Confirmar" %>" styleClass="confirmar" 
							onclick="<%= newObject ? "return prepareToCreate($(this).parents().eq(3));" : "prepareToSubmit($(this).parents().eq(3));"%>" 
							property="<%= newObject ? "createPeriod" : "editPeriod" %>" disabled="<%= newObject %>"/>
							
							<html:submit value="Cancelar" styleClass="cancelar" 
								onclick="<%= newObject ? "" : "javascript:$(this).parents().eq(1).hide(); return false;" %>" property="removeNewPeriod"/>
						</fieldset>
						
						<logic:equal name="newObject" value="false">
						<div class="links-periodo">
							<span class="duplicate"><a href="#" onclick="duplicatePeriodFunction($(this), ${id}); return false;">
							<bean:message key="label.occupation.period.duplicate" bundle="SOP_RESOURCES"/></a></span>
							<a class="eliminar" onclick="<%= "deletePeriod($(this)," + id + " )"%>" style="color: red"><bean:message key="label.occupation.period.delete" bundle="SOP_RESOURCES"/></a>
						</div>
						</logic:equal>
					</div>
					
					<ul class="courses-list">
						<logic:iterate id="reference" name="period" property="references">
							<bean:define id="course" name="reference" property="executionDegree"/>
							<li>									
							<div id="oid" style="display:none"><bean:write name="course" property="oid"/></div>
							<div id="years" style="display:none"><bean:write name="reference" property="curricularYearsString" /></div>
							<bean:write name="course" property="degree.presentationName"/> <span>- <bean:write name="reference" property="curricularYearsPresentationString"/></span>
							<img src="${pageContext.request.contextPath}/images/iconRemoveOff.png" alt="remove"/>
							</li>
						</logic:iterate>
					</ul>
					<ul class="placeholder-tip">
						<li><span class="small"><bean:message key="<%= "label.occupation.period.dragCourses" + (newObject ? ".new" : "") %>" bundle="SOP_RESOURCES"/></span>
						<html:submit value="Descartar Alterações" styleClass="saveButton" onclick="<%= "this.form.submit;" %>"/>
						<html:submit value="Guardar Alterações" styleClass="saveButton" onclick="<%= "prepareEditPeriod($(this).parents().eq(3), " + id + ");" %>" property="editPeriodCourses" style="margin-right: 8px;"/>
						</li>
					</ul>
				</div><!--period-->
				
				</fr:form>
				</div>
				
				</logic:iterate>				
				</div>
			
			</section>
		</div><!--main-->
		
		<div class="col-md-4">
		<div data-spy="affix" id="sidebar">
			
			<div id="years-filters">
				<h4><bean:message key="label.select.years" bundle="SOP_RESOURCES"/></h4>
				<span>
					<input type="checkbox" name="first-year" value="" id="first-year" checked>
					<label for="first-year"><bean:message key="label.first.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="second-year" value="" id="second-year" checked>
					<label for="second-year"><bean:message key="label.second.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="third-year" value="" id="third-year" checked>
					<label for="third-year"><bean:message key="label.third.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="fourth-year" value="" id="fourth-year" checked>
					<label for="fourth-year"><bean:message key="label.fourth.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="fifth-year" value="" id="fifth-year" checked>
					<label for="fifth-year"><bean:message key="label.fiveth.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="sixth-year" value="" id="sixth-year" checked>
					<label for="sixth-year"><bean:message key="label.sixth.year" bundle="APPLICATION_RESOURCES"/></label>
				</span>
				<span>
					<input type="checkbox" name="all-year" value="" id="all-year" checked>
					<label for="all-year"><bean:message key="label.all.years" bundle="SOP_RESOURCES"/></label>
				</span>
			</div>
			
			<div class="panel-group" id="courses">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#courses" data-target="#collapseFirst" href="#">
								<bean:message key="label.first.cycle" bundle="SOP_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseFirst" class="panel-collapse collapse in">
						<div class="panel-body">
							<logic:iterate id="degree" name="managementBean" property="degrees">
								<logic:equal value="true" name="degree" property="degreeType.firstCycle">
									<logic:equal value="false" name="degree" property="degreeType.secondCycle">
										<div class="draggable_course">
											<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
											<div id="presentationName" style="display:none"><bean:write name="degree" property="degree.presentationName"/></div>
											<div id="availableYears" style="display: none"><bean:write name="degree" property="degreeCurricularPlan.durationInYears"/></div>
											<div id="name"><bean:write name="degree" property="degreeName"/></div>
										</div>
									</logic:equal>
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#courses" data-target="#collapseSecond" href="#">
								<bean:message key="label.second.cycle" bundle="SOP_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseSecond" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="degree" name="managementBean" property="degrees">
								<logic:equal value="false" name="degree" property="degreeType.firstCycle">
									<logic:equal value="true" name="degree" property="degreeType.secondCycle">
										<div class="draggable_course">
											<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
											<div id="presentationName" style="display:none"><bean:write name="degree" property="degree.presentationName"/></div>
											<div id="availableYears" style="display: none"><bean:write name="degree" property="degreeCurricularPlan.durationInYears"/></div>
											<div id="name"><bean:write name="degree" property="degreeName"/></div>
										</div>
									</logic:equal>
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#courses" data-target="#collapseIntegrated" href="#">
								<bean:message key="label.firstAndSecond.cycles" bundle="SOP_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseIntegrated" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="degree" name="managementBean" property="degrees">
								<logic:equal value="true" name="degree" property="degreeType.firstCycle">
									<logic:equal value="true" name="degree" property="degreeType.secondCycle">
										<div class="draggable_course">
											<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
											<div id="presentationName" style="display:none"><bean:write name="degree" property="degree.presentationName"/></div>
											<div id="availableYears" style="display: none"><bean:write name="degree" property="degreeCurricularPlan.durationInYears"/></div>
											<div id="name"><bean:write name="degree" property="degreeName"/></div>
										</div>
									</logic:equal>
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#courses" data-target="#collapseThird" href="#">
								<bean:message key="label.third.cycle" bundle="SOP_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseThird" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="degree" name="managementBean" property="degrees">
								<logic:equal value="true" name="degree" property="degreeType.thirdCycle">
									<div class="draggable_course">
										<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
										<div id="presentationName" style="display:none"><bean:write name="degree" property="degree.presentationName"/></div>
										<div id="availableYears" style="display: none"><bean:write name="degree" property="degreeCurricularPlan.durationInYears"/></div>
										<div id="name"><bean:write name="degree" property="degreeName"/></div>
									</div>							
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#courses" data-target="#collapseOther" href="#">
								<bean:message key="label.other.cycles" bundle="SOP_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseOther" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="degree" name="managementBean" property="degrees">
								<logic:equal value="false" name="degree" property="degreeType.firstCycle">
									<logic:equal value="false" name="degree" property="degreeType.secondCycle">
										<logic:equal value="false" name="degree" property="degreeType.thirdCycle">
											<div class="draggable_course">
												<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
												<div id="presentationName" style="display:none"><bean:write name="degree" property="degree.presentationName"/></div>
												<div id="availableYears" style="display: none"><bean:write name="degree" property="degreeCurricularPlan.durationInYears"/></div>
												<div id="name"><bean:write name="degree" property="degreeName"/></div>
											</div>
										</logic:equal>
									</logic:equal>
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
			</div>
		</div><!--courses-->
		</div><!--sidebar-->
		</div><!--all-->

		<div id="date-prototype" class="date hide">
			<div class="data-bloco">
				<label for="data-inicio"><bean:message key="label.beginDate" bundle="APPLICATION_RESOURCES"/></label>
				<input type="text" name="data-inicio" class="startDate prototype" readonly="readonly">
			</div>
			<div class="data-bloco">
				<label for="data-fim"><bean:message key="label.endDate" bundle="APPLICATION_RESOURCES"/></label>
				<input type="text" name="data-fim" class="endDate prototype" readonly="readonly">
			</div>
			<span class="symbol add-date"><img src="${pageContext.request.contextPath}/images/iconCreatePeriod.png"/></span>
			<span class="symbol remove-date"><img src="${pageContext.request.contextPath}/images/iconRemovePeriod.png"/></span>
		</div>

		<div class="modal fade" id="duplicate-dialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<fr:form action="/periods.do?method=duplicatePeriod">
						<div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					        <h4><bean:message key="label.occupation.period.duplicate"></bean:message></h4>
						</div>
						<div class="modal-body">
							<fr:edit name="managementBean">
								<fr:schema bundle="SOP_RESOURCES" type="org.fenixedu.academic.dto.resourceAllocationManager.PeriodsManagementBean">
									<fr:slot name="newPeriodType" layout="radio" key="label.occupation.period.type" />
								</fr:schema>
							</fr:edit>
							<input type="hidden" name="toDuplicateId" value=""/>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="button.cancel"></bean:message></button>
							<button type="submit" class="btn btn-default"><bean:message key="label.occupation.period.duplicate"></bean:message></button>
						</div>
					</fr:form>
				</div>
			</div>
		</div>

		<div class="modal fade" id="change-year-modal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				        <h4><bean:message key="label.executionYear"></bean:message></h4>
					</div>
					<div class="modal-body">
						<fr:form action="/periods.do?method=managePeriods">
							<fr:edit id="managementBean2" name="managementBean">
								<fr:schema bundle="SOP_RESOURCES" type="org.fenixedu.academic.dto.resourceAllocationManager.PeriodsManagementBean">
									<fr:slot name="executionYear" layout="menu-select-postback">
										<fr:property name="from" value="years"/>
										<fr:property name="format" value="\${year}" />
									</fr:slot>
								</fr:schema>
							</fr:edit>
						</fr:form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="button.cancel"></bean:message></button>
					</div>
				</div>
			</div>

		
		</div>
<script src="<%= request.getContextPath()%>/javaScript/jquery/jquery-ui.js" type="application/javascript"></script>
