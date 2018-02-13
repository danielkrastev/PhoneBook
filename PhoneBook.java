package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {

	private Map<String, String> nameCatalog;
	private Map<String, Integer> callCatalog;

	public PhoneBook(){
		this.nameCatalog = new TreeMap<String, String>();
		this.callCatalog = new TreeMap<String, Integer>();
	}

	public boolean is_valid (String phoneNumber) {
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

	private String normalize (String phoneNumber) throws ValidationException  {
		if (! is_valid(phoneNumber)) {
			throw new ValidationException();
		}
		if (phoneNumber.startsWith("00")) {
			return phoneNumber.replaceFirst("00", "+");
		}else {
			return phoneNumber.replaceFirst("0", "+359");
		}
	}

	public void add(String name, String phoneNumber) throws ValidationException {
		String number = normalize(phoneNumber);
		this.nameCatalog.put(name, number);
		this.callCatalog.put(number, 0);
	}

	public void remove (String name) {
		this.nameCatalog.remove(name);
	}

	public String get(String name) {
		return nameCatalog.get(name);
	}

	public void readFromFile(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                if (line.equals("")){
                	continue;
                }
                String [] parts = line.split("\\s+");
                StringBuilder name = new StringBuilder();
                String phoneNumber = parts[parts.length -1];
                
                for (int i=0; i<parts.length-1; i++) {
                	name.append(parts[i] + " ");
                }
                try {
                	add(name.toString().trim(), phoneNumber);
                }catch (ValidationException e) {
                	continue;
                }
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

	public void makeCall(String name){
		String number = nameCatalog.get(name);
		if (number != null) {
			Integer calls = callCatalog.get(number);
			callCatalog.put(number, ++calls);
		}
	}

	public void print() {
		for (Map.Entry<String,String> entry : nameCatalog.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}

	public void printCalls() {
		for (Map.Entry<String,Integer> entry : callCatalog.entrySet()) {
			System.out.println(entry.getKey() + "-" + entry.getValue());
		}
	}
}
