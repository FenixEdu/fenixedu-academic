/**
 * 
 */

package net.sourceforge.fenixedu.domain.accessControl;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 0:35:00,22/Set/2005
 * @version $Id$
 */
public enum GroupTypes {
	UNION_GROUP {
		@Override
		public Class getImplementationClass() {
			return GroupUnion.class;
		}
	},
	INTERSECTION_GROUP {
		@Override
		public Class getImplementationClass() {
			return GroupIntersection.class;
		}
	},
	DIFFERENCE_GROUP {
		@Override
		public Class getImplementationClass() {
			return GroupDifference.class;
		}
	},
	EXECUTION_COURSE_STUDENTS_GROUP {
		@Override
		public Class getImplementationClass() {
			return ExecutionCourseStudentsGroup.class;
		}
	},
	EXECUTION_COURSE_TEACHERS_GROUP {
		@Override
		public Class getImplementationClass() {
			return ExecutionCourseTeachersGroup.class;
		}
	},
	PERSON_GROUP {
		@Override
		public Class getImplementationClass() {
			return PersonGroup.class;
		}
	},
	ROLE_GROUP {
		@Override
		public Class getImplementationClass() {
			return RoleGroup.class;
		}
	};
	abstract public Class getImplementationClass();

	public static GroupTypes userGroupTypeByClass(Class clazz) throws InvalidUserGroupTypeException {
		GroupTypes result = null;
		GroupTypes[] types = GroupTypes.values();
		for (GroupTypes type : types) {
			if (type.getImplementationClass().equals(clazz)) {
				result = type;
			}
		}

		return result;
	}
}
