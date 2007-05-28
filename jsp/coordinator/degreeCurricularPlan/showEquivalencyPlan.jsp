<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<br/>
<h2><bean:message key="title.equivalency.plan"/></h2>

<div class='simpleblock4'>
	<logic:notPresent name="degreeCurricularPlan" property="equivalencePlan">
		<bean:message key="message.no.equivalency.table.exists"/>
		<br/>
		<bean:message key="label.create.equivalency.table.for.degree.curricular.plan"/>
		<fr:edit name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"
				schema="degreeCurricularPlan.createEquivalencyPlan">
		    <fr:layout>
	    	    <fr:property name="classes" value="thtop width8em"/>
	        	<fr:property name="columnClasses" value=",pbottom1,valigntop"/>
		    </fr:layout>
		</fr:edit>
	</logic:notPresent>
	<logic:present name="degreeCurricularPlan" property="equivalencePlan">
		<bean:message key="message.equivalency.table.from.degree.curricular.plan"/>
		<br/>
		<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan"/>
	</logic:present>
</div>

<logic:present name="degreeCurricularPlan" property="equivalencePlan">
	<logic:iterate id="curricularCourse" name="degreeCurricularPlan" property="allCurricularCourses">

		<table class="showinfo3 mvert0" style="width: 64em;">
			<tr class="bgcolor2">
				<th class="aleft" colspan="5">
					<bean:write name="curricularCourse" property="name"/>
				</th>
			</tr>
		</table>

		<div class="indent3">
			<table class="showinfo3 mvert0" style="width: 61em;">
				<tr>
					<td>
						Abc 1
					</td>
					<td class="smalltxt" align="center">
						Abc 2
					</td>
					<td class="highlight2 smalltxt" align="center" style="width: 1em;">
						Abc 3
					</td>
					<td class="smalltxt" align="right">
						<span style="color: #888">
							Abc 4
						</span>
						Abc 5
						<span style="color: #888">
							Abc 6
						</span>
						Abc 7
						<span style="color: #888">
							Abc 8
						</span>
						Abc 9
					</td>
					<td class="smalltxt" align="right">
						Abc 10
					</td>
				</tr>
			</table>
		</div>
	</logic:iterate>
</logic:present>