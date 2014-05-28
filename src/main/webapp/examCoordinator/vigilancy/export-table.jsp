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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>

<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style type="text/css" media="screen,print">
body {
font-family: Verdana, sans-serif;
font-size: 71%;
}
table { font-size: 1.0em; }
.tstyle1 { border-collapse: collapse; margin: 1em 0; }
.tstyle1 th { padding: 5px; text-align: center; }
.tstyle1 th { border: 1px solid #ccc; background: #f8f8f8 url(../../pics/back_32gray.gif) left bottom repeat-x;}
.tstyle1 td { padding: 5px; }
.tstyle1 td { border: 1px solid #cccccc; }

.mbottom2 { margin-bottom: 2em; }
.mtop025 { margin-top: 0.25em;}
.mtop2 { margin-top: 2em; }
.mbottom0 { margin-bottom: 0; }

ul.list2 { padding-left: 0; list-style: none; }
ul.list2 li { padding: 0.25em 0;}

/* lists */
ul.liinline {
margin: 0;
padding: 0;
}
ul.liinline li {
display: inline;
margin: 0;
padding: 0 0.25em;
}
.color888 { color: #888; }
.color007b4d, a.color007b4d { color: #007b4d; }
tr.vigilancyHeaderColumns th { background: #fcfcfc; }
.width250px { width: 250px; }
.bluetxt { color: #00427b;}
.nowrap {
white-space: nowrap;
}
</style>

</head>
<body>

<logic:present name="bean" property="selectedVigilantGroup">
	<fr:view name="bean" property="selectedVigilantGroup" layout="values" schema="presentVigilantGroupName"/>
</logic:present>

<logic:notPresent name="bean" property="selectedVigilantGroup">
	<fr:view name="LOGGED_USER_ATTRIBUTE" property="person.currentExamCoordinator.vigilantGroups">
			<fr:layout name="flowLayout">
				<fr:property name="htmlSeparator" value=","/>
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="presentVigilantGroupName"/>
			</fr:layout>
	</fr:view>
</logic:notPresent>

	
	<div class="mbottom2">
	<p class="mtop2 mbottom0"><em><bean:message key="label.vigilancy.label" bundle="VIGILANCY_RESOURCES"/>:</em></p>
	<ul class="list2 mtop025 liinline mbottom0">
		<li><em><strong><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
	</ul>
	
<table>
	<tr class="color888">
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #00427b; background: #9ebcd7; float:left; margin-right: 0.5em;"></div> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #007b4d; background: #9ed7c2; float:left; margin-right: 0.5em;"></div> <bean:message key="label.teachersVigilants" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #888; background: #ccc; float:left; margin-right: 0.5em;"></div> <bean:message key="label.cancelledConvokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #d2d292; background: #ffffe5; float:left; margin-right: 0.5em;"></div> <bean:message key="label.problematicConvoke" bundle="VIGILANCY_RESOURCES"/></td>
	</tr>
</table>


</div>

	<logic:present name="group">

	<fr:view name="group" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1" />	
		<fr:property name="rowClasses" value="vigilancyHeaderColumns" />
		
		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints())%>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		<fr:property name="showPointsWeight" value="<%= String.valueOf(bean.isShowPointsWeight()) %>"/>
		
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForCoordinator.withoutLink"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithoutLink"/>	
		</logic:equal>
		<fr:property name="columnClasses" value=",,nowrap,,,,,,,,,,,,,,,,,,,,,,"/>	
		
	</fr:layout>
</fr:view>

</logic:present>

</body>
</html:html>