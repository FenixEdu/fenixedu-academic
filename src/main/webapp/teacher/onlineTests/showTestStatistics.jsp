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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="infoInquiryStatisticsList">

<html:form action="/testDistribution">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDistributedTests"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= (pageContext.findAttribute("objectCode")).toString() %>"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="10">
	<bean:define id="order" value="1"/>
	<logic:iterate id="infoInquiryStatistic" name="infoInquiryStatisticsList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics" indexId="iterateIndex">
		<bean:define id="testQuestion" name ="infoInquiryStatistic" property="infoStudentTestQuestion" type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest"/>
		<bean:define id="testCode" name="distributedTest" property="externalId"/>
		<bean:define id="student" name="testQuestion" property="student" type="net.sourceforge.fenixedu.domain.student.Registration"/>
		<bean:define id="studentCode" name="student" property="externalId"/>
		<logic:equal name="iterateIndex" value="0">
			<center><h2><bean:write name="distributedTest" property="title"/></h2>
			<b><bean:write name="distributedTest" property="testInformation"/></b></center>
			<br/>
			<br/>
		</logic:equal>
		<bean:define id="testQuestion" name ="infoInquiryStatistic" property="infoStudentTestQuestion" type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion"/>
		<bean:define id="index" value="0"/>
		<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.domain.onlineTests.Question"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest"/>
		<bean:define id="questionCode" name="question" property="externalId"/>
		<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
		<bean:define id="correction" name="testQuestion" property="distributedTest.correctionAvailability" type="net.sourceforge.fenixedu.util.tests.CorrectionAvailability"/>
		<bean:define id="formula" name="testQuestion" property="correctionFormula.formula"/>
		<bean:define id="testType" name="testQuestion" property="distributedTest.testType.type"/>
		
		<logic:iterate id="subQuestion" name="testQuestion" property="studentSubQuestions" type="net.sourceforge.fenixedu.domain.onlineTests.SubQuestion" indexId="itemIndex">
		<bean:define id="item" value="<%=itemIndex.toString()%>"/>
		<%if(testQuestion.getStudentSubQuestions().size()<=1 || (testQuestion.getItemId()!=null && subQuestion.getItemId()!=null && testQuestion.getItemId().equals(subQuestion.getItemId()))){%>
		<bean:define id="questionType" name="subQuestion" property="questionType.type"/>	
	
		<%if(testQuestion.getStudentSubQuestions().size()<=1 || itemIndex.equals(new Integer(0))){%>
			<tr><td><hr/></td></tr>
			<tr><td><b><bean:message key="message.tests.question" />:</b>&nbsp;<bean:write name="order"/></td></tr>
			<bean:define id="order" value="<%= (new Integer(Integer.parseInt(order)+1)).toString() %>"/>
		<%}
		 if(testQuestion.getStudentSubQuestions().size()>1){
		 	if(itemIndex.equals(new Integer(0))){%>
		 		<tr><td><span class="error">Esta pergunta é uma pergunta com alíneas. Após responder poderá surgir uma nova alínea para responder.</span></td></tr>
		 	<%}%>
			<tr><td><br/><b><bean:write name="subQuestion" property="title"/></b></td></tr>
		<%}%>
			<tr><td>
		<bean:define id="index" value="0"/>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionPresentation" name="subQuestion" property="prePresentation">
			<bean:define id="questionLabel" name="questionPresentation" property="label"/>	
			<%if (((String)questionLabel).startsWith("image/")){%>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<logic:equal name="correctionType" value="studentCorrection">
					<html:img bundle="HTMLALT_RESOURCES" altKey="img.img" align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode+"&amp;studentCode="+ studentCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>"/>
				</logic:equal>
				<logic:notEqual name="correctionType" value="studentCorrection">
					<html:img bundle="HTMLALT_RESOURCES" altKey="img.img" align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>"/>
				</logic:notEqual>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%} else if (((String)questionLabel).equals("image_label")){%>				
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="questionPresentation" property="value"/>
				<br/>
			<%}else if (((String)questionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else{%>
				<bean:write name="questionPresentation" property="value"/>
			<%}%>
		</logic:iterate>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		
		<tr><td>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionBody" name="subQuestion" property="presentation">
			<bean:define id="questionLabel" name="questionBody" property="label"/>	
			<%if (((String)questionLabel).startsWith("image/")){%>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<logic:equal name="correctionType" value="studentCorrection">
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode+"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
				</logic:equal>
				<logic:notEqual name="correctionType" value="studentCorrection">
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
				</logic:notEqual>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%} else if (((String)questionLabel).equals("image_label")){%>				
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="questionBody" property="value"/>
				<br/>
			<%}else if (((String)questionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else{%>
				<bean:write name="questionBody" property="value"/>
			<%}%>
		</logic:iterate>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		
		
		<bean:define id="cardinality" name="subQuestion" property="questionType.cardinalityType.type"/>
		<bean:define id="indexOption" value="0"/>
		<bean:size id="size" name="infoInquiryStatistic" property="optionStatistics"/>
		
		<table><tr><td>
		<logic:iterate id="questionOption" name="subQuestion" property="options">
		<logic:iterate id="optionBody" name="questionOption" property="optionContent">
			<bean:define id="optionLabel" name="optionBody" property="label"/>
			<%if (((String)optionLabel).startsWith("image/")){ %>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<logic:equal name="correctionType" value="studentCorrection">
					<html:img bundle="HTMLALT_RESOURCES" altKey="img.img" align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;item="+item.toString()%>"/>
				</logic:equal>
				<logic:notEqual name="correctionType" value="studentCorrection">
					<bean:define id="optionShuffle" name="testQuestion" property="optionShuffle"/>
					<html:img bundle="HTMLALT_RESOURCES" altKey="img.img" align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;optionShuffle="+optionShuffle.toString()+"&amp;item="+item.toString()%>"/>
				</logic:notEqual>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%}else if (((String)optionLabel).equals("image_label")){%>
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="optionBody" property="value"/>
				<br/>
			<%}else if (((String)optionLabel).equals("response_label")){ %>
				<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
				<%	if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
					</td></tr><tr><td>
					<logic:iterate id="statistic" name="infoInquiryStatistic" property="optionStatistics" offset="<%=new Integer(new Integer(indexOption).intValue()-1).toString()%>" length="1"/>
					<b><bean:write name="statistic" property="value"/>)</b>
					</td><td>
				<%}else{%> <%--QuestionType.STR ou QuestionType.NUM--%>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.selected" property="selected" value="" disabled="true"/>
				<% } %>
		<%--
		
		<tr>
		<td><b><bean:write name="statistic" property="value"/>)</b>
		<logic:equal name="i" value="<%=new Integer(size.intValue()-1).toString()%>">:</logic:equal>
			<logic:notEqual name="i" value="<%=new Integer(size.intValue()-1).toString()%>">)</logic:notEqual>
		</b>
		<bean:write name="statistic" property="value"/></td>
		</tr>
		</logic:iterate>
		 --%>
			<%}else if (((String)optionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else {%>
				<bean:write name="optionBody" property="value"/>
			<%}%>
		</logic:iterate>
		</logic:iterate>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		
		
		<%if(((Integer)questionType).intValue()<=1 ){ %> 
			<logic:iterate id="statistic" name="infoInquiryStatistic" property="optionStatistics" offset="<%=new Integer(size.intValue()-1).toString()%>" length="1">
				<tr>
				<td colspan="2"><b><bean:write name="statistic" property="label"/>:</b>
				<bean:write name="statistic" property="value"/></td>
				</tr>
			</logic:iterate>
		<%}else{ %>
			<logic:iterate id="statistic" name="infoInquiryStatistic" property="optionStatistics" indexId="i">
				<tr>
				<logic:equal name="i" value="<%=new Integer(size.intValue()-1).toString()%>">
				<td><b><bean:write name="statistic" property="label"/>:</logic:equal>
				<logic:notEqual name="i" value="<%=new Integer(size.intValue()-1).toString()%>">
				<td colspan="2"><b><bean:write name="statistic" property="label"/>)</logic:notEqual>
				</b>
				<bean:write name="statistic" property="value"/></td>
				</tr>
			</logic:iterate>
		<%} %> 
	<%} %> 
	</td></tr></table>
	</logic:iterate>
	</logic:iterate>
	<tr><td><hr/></td></tr>
	</table>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
</logic:present>