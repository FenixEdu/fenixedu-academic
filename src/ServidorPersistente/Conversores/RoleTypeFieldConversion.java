/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.RoleType;

/**
 * @author jpvl
 */
public class RoleTypeFieldConversion implements FieldConversion {

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
	 */
	public Object javaToSql(Object obj) throws ConversionException {
		if (obj instanceof RoleType) {
			RoleType roleType = (RoleType) obj;
			return new Integer(roleType.getId());
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
	 */
	public Object sqlToJava(Object obj) throws ConversionException {
		RoleType roleType = null;
		if (obj instanceof Integer) {
			Integer roleTypeId = (Integer) obj;
			switch (roleTypeId.intValue()) {
				case RoleType.PERSON_TYPE :
					roleType = RoleType.PERSON;
					break;
				case RoleType.STUDENT_TYPE :
					roleType = RoleType.STUDENT;
					break;
				case RoleType.TEACHER_TYPE :
					roleType = RoleType.TEACHER;
					break;
				case RoleType.TIME_TABLE_MANAGER_TYPE :
					roleType = RoleType.TIME_TABLE_MANAGER;
					break;
				case RoleType.MASTER_DEGREE_CANDIDATE_TYPE :
					roleType = RoleType.MASTER_DEGREE_CANDIDATE;
					break;
				case RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE :
					roleType = RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE;
					break;
				case RoleType.TREASURY_TYPE :
					roleType = RoleType.TREASURY;
					break;
				case RoleType.COORDINATOR_TYPE :
					roleType = RoleType.COORDINATOR;

			}
		}else{
			throw new IllegalArgumentException ("Illegal role type!("+obj+")");
		}
		return roleType;

	}

}
