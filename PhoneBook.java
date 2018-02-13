package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {

	class ValidationException extends Exception{
	}
	
	class PhoneBookRecord{
		String phoneNumber;
		Integer outCalls;
		
		public PhoneBookRecord(String pn, Integer c){
			this.phoneNumber = pn;
			this.outCalls = c;
		}

		public String getPhoneNumber() {
			return this.phoneNumber;
		}
		
		public void setOutCalls(Integer calls) {
			this.outCalls = calls;
		}

		public Integer getOutCalls() {
			return this.outCalls;
		}

		@Override
		public String toString() {
			return phoneNumber + " " + outCalls;
		}
	}

	public static Comparator<PhoneBookRecord> PhoneBookComparator = new Comparator<PhoneBookRecord>() {
		@Override
		public int compare(PhoneBookRecord first, PhoneBookRecord second) {
		    // sort in descending order
		    return second.getOutCalls() - first.getOutCalls();
	    }};

	private Map<String, PhoneBookRecord> nameCatalog;
	//store the 5 top numbers 
	private TreeSet<PhoneBookRecord> mostUsed; 

	public PhoneBook(){
		this.nameCatalog = new TreeMap<String, PhoneBookRecord>();
		this.mostUsed = new TreeSet <PhoneBookRecord>(PhoneBookComparator);
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

	private String normalize (String phoneNumber) throws ValidationException {
		// Transforms a valid phone number into normalized format
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
		this.nameCatalog.put(name, new PhoneBookRecord(number, 0));
	}

	public void remove (String name) {
		this.nameCatalog.remove(name);
	}

	public String getPhoneNumber(String name) {
		return nameCatalog.get(name).getPhoneNumber();
	}
	
	public Integer getOutCalls(String name) {
		return nameCatalog.get(name).getOutCalls();
	}

	public void readFromFile(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                if (line.equals("")){
                	//empty line
                	continue;
                }
                String [] line_parts = line.split("\\s+");
                
                if (line_parts.length == 1) {
                	//invalid record, ignoring
                	continue;
                }
                
                StringBuilder name = new StringBuilder();
                String phoneNumber = line_parts[line_parts.length -1];
                
                for (int i=0; i<line_parts.length-1; i++) {
                	name.append(line_parts[i] + " ");
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

	public void makeCall(String name) {
		/*
		 * Increment the calls and update mostUsed if needed
		 */
		PhoneBookRecord record = nameCatalog.get(name);
		if (record != null) {
			Integer calls = record.getOutCalls();
			calls++;
			record.setOutCalls(calls);
			nameCatalog.put(name, record);

			// Check if this record is not in the top used
			if (!mostUsed.contains(record)) {
				if(mostUsed.size() < 5) {
					mostUsed.add(record);
				}else {
					// Get the last element in mostUsed
					PhoneBookRecord last = mostUsed.last();
					if (calls > last.getOutCalls()) {
						mostUsed.remove(last);
						mostUsed.add(record);
					}
				}
			}
		}
	}

	public void print() {
		for (Map.Entry<String, PhoneBookRecord> entry : nameCatalog.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue().getPhoneNumber());
		}
	}

	public void printCalls() {
		//Collection <PhoneBookRecord>  records = nameCatalog.values();
		List<PhoneBookRecord> records = new ArrayList<PhoneBookRecord>(nameCatalog.values());
		Collections.sort(records, PhoneBookComparator);
		System.out.println(records);
	}
	
	public void printMostUsed() {
		for(PhoneBookRecord record: mostUsed){
			System.out.println(record);
		}
	}
}
