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
			"#33CCFF",
			"#99CCFF",
			"#66CCFF",
			"#00CC99",
			"#33CC99",
			"#66CC99",
			"#99CC99",
			"#33CC66",
			"#66CC66",
			"#99CC66",
			"#33CC33",
			"#66CC33",
			"#99CC33",
			"#33CCCC",
			"#99CCCC",
			"#66CCCC" };

	public ColorPicker() {
		lessonColors = new HashMap();
	}

	public String getBackgroundColor(InfoLessonWrapper infoLessonWrapper) {
		if ((infoLessonWrapper == null)
			|| (infoLessonWrapper.getInfoLesson() == null)) {
			/* blank slot color*/
			return "#CCCCCC";
		} else {
			InfoLesson infoLesson = infoLessonWrapper.getInfoLesson();
			String colorKeyInfoLesson = getColorKeyFromInfoLesson(infoLesson);
			String color = (String) lessonColors.get(colorKeyInfoLesson);

			if (color == null) {

				for (int colorIndex = 0;
					colorIndex < colorPallete.length && color == null;
					colorIndex++) {
					String colorFromPallete = colorPallete[colorIndex];
					if (!lessonColors.containsValue(colorFromPallete)) {
						color = colorFromPallete;
						lessonColors.put(colorKeyInfoLesson, color);
					}
				}
				if (color == null) {
					color = colorPallete[0];
				}

			}
			return color;
		}

	}

	abstract protected String getColorKeyFromInfoLesson(InfoLesson infoLesson);

}
