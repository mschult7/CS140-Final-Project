package project;

import java.io.*;
import java.util.*;
import static project.Instruction.*;

public class FullAssembler implements Assembler {
	private boolean readingCode = true;
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {

		if(error==null) {
			throw new IllegalArgumentException("Error cant be null");
		}
		ArrayList<String> list = new ArrayList<String>();
		try (Scanner s = new Scanner(new File(inputFileName))){
			while (s.hasNextLine()){
				list.add(s.nextLine());
			}	
		} catch(FileNotFoundException e) {
			error.append("\n File not found");
			return -1;
		}
		boolean checkBlank = false;
		int lineErr = 0;
		int retval = 0;
		for(int i=0;i<list.size();i++) {
			String line = list.get(i);
			String[] parts = line.trim().split("\\s+");
			//Checks for blank line error
			if(line.trim().length() == 0 && readingCode) {
				checkBlank=true;
				lineErr = i+1;
			} else if(checkBlank){
				error.append("\nIllegal blank line in the source file on line " + lineErr);
				retval = lineErr;
				checkBlank = false;
			}
			//illegal white space
			if(!checkBlank && readingCode) {
				if(line.charAt(0)==(' ')|| line.charAt(0)==('\t')) {

					lineErr = i+1;
					error.append("\nLine " + lineErr + " starts with illegal white space");
					retval = lineErr;
				}
			}

			//DATA separator error
			if(!checkBlank && line.trim().toUpperCase().equals("DATA") && readingCode) {
				if(readingCode==false) {
					lineErr = i+1;
					error.append("\n Extra DATA segments on line " + lineErr);
					retval = lineErr;
				}
				readingCode = false;
				if(!line.trim().equals("DATA")) {
					lineErr = i+1;
					error.append("\nLine does not have DATA in upper case");
					retval = lineErr;
				}

			}
			//opcode checker
			if(!checkBlank && !opcodes.keySet().contains(line.trim().split("\\s+")[0]) && readingCode) {
				if(opcodes.keySet().contains(line.trim().split("\\s+")[0].toUpperCase())) {
					lineErr = i+1;
					error.append("\nMneumonic must be uppercase in line " + lineErr);
					retval = lineErr;
				} else {
					lineErr = i+1;
					error.append("\nIllegal Mneumonic on line " + lineErr);

					retval = lineErr;
				}
			}
			//mneumonic argument checker
			if(!checkBlank && opcodes.keySet().contains(parts[0]) && readingCode) {
				if(Assembler.noArgument.contains(line.trim().split("\\s+")[0])) {
					if(parts.length!=1) {
						lineErr = i+1;
						error.append("\nError on line " + (i+1) + ": this mnemonic cannot take arguments");
						retval = lineErr;
					}
				} else {
					if(parts.length>2) {
						lineErr = i+1;
						error.append("\nError on line " + (i+1) + ": this mnemonic has too many arguments");
						retval = lineErr;
					} else if(parts.length<2) {
						lineErr = i+1;
						error.append("\nError on line " + (i+1) + ": this mnemonic is missing an argument");
						retval = lineErr;
					} else {
						try{
							int flags = 0;
							if(parts[1].charAt(0) == '#') {
								flags = 2;
								parts[1] = parts[1].substring(1);

							}else if(parts[1].charAt(0) == '@') {
								flags =4;
								parts[1] = parts[1].substring(1);
							}else if(parts[1].charAt(0) == '&') {
								flags = 6;
								parts[1] = parts[1].substring(1);
							}
							int arg = Integer.parseInt(parts[1],16);
							//.. the rest of setting up the opPart
						} catch(NumberFormatException e) {
							error.append("\nError on line " + (i+1) + 
									": argument is not a hex number");
							retval = i + 1;				
						} // At this point, all the code input has been put in a List and i is the current index
						// so the line number is 1 larger than the index (index 0 corresponds to line 1)

					}
				}
			}

		}
		System.out.println(error);
		return retval;
	}
	public static void main(String[] args) {
		StringBuilder error = new StringBuilder();
		System.out.println("Enter the name of the file without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) { 
			String filename = keyboard.nextLine();
			//			System.out.println(new SimpleAssembler().assemble(filename + ".pasm", 
			//					filename + ".pexe", error));
			System.out.println(new FullAssembler().assemble(filename + ".pasm", filename + ".pexe", error));
		}
	}
}
