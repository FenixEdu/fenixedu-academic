/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.TagLib.sop.v3.colorPickers;

import DataBeans.InfoLesson;
import DataBeans.InfoShowOccupation;
import ServidorApresentacao.TagLib.sop.v3.ColorPicker;

/**
 * @author jpvl
 */
public class RoomTimeTableColorPicker extends ColorPicker {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.TagLib.sop.v3.ColorPicker#getColorKeyFromInfoLesson(DataBeans.InfoLesson)
     */
    protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation) {
        StringBuffer strBuffer = new StringBuffer();

        if (infoShowOccupation instanceof InfoLesson) {
            InfoLesson infoLesson = (InfoLesson) infoShowOccupation;

            strBuffer.append(infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
        } else {
            strBuffer.append("EXAM");
        }

        return strBuffer.toString();
    }

    /*
     * protected String getColorKeyFromInfoLesson(InfoLesson infoLesson) {
     * StringBuffer strBuffer = new StringBuffer(); strBuffer.append(
     * infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla()); return
     * strBuffer.toString(); }
     */
}