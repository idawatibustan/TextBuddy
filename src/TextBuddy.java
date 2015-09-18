import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Command{
	public String type;
	public String detail;
	
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
			executeCommand(sc.nextLine(), tb);
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
	}
	
	//print to user
	public static void toUser(String msg){
		System.out.println(msg);
	}

	//execute relevant command
	public static void executeCommand(String commandLine, TextBuddy tb) throws IOException{
		Command cmd = new Command(getFirstWord(commandLine), removeFirstWord(commandLine));
		switch(cmd.type){
		case "add":
			doAdd(cmd.detail, tb); break;
//		case "display":
//			int count = doDisplay(tb1.file); 
//			if(count == 0){toUser(tb1.fileName + MESSAGE_EMPTY);} break;
//		case "delete":			
//			String content = doDelete(Integer.valueOf(removeFirstWord(commandLine)), tb1.file); 
//			toUser(MESSAGE_DELETE + tb1.fileName + ": \"" + content + "\""); break;
//		case "clear":
//			doClear(tb1.file); toUser(MESSAGE_CLEAR + tb1.fileName); break;
//		case "exit":
//			System.exit(0);
//		default: toUser("Command Error!");
		//TODO
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
	public static void doAdd(String content, TextBuddy tb) throws IOException{
		FileWriter fw = new FileWriter(tb.file, true);
		fw.write(content);
		fw.write(System.lineSeparator());
		fw.close();
		toUser("added to " + tb.fileName + ": \"" + content + "\"");
	}
	
	//execute command: display
	public static int doDisplay(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		int count = 0;
		
		for(String line=br.readLine(); line!= null; line=br.readLine()){
			toUser(++count + ". " + line);
		}
		br.close();
		
		return count;
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

	//execute command: clear
	public static void doClear(File file) throws IOException{
		FileWriter fwo = new FileWriter(file, false);
		fwo.write("");
		fwo.close();
	}
}