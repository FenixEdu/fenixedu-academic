<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<head>
	<title>
		<bean:message key="public.QUC.teacherReport" bundle="TITLES_RESOURCES" /> -
		<bean:write name="teachingInquiry" property="professorship.executionCourse.nome"/> -
		<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
	
</head>

<body class="survey">

<style>
body.survey {
background: #fff;
margin: 2em;
font-size: 70%;
}
.acenter { text-align: center !important; }
.aright { text-align: right !important; }
.aleft { text-align: left !important; }
th:first-child {
width: 250px;
}
body.survey table {
}
body.survey table th {
vertical-align: bottom;
}
body.survey table td {
text-align: center;
}
table.td50px td {
width: 60px;
}
table tr.top th { border-top: 4px solid #ddd; }
table tr.top td { border-top: 4px solid #ddd; }

body.survey table {
}
.thtop th { vertical-align: top; }
.vatop { vertical-align: top !important; }
.vamiddle { vertical-align: middle !important; }
.tdright td { text-align: right !important; }
.tdleft td { text-align: left !important; }


a.help {
position: relative;
text-decoration: none;
border: none !important;
width: 20px;
}
a.help span {
display: none;
}
a.help:hover {
z-index: 100;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a { color: #105c93; }

table th.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}
table td.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}

.thtop { vertical-align: top !important; width: 300px;}
.biggerCell  { width: 400px !important; text-align: left !important;}
</style>



<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<div class="infoop2" style="font-size: 1.3em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">Semestre: 
		<bean:write name="teachingInquiry" property="professorship.executionCourse.executionPeriod.semester"/>
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:message bundle="APPLICATION_RESOURCES" locale="pt_PT" key="of" /> 
		<bean:write name="teachingInquiry" property="professorship.executionCourse.executionYear.name"/></span></p>	
	<p style="margin: 0.75em 0;">Unidade curricular: <bean:write name="teachingInquiry" property="professorship.executionCourse.nome"/></p>
	<p style="margin: 0.75em 0;">Docente:
		 <logic:notEmpty name="teachingInquiry" property="professorship.teacher.category" >
		 	<bean:write name="teachingInquiry" property="professorship.teacher.category.name.content"/> -
		 </logic:notEmpty>
		 <bean:write name="teachingInquiry" property="professorship.person.name"/></p>
</div>


<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.teachingAndLearningConditions" bundle="INQUIRIES_RESOURCES"/></h4>
<div class="biggerInputText">
	<fr:view name="teachingInquiryDTO" property="firstPageFirstBlock" />	
</div>

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.studentsEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
<div class="biggerInputText">
	<fr:view name="teachingInquiryDTO" property="firstPageSecondBlockFirstPart" />
</div>

<div class="smallerInputText">
	<fr:view name="teachingInquiryDTO" property="firstPageSecondBlockSecondPart" >
        <fr:layout name="tabular-editable" >
            <fr:property name="columnClasses" value=",center,center,center,center,center,center,center,center,center,center,center,,,,,"/>
        </fr:layout>            
    </fr:view>
</div>

<fr:view name="teachingInquiryDTO" property="firstPageSecondBlockFourthPart" />

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.autoEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
<fr:view name="teachingInquiryDTO" property="firstPageThirdBlock" >
	<fr:layout name="tabular-editable" >
		<fr:property name="columnClasses" value="thtop,biggerCell,,,,,,,,,,,,,,,"/>
	</fr:layout>		
</fr:view>			

<fr:view name="teachingInquiryDTO" property="firstPageFourthBlock" />

<c:if test="${teachingInquiryDTO.professorship.responsibleFor}">

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuEvaluationMethod" bundle="INQUIRIES_RESOURCES"/></h4>
<bean:message key="subtitle.teachingInquiries.cuEvaluationMethod" bundle="INQUIRIES_RESOURCES"/>
<div class="smallerInputText">
    <fr:view name="teachingInquiryDTO" property="secondPageFourthBlock" />
</div>
<div class="biggerInputText">
    <fr:view name="teachingInquiryDTO" property="secondPageFourthBlockThirdPart" />
</div>


<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuGlobalEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
<div class="biggerInputText">
    <fr:view name="teachingInquiryDTO" property="secondPageFifthBlockFirstPart" />
</div>
<fr:view name="teachingInquiryDTO" property="secondPageFifthBlockSecondPart" />

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuStudentsCompetenceAcquisitionAndDevelopmentLevel" bundle="INQUIRIES_RESOURCES"/></h4>
<fr:view name="teachingInquiryDTO" property="secondPageSixthBlock" />

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.globalClassificationOfThisCU" bundle="INQUIRIES_RESOURCES"/></h4>
<fr:view name="teachingInquiryDTO" property="secondPageSeventhBlock" />

<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.cuTeachingProcess" bundle="INQUIRIES_RESOURCES"/></h4>
<fr:view name="teachingInquiryDTO" property="secondPageEighthBlock" >
    <fr:layout name="tabular-editable" >
        <fr:property name="columnClasses" value="thtop,biggerCell,,,,,,"/>
    </fr:layout>        
</fr:view>  

<p class="mtop15">
    <strong>
        <bean:message key="label.teachingInquiries.negativeResultsResolutionAndImproovementPlanOfAction" bundle="INQUIRIES_RESOURCES"/>
    </strong>
</p>

<div class="tablenoborder">
    <fr:view name="teachingInquiryDTO" property="thirdPageNinthBlock" >
        <fr:layout name="tabular-editable" >
            <fr:property name="columnClasses" value="thtoptd300 dnone,biggerCell,,,,,,"/>
        </fr:layout>        
    </fr:view>
</div>

 <fr:view name="teachingInquiryDTO" property="thirdPageReportDisclosureBlock" />

</c:if>

</body>

</html>