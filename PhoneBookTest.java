package test;

public class PhoneBookTest {

	public static void main(String [] args) {
		PhoneBook phoneBook = new PhoneBook();
		String phoneBookFile = new PhoneBookTest().getClass().getResource("phone_numbers.txt").toString();
		phoneBook.readFromFile(phoneBookFile.replace("file:", ""));
		phoneBook.print();
		assert phoneBook.is_valid("0879598304");

		phoneBook.makeCall("Dimitar Petrov");
		phoneBook.makeCall("Peter Kolarov");
		phoneBook.makeCall("Peter Andreev");
		phoneBook.makeCall("Ivan Ivanov");
		phoneBook.makeCall("Nikolay Petrov");
		phoneBook.makeCall("Dimitar Petrov");
		phoneBook.makeCall("Peter Kolarov");
		phoneBook.makeCall("Dimitar Petrov");
		phoneBook.makeCall("Peter Andreev");
		phoneBook.makeCall("Peter Asenov");
		phoneBook.makeCall("Nikolay Petrov");
		phoneBook.makeCall("Ivan Ivanov");
		phoneBook.makeCall("Dimitar Petrov");

		phoneBook.printCalls();
		phoneBook.printMostUsed();
	}
}
