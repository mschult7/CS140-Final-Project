package project;

public class Test {
	public static void main(String[] args) {
		Instruction instr = new Instruction((byte)0xB, 100);
		Instruction.checkParity(instr);
		
	}
}
