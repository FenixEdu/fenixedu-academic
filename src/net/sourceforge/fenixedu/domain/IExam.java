package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.Season;

/**
 * 18/Mar/2003
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public interface IExam extends IWrittenEvaluation {

    public Season getSeason();

    public List getAssociatedRooms();

    public void setSeason(Season epoca);

}