<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="string" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<logic:present name="homepage">
	<logic:equal name="homepage" property="activated" value="true">

		<h1 id="no">
			<%-- <bean:message bundle="HOMEPAGE_RESOURCES" key="title.homepage.of"/>: --%>
			<bean:write name="homepage" property="ownersName"/>
		</h1>

	<table class="invisible thleft">
		<!-- photo -->
		<logic:equal name="homepage" property="showPhoto" value="true">
        <tr>
            <th></th>
            <td>
                <logic:notEmpty name="homepage" property="person.personalPhoto">
            			<bean:define id="homepageID" name="homepage" property="idInternal"/>
            			<html:img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>" style="padding: 1em 0;" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:notEmpty>
<%--
                <logic:empty name="homepage" property="person.personalPhoto">
                    <html:img src="<%= request.getContextPath() +"/images/photoPlaceHolder.jpg" %>" style="padding: 1em 0;" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
                </logic:empty>
--%>                
            </td>
        </tr>
		</logic:equal>

		<!-- units -->
		<logic:equal name="homepage" property="showUnit" value="true">
		<tr>
			<th><bean:message key="label.homepage.showUnit" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:present name="homepage" property="person.employee.currentWorkingContract.workingUnit">
					<bean:define id="currentUnit" name="homepage" property="person.employee.currentWorkingContract.workingUnit" toScope="request"/>
					<jsp:include page="unitStructure.jsp"/>
				</logic:present>
			</td>
		</tr>
		</logic:equal>
		
		<!-- categories -->
		<logic:equal name="homepage" property="showCategory" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCategory" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<string:capitalizeAllWords>
						<string:lowerCase>
							<bean:write name="homepage" property="person.teacher.category.name.content"/>
						</string:lowerCase>
					</string:capitalizeAllWords>
				</logic:present>
			</logic:present>
			</td>
		</tr>
		</logic:equal>
		

		<!-- research unit -->
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
		<logic:empty name="homepage" property="person.workingResearchUnits"> 
			<logic:present name="homepage" property="person.teacher">
				<logic:present name="homepage" property="person.employee.currentWorkingContract">
					<logic:present name="homepage" property="researchUnitHomepage">
						<logic:present name="homepage" property="researchUnit">
							<bean:define id="researchUnitHomepage" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
							<logic:notEmpty name="researchUnitHomepage">
								<tr>
									<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
									<logic:match name="homepage" property="researchUnitHomepage" value="http://">
										<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
										<td>
											<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
										</td>
									</logic:match>
									<logic:notMatch name="homepage" property="researchUnitHomepage" value="http://">
										<logic:match name="homepage" property="researchUnitHomepage" value="https://">
											<bean:define id="url" type="java.lang.String" name="homepage" property="researchUnitHomepage"/>
											<td>
												<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
											</td>
										</logic:match>
										<logic:notMatch name="homepage" property="researchUnitHomepage" value="https://">
											<td>
												<bean:define id="url" type="java.lang.String">http://<bean:write name="homepage" property="researchUnitHomepage"/></bean:define>
												<%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:write name="homepage" property="researchUnit.content"/></html:link>
											</td>
										</logic:notMatch>
									</logic:notMatch>
								</tr>
							</logic:notEmpty>
						</logic:present>
					</logic:present>
				</logic:present>
			</logic:present>
		 </logic:empty>
		<logic:notEmpty name="homepage" property="person.workingResearchUnits">
			<tr>
				<th><bean:message key="label.homepage.showResearchUnitHomepage" bundle="HOMEPAGE_RESOURCES"/>:</th>
				<td>
					<logic:iterate id="unit" name="homepage" property="person.workingResearchUnits" length="1">
						<fr:view name="unit">
							<fr:layout name="unit-link">
								<fr:property name="unitLayout" value="values"/>
								<fr:property name="unitSchema" value="unit.name"/>
								<fr:property name="targetBlank" value="true"/>
								<fr:property name="parenteShown" value="true"/>
								<fr:property name="separator" value="<br/>"/>
							</fr:layout>
						</fr:view>
					</logic:iterate>
				</td>
				<logic:iterate id="unit" name="homepage" property="person.workingResearchUnits" offset="1">
					<td>&nbsp;</td>
					<td>
						<fr:view name="unit">
							<fr:layout name="unit-link">
								<fr:property name="unitLayout" value="values"/>
								<fr:property name="unitSchema" value="unit.name"/>
								<fr:property name="targetBlank" value="true"/>
								<fr:property name="parenteShown" value="true"/>
								<fr:property name="separator" value="<br/>"/>
							</fr:layout>
						</fr:view>
					</td>
				</logic:iterate>
			</tr>
		</logic:notEmpty>
		</logic:equal>


		<!--  -->
		<logic:equal name="homepage" property="showActiveStudentCurricularPlans" value="true">
            <logic:notEmpty name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<th><bean:message key="label.homepage.showActiveStudentCurricularPlans" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">								
					<app:contentLink name="studentCurricularPlan" property="degree.site">					
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="registration.degreeName"/>
					</app:contentLink>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.activeStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,
					<app:contentLink name="studentCurricularPlan" property="degree.site">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</app:contentLink>
				</logic:iterate>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>


		<!--  -->
		<logic:notEmpty name="personAttends">
		<logic:equal name="homepage" property="showCurrentAttendingExecutionCourses" value="true">
		<tr>
			<th><bean:message key="label.homepage.showCurrentAttendingExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="attend" name="personAttends" length="1">
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>					
					<app:contentLink name="executionCourse" property="site">
						<bean:write name="executionCourse" property="nome"/>
					</app:contentLink>
				</logic:iterate>
				<logic:iterate id="attend" name="personAttends" offset="1">
					,
					<bean:define id="executionCourse" name="attend" property="disciplinaExecucao"/>
					<app:contentLink name="executionCourse" property="site">
						<bean:write name="executionCourse" property="nome"/>
					</app:contentLink>
				</logic:iterate>
			</td>
		</tr>
		</logic:equal>
		</logic:notEmpty>


		<!--  -->
		<logic:equal name="homepage" property="showAlumniDegrees" value="true">
        <logic:notEmpty name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName">
		<tr>
			<th><bean:message key="label.homepage.showAlumniDegrees" bundle="HOMEPAGE_RESOURCES"/>:</th>
			<td>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" length="1">
					<app:contentLink name="studentCurricularPlan" property="degree.site">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</app:contentLink>
				</logic:iterate>
				<logic:iterate id="studentCurricularPlan" name="homepage" property="person.completedStudentCurricularPlansSortedByDegreeTypeAndDegreeName" offset="1">
					,				
					<app:contentLink name="studentCurricularPlan" property="degree.site">
						<logic:present name="studentCurricularPlan" property="specialization.name">
							<logic:equal name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:equal>
							<logic:notEqual name="studentCurricularPlan" property="specialization.name" value="STUDENT_CURRICULAR_PLAN_SPECIALIZATION">
								<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
							</logic:notEqual>
						</logic:present>
						<logic:notPresent name="studentCurricularPlan" property="specialization.name">
							<bean:message name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:notPresent>
						<bean:message key="label.in" bundle="HOMEPAGE_RESOURCES"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
					</app:contentLink>
				</logic:iterate>
			</td>
		</tr>
        </logic:notEmpty>
		</logic:equal>

        <!-- email -->
        <fr:view name="homepage" property="person.emailAddresses">
            <fr:layout name="contact-table">
                <fr:property name="publicSpace" value="true"/>
                <fr:property name="bundle" value="HOMEPAGE_RESOURCES" />
                <fr:property name="label" value="label.homepage.showEmail" />
                <fr:property name="defaultLabel" value="label.homepage.contact.default" />
            </fr:layout>
        </fr:view>

        <!-- phone -->
        <fr:view name="homepage" property="person.phones">
            <fr:layout name="contact-table">
                <fr:property name="publicSpace" value="true"/>
                <fr:property name="types" value="PERSONAL" />
                <fr:property name="bundle" value="HOMEPAGE_RESOURCES" />
                <fr:property name="label" value="label.homepage.showTelephone" />
                <fr:property name="defaultLabel" value="label.homepage.contact.default" />
            </fr:layout>
        </fr:view>

        <!-- work phone -->
        <fr:view name="homepage" property="person.phones">
            <fr:layout name="contact-table">
                <fr:property name="publicSpace" value="true"/>
                <fr:property name="types" value="WORK" />
                <fr:property name="bundle" value="HOMEPAGE_RESOURCES" />
                <fr:property name="label" value="label.homepage.showWorkTelephone" />
                <fr:property name="defaultLabel" value="label.homepage.contact.default" />
            </fr:layout>
        </fr:view>

        <!-- mobile phone -->
        <fr:view name="homepage" property="person.mobilePhones">
            <fr:layout name="contact-table">
                <fr:property name="publicSpace" value="true"/>
                <fr:property name="bundle" value="HOMEPAGE_RESOURCES" />
                <fr:property name="label" value="label.homepage.showMobileTelephone" />
                <fr:property name="defaultLabel" value="label.homepage.contact.default" />
            </fr:layout>
        </fr:view>

        <!-- alternative page -->
        <fr:view name="homepage" property="person.webAddresses">
            <fr:layout name="contact-table">
                <fr:property name="publicSpace" value="true"/>
                <fr:property name="bundle" value="HOMEPAGE_RESOURCES" />
                <fr:property name="label" value="label.homepage.showAlternativeHomepage" />
                <fr:property name="defaultLabel" value="label.homepage.contact.default" />
            </fr:layout>
        </fr:view>

		<!--  -->
		<logic:equal name="homepage" property="showCurrentExecutionCourses" value="true">
            <logic:present name="homepage" property="person.teacher">
                <logic:present name="homepage" property="person.employee.currentWorkingContract">
                		<tr>
                			<th><bean:message key="label.homepage.showCurrentExecutionCourses" bundle="HOMEPAGE_RESOURCES"/>:</th>
                			<td>
                				<bean:define id="executionCourses" name="homepage" property="person.teacher.currentExecutionCourses"/>

        						<logic:iterate id="executionCourse" name="executionCourses" length="1">        						
	        						<app:contentLink name="executionCourse" property="site">
										<bean:write name="executionCourse" property="nome"/>
									</app:contentLink>		        							        						
        						</logic:iterate>

        						<logic:iterate id="executionCourse" name="executionCourses" offset="1">
        							,        							
        							<app:contentLink name="executionCourse" property="site">
										<bean:write name="executionCourse" property="nome"/>
									</app:contentLink>	        						        						
        						</logic:iterate>        						
                			</td>
                		</tr>
                </logic:present>
            </logic:present>
		</logic:equal>
						
		</table>

<!--
		<logic:equal name="homepage" property="showResearchUnitHomepage" value="true">
			<br/>
		</logic:equal>
-->
		
	</logic:equal>
</logic:present>