<%@ page language="java" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>
			<jsp:include page="examContext.jsp"/>
         </td>
    </tr>
</table>
<br/>

<h2><bean:message key="title.exam.edit"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/editExam">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<%--
		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
--%>

		<logic:present name="examDateAndTime">
			<html:hidden alt="<%= SessionConstants.EXAM_DATEANDTIME %>" property="<%= SessionConstants.EXAM_DATEANDTIME %>"
						 value="<%= pageContext.findAttribute("examDateAndTime").toString() %>"/>
		</logic:present>

		<logic:present name="executionDegreeOID">
			<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		</logic:present>
		<logic:present name="executionCourseOID">
			<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		</logic:present>

		<bean:define id="oldExamSeason"
					 name="<%= SessionConstants.INFO_EXAMS_KEY %>"
					 property="infoExam.season.season"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldExamSeason" property="oldExamSeason"
					 value="<%= pageContext.findAttribute("oldExamSeason").toString() %>"/>

		<logic:present name="nextPage">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.input" property="input"
						 value="<%= pageContext.findAttribute(SessionConstants.NEXT_PAGE).toString() %>"/>
		</logic:present>

	<logic:present name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:iterate id="year" name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
			<logic:equal name="year" value="1">
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEARS_1 %>" property="<%= SessionConstants.CURRICULAR_YEARS_1 %>"
							 value="1"/>
			</logic:equal>
			<logic:equal name="year" value="2">
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEARS_2 %>" property="<%= SessionConstants.CURRICULAR_YEARS_2 %>"
							 value="2"/>
			</logic:equal>
			<logic:equal name="year" value="3">
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEARS_3 %>" property="<%= SessionConstants.CURRICULAR_YEARS_3 %>"
							 value="3"/>
			</logic:equal>
			<logic:equal name="year" value="4">
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEARS_4 %>" property="<%= SessionConstants.CURRICULAR_YEARS_4 %>"
							 value="4"/>
			</logic:equal>
			<logic:equal name="year" value="5">
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEARS_5 %>" property="<%= SessionConstants.CURRICULAR_YEARS_5 %>"
							 value="5"/>
			</logic:equal>
		</logic:iterate>
	</logic:present>

	<table cellpadding="0" cellspacing="2">
    	<tr>
        	<td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.year"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/>
            </td>
            <td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.month"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.month" property="month">
		            <option value="" selected="selected">[Mï¿½s]</option>
		            <html:options collection="<%= SessionConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
	            </html:select>
            </td>
            <td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.day"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.day" property="day">
                	<option value="" selected="selected">[Dia]</option>
                    <html:options name="<%= SessionConstants.LABLELIST_DAYSOFMONTH %>"/>
                </html:select>
            </td>
		</tr>
        <tr>
            <td nowrap="nowrap" class="formTD" align="right">
                <bean:message key="property.exam.beginning"/>
            </td>
            <td nowrap="nowrap" align="left">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginning" property="beginning">
                  	<option value="" selected="selected">[Turno]</option>                        
                    <html:options name="<%= SessionConstants.LABLELIST_HOURS %>"/>
                </html:select>
            </td>
       	</tr>
        <tr>
            <td nowrap="nowrap" class="formTD" align="right">
                <bean:message key="property.exam.season"/>
            </td>
            <td nowrap="nowrap" align="left">
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.season" property="season">
                  	<option value="" selected="selected">[Época]</option>           
		            <html:options collection="<%= SessionConstants.LABLELIST_SEASONS %>" property="value" labelProperty="label"/>                  	             
                </html:select>
            </td>
       	</tr>
	</table>
	<br/>
    <table align="lef">
    	<tr align="center">
        	<td>
        		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
            	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
              		<bean:message key="label.edit"/>
             	</html:submit>
            </td>
            <td width="20"> </td>
            <td>
            	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
                	<bean:message key="label.clear"/>
                </html:reset>
            </td>
		</tr>
	</table>
</html:form>

	<br/>
	<br/>
	<bean:message key="property.exam.rooms"/>
	<logic:present name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExam.associatedRooms">
	<table cellpadding="0" cellspacing="2">
		<tr>
       		<td nowrap class="formTD" align="right">
           		Nome
			</td>
			<td nowrap class="formTD" align="right">
           		Capacidade Exame
			</td>
		</tr>
		<logic:iterate id="infoRoom" name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExam.associatedRooms">
		<tr>
			<td nowrap class="formTD" align="right">
				<bean:write name="infoRoom" property="nome"/>
			</td>
			<td nowrap class="formTD" align="right">
				<bean:write name="infoRoom" property="capacidadeExame"/>
			</td>
		</tr>
		</logic:iterate>
	</table>
	</logic:present>

	<logic:notPresent name="<%= SessionConstants.INFO_EXAMS_KEY %>" property="infoExam.associatedRooms">
		<bean:message key="message.exam.no.rooms"/> <br/>
	</logic:notPresent>

	<html:link page="<%= "/editExamRooms.do?method=prepare&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
  							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
							+ "="
  							+ pageContext.findAttribute("executionDegreeOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_COURSE_OID
							+ "="
  							+ pageContext.findAttribute("executionCourseOID")
							+ "&amp;"
							+ "oldExamSeason"
							+ "="
  							+ pageContext.findAttribute("oldExamSeason")
							+ "&amp;"
							+ "input"
							+ "="
  							+ pageContext.findAttribute(SessionConstants.NEXT_PAGE)
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEARS_1
							+ "="
  							+ pageContext.findAttribute("curricularYears_1")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEARS_2
							+ "="
  							+ pageContext.findAttribute("curricularYears_2")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEARS_3
							+ "="
  							+ pageContext.findAttribute("curricularYears_3")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEARS_4
							+ "="
  							+ pageContext.findAttribute("curricularYears_4")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEARS_5
							+ "="
  							+ pageContext.findAttribute("curricularYears_5")
							+ "&amp;"
							+ SessionConstants.EXAM_DATEANDTIME
							+ "="
  							+ pageContext.findAttribute("examDateAndTime")
					 %>">


   		<bean:message key="lable.changeRoom"/>
   	</html:link>
