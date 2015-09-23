import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TextBuddyTest {

	@Test
	public void listShouldBeSorted() throws IOException {
		
		//TextBuddy is tested
		TextBuddy tester = new TextBuddy("text.txt");
		
		//create list of items
		assertEquals(0, tester.numEntry);
		tester.executeCommand("add Potato");
		assertEquals(1, tester.numEntry);
		tester.executeCommand("add Brocolli");
		assertEquals(2, tester.numEntry);
		tester.executeCommand("add French Fries");
		assertEquals(3, tester.numEntry);
		
		//get arraylist of items
		ArrayList<String> list = new ArrayList<String>();
		list.add("Potato"); list.add("Brocolli"); list.add("French Fries");
		assertEquals(list, tester.getList());
		
		tester.sortList();
		
		list.clear(); list.add("Brocolli"); list.add("French Fries"); list.add("Potato");
		assertEquals(list, tester.getList());
	}

}
