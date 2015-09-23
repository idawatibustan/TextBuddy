import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TextBuddyTest {

	@Test
	public void listShouldBeSorted() throws IOException {
		
		//TextBuddy is tested
		TextBuddy tester = new TextBuddy("text.txt");
		
		tester.clearList();
		
		//create list of items
		assertEquals(0, tester.numEntry);
		tester.executeCommand("add Potato");
		tester.executeCommand("add Brocolli");
		tester.executeCommand("add Fries");
		tester.executeCommand("add French Fries");
		tester.executeCommand("add Curly Fries");
		assertEquals(5, tester.numEntry);
		
		tester.displayList();
		//get arraylist of items
		ArrayList<String> list = new ArrayList<String>();
		list.add("Potato");
		list.add("Brocolli");
		list.add("Fries");
		list.add("French Fries");
		list.add("Curly Fries");
		assertEquals(list, tester.getList());
		
		tester.sortList(); //sort
		
		tester.displayList();
		//sorted list
		list.clear();
		list.add("Brocolli");
		list.add("Curly Fries");
		list.add("French Fries");
		list.add("Fries");
		list.add("Potato");
		assertEquals(list, tester.getList());
		
		assertEquals("2. Curly Fries\n3. French Fries\n4. Fries\n", tester.searchItem("Fries"));
	}

}
