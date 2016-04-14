package hr.fer.zemris.java.tecaj.hw5.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class SimpleHashtableTests {

	/* ------------------------------ Prob1 tests ------------------------------ */
	
	@Test(expected=IllegalArgumentException.class)
	public void testNegativeValueToConstructor() {
		// must throw
		new SimpleHashtable<>(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPutNull() {
		// must throw
		new SimpleHashtable<>().put(null, 5);
	}
	
	@Test
	public void testPutSameKey() {
		SimpleHashtable<String, Integer> table = getTable();
		assertEquals((Integer) 1, table.get("Benković"));
		assertEquals((Integer) 2, table.get("Bobić"));
		assertEquals((Integer) 3, table.get("Žuljević"));
		
		table.put("Benković", 3);
		assertEquals((Integer) 3, table.get("Benković"));
		
		table.put("Bobić", 5);
		assertEquals((Integer) 5, table.get("Bobić"));
		
		table.put("Žuljević", 1);
		assertEquals((Integer) 1, table.get("Žuljević"));
	}
	
	@Test
	public void testGetNull() {
		assertEquals(null, getTable().get(null));
	}
	
	@Test
	public void testGetNonExistent() {
		assertEquals(null, getTable().get("Magzan"));
	}
	
	@Test
	public void testSize() {
		SimpleHashtable<String, Integer> table = getTable();
		assertEquals(18, table.size());
		
		table.remove("Bobić");
		table.remove("Benković");
		table.remove("Žuljević");
		
		assertEquals(15, table.size());
	}
	
	@Test
	public void testContainsKey() {
		SimpleHashtable<String, Integer> table = getTable();
		
		assertEquals(true, table.containsKey("Benković"));
		assertEquals(true, table.containsKey("Bobić"));
		assertEquals(true, table.containsKey("Žuljević"));
		
		assertEquals(false, table.containsKey("Magzan"));
	}
	
	@Test
	public void testContainsKeyNull() {
		assertEquals(false, getTable().containsKey(null));
	}
	
	@Test
	public void testContainsValue() {
		SimpleHashtable<String, Integer> table = getTable();
		
		assertEquals(true, table.containsValue(1));
		assertEquals(true, table.containsValue(3));
		assertEquals(true, table.containsValue(5));
		
		assertEquals(false, table.containsValue(-1));
		assertEquals(false, table.containsValue(0));
		assertEquals(false, table.containsValue(6));
	}
	
	@Test
	public void testContainsValueNull() {
		SimpleHashtable<String, Integer> table = getTable();
		assertEquals(false, table.containsValue(null));
		
		table.put("Magzan", null);
		assertEquals(true, table.containsValue(null));
	}
	
	@Test
	public void testRemove() {
		SimpleHashtable<String, Integer> table = getTable();
		table.remove("Bobić");
		table.remove("Žuljević");
		
		assertEquals(false, table.containsKey("Bobić"));
		assertEquals(false, table.containsKey("Žuljević"));
	}
	
	@Test
	public void testRemoveNull() {
		SimpleHashtable<String, Integer> table = getTable();
		table.remove(null);
		
		// element must not be removed
		table.put("Magzan", null);
		table.remove(null);
		
		assertEquals(19, table.size());
	}

	/*
	 * Does not test the whole string, but only count commas.
	 */
	@Test
	public void testToString() {
		String expected = "[Novački=4, Orešković=5, Miniri=2, Bobić=2, "
				+ "Đurdek=1, Benković=1, Mamić=5, Ćurin=5, Jeleč=3, "
				+ "Martinjak=1, Jakovljev=2, Cicijelj=4, Žuljević=3, "
				+ "Kratofil=4, Žabić=2, Bruck=3, Mrvelj=3, Šimunović=1]";
		String actual = getTable().toString();
		
		// count the number of occurrences of char ','
		int expectedCommas = countOccurrencesOf(expected, ',');
		int actualCommas = countOccurrencesOf(actual, ',');
		
		assertEquals(expectedCommas, actualCommas);
		assertEquals(true, actual.startsWith("["));
		assertEquals(true, actual.endsWith("]"));
	}
	
//	@Test // in order to test this, change visibility of 'table' to 'package'
//	public void testPowerOfTwo() {
//		SimpleHashtable<String, Integer> table1 = new SimpleHashtable<>(1);
//		assertEquals(1, table1.table.length);
//		
//		SimpleHashtable<String, Integer> table2 = new SimpleHashtable<>(2);
//		assertEquals(2, table2.table.length);
//		
//		SimpleHashtable<String, Integer> table4 = new SimpleHashtable<>(3);
//		assertEquals(4, table4.table.length);
//		
//		table4 = new SimpleHashtable<>(4);
//		assertEquals(4, table4.table.length);
//		
//		SimpleHashtable<String, Integer> table8 = new SimpleHashtable<>(5);
//		assertEquals(8, table8.table.length);
//		
//		SimpleHashtable<String, Integer> table64 = new SimpleHashtable<>(33);
//		assertEquals(64, table64.table.length);
//		
//		SimpleHashtable<String, Integer> table128 = new SimpleHashtable<>(100);
//		assertEquals(128, table128.table.length);
//		
//		SimpleHashtable<String, Integer> table256 = new SimpleHashtable<>(129);
//		assertEquals(256, table256.table.length);
//		
//		SimpleHashtable<String, Integer> table1024 = new SimpleHashtable<>(1024);
//		assertEquals(1024, table1024.table.length);
//	
//		SimpleHashtable<String, Integer> table65536 = new SimpleHashtable<>(65535);
//		assertEquals(65536, table65536.table.length);
//	}
	
	
	/* ------------------------------ Prob2 tests ------------------------------ */
	
	@Test
	public void testClear() {
		SimpleHashtable<String, Integer> table = getTable();
		table.clear();
		
		assertEquals(0, table.size());
		assertEquals(true, table.isEmpty());
		
		assertEquals(null, table.get("Benković"));
		assertEquals(null, table.get("Bobić"));
		assertEquals(null, table.get("Žuljević"));
		
		assertEquals(false, table.containsKey("Benković"));
		assertEquals(false, table.containsKey("Bobić"));
		assertEquals(false, table.containsKey("Žuljević"));
		
		assertEquals(false, table.containsValue(1));
		assertEquals(false, table.containsValue(3));
		assertEquals(false, table.containsValue(5));
	}
	
	/*
	 * Since hash values are equal across all computers, it is legal to assume
	 * the toString will return a String as specified. This test may fail due
	 * to spaces after commas. In that case, remove the spaces.
	 */
	@Test
	public void testToStringHardcore() {
		String expected = "[Novački=4, Đurdek=1, Benković=1, Ćurin=5, "
				+ "Jakovljev=2, Kratofil=4, Bruck=3, Mrvelj=3, "
				+ "Orešković=5, Miniri=2, Bobić=2, Mamić=5, Jeleč=3, "
				+ "Martinjak=1, Cicijelj=4, Žuljević=3, Žabić=2, Šimunović=1]";
		String actual = getTable().toString();

		assertEquals(expected, actual);
	}
	
	@Test
	public void testPutAll() {
		SimpleHashtable<String, Integer> table1 = getTable();
		SimpleHashtable<String, Integer> table2 = new SimpleHashtable<>(table1);
		
		SimpleHashtable<String, Integer> table3 = new SimpleHashtable<>();
		table3.putAll(table2);
		
		assertEquals(table1.toString(), table2.toString());
		assertEquals(table1.toString(), table3.toString());
		assertEquals(table2.toString(), table3.toString());
	}
	
	
	/* ------------------------------ Prob3 tests ------------------------------ */
	
	@Test
	public void testForEach() {
		SimpleHashtable<String, Integer> table = getTable();
		
		int count = 0;
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			assertEquals(true, table.containsKey(entry.getKey()));
			assertEquals(true, table.containsValue(entry.getValue()));
			count++;
		}
		
		assertEquals(table.size(), count);
	}
	
	@Test
	public void testForEachRemoveNormally() {
		SimpleHashtable<String, Integer> table = getTable();
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = table.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> entry = iter.next();
			if (entry.getValue().equals(5)) {
				iter.remove();
			}
		}
		
		assertEquals(table.size(), 15);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testForEachRemoveTwiceInARow() {
		SimpleHashtable<String, Integer> table = getTable();
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = table.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> entry = iter.next();
			if (entry.getValue().equals(5)) {
				iter.remove();
				// must throw
				iter.remove();
			}
		}
		
		assertEquals(table.size(), 15);
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void testForEachConcurrentRemove() {
		SimpleHashtable<String, Integer> table = getTable();
		
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			if (entry.getValue().equals(5)) {
				// must throw
				table.remove(entry.getKey());
			}
		}
	}
	
	@Test
	public void testForEachUpdateExistingEntry() {
		SimpleHashtable<String, Integer> table = getTable();
		
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			if (entry.getValue().equals(5)) {
				// must NOT throw
				table.put(entry.getKey(), 4);
			}
		}
	}
	
	@Test
	public void testForEachOnEmptyCollection() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(100);
		
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			assertEquals(null, entry.getKey());
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testForEachOnMoreCases() {
		// First three should form a table like this: [čćđšž=3, abc12345=2, abab=1, null]
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);
		table.put("abab", 1);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table);
		table.put("abc12345", 2);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table);
		table.put("čćđšž", 3);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table);
		table.put("SimpleHashtable", 4);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table);
	}
	
	@Test
	public void testResize() {
		/* 0.75 * 4 = 3 - table should be resized after 3 elements are added. */
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);
		/* The two first elements of lists of entries must NOT be in the same order. */
		List<String> listOfEntries1 = new ArrayList<>();
		List<String> listOfEntries2 = new ArrayList<>();
		
		table.put("abc12345", 1);
		table.put("čćđšž", 2);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			listOfEntries1.add(entry.getKey());
		}
