package net.sourceforge.fenixedu.tools.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.fenixframework.pstm.Transaction;


/**
 * @author David Santos in Jan 29, 2004
 */

public class VerifyLEECCurricularPlan {
    public static void main(String args[]) {
        try {
            Transaction.begin();

            DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(48);
            List branches = degreeCurricularPlan.getAreas();

            Iterator iterator = branches.iterator();
            while (iterator.hasNext()) {
                Branch branch = (Branch) iterator.next();
                if (branch.getBranchType().equals(BranchType.COMNBR)) {
                    System.out.println("BASES: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.BASE);
                } else {
                    System.out.println("�REA DE ESPECIALIZA��O: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.SPECIALIZATION);
                    System.out.println("�REA SECUND�RIA: [" + branch.getName() + "]");
                    printItForThisAreaWithScopes(branch, AreaType.SECONDARY);
                }
            }

            printItForPrecedences(degreeCurricularPlan);

            Transaction.commit();
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * @param branch
     * @param areaType
     */
    private static void printItForThisAreaWithScopes(Branch branch, AreaType areaType) {
        List<CurricularCourseGroup> groups = branch.readCurricularCourseGroupsByAreaType(areaType);
        Iterator iterator1 = groups.iterator();
        while (iterator1.hasNext()) {
            CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) iterator1.next();
            List scientificAreas = curricularCourseGroup.getScientificAreas();
            Iterator iterator2 = scientificAreas.iterator();

            System.out.println("");
            System.out.print("\t");
            System.out.println("GRUPO: [" + curricularCourseGroup.getIdInternal()
                    + "] N� M�NIMO DE CR�DITOS: ["
                    + curricularCourseGroup.getMinimumCredits().toString()
                    + "] N� M�XIMO DE CR�DITOS: ["
                    + curricularCourseGroup.getMaximumCredits().toString() + "]");

            while (iterator2.hasNext()) {
                ScientificArea scientificArea = (ScientificArea) iterator2.next();

                System.out.println("");
                System.out.print("\t\t");
                System.out.println("�REA CIENT�FICA: [" + scientificArea.getName() + "]");

                List curricularCourses = getCurricularCourseScopesByScientificAreaInGroup(
                        curricularCourseGroup, scientificArea);
                sortCurricularCourseScopes(curricularCourses);

                Iterator iterator3 = curricularCourses.iterator();
                while (iterator3.hasNext()) {
                    CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iterator3
                            .next();
                    if (curricularCourseScope.getEndDate() == null) {
                        String name = curricularCourseScope.getCurricularCourse().getName();
                        String year = curricularCourseScope.getCurricularSemester().getCurricularYear()
                                .getYear().toString();
                        String semester = curricularCourseScope.getCurricularSemester().getSemester()
                                .toString();
                        System.out.print("\t\t\t");
                        System.out.println("NOME: [" + name + "], ANO: [" + year + "], SEMESTRE: ["
                                + semester + "]");
                        //						System.out.println("NOME: [" + name + "], ANO: [" +
                        // year + "], SEMESTRE: ["
                        //                                + semester + "], ID ["
                        //                                +
                        // curricularCourseScope.getCurricularCourse().getIdInternal().toString()
                        //                                + "]");
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * @param curricularCourseGroup
     * @param scientificArea
     * @return curricular course scopes list from the group and belonging to
     *         that scientific area.
     */
    private static List getCurricularCourseScopesByScientificAreaInGroup(
            CurricularCourseGroup curricularCourseGroup, ScientificArea scientificArea) {
        List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
        List curricularCourses = curricularCourseGroup.getCurricularCourses();
        Iterator iterator1 = curricularCourses.iterator();
        while (iterator1.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iterator1.next();
            if (curricularCourse.getScientificArea().equals(scientificArea)) {
                Iterator iterator2 = curricularCourse.getScopes().iterator();
                while (iterator2.hasNext()) {
                    CurricularCourseScope curricularCourseScope = (CurricularCourseScope) iterator2
                            .next();
                    result.add(curricularCourseScope);
                }
            }
        }
        return result;
    }

    /**
     * @param curricularCourseScopes
     */
    private static void sortCurricularCourseScopes(List curricularCourseScopes) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("curricularCourse.name"));
        comparatorChain.addComparator(new BeanComparator("curricularSemester.curricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("curricularSemester.semester"));
        Collections.sort(curricularCourseScopes, comparatorChain);
    }

    /**
     * @param degreeCurricularPlan
     */
    private static void printItForPrecedences(DegreeCurricularPlan degreeCurricularPlan) {
        System.out.println("PRECED�NCIAS:");
        List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses(); 
        sortCurricularCourses(curricularCourses);
        Iterator iterator1 = curricularCourses.iterator();
        while (iterator1.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iterator1.next();
            List precedences = curricularCourse.getPrecedences();
            if (precedences != null && !precedences.isEmpty()) {
                System.out.print("\t");
                System.out.println("DISCIPLINA: [" + curricularCourse.getName()
                        + "] TEM PRECED�NCIA DE:");
                Iterator iterator2 = precedences.iterator();
                while (iterator2.hasNext()) {
                    Precedence precedence = (Precedence) iterator2.next();
                    List restrictions = precedence.getRestrictions();
                    Iterator iterator3 = restrictions.iterator();
                    while (iterator3.hasNext()) {
                        Restriction restriction = (Restriction) iterator3.next();
                        if (restriction instanceof RestrictionByNumberOfDoneCurricularCourses) {
                            RestrictionByNumberOfDoneCurricularCourses actualRestriction = (RestrictionByNumberOfDoneCurricularCourses) restriction;
                            Integer numberOfDoneCurricularCourses = actualRestriction
                                    .getNumberOfCurricularCourses();
                            System.out.print("\t\t");
                            System.out.println(numberOfDoneCurricularCourses + " disciplinas feitas");
                        } else if (restriction instanceof RestrictionDoneCurricularCourse) {
                            RestrictionDoneCurricularCourse actualRestriction = (RestrictionDoneCurricularCourse) restriction;
                            System.out.print("\t\t");
                            System.out.println(actualRestriction.getPrecedentCurricularCourse()
                                    .getName()
                                    + " feita");
                        } else if (restriction instanceof RestrictionPeriodToApply) {
                            RestrictionPeriodToApply actualRestriction = (RestrictionPeriodToApply) restriction;
                            System.out.print("\t\t");
                            System.out.println("no "
                                    + actualRestriction.getPeriodToApplyRestriction().getValue()
                                    + "� semestre");
                        } else {
                            throw new RuntimeException("RESTRI��O DESCONHECIDA!");
                        }
                        if (iterator3.hasNext()) {
                            System.out.print("\t\t\t");
                            System.out.println("E");
                        }
                    }
                    if (iterator2.hasNext()) {
                        System.out.print("\t\t");
                        System.out.println("OU");
                    }
                }
            }
        }
        System.out.println("");
        System.out.println("");
    }

    /**
     * @param curricularCourseList
     */
    private static void sortCurricularCourses(List curricularCourseList) {
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("name"));

        Collections.sort(curricularCourseList, comparatorChain);
    }

}
