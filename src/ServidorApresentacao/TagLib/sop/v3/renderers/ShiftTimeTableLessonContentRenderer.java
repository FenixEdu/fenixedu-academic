package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShowOccupation;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ShiftTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
        //InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        
        if (showOccupation instanceof InfoLesson) 
        {
            InfoLesson lesson = (InfoLesson) showOccupation;
            
			strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
	
			strBuffer.append("(");
			strBuffer.append(lesson.getTipo()).append(")");
			strBuffer
				.append("<a class='timetable' href='siteViewer.do?method=roomViewer&amp;roomName=")
				.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome())
				.append("'>")
				.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome())
				.append("</a>");
        }
        else
        {
            InfoExam infoExam = (InfoExam) showOccupation;
            for (int iterEC=0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++)
			{
				InfoExecutionCourse infoEC = (InfoExecutionCourse) infoExam.getAssociatedExecutionCourse().get(iterEC);
				if(iterEC != 0)
				{    
				    strBuffer.append(", ");
				}
				strBuffer.append(infoEC.getSigla());
				
			}
            strBuffer.append(" - ");
            strBuffer.append(infoExam.getSeason().getSeason());
            strBuffer.append("ª época");            
        }
        
        return strBuffer;
	}

}
