package net.sourceforge.fenixedu.domain.vigilancy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TestNGBase;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.testng.Assert;
import org.testng.annotations.Test;



public class VigilantGroupTest extends TestNGBase {

//    private Convoke generateConvokeInSpecificState(boolean accepted, boolean active, boolean attended, WrittenEvaluation writtenEvaluation) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
//    
//        Class[] classes = { Boolean.class, Boolean.class, Boolean.class,  WrittenEvaluation.class };
//        Constructor constructor = Convoke.class.getDeclaredConstructor(classes);
//
//        constructor.setAccessible(true);
//        Object[] objects = { accepted, active, attended, writtenEvaluation };
//        Convoke convoke = (Convoke) constructor.newInstance(objects);
//        constructor.setAccessible(false);
//        return convoke;
//    }
//    
    private WrittenEvaluation createWrittenEvaluation() {
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay((new YearMonthDay()).minusMonths(1));
        HourMinuteSecond time = new HourMinuteSecond();
        writtenEvaluation.setBeginningDateHourMinuteSecond(time);
        writtenEvaluation.setEndDateHourMinuteSecond(time.plusHours(3));
        return writtenEvaluation;
    }
    
    @Test(alwaysRun=true)
    public void removeVigilantsFromGroup() {
       VigilantGroup group = new VigilantGroup();
       Vigilant vigilant1 = new Vigilant();
       Vigilant vigilant2 = new Vigilant();
       Vigilant vigilant3 = new Vigilant();
       Vigilant vigilant4 = new Vigilant();
       
//       vigilant4.addConvokes(new Convoke());
       
       group.addVigilants(vigilant1);
       group.addVigilants(vigilant2);
       group.addVigilants(vigilant3);
       
       List<Vigilant> vigilants = new ArrayList<Vigilant> ();
       List<Vigilant> unableToRemove;
       vigilants.add(vigilant1);
       vigilants.add(vigilant2);
       vigilants.add(vigilant3);
       
       
       unableToRemove = group.removeGivenVigilantsFromGroup(vigilants);
       
       Assert.assertTrue(unableToRemove.isEmpty());
       Assert.assertTrue(group.getVigilants().isEmpty());
       
       vigilant1 = new Vigilant();
       vigilant2 = new Vigilant();
       vigilant3 = new Vigilant();
       group.addVigilants(vigilant1);
       group.addVigilants(vigilant2);
       group.addVigilants(vigilant3);
       group.addVigilants(vigilant4);
       
       vigilants.add(vigilant1);
       vigilants.add(vigilant3);
       vigilants.add(vigilant4);
       
       unableToRemove = group.removeGivenVigilantsFromGroup(vigilants);
       Assert.assertFalse(unableToRemove.isEmpty());
       Assert.assertEquals(unableToRemove.size(),1);
       Assert.assertEquals(unableToRemove.get(0),vigilant4);
       Assert.assertEquals(group.getVigilantsCount(),2);
    }
    
    @Test(alwaysRun=true)
    public void testConvokeSelectionPointsAndTeacherOrdenation() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        
        List<Vigilant> vigilants = new ArrayList<Vigilant> ();
        for(int i = 0; i<10; i++) {
            vigilants.add(new Vigilant(new Person()));
        }

        ExecutionPeriod executionPeriod = new ExecutionPeriod();
        executionPeriod.setState(PeriodState.CURRENT);
        
        ExecutionCourse executionCourse = new ExecutionCourse("", "", executionPeriod);
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay((new YearMonthDay()).plusMonths(1));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond((new HourMinuteSecond()).plusHours(3));
        executionCourse.addAssociatedEvaluations(writtenEvaluation);
        
        VigilantGroup group = new VigilantGroup();
        for(Vigilant vigilant : vigilants) {
            group.addVigilants(vigilant);
        }
        group.setConvokeStrategy("ConvokeByPoints");
        
        Vigilant vigilant4 = vigilants.get(3);
//        vigilant4.addConvokes(generateConvokeInSpecificState(true,true,true, createWrittenEvaluation()));
//        vigilant4.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
        
        Vigilant vigilant6 = vigilants.get(5);
//        vigilant6.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
//        vigilant6.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
        
