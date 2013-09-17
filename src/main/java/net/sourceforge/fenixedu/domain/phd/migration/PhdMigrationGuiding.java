package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.Map;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean.PhdParticipantSelectType;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean.PhdParticipantType;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PhdMigrationGuidingNotFoundException;

public class PhdMigrationGuiding extends PhdMigrationGuiding_Base {
    public static final String IST_INSTITUTION_CODE = "0807";

    private transient PhdMigrationGuidingBean guidingBean;

    protected PhdMigrationGuiding() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected PhdMigrationGuiding(String data) {
        super();
        setData(data);
    }

    public class PhdMigrationGuidingBean {

        private transient String data;

        private transient Integer phdStudentNumber;
        private transient String institutionCode;
        private transient String name;
        private transient String teacherCode;

        public PhdMigrationGuidingBean(String data) {
            setData(data);
            parse();
        }

        public void parse() {
            try {
                String[] compounds = getData().split("\\t");

                this.phdStudentNumber = Integer.parseInt(compounds[0].trim());
                this.teacherCode = compounds[2].trim();
                this.institutionCode = compounds[3].trim();
                this.name = compounds[4].trim();
            } catch (NoSuchElementException e) {
                throw new IncompleteFieldsException();
            }
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Integer getPhdStudentNumber() {
            return phdStudentNumber;
        }

        public void setPhdStudentNumber(Integer phdStudentNumber) {
            this.phdStudentNumber = phdStudentNumber;
        }

        public String getInstitutionCode() {
            return institutionCode;
        }

        public void setInstitutionCode(String institutionCode) {
            this.institutionCode = institutionCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacherCode() {
            return teacherCode;
        }

        public void setTeacherCode(String teacherCode) {
            this.teacherCode = teacherCode;
        }

    }

    public boolean hasPersonalBean() {
        return guidingBean != null;
    }

    public PhdMigrationGuidingBean getGuidingBean() {
        if (hasPersonalBean()) {
            return guidingBean;
        }

        guidingBean = new PhdMigrationGuidingBean(getData());
        return guidingBean;
    }

    public void setGuidingBean(PhdMigrationGuidingBean guidingBean) {
        this.guidingBean = guidingBean;
    }

    public void parseAndSetNumber(Map<String, String> INSTITUTION_MAP) {
        final PhdMigrationGuidingBean guidingBean = getGuidingBean();

        setTeacherNumber(guidingBean.getTeacherCode());
        setInstitution(INSTITUTION_MAP.get(guidingBean.getInstitutionCode()));
    }

    public void parse() {
        getGuidingBean();
    }

    public boolean isExternal() {
        return !getGuidingBean().getInstitutionCode().equals(IST_INSTITUTION_CODE);
    }

    public PhdParticipantBean getPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {

        if (isExternal()) {
            return getExternalPhdParticipantBean(individualProcess);
        } else {
            return getInternalPhdParticipantBean(individualProcess);
        }

    }

    private PhdParticipantBean getExternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
        final PhdParticipantBean participantBean = new PhdParticipantBean();
        participantBean.setParticipantType(PhdParticipantType.EXTERNAL);
        participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
        participantBean.setIndividualProgramProcess(individualProcess);
        participantBean.setName(getGuidingBean().getName());
        participantBean.setWorkLocation(getInstitution());
        participantBean.setInstitution(getInstitution());

        return participantBean;
    }

    private PhdParticipantBean getInternalPhdParticipantBean(final PhdIndividualProgramProcess individualProcess) {
        final PhdParticipantBean participantBean = new PhdParticipantBean();
        participantBean.setIndividualProgramProcess(individualProcess);
        final Teacher teacher =
                Employee.readByNumber(Integer.valueOf(getGuidingBean().getTeacherCode())).getPerson().getTeacher();

        if (teacher == null) {
            throw new PhdMigrationGuidingNotFoundException("The guiding is not present in the system as a teacher");
        }

        for (PhdParticipant existingParticipant : individualProcess.getParticipants()) {
            if (!existingParticipant.isInternal()) {
                continue;
            }

            final InternalPhdParticipant existingInternalParticipant = (InternalPhdParticipant) existingParticipant;
            final Person existingInternalPerson = existingInternalParticipant.getPerson();

            if (teacher.getPerson() == existingInternalPerson) {
                // The guider is already associated with the process
                participantBean.setInternalParticipant(teacher.getPerson());
                participantBean.setParticipant(existingParticipant);
                participantBean.setParticipantSelectType(PhdParticipantSelectType.EXISTING);

                return participantBean;
            }
        }

        // The guiding is in the system as teacher, but not yet associated with
        // the process
        participantBean.setParticipantSelectType(PhdParticipantSelectType.NEW);
        participantBean.setInternalParticipant(teacher.getPerson());
        participantBean.setInstitution(getInstitution());
        participantBean.setWorkLocation(getInstitution());
        return participantBean;
    }

    @Deprecated
    public boolean hasData() {
        return getData() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasTeacherNumber() {
        return getTeacherNumber() != null;
    }

    @Deprecated
    public boolean hasPhdMigrationProcess() {
        return getPhdMigrationProcess() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

    @Deprecated
    public boolean hasParseLog() {
        return getParseLog() != null;
    }

}
