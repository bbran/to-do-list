// ToDoItemRepository.java
package com.libertymutual.goforcode.todolist.services;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.libertymutual.goforcode.todolist.models.ToDoItem;

@Service
public class ToDoItemRepository {

    private int nextId = 1;
    private List<ToDoItem>	items = new ArrayList<ToDoItem>();

    /**
     * Get all the items from the file. 
     * @return A list of the items. If no items exist, returns an empty list.
     */
    public List<ToDoItem> getAll() {
    	
		try (Reader in = new FileReader("todolist.csv")) {
	    	Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
	    	 items.clear();
	    	for (CSVRecord record : records)	{
	    		ToDoItem item = new ToDoItem();
	    		item.setId(Integer.parseInt(record.get(0)));
	    		item.setText(record.get(1));
	    		item.setComplete(Boolean.parseBoolean(record.get(2)));
	    		items.add(item);
	    	}
	    	if (items.size() > 0)	{
	    		nextId = items.get(items.size() - 1).getId() + 1;
	    	}
	    	return items;
	    	
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
		} catch (IOException e1) {
			System.err.println("Could not access file.");
		}
		return Collections.emptyList();
    }

    /**
     * Assigns a new id to the ToDoItem and saves it to the file.
     * @param item The to-do item to save to the file.
     */
    public void create(ToDoItem item) {
        item.setId(nextId);
        
        try (	FileWriter writer = new FileWriter("todolist.csv", true);
        		BufferedWriter bw = new BufferedWriter(writer);
        		CSVPrinter printer = CSVFormat.RFC4180.print(bw))	{
        	String[] newItem = new String[] {Integer.toString(item.getId()), item.getText(), Boolean.toString(item.isComplete())};
        	printer.printRecord(newItem);

        } catch (IOException e) {
			System.err.println("Could not access file");
		}
    }

    /**
     * Gets a specific ToDoItem by its id.
     * @param id The id of the ToDoItem.
     * @return The ToDoItem with the specified id or null if none is found.
     */
    public ToDoItem getById(int id) {
        ToDoItem selectedItem = null;
        for (ToDoItem item : items)	{
        	if (item.getId() == id)	{
        		selectedItem = item;
        	}
        }
        return selectedItem;
    }

    /**
     * Updates the given to-do item in the file.
     * @param item The item to update.
     */
    public void update(ToDoItem item) {
    	
        try (	FileWriter writer = new FileWriter("todolist.csv");
        		BufferedWriter bw = new BufferedWriter(writer);
        		CSVPrinter printer = CSVFormat.RFC4180.print(bw))	{
        	List<String[]> newItems = new ArrayList<String[]>();
        	for (ToDoItem itm : items) {
        		newItems.add(new String[] {Integer.toString(itm.getId()), itm.getText(), Boolean.toString(itm.isComplete())});
        	}
        	printer.printRecords(newItems);

        } catch (IOException e) {
			System.err.println("Could not access file");
		}
    }

}
