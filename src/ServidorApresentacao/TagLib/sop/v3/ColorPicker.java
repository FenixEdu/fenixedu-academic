/*
 * Created on 27/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.TagLib.sop.v3;

import java.util.HashMap;

import DataBeans.InfoLesson;

/**
 * @author jpvl
 */
public abstract class ColorPicker {
	private HashMap lessonColors;
	private String[] colorPallete =
		{
			"#00CCFF",
			"#0099FF",
			"#0066FF",
			"#00CCCC",
			"#0099CC",
			"#0066CC",
			"#0033CC",
			"#00CC99",
			"#009999",
			"#006699",
			"#003399"};

	public ColorPicker(){
		lessonColors = new HashMap();
	}
	
	public String getBackgroundColor(InfoLessonWrapper infoLessonWrapper){
		if ((infoLessonWrapper == null) ||(infoLessonWrapper.getInfoLesson() == null)){
			/* blank slot color*/
			return "#CCCCCC";
		}else {
			InfoLesson infoLesson = infoLessonWrapper.getInfoLesson();
			String colorKeyInfoLesson  = getColorKeyFromInfoLesson(infoLesson);
			String color = (String) lessonColors.get(colorKeyInfoLesson);
			
			if (color == null){
				
				for (int colorIndex = 0; colorIndex < colorPallete.length && color == null; colorIndex++) {
					String colorFromPallete = colorPallete[colorIndex];
					if (!lessonColors.containsValue(colorFromPallete)){
						color = colorFromPallete;
						lessonColors.put(colorKeyInfoLesson, color);
					}
				}
				if (color == null){
					color = colorPallete[0];
				}
				
			}
			return color;
		}
		
	}
	
	abstract protected String getColorKeyFromInfoLesson(InfoLesson infoLesson);
	
}
