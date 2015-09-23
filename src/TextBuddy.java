import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
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
	public static final String MESSAGE_DELETE = " deleted from ";
	public static final String MESSAGE_CLEAR = "all content deleted from ";
	public static final String MESSAGE_EMPTY = " is empty";
	public static final String MESSAGE_ERROR = "Command Error!";
	public static final String MESSAGE_SORTED = " is sorted!";
	
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
	public ArrayList<String> getList() throws IOException{
		ArrayList<String> items = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(this.file));
		int count = 0;
		while(count < this.numEntry){
				items.add(br.readLine());
				count++;
		}
		br.close();
		return items;
	}
	
	//store items in list into the file
	public void storeListToFile(ArrayList<String> list) throws IOException{
		FileWriter fw = new FileWriter(this.file, false);
		fw.write("");
		fw.close();
		
		fw = new FileWriter(this.file, true);
		int count = 0;
		while(count < this.numEntry){
			fw.write(list.get(count++));
			fw.write(System.lineSeparator());
		}
		fw.close();
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
			this.sortList();
			displayMsg(this.fileName + MESSAGE_SORTED); break;
		case "search":
			this.searchItem(cmd.detail); break;
		case "exit":
			System.exit(0);
		default: displayMsg(MESSAGE_ERROR);
		}
	}
	
	//execute command: add one item to list
	public void addItem(String content) throws IOException{
		FileWriter fw = new FileWriter(this.file, true);
		fw.write(content);
		fw.write(System.lineSeparator());
		fw.close();
		displayMsg("added to " + this.fileName + ": \"" + content + "\"");
	}
	
	//execute command: display items on list
	public void displayList() throws IOException{
		ArrayList<String> list = new ArrayList<String>(this.getList());
		int count = 0;
		while(count < this.numEntry){
			displayMsg(count+1 + ". " + list.get(count));
			count++;
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
		ArrayList<String> list = this.getList();
		list.remove(lineNum-1);
		this.storeListToFile(list);
	}
	
	//execute command: sort list with alphabetical order
	public void sortList() throws IOException{
		ArrayList<String> list = this.getList();
		Collections.sort(list);
		this.storeListToFile(list
				);
	}

	//execute command: search word and return lines containing the word
	public void searchItem(String key){
		//TODO
	}
}