//		System.out.println(table);
		
		table.put("SimpleHashtable", 3);
		table.put("abab", 4);
		for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
			listOfEntries2.add(entry.getKey());
		}
//		System.out.println(table);
		
		assertNotEquals(listOfEntries1.get(0), listOfEntries2.get(0));
		assertNotEquals(listOfEntries1.get(1), listOfEntries2.get(1));
	}
	
	
	
	/**
	 * Returns a SimpleHashtable filled with some data.
	 * <p>
	 * Size of the returned table is 18.
	 */
	private static SimpleHashtable<String, Integer> getTable() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		
		table.put("Benković",	1);
		table.put("Bobić",		2);
		table.put("Bruck",		3);
		table.put("Cicijelj",	4);
		table.put("Ćurin",		5);
		table.put("Đurdek",		1);
		table.put("Jakovljev",	2);
		table.put("Jeleč",		3);
		table.put("Kratofil",	4);
		table.put("Mamić",		5);
		table.put("Martinjak",	1);
		table.put("Miniri",		2);
		table.put("Mrvelj",		3);
		table.put("Novački",	4);
		table.put("Orešković",	5);
		table.put("Šimunović",	1);
		table.put("Žabić",		2);
		table.put("Žuljević",	3);
		
		return table;
	}
	
	/**
	 * Counts the number of occurrences of the specified char in a string.
	 */
	private static int countOccurrencesOf(String string, char c) {
		return string.length() - string.replace(c + "", "").length();
	}

}
