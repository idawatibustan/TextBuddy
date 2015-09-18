import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class TextBuddy{
	public static final String MESSAGE_WELCOME = "Welcome to TextBuddy. ";
	public static final String MESSAGE_FILE_READY= " is ready for use";
	public static final String MESSAGE_COMMAND = "command: \n";
	public static final String MESSAGE_ADD = "added to ";
	public static final String MESSAGE_DELETE = "deleted from ";
	public static final String MESSAGE_CLEAR = "all content deleted from ";
	public static final String MESSAGE_EMPTY = " is empty";
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException{
		String fileName = args[0];
		
		//create file
		File file = new File("/users/idawatibustan/desktop/" + fileName);
		try{
			//if file doesnt exists, create file
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		//create file end
		
		toUser(MESSAGE_WELCOME + fileName + MESSAGE_FILE_READY);
		
		while(true){ 
			System.out.print(MESSAGE_COMMAND);
			String commandLine = sc.nextLine();
			switch(getFirstWord(commandLine)){
			case "add":
				doAdd(removeFirstWord(commandLine), file, fileName); break;
			case "display":
				int count = doDisplay(file); 
				if(count == 0){toUser(fileName + MESSAGE_EMPTY);} break;
			case "delete":
				
				String content = doDelete(Integer.valueOf(removeFirstWord(commandLine)), file); 
				toUser(MESSAGE_DELETE + fileName + ": \"" + content + "\""); break;
			case "clear":
				doClear(file); toUser(MESSAGE_CLEAR + fileName); break;
			case "exit":
				System.exit(0);
			default: toUser("Command Error!");
			}
		}
	}
	
	public static void toUser(String msg){
		System.out.println(msg);
	}
	
	public static String getFirstWord(String string){
		return string.trim().split("\\s+")[0];
	}
	
	public static String removeFirstWord(String string){
		return string.replace(getFirstWord(string),"").trim();
	}
	
	public static void doAdd(String content, File file, String fileName) throws IOException{
		FileWriter fw = new FileWriter(file, true);
		fw.write(content);
		fw.write(System.lineSeparator());
		fw.close();
		toUser("added to " + fileName + ": \"" + content + "\"");
	}
	
	public static int doDisplay(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		int count = 0;
		
		for(String line=br.readLine(); line!= null; line=br.readLine()){
			toUser(++count + ". " + line);
		}
		br.close();
		
		return count;
	}
	
	public static String doDelete(Integer line, File file) throws IOException{
		File temp = new File("/users/idawatibustan/desktop/temp.txt");
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

	
	public static void doClear(File file) throws IOException{
		FileWriter fwo = new FileWriter(file, false);
		fwo.write("");
		fwo.close();
	}
}