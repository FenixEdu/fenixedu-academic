/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.TagLib.sop.v3.colorPickers;

import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.ColorPicker;

/**
 * @author jpvl
 */
public class ExecutionCourseTimeTableColorPicker extends ColorPicker {

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TagLib.sop.v3.ColorPicker#getColorKeyFromInfoLesson(DataBeans.InfoLesson)
	 */
	protected String getColorKeyFromInfoLesson(InfoLesson infoLesson) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(
			infoLesson.getTipo().getSiglaTipoAula());
		return strBuffer.toString();
	}

}
