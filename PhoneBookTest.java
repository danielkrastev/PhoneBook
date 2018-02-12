package test;

public class PhoneBookTest {
	
	public void test() {
		try {
			assert(0>45);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args) {
		PhoneBook phoneBook = new PhoneBook();
		String phoneBookFile = new PhoneBookTest().getClass().getResource("phone_numbers.txt").toString();
		phoneBook.readFromFile(phoneBookFile.replace("file:", ""));
		phoneBook.print();
		assert phoneBook.validate("0879598304");
	}
	
}

