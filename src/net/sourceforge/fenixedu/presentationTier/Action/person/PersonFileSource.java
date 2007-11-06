package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public interface PersonFileSource extends Serializable {

	public static Comparator<PersonFileSource> COMPARATOR = new Comparator<PersonFileSource>() {

		public int compare(PersonFileSource o1, PersonFileSource o2) {
			int c = o1.getName().compareTo(o2.getName());
			if (c != 0) {
				return c;
			}
			else {
				int o1Count = o1.getCount();
				int o2Count = o2.getCount();
				
				if (o1Count < o2Count) {
					return -1;
				}
				else if (o1Count > o2Count) {
					return 1;
				}
				else {
					return 0;
				}
			}
		}
		
	};
	
	public MultiLanguageString getName();
	public List<PersonFileSource> getChildren();
	public int getCount();
	public boolean isAllowedToUpload(Person person);
}
