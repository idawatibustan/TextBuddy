import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

class Command{
	public String type;
	public String detail;
	
	//constructor
	public Command(String type, String detail){
		this.type = type;
		this.detail = detail;
	}	
}

public class TextBuddy{
	//attributes
	public String fileName;
	public File file;
	public int numEntry;
	
	//constructor
	public TextBuddy(String name) {
		this.fileName = name;
		this.file = new File(name);
		try{
			if(!file.exists()){
				file.createNewFile(); //create if file not exist
			}
		} catch (IOException e){ // there is an error in creating the file
			e.printStackTrace(); // log the error
		}
		this.numEntry = 0;
	}
	
	//messages constants
	public static final String MESSAGE_WELCOME = "Welcome to TextBuddy. ";
	public static final String MESSAGE_FILE_READY= " is ready for use";
	public static final String MESSAGE_COMMAND = "command: ";
	public static final String MESSAGE_ADD = "added to ";
	public static final String MESSAGE_DELETE = "deleted from ";
	public static final String MESSAGE_CLEAR = "all content deleted from ";
	public static final String MESSAGE_EMPTY = " is empty";
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		TextBuddy tb = new TextBuddy(args[0]);
		toUser(MESSAGE_WELCOME + tb.fileName + MESSAGE_FILE_READY);
		
		while(true){
			toUser(MESSAGE_COMMAND);
			tb.executeCommand(sc.nextLine());
		}
//			String commandLine = sc.nextLine();
//			switch(getFirstWord(commandLine)){
//			case "add":
//				doAdd(removeFirstWord(commandLine), tb1.file, tb1.fileName); break;
//			case "display":
//				int count = doDisplay(tb1.file); 
//				if(count == 0){toUser(tb1.fileName + MESSAGE_EMPTY);} break;
//			case "delete":
//				
//				String content = doDelete(Integer.valueOf(removeFirstWord(commandLine)), tb1.file); 
//				toUser(MESSAGE_DELETE + tb1.fileName + ": \"" + content + "\""); break;
//			case "clear":
//				doClear(tb1.file); toUser(MESSAGE_CLEAR + tb1.fileName); break;
//			case "exit":
//				System.exit(0);
//			default: toUser("Command Error!");
//			}
	}
	
	//print to user
	public static void toUser(String msg){
		System.out.println(msg);
	}

	//textbuddy instance - execute relevant command
	public void executeCommand(String commandLine) throws IOException{
		Command cmd = new Command(getFirstWord(commandLine), removeFirstWord(commandLine));
		switch(cmd.type){
		case "add":
			this.doAdd(cmd.detail); this.numEntry++; break;
		case "display":
			if(this.numEntry == 0){toUser(this.fileName + MESSAGE_EMPTY);}
			this.doDisplay();
			break;
		case "clear":
			this.doClear(); this.numEntry = 0; break;
//		case "delete":			
//			String content = doDelete(Integer.valueOf(removeFirstWord(commandLine)), tb1.file); 
//			toUser(MESSAGE_DELETE + tb1.fileName + ": \"" + content + "\""); break;
		case "exit":
			System.exit(0);
		default: toUser("Command Error!");
		}
	}
	
	//get first word: command word
	public static String getFirstWord(String string){
		return string.trim().split("\\s+")[0];
	}
	
	//remove first word: command word
	public static String removeFirstWord(String string){
		return string.replace(getFirstWord(string),"").trim();
	}
	
	//execute command: add
	public void doAdd(String content) throws IOException{
		FileWriter fw = new FileWriter(this.file, true);
		fw.write(content);
		fw.write(System.lineSeparator());
		fw.close();
		toUser("added to " + this.fileName + ": \"" + content + "\"");
	}
	
	//execute command: display
	public void doDisplay() throws IOException{
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(this.file));
		while(count != this.numEntry){
			String line = new String(br.readLine());
			toUser(++count + ". " + line);
		}
		br.close();
	}
	
	//execute command: clear
	public void doClear() throws IOException{
		FileWriter fw = new FileWriter(this.file, false);
		fw.write("");
		fw.close();
		toUser(MESSAGE_CLEAR + this.fileName);
	}
	
	//execute command: delete
	public static String doDelete(Integer line, File file) throws IOException{
		File temp = new File("temp.txt");
		try{
			//if file doesnt exists, create file
			if(!temp.exists()){
				temp.createNewFile();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		FileWriter fw = new FileWriter(temp, true);
		
		String content = new String();
		String tempContent = new String();
		int count = 1;
		while(true){
			tempContent = br.readLine();
			if(tempContent == null){
				break;
			}
			if(count != line && tempContent != null){
				fw.write(tempContent);
				fw.write(System.lineSeparator());
			} else {
				content = tempContent;
			}
			count++;
		}		
		br.close();
		fw.close();

		if(!file.delete()){
			toUser("Could not delete file");
		}
		
		if(!temp.renameTo(file)){
			toUser("Could not rename file");
		}
		
		return content;
	}

}