        Vigilant vigilant7 = vigilants.get(6);
//        vigilant7.addConvokes(generateConvokeInSpecificState(true,true,true, createWrittenEvaluation()));
//        vigilant7.addConvokes(generateConvokeInSpecificState(true,true,true, createWrittenEvaluation()));
        Teacher t7 = new Teacher(0, vigilant7.getPerson());
        Professorship professorship = new Professorship();
        professorship.setExecutionCourse(executionCourse);
        t7.addProfessorships(professorship);
        Category category = new Category();
        category.setWeight(6);
//        TeacherLegalRegimen regimen = new TeacherLegalRegimen(t7,category,new Date(), new Date(),0.0,0,LegalRegimenType.TERM_CONTRACT,RegimenType.INTEGRAL_TIME,0);


//        List<Vigilant> sugestion = group.sugestVigilantsToConvoke(writtenEvaluation,5);  
//        Assert.assertEquals(sugestion.get(0),vigilant7);
//        Assert.assertEquals(sugestion.get(1),vigilant6);
//        Assert.assertEquals(sugestion.get(2),vigilant4);
    }
    
    @Test(alwaysRun=true)
    public void sugestionWithIncompatiblePerson() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Vigilant> vigilants = new ArrayList<Vigilant> ();
        for(int i = 0; i<10; i++) {
            vigilants.add(new Vigilant(new Person()));
        }
        
        ExecutionCourse executionCourse = new ExecutionCourse("", "", (ExecutionPeriod) null);
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        writtenEvaluation.setDayDateYearMonthDay((new YearMonthDay()).plusMonths(1));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond((new HourMinuteSecond()).plusHours(3));
        executionCourse.addAssociatedEvaluations(writtenEvaluation);
        
        Vigilant vigilant1 = vigilants.get(0);
        Vigilant vigilant2 = vigilants.get(1);
        vigilant2.setIncompatiblePerson(vigilant1.getPerson());
        
//        vigilant1.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
//        vigilant1.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
//        vigilant2.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
//        vigilant2.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
        
        VigilantGroup group = new VigilantGroup();
        for(Vigilant vigilant : vigilants) {
            group.addVigilants(vigilant);
        }
      
        group.setConvokeStrategy("ConvokeByPoints");
        
//        List<Vigilant> sugestion = group.sugestVigilantsToConvoke(writtenEvaluation,3);
//        Assert.assertEquals(sugestion.get(0),vigilant1);
//        Assert.assertNotSame(sugestion.get(1),vigilant2);
//        Assert.assertNotSame(sugestion.get(2),vigilant2);
        
    }
    @Test(alwaysRun=true)
    public void sugestVigilantsDealingWithUnavailablePeriods() throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Vigilant> vigilants = new ArrayList<Vigilant> ();
        for(int i = 0; i<10; i++) {
            vigilants.add(new Vigilant(new Person()));
        }
        
        ExecutionCourse executionCourse = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        WrittenEvaluation writtenEvaluation = new WrittenEvaluation();
        YearMonthDay currentDate = new YearMonthDay();
        writtenEvaluation.setDayDateYearMonthDay(currentDate.plusMonths(1));
        writtenEvaluation.setBeginningDateHourMinuteSecond(new HourMinuteSecond());
        writtenEvaluation.setEndDateHourMinuteSecond((new HourMinuteSecond()).plusHours(3));
        executionCourse.addAssociatedEvaluations(writtenEvaluation);
        
        Vigilant vigilant1 = vigilants.get(0);
        DateTime begin = new DateTime(currentDate.minusMonths(1).getYear(),currentDate.minusMonths(1).getMonthOfYear(),currentDate.minusMonths(1).getDayOfMonth(),1,0,0,0);
        DateTime end = new DateTime(currentDate.plusMonths(2).getYear(),currentDate.plusMonths(2).getMonthOfYear(),currentDate.plusMonths(2).getDayOfMonth(),1,0,0,0);
        vigilant1.addUnavailablePeriods(new UnavailablePeriod(begin,end,""));
        
//        vigilant1.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
//        vigilant1.addConvokes(generateConvokeInSpecificState(true,true,false, createWrittenEvaluation()));
        
        VigilantGroup group = new VigilantGroup();
        for(Vigilant vigilant : vigilants) {
            group.addVigilants(vigilant);
        }
        group.setConvokeStrategy("ConvokeByPoints");
        
//        List<Vigilant> sugestion = group.sugestVigilantsToConvoke(writtenEvaluation,3);
//        Assert.assertNotSame(sugestion.get(0),vigilant1);
//        Assert.assertNotSame(sugestion.get(1),vigilant1);
//        Assert.assertNotSame(sugestion.get(2),vigilant1);
        
    }
}
