import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
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
	public static final String MESSAGE_DELETE = " deleted from ";
	public static final String MESSAGE_CLEAR = "all content deleted from ";
	public static final String MESSAGE_EMPTY = " is empty";
	public static final String MESSAGE_ERROR = "Command Error!";
	
	//scanner
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		TextBuddy tb = new TextBuddy(args[0]);
		tb.executeCommand("clear");
		displayMsg(MESSAGE_WELCOME + tb.fileName + MESSAGE_FILE_READY);
		
		while(true){
			displayMsg(MESSAGE_COMMAND);
			tb.executeCommand(sc.nextLine());
		}
	}
	
	//print to user
	public static void displayMsg(String msg){
		System.out.println(msg);
	}

	//get first word: command word
	public static String getFirstWord(String string){
		return string.trim().split("\\s+")[0];
	}
	
	//remove first word to get command details
	public static String removeFirstWord(String string){
		return string.replace(getFirstWord(string),"").trim();
	}

	//get items as arrayList
	public ArrayList<String> getList(File file, int numEntry) throws IOException{
		ArrayList<String> items = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		int count = 0;
		while(count < numEntry){
				items.add(br.readLine());
		}
		br.close();
		return items;
	}
	
	/***************COMMANDS****************/
	//choose command to execute
	public void executeCommand(String commandLine) throws IOException{
		Command cmd = new Command(getFirstWord(commandLine), removeFirstWord(commandLine));
		switch(cmd.type){
		case "add":
			this.addItem(cmd.detail); this.numEntry++; break;
		case "display":
			if(this.numEntry == 0){
				displayMsg(this.fileName + MESSAGE_EMPTY);
			} else {
				this.displayList();
			}
			break;
		case "clear":
			this.clearList(); this.numEntry = 0; break;
		case "delete":
			this.deleteItem(Integer.valueOf(cmd.detail)); this.numEntry--; break;
		case "sort":
			this.sortList(); break;
		case "search":
			this.searchItem(cmd.detail); break;
		case "exit":
			System.exit(0);
		default: displayMsg(MESSAGE_ERROR);
		}
	}
	
	//execute command: add
	public void addItem(String content) throws IOException{
		FileWriter fw = new FileWriter(this.file, true);
		fw.write(content);
		fw.write(System.lineSeparator());
		fw.close();
		displayMsg("added to " + this.fileName + ": \"" + content + "\"");
	}
	
	//execute command: display numbered list of items
//	public void displayList() throws IOException{
//		int count = 0;
//		BufferedReader br = new BufferedReader(new FileReader(this.file));
//		while(count != this.numEntry){
//			String line = new String(br.readLine());
//			displayMsg(++count + ". " + line);
//		}
//		br.close();
//	}
	public void displayList() throws IOException{
		displayMsg("create list");
		ArrayList<String> list = new ArrayList<String>(this.getList(this.file, this.numEntry));
		int count = 0;
		displayMsg("displaying list");
		while(count < this.numEntry){
			displayMsg("showing item");
			displayMsg(count+1 + ". " + list.get(++count));
		}
	}
	
	
	//execute command: clear all items in the list
	public void clearList() throws IOException{
		FileWriter fw = new FileWriter(this.file, false);
		fw.write("");
		fw.close();
		displayMsg(MESSAGE_CLEAR + this.fileName);
	}
	
	//execute command: delete one item of given number from the list
	public void deleteItem(Integer lineNum) throws IOException{
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
		while(count <= this.numEntry){
			tempContent = br.readLine();
			if(count != lineNum){
				fw.write(tempContent);
				fw.write(System.lineSeparator());
			} else {
				content = tempContent;
			}
			count++;
		}		
		br.close();
		fw.close();

		//delete current file
		if(!this.file.delete()){
			displayMsg("Could not delete existing file");
		}
		
		//change temp to current file
		if(!temp.renameTo(this.file)){
			displayMsg("Could not rename temp file");
		}	
		displayMsg(content + MESSAGE_DELETE + this.fileName);
	}

	//execute command: sort list with alphabetical order
	public void sortList(){
		//TODO
	}

	//execute command: search word and return lines containing the word
	public void searchItem(String key){
		//TODO
	}
}