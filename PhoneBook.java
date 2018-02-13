package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {

	private HashMap<String, String> phoneBook;

	public PhoneBook(){
		this.phoneBook = new HashMap<String, String>();
	}
	
	public boolean validate(String phoneNumber) {
		/*
		 * Validates a Bulgarian phone number
		 * first digits are either a single 0 or 00359  
		 * next two digits are 87, 88 or 89
		 * next digit is is between 2-9
		 * next are exactly 6 digits between 0-9
		 * */
		Pattern p = Pattern.compile("08[7,8,9][2-9]\\d{6}|003598[7,8,9][2-9]\\d{6}");  
		Matcher m = p.matcher(phoneNumber);  
		return  m.matches();  		
	}

	private String normalize(String phoneNumber) {
		if (phoneNumber.startsWith("00")) {
			return phoneNumber.replaceFirst("00", "+");
		}else if (phoneNumber.startsWith("0")){
			return phoneNumber.replaceFirst("0", "+359");
		}
		return null;
	}

	public void readFromFile(String fileName) {
		String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.equals("")){
                	continue;
                }
                String [] parts = line.split("\\s+");

                StringBuilder name = new StringBuilder();
                String phoneNumber = parts[parts.length -1];

                if (! this.validate(phoneNumber)) {
                	continue;
                }
                
                String normalizedPhoneNumber = this.normalize(phoneNumber); 
                
                for (int i=0; i<parts.length-1; i++) {
                	name.append(parts[i]);
                }
                
                phoneBook.put(name.toString(), normalizedPhoneNumber);
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Could not open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

	public void print() {
		System.out.println(phoneBook.toString());
	}
}
