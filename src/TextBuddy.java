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
		tb.executeCommand("clear");
		toUser(MESSAGE_WELCOME + tb.fileName + MESSAGE_FILE_READY);
		
		while(true){
			toUser(MESSAGE_COMMAND);
			tb.executeCommand(sc.nextLine());
		}
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
		case "delete":
			this.doDelete(Integer.valueOf(cmd.detail)); this.numEntry--; break;
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
	public String doDelete(Integer lineNum) throws IOException{
		File temp = new File("temp.txt");
		try{
			if(!temp.exists()){
				temp.createNewFile();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new FileReader(this.file));
		FileWriter fw = new FileWriter(temp, true);
		
		String content = new String();
		String tempContent = new String();
		int count = 1;
		while(count != this.numEntry){
			tempContent = br.readLine();
			if(count != lineNum){
				fw.write(tempContent);
				fw.write(System.lineSeparator());
			}
			count++;
		}		
		br.close();
		fw.close();

		//delete current file
		if(!this.file.delete()){
			toUser("Could not delete existing file");
		}
		
		//change temp to current file
		if(!temp.renameTo(this.file)){
			toUser("Could not rename temp file");
		}	
		return content;
	}

}