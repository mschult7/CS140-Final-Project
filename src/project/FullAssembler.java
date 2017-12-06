package project;

import java.io.*;
import java.util.*;
import static project.Instruction.*;

public class FullAssembler implements Assembler {
	private boolean readingCode = true;
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {


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

		int lineErr = -1;
		for(int i=0;i<list.size();i++) {
			String line = list.get(i);
			//Checks for blank line error
			if(line.trim().length() == 0) {
				checkBlank=true;
				lineErr = i+1;
			} else if(checkBlank){
				error.append("\nIllegal blank line(" + lineErr + ") in the source file");
				checkBlank = false;
			}
			//illegal white space
			if(line.charAt(0)==' ' || line.charAt(0)==('\t')) {
				lineErr = i+1;
				error.append("\nLine " + lineErr + " starts with illegal white space");
			}
			//DATA separator error
			if(line.trim().toUpperCase().equals("DATA")) {
				if(readingCode==false) {
					lineErr = i+1;
					error.append("\n Extra DATA segments on line " + i+1);
				}
				readingCode = false;
				if(!line.trim().equals("DATA")) {
					lineErr = i+1;
					error.append("\nLine does not have DATA in upper case");
				}

			}
			//opcode checker
			if(!opcodes.keySet().contains(line.trim().split("\\s+")[0])) {
				if(opcodes.keySet().contains(line.trim().split("\\s+")[0].toUpperCase())) {
					lineErr = i+1;
					error.append("\nMneumonic must be uppercase in line " + i+1);
				} else {
					error.append("\nIllegal Mneumonic on line " + i+1);
					lineErr = i+1;
				}			
			}
			//
		}


		return lineErr;
	}

}
