<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p class="invisible"><strong>&raquo; Gest&atilde;o de Exames</strong></p>
<ul>
  <li><html:link page="/chooseExamsContextDA.do?method=prepare&amp;nextPage=createExam&amp;inputPage=chooseExamsContext"><bean:message key="link.exams.create"/></html:link></li>
  <li><html:link page="/chooseDayAndShiftForm.do?method=prepare&amp;nextPage=viewExams"><bean:message key="link.exams.listByDayAndShift"/></html:link></li>
  <li><html:link page="/chooseExamsContextDA.do?method=prepare&amp;nextPage=listByDegreeAndAcademicYear&amp;inputPage=chooseExamsContext"><bean:message key="link.exams.listByDegreeAndAcademicYear"/></html:link></li>
  <li><html:link page="/chooseExamsMapContextDA.do?method=prepare"><bean:message key="link.exams.map"/></html:link></li>
  <li><html:link page="/chooseDayAndShiftForm.do?method=prepare&amp;nextPage=viewEmptyRooms"><bean:message key="link.exams.searchEmptyRooms"/></html:link></li>
  <li><html:link page="/consultRoomsForExams.do?method=prepare"><bean:message key="link.exams.consultRoomOccupation"/></html:link></li>
</ul>
