/*
 * Created on May 25, 2003
 *  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap;

import java.util.Calendar;

import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers.ExamsMapSlotContentRenderer;

/*
 * @author Luis Cruz & Sara Ribeiro
 *  
 */

public class ExamsMapForRoomRenderer implements IExamsMapRenderer {

    private String[] daysOfWeek = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };

    private int numberOfWeks;

    private ExamsMap examsMap;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

    private String user;

    public ExamsMapForRoomRenderer(ExamsMap examsMap,
            ExamsMapSlotContentRenderer examsMapSlotContentRenderer, String typeUser) {
        setExamsMap(examsMap);
        setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
        numberOfWeks = examsMap.getDays().size() / 6;
        setUser(typeUser);
    }

    public StringBuffer render() {
        StringBuffer strBuffer = new StringBuffer("");

        strBuffer.append("<table class='examMapContainer' cellspacing='0' cellpadding='3' width='95%'>");
        strBuffer.append("<tr>");
        strBuffer.append("<td>");
        renderExamsMapForRoom(strBuffer);
        strBuffer.append("</td>");
        strBuffer.append("</tr>");
        strBuffer.append("</table>");

        strBuffer.append("<br />");

        return strBuffer;
    }

    private void renderExamsMapForRoom(StringBuffer strBuffer) {
        strBuffer.append("<table class='examMap' cellspacing='0' cellpadding='3' width='95%'>");

        strBuffer.append("<tr>");
        renderHeader(strBuffer);
        strBuffer.append("</tr>");

        for (int week = 0; week < numberOfWeks; week++) {
            strBuffer.append("<tr>");
            renderLabelsForRowOfDays(strBuffer, week);
            strBuffer.append("</tr>\r\n");
            strBuffer.append("<tr>");
            renderExamsForRowOfDays(strBuffer, week);
            strBuffer.append("</tr>\r\n");
        }

        strBuffer.append("</table>");
    }

    private void renderExamsForRowOfDays(StringBuffer strBuffer, int week) {
        for (int slot = 0; slot < daysOfWeek.length; slot++) {

            ExamsMapSlot examsMapSlot = (ExamsMapSlot) examsMap.getDays().get(
                    week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_content";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                classCSS += "_first";
            }

            if (week == numberOfWeks - 1) {
                classCSS += "_bottom";
            }
            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(examsMapSlotContentRenderer.renderDayContents((ExamsMapSlot) examsMap
                    .getDays().get(week * daysOfWeek.length + slot), user));
            strBuffer.append("</td>");
        }
    }

    private void renderLabelsForRowOfDays(StringBuffer strBuffer, int week) {
        for (int slot = 0; slot < daysOfWeek.length; slot++) {
            ExamsMapSlot examsMapSlot = (ExamsMapSlot) examsMap.getDays().get(
                    week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_day";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                classCSS += "_first";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(examsMapSlotContentRenderer.renderDayLabel(examsMapSlot, examsMap));
            strBuffer.append("</td>");
        }
    }

    private void renderHeader(StringBuffer strBuffer) {
        for (int index = 0; index < this.daysOfWeek.length; index++) {
            StringBuffer classCSS = new StringBuffer("examMap_header");
            if (index == 0) {
                classCSS.append("_first");
            }
            strBuffer.append("<td class='").append(classCSS).append("'>\r\n").append(daysOfWeek[index])
                    .append("</td>\r\n");
        }
    }

    /**
     * @param map
     */
    private void setExamsMap(ExamsMap map) {
        examsMap = map;
    }

    /**
     * @param renderer
     */
    private void setExamsMapSlotContentRenderer(ExamsMapSlotContentRenderer renderer) {
        examsMapSlotContentRenderer = renderer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String string) {
        user = string;
    }

}