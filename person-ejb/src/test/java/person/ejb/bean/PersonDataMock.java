package person.ejb.bean;

import java.util.Arrays;
import java.util.List;

import person.common.pojo.Person;

public class PersonDataMock {

	public static Person nikolaTesla = Person.builder().id(1).firstName("Nikola").lastName("Tesla").build();
	public static Person albertEinstein = Person.builder().id(2).firstName("Albert").lastName("Einstein").build();
	public static Person carlSagan = Person.builder().id(3).firstName("Carl").lastName("Sagan").build();

	public static PersonH2 nikolaTeslaH2 = new PersonH2(1, "Nikola", "Tesla");
	public static PersonH2 albertEinsteinH2 = new PersonH2(2, "Albert", "Einstein");
	public static PersonH2 carlSaganH2 = new PersonH2(3, "Carl", "Sagan");

	private PersonDataMock() {
	}

	public static List<Person> allPersons() {
		return Arrays.asList(nikolaTesla, albertEinstein, carlSagan);
	}

	public static List<PersonH2> allPersonEntities() {
		return Arrays.asList(nikolaTeslaH2, albertEinsteinH2, carlSaganH2);
	}

}
