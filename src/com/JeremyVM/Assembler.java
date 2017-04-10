package com.JeremyVM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
class Func{
	public String name = "";
	public int loc = 0;
}
public class Assembler {
	private static 
	ArrayList<Func> macros = new ArrayList<Func>();
	public static void main(String[] args) throws Exception {

		if (args.length == 2) {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String s = "";
			String all = "";
			while (s != null) {
				all += s + "\n";
				s = br.readLine();
			}
			all = all.substring(1, all.length() - 1);
			br.close();
			
			premacro(all);
			int[] bytecode = tokenize(all);
			BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
			for(int i=0;i<bytecode.length;i++){
				bw.write(bytecode[i]);
				if(bytecode[i]<256){
					bw.write(0);
					bw.write(0);
					bw.write(0);
				}else if (bytecode[i]<65536){
					bw.write(0);
					bw.write(0);
				}else if(bytecode[i]<16777216){
					bw.write(0);
				}
			}
			bw.flush();
			bw.close();
		} else {
			System.out.println("JeremyVM Assembler\nCreated By Jeremy Chow, 2016");
		}
	}

	private static void premacro(String all) {
		String[] parts = all.split("\\s");
		int i=0;
		for(String part:parts){
			if(part.startsWith(":")){
				i--;
				Func m=new Func();
				m.loc = i;
				m.name = part.substring(1);
				macros.add(m);
				System.out.println("Function:"+m.name+" "+m.loc);
				
			}
			if(part.length()!=0)
			i++;
		}
	}

	private static int[] tokenize(String all) {
		String[] parts = all.split("\\s");
		ArrayList<Integer> bytecode = new ArrayList<Integer>();
		for(String part:parts){
			try{
				bytecode.add(Integer.parseInt(part));
				continue;
			}catch (NumberFormatException nfe){
				switch(part.toUpperCase()){
				case "HALT":
					bytecode.add(0);
					break;
				case "POP":
					bytecode.add(1);
					break;
				case "PUSH":
					bytecode.add(2);
					break;
				case "IADD":
					bytecode.add(3);
					break;
				case "ISUB":
					bytecode.add(4);
					break;
				case "IMUL":
					bytecode.add(5);
					break;
				case "IDIV":
					bytecode.add(6);
					break;
				case "PRINT":
					bytecode.add(7);
					break;
				case "STORE":
					bytecode.add(8);
					break;
				case "LOAD":
					bytecode.add(9);
					break;
				case "CALL":
					bytecode.add(10);
					break;
				case "RET":
					bytecode.add(11);
					break;
				case "CMP":
					bytecode.add(12);
					break;
				case "JNE":
					bytecode.add(13);
					break;
				case "JE":
					bytecode.add(14);
					break;
				case "MSTORE":
					bytecode.add(15);
					break;
				case "":
					break;
				default:
					if(part.toUpperCase().startsWith(":"))
						break;
					else{
						boolean breakNow = false;
						for(Func macro : macros){
							if(part.contains(macro.name)&&part.length()==macro.name.length()){
								bytecode.add(macro.loc);
								breakNow = true;
								break;
							}
						}
						if(breakNow){
							break;
						}
					}
					try {
						throw new SyntaxException(part, "Unidentified Word");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				continue;
			}
		}
		int[] bc = new int[bytecode.size()];
		for(int i=0;i<bc.length;i++){
			bc[i] = bytecode.get(i);
			System.out.println("Bytecode:"+bc[i]);
		}
		return bc;
	}

}
