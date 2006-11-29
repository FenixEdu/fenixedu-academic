package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurricularPeriodTest extends DomainTestBase {

    private CurricularPeriod curricularPeriodThreeYear;

    private CurricularPeriod curricularPeriodTwoYear;

    private CurricularPeriod curricularPeriodYear1;

    private CurricularPeriod curricularPeriodYear2;

    private CurricularPeriod curricularPeriodYear3;

    private CurricularPeriod curricularPeriodSemester1;

    private CurricularPeriod curricularPeriodSemester2;

    private CurricularPeriod curricularPeriodSemester3;

    private CurricularPeriod curricularPeriodSemester4;

    protected void setUpAddCase() {

        curricularPeriodThreeYear = new CurricularPeriod(CurricularPeriodType.THREE_YEAR);
        curricularPeriodTwoYear = new CurricularPeriod(CurricularPeriodType.TWO_YEAR);
        curricularPeriodYear1 = new CurricularPeriod(CurricularPeriodType.YEAR);
        curricularPeriodYear2 = new CurricularPeriod(CurricularPeriodType.YEAR);
        curricularPeriodYear3 = new CurricularPeriod(CurricularPeriodType.YEAR);
        curricularPeriodSemester1 = new CurricularPeriod(CurricularPeriodType.SEMESTER);
        curricularPeriodSemester2 = new CurricularPeriod(CurricularPeriodType.SEMESTER);
        curricularPeriodSemester3 = new CurricularPeriod(CurricularPeriodType.SEMESTER);
        curricularPeriodSemester4 = new CurricularPeriod(CurricularPeriodType.SEMESTER);

        curricularPeriodTwoYear.addChilds(curricularPeriodYear1);
        curricularPeriodTwoYear.addChilds(curricularPeriodYear2);

        curricularPeriodYear1.addChilds(curricularPeriodSemester1);
        curricularPeriodYear1.addChilds(curricularPeriodSemester2);

        curricularPeriodSemester4.setChildOrder(2);
        curricularPeriodYear2.addChilds(curricularPeriodSemester4);
        curricularPeriodSemester3.setChildOrder(1);
        curricularPeriodYear2.addChilds(curricularPeriodSemester3);

    }

    public void testAddChilds() {

        setUpAddCase();

        // test adding new childs with over-weight
        try {
            curricularPeriodTwoYear.addChilds(curricularPeriodYear3);
            fail("Expected DomainException: should not have been added because of the weight out of limits.");
        } catch (DomainException e) {
        }

        try {
            curricularPeriodYear1.addChilds(curricularPeriodYear3);
            fail("Expected DomainException: should not have been added because of the weight out of limits.");
        } catch (DomainException e) {
        }

        // test parents
        assertEquals(curricularPeriodYear1.getParent(), curricularPeriodTwoYear);
        assertEquals(curricularPeriodYear2.getParent(), curricularPeriodTwoYear);
        assertEquals(curricularPeriodSemester1.getParent(), curricularPeriodYear1);
        assertEquals(curricularPeriodSemester1.getParent(), curricularPeriodYear1);

        // test childs
        assertTrue(curricularPeriodTwoYear.hasAnyChilds());
        assertEquals(Integer.valueOf(curricularPeriodTwoYear.getChildsCount()), Integer.valueOf(2));
        assertFalse(curricularPeriodSemester1.hasAnyChilds());

        // test non-ordered addings
        assertEquals(curricularPeriodYear1.getChildOrder(), Integer.valueOf(1));
        assertEquals(curricularPeriodYear2.getChildOrder(), Integer.valueOf(2));
        assertEquals(curricularPeriodSemester1.getChildOrder(), Integer.valueOf(1));
        assertEquals(curricularPeriodSemester2.getChildOrder(), Integer.valueOf(2));

        // test ordered addings
        assertEquals(curricularPeriodSemester3.getChildOrder(), Integer.valueOf(1));
        assertEquals(curricularPeriodSemester4.getChildOrder(), Integer.valueOf(2));

        // test overwrite child
        try {
            curricularPeriodYear2.addChilds(curricularPeriodSemester1);
            fail("Expected DomainException: should not have been added because of the child already exists.");
        } catch (DomainException e) {
        }

    }

    public void testGetChildByOrder() {

        setUpAddCase();

        assertEquals(curricularPeriodTwoYear.getChildByOrder(2), curricularPeriodYear2);
        assertNull(curricularPeriodYear1.getChildByOrder(0));
        assertNull(curricularPeriodYear1.getChildByOrder(3));

    }

    public void testGetCurricularPeriod() {

        setUpAddCase();

        CurricularPeriod curricularPeriodRetrieved = curricularPeriodTwoYear.getCurricularPeriod(
                new CurricularPeriodInfoDTO(2, CurricularPeriodType.YEAR), new CurricularPeriodInfoDTO(
                        1, CurricularPeriodType.SEMESTER));

        assertEquals(curricularPeriodRetrieved, curricularPeriodSemester3);

        curricularPeriodRetrieved = curricularPeriodTwoYear.getCurricularPeriod(
                new CurricularPeriodInfoDTO(2, CurricularPeriodType.YEAR), new CurricularPeriodInfoDTO(
                        3, CurricularPeriodType.SEMESTER));

        assertNull(curricularPeriodRetrieved);

        curricularPeriodRetrieved = curricularPeriodTwoYear.getCurricularPeriod();
        assertEquals(curricularPeriodTwoYear, curricularPeriodRetrieved);

    }

    public void testCreateCurricularPeriod() {

        setUpAddCase();

        CurricularPeriod curricularPeriodParent = curricularPeriodThreeYear.getCurricularPeriod();
        CurricularPeriod curricularPeriodCreated = new CurricularPeriod(CurricularPeriodType.YEAR, 3,
                curricularPeriodParent);

        curricularPeriodParent = curricularPeriodThreeYear
                .getCurricularPeriod(new CurricularPeriodInfoDTO(3, CurricularPeriodType.YEAR));
        curricularPeriodCreated = new CurricularPeriod(CurricularPeriodType.SEMESTER, 2,
                curricularPeriodParent);

        assertEquals(curricularPeriodCreated.getParent(), curricularPeriodParent);
        assertEquals(curricularPeriodCreated.getParent().getParent(), curricularPeriodThreeYear);

        // test creating a curricular period with overwrite
        try {
            new CurricularPeriod(CurricularPeriodType.SEMESTER, 2, curricularPeriodParent);
            fail("Expected DomainException: should not have been added because of the child already exists.");
        } catch (DomainException e) {
        }

        // test creating a curricular period with over-weight
        new CurricularPeriod(CurricularPeriodType.SEMESTER, 1, curricularPeriodParent);
        try {
            new CurricularPeriod(CurricularPeriodType.SEMESTER, 3, curricularPeriodParent);
            fail("Expected DomainException: should not have been added because of the weight out of limits.");
        } catch (DomainException e) {
        }

    }

    public void testAddCurricularPeriod() {

        setUpAddCase();

        try {
            curricularPeriodThreeYear.addCurricularPeriod(new CurricularPeriodInfoDTO(3,
                    CurricularPeriodType.YEAR),
                    new CurricularPeriodInfoDTO(3, CurricularPeriodType.YEAR));
            fail("Expected DomainException: should not have been added because the path have periods of the same type.");
        } catch (DomainException e) {
        }

        curricularPeriodThreeYear.addCurricularPeriod(new CurricularPeriodInfoDTO(2,
                CurricularPeriodType.SEMESTER),
                new CurricularPeriodInfoDTO(3, CurricularPeriodType.YEAR));

        CurricularPeriod thirdYearPeriod = curricularPeriodThreeYear.getChildByOrder(3);
        assertEquals(thirdYearPeriod.getPeriodType(), CurricularPeriodType.YEAR);

        CurricularPeriod secondSemesterPeriod = thirdYearPeriod.getChildByOrder(2);
        assertEquals(secondSemesterPeriod.getPeriodType(), CurricularPeriodType.SEMESTER);
        assertNull(thirdYearPeriod.getChildByOrder(1));

    }

}
