package net.sourceforge.fenixedu.util;

/**
 * @author David Santos Jan 13, 2004
 */

public class BranchType extends FenixUtil {
    public static final int COMMON_BRANCH_INT = 1;

    public static final int SPECIALIZATION_BRANCH_INT = 2;

    public static final int SECUNDARY_BRANCH_INT = 3;

    public static final String COMMON_BRANCH_STR = "COMNBR";

    public static final String SPECIALIZATION_BRANCH_STR = "SPECBR";

    public static final String SECUNDARY_BRANCH_STR = "SECNBR";

    public static final BranchType COMMON_BRANCH = new BranchType(BranchType.COMMON_BRANCH_INT);

    public static final BranchType SPECIALIZATION_BRANCH = new BranchType(
            BranchType.SPECIALIZATION_BRANCH_INT);

    public static final BranchType SECUNDARY_BRANCH = new BranchType(BranchType.SECUNDARY_BRANCH_INT);

    private int type;

    private BranchType(int type) {
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BranchType) {
            BranchType scopeObj = (BranchType) obj;
            return this.type == scopeObj.getType();
        }
        return false;
    }

    /**
     * @return
     */
    public int getType() {
        return this.type;
    }
}