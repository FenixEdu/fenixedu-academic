<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="professorship.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<style>
.thtop { vertical-align: top; }
.biggerTextarea textarea { width: 400px; height: 100px; }
.biggerInputText input[type="text"] { width: 400px !important; }
.smallerInputText input[type="text"] { width: 50px !important; }
</style>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/teachingInquiry.do?method=showInquiries2ndPage">
			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.teachingAndLearningConditions" bundle="INQUIRIES_RESOURCES"/></h4>
			<div class="biggerInputText">
				<fr:edit name="teachingInquiry" property="firstPageFirstBlock" />	
			</div>
			
			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.studentsEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
			<div class="biggerInputText">
				<fr:edit name="teachingInquiry" property="firstPageSecondBlockFirstPart" />
			</div>
			
			<div class="smallerInputText">
				<fr:edit name="teachingInquiry" property="firstPageSecondBlockSecondPart" />
			</div>
			
			<fr:edit name="teachingInquiry" property="firstPageSecondBlockFourthPart" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.autoEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="firstPageThirdBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value="thtop,biggerTextarea,,,,,,"/>
				</fr:layout>		
			</fr:edit>			
            
            <fr:edit name="teachingInquiry" property="firstPageFourthBlock" />

			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
		
			<br/>
			
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		

		<fr:form action="/teachingInquiry.do?method=showInquiriesPrePage">
			<bean:define id="executionCourseID" ><bean:write name="teachingInquiry" property="professorship.executionCourse.idInternal" /></bean:define>
			<html:hidden property="executionCourseID" value="<%= executionCourseID %>"/>
			
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<br/>
	</div>
</div>	