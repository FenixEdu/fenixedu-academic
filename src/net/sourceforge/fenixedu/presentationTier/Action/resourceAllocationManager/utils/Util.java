package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.util.LabelValueBean;

public class Util {

    public static List<LabelValueBean> readExistingBuldings(String name, String value) throws FenixFilterException, FenixServiceException {
        List<LabelValueBean> edificios = new ArrayList<LabelValueBean>();

        if (name != null)
            edificios.add(new LabelValueBean(name, value));

        final Object args[] = {};
        final List<InfoBuilding> infoBuildings = (List<InfoBuilding>) ServiceUtils.executeService(null, "ReadBuildings", args);
        Collections.sort(infoBuildings, new BeanComparator("name"));

        for (final Iterator<InfoBuilding> iterator = infoBuildings.iterator(); iterator.hasNext(); ) {
            final InfoBuilding infoBuilding = iterator.next();
            edificios.add(new LabelValueBean(infoBuilding.getName(), infoBuilding.getName()));
        }

        return edificios;
    }

    public static List<LabelValueBean> readTypesOfRooms(String name, String value) {
        List<LabelValueBean> tipos = new ArrayList<LabelValueBean>();

        if (name != null)
            tipos.add(new LabelValueBean(name, value));

        List<RoomClassification> roomClassifications = RootDomainObject.getInstance().getRoomClassification();
        for (RoomClassification classification : RoomClassification.sortByRoomClassificationAndCode(roomClassifications)) {          
            if(classification.hasParentRoomClassification()) {
                tipos.add(new LabelValueBean(classification.getPresentationCode() + " - " 
            	    + classification.getName().getContent(Language.getLanguage()), classification.getIdInternal().toString()));
            }
	}
               
        return tipos;
    }

    public static List<LabelValueBean> getDaysOfWeek() {
        List<LabelValueBean> weekDays = new ArrayList<LabelValueBean>();
        weekDays.add(new LabelValueBean("segunda", (new Integer(DiaSemana.SEGUNDA_FEIRA)).toString()));
        weekDays.add(new LabelValueBean("terça", (new Integer(DiaSemana.TERCA_FEIRA)).toString()));
        weekDays.add(new LabelValueBean("quarta", (new Integer(DiaSemana.QUARTA_FEIRA)).toString()));
        weekDays.add(new LabelValueBean("quinta", (new Integer(DiaSemana.QUINTA_FEIRA)).toString()));
        weekDays.add(new LabelValueBean("sexta", (new Integer(DiaSemana.SEXTA_FEIRA)).toString()));
        weekDays.add(new LabelValueBean("sábado", (new Integer(DiaSemana.SABADO)).toString()));

        return weekDays;
    }

    public static List<String> getHours() {
        List<String> hoursList = new ArrayList<String>();
        for (int i = 8; i <= 23; i++) {
            hoursList.add(String.valueOf(i));    
	}        
        return hoursList;
    }

    public static List<String> getMinutes() {
        List<String> minutesList = new ArrayList<String>();
        minutesList.add("00");
        minutesList.add("30");
        return minutesList;
    }

    public static List<String> getDaysOfMonth() {
        List<String> daysOfMonthList = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            daysOfMonthList.add(String.valueOf(i));    
	}      
        return daysOfMonthList;
    }

    public static List<LabelValueBean> getMonthsOfYear() {
        List<LabelValueBean> monthsOfYearList = new ArrayList<LabelValueBean>();

        monthsOfYearList.add(new LabelValueBean("Janeiro", "" + Calendar.JANUARY));
        monthsOfYearList.add(new LabelValueBean("Fevereiro", "" + Calendar.FEBRUARY));
        monthsOfYearList.add(new LabelValueBean("Março", "" + Calendar.MARCH));
        monthsOfYearList.add(new LabelValueBean("Abril", "" + Calendar.APRIL));
        monthsOfYearList.add(new LabelValueBean("Maio", "" + Calendar.MAY));
        monthsOfYearList.add(new LabelValueBean("Junho", "" + Calendar.JUNE));
        monthsOfYearList.add(new LabelValueBean("Julho", "" + Calendar.JULY));
        monthsOfYearList.add(new LabelValueBean("Agosto", "" + Calendar.AUGUST));
        monthsOfYearList.add(new LabelValueBean("Setembro", "" + Calendar.SEPTEMBER));
        monthsOfYearList.add(new LabelValueBean("Outubro", "" + Calendar.OCTOBER));
        monthsOfYearList.add(new LabelValueBean("Novembro", "" + Calendar.NOVEMBER));
        monthsOfYearList.add(new LabelValueBean("Dezembro", "" + Calendar.DECEMBER));

        return monthsOfYearList;
    }

    public static List<LabelValueBean> getExamSeasons() {
        List<LabelValueBean> examSeasonsList = new ArrayList<LabelValueBean>();

        examSeasonsList.add(new LabelValueBean(Season.SEASON1_STRING, "" + Season.SEASON1));
        examSeasonsList.add(new LabelValueBean(Season.SEASON2_STRING, "" + Season.SEASON2));

        return examSeasonsList;
    }

  
    public static List<String> getYears() {
        List<String> yearsList = new ArrayList<String>();

        yearsList.add("2002");
        yearsList.add("2003");
        yearsList.add("2004");
        return yearsList;
    }
    
    public static List<String> getExamShifts() {
        List<String> hoursList = new ArrayList<String>();
        hoursList.add("9");
        hoursList.add("13");
        hoursList.add("17");
        return hoursList;
    }

}