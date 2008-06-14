<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="DEPARTMENT_MEMBER,DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<logic:notEmpty name="teacherPersonalExpectation">
			
	<%-- ****** Education Expectations ****** --%>
	
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.education" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>
		
		<%-- Graduations --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewGraduationExpectations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		
		<%-- Cientific Pos-Graduations --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewCientificPosGraduationsExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		
		<%-- Professional Pos-Graduations --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalPosGraduationsExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		
		<%-- Seminaries --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewSeminariesExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		
		<%-- Main Focus --%>			
		<fr:view name="teacherPersonalExpectation" schema="ViewEducationMainFocusExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<ul class="list5">
					<li>
						<html:link page="/personalExpectationManagement.do?method=prepareEditEducationExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
							<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
						</html:link>
					</li>
				</ul>
			</logic:equal>
		</logic:empty>	
													
	
		<%-- ****** Researche And Development Expectations ****** --%>			
		
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.investigation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>	
	
		<%-- Publications And Projects  --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.publicationsAndProjects" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewPublicationsAndProjectsExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
		<%-- Publications And Projects Main Focus --%>
		<fr:view name="teacherPersonalExpectation" schema="ViewPublicationsAndProjectsMainFocusExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
			
		<%-- Orientation --%>
		<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.orientation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
		<fr:view name="teacherPersonalExpectation" schema="ViewOrientationsExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
	
		<%-- Orientation Main Focus --%>
		<fr:view name="teacherPersonalExpectation" schema="ViewOrientationMainFocusExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<ul class="list5">
					<li>
						<html:link page="/personalExpectationManagement.do?method=prepareEditResearchAndDevelopmentExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
							<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
						</html:link>
					</li>
				</ul>
			</logic:equal>
		</logic:empty>
		

	<%-- ****** University Service Expectations ****** --%>	
						
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.universityService" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>	
		
		<fr:view name="teacherPersonalExpectation" schema="ViewUniversityServiceExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>							
		<fr:view name="teacherPersonalExpectation" schema="ViewUniversityServiceMainFocusExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
		<logic:empty name="noEdit">	
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<ul class="list5">
					<li>
						<html:link page="/personalExpectationManagement.do?method=prepareEditUniversityServicesExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
							<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
						</html:link>
					</li>
				</ul>
			</logic:equal>
		</logic:empty>
							
	<%-- ****** Professional Activity Expectations ****** --%>		
		
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.professionalActivity" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>			
		
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalActivitiesExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalActivitiesMainFocusExpectations" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width14em,"/>
			</fr:layout>
		</fr:view>
	
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<ul class="list5">
					<li>
						<html:link page="/personalExpectationManagement.do?method=prepareEditProfessionalActivitiesExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
							<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
						</html:link>
					</li>
				</ul>
			</logic:equal>
		</logic:empty>	
										
	</logic:notEmpty>	
	
</logic:present>	