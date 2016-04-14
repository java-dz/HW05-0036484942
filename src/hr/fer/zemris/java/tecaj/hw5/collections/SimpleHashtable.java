package hr.fer.zemris.java.tecaj.hw5.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The <tt>SimpleHashtable</tt> class represents a hash table used to implement
 * an associative array, a structure that can map keys to values.
 * <p>
 * A hash table uses a hash function to compute an index into an array of slots,
 * from which the desired value can be found.
 * <p>
 * An instance of <tt>SimpleHashtable</tt> has two parameters that affect its
 * performance: <i>initial capacity</i> and <i>load factor</i>. The
 * <i>capacity</i> is the number of slots in the hash table, and the initial
 * capacity is simply the capacity at the time the hash table is created. The
 * <i>load factor</i> is a measure of how full the hash table is allowed to get
 * before its capacity is automatically increased. When the number of entries in
 * the hash table exceeds the product of the load factor and the current
 * capacity, the hash table is <i>rehashed</i> (that is, internal data
 * structures are rebuilt) so that the hash table has approximately twice the
 * number of slots.
 * <p>
 * This class is an upgrade of a <tt>SimpleHashtable</tt> made in OOP on
 * academic year 2015/16.
 * 
 * @author Mario Bobic
 * @param <K> key argument
 * @param <V> value argument
 * @version 1.1
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/** Default initial capacity. */
	private static final int DEFAULT_CAPACITY = 16;
	
	/** Default load factor of a table. */
	private static final float LOAD_FACTOR = 0.75f;
	
	/** Number of times the table is expanded when resizing is done. */
	private static final int MULTIPLICATION_FACTOR = 2;
	
	/** The internal storage of this table. */
	private TableEntry<K, V>[] table;
	/** Number of currently stored elements in this table. */
	private int size;
	/**
	 * The number of times this list has been <i>structurally modified</i>.
	 * Structural modifications are those that change the size of the list, or
	 * otherwise perturb it in such a fashion that iterations in progress may
	 * yield incorrect results.
	 */
	private int modificationCount;

	/**
	 * Constructs a new <tt>SimpleHashtable</tt> object that stores entries in a
	 * table of size 16. The table is initially empty.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructs a new <tt>SimpleHashtable</tt> object that stores entries in a
	 * table sized the first power of two that is greater than or equal to the
	 * argument value. The object is initially empty.
	 * <p>
	 * If the specified value is less than 1, an
	 * {@linkplain IllegalArgumentException} is thrown.
	 * 
	 * @param n initial hash table size
	 * @throws IllegalArgumentException if <tt>n &lt; 1</tt>
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
				"Initial size of the table must be greater than 0.");
		}
		int tableSize = nextPowerOfTwo(n);
		table = new TableEntry[tableSize];
		size = 0;
	}
	
	/**
	 * Constructs a new <tt>SimpleHashtable</tt> with the same mappings as the
	 * specified <tt>hashtable</tt>. The <tt>SimpleHashtable</tt> is created
	 * with an initial capacity sufficient to hold the mappings in the specified
	 * <tt>hashtable</tt>.
	 * 
	 * @param hashtable hash table whose elements are to be copied to this one
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(SimpleHashtable<? extends K, ? extends V> hashtable) {
		table = new TableEntry[hashtable.table.length];
		putAll(hashtable);
	}

	/**
	 * Returns the first power of two that is greater than or equal to the
	 * argument value. Special cases:
	 * <ul>
	 * <li>If the argument value is already a power of two, then the result is
	 * the same as the argument.
	 * <li>If the argument value is less than or equal to zero, then the result
	 * is first positive power of two, which is 1.
	 * </ul>
	 * 
	 * @param n a value
	 * @return the first integer power of two that is greater than or equal to
	 *         the argument
	 */
	private int nextPowerOfTwo(int n) {
		if (n <= 0) return 1;
		int exponent = (int) (Math.ceil(Math.log(n) / Math.log(2)));
		return (int) Math.pow(2, exponent);
	}
	
	/**
	 * Calculates hash code of a given key argument and returns the slot to
	 * which it belongs.
	 * 
	 * @param key key whose slot is to be determined
	 * @return slot to which the object belongs
	 */
	private int determineSlot(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}
	
	/**
	 * Associates the specified value with the specified key in this table. If
	 * the table previously contained a mapping for the key, the old value is
	 * replaced.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 */
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Key must not be null.");
		}
		
		TableEntry<K, V> oldEntry = getEntry(key);
		
		if (oldEntry == null) {			// Entry does not exist
			TableEntry<K, V> newEntry = new TableEntry<K, V>(key, value, null);
			int slot = determineSlot(key);
			
			if (table[slot] == null) {	// Empty slot
				table[slot] = newEntry;
			} else {					// Slot not empty
				getLastInSlot(slot).next = newEntry;
			}
			
			size++;
			modificationCount++;
			checkCapacity();
		} else {						// Entry exists
			oldEntry.value = value;
		}
	}
	
	/**
	 * Check if the number of entries in the hash table exceeds the product of
	 * the load factor and the current capacity. If the test returns true, the
	 * hash table is <i>rehashed</i> (that is, internal data structures are
	 * rebuilt) so that the hash table has approximately twice the number of
	 * slots.
	 */
	void checkCapacity() {
		if (size >= LOAD_FACTOR*table.length) {
			resize();
		}
	}
	
	/**
	 * <i>Rehashes</i> the hash table (that is, rebuilds the internal data
	 * structures) so that the hash table has approximately twice the number of
	 * slots.
	 */
	@SuppressWarnings("unchecked")
	void resize() {
		TableEntry<K, V>[] oldTable = table;
		table = new TableEntry[MULTIPLICATION_FACTOR*table.length];
		size = 0;
		putAllTable(oldTable);
	}

	/**
	 * Returns the value to which the specified key is mapped, or <tt>null</tt>
	 * if this table contains no mapping for the key.
	 * <p>
	 * A return value of <tt>null</tt> does not <i>necessarily</i> indicate that
	 * the table contains no mapping for the key; it's also possible that the
	 * table explicitly maps the key to <tt>null</tt>. The
	 * {@linkplain #containsKey} operation may be used to distinguish these two
	 * cases.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key key of the entry
	 * @return value associated with the key if found, <tt>null</tt> otherwise
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = getEntry(key);
		return entry == null ? null : entry.value;
	}
	
	/**
	 * Returns the table entry to which the specified key belongs, or
	 * <tt>null</tt> if this table contains no mapping for the key.
	 * <p>
	 * A return value of <tt>null</tt> means that the table entry does not exist
	 * in this hash table.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key key of the entry
	 * @return existing entry if found, <tt>null</tt> otherwise
	 */
	private TableEntry<K, V> getEntry(Object key) {
		if (key == null) {
			return null;
		}
		
		int slot = determineSlot(key);
		
		TableEntry<K, V> currentInList = table[slot];
		while (currentInList != null) {
			if (currentInList.key.equals(key)) {
				return currentInList;
			}
			currentInList = currentInList.next;
		}
		
		return null;
	}
	
	/**
	 * Returns the last table entry in the specified <tt>slot</tt>. This method
	 * iterates through all elements of the singly linked list in the specified
	 * slot to find the last one.
	 * 
	 * @param slot slot of which the last element is to be returned
	 * @return the last element of the specified slot
	 */
	private TableEntry<K, V> getLastInSlot(int slot) {
		TableEntry<K, V> currentInList = table[slot];
		
		while (currentInList.next != null) {
			currentInList = currentInList.next;
		}
		
		return currentInList;
	}
	
	/**
	 * Returns the size of this hash table object. Size is determined by the
	 * number of entries in the table.
	 * 
	 * @return the size of this hash table
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns <tt>true</tt> if this table contains the desired key.
	 * False otherwise.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key key whose presence in this table is to be tested
	 * @return true if this table contains the specified key
	 */
	public boolean containsKey(Object key) {
		TableEntry<K, V> entry = getEntry(key);
		return entry == null ? false : true;
	}
	
	/**
	 * Returns <tt>true</tt> if this table contains the desired value.
	 * False otherwise.
	 * <p>
	 * Time complexity: O(n)
	 * 
	 * @param value value whose presence in this table is to be tested
	 * @return true if this map contains the specified value
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> entry : this) {
			if (Objects.equals(entry.value, value)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the mapping for the specified key from this table if present.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key key whose mapping is to be removed from the table
	 */
	public void remove(Object key) {
		TableEntry<K, V> entry = getEntry(key);
		if (entry == null) {
			return;
		}
		
		int slot = determineSlot(key);
		if (entry == table[slot]) {
			// The wanted element is the first element in slot
			table[slot] = entry.next;
		} else {
			// The wanted element is somewhere else in slot
			TableEntry<K, V> currentInList = table[slot];
			while (!currentInList.next.equals(entry)) {
				currentInList = currentInList.next;
			}
			// Replace "entry" with the next one
			currentInList.next = currentInList.next.next;
		}
		
		size--;
		modificationCount++;
	}
	
	/**
	 * Returns true if this table contains no entries.
	 * False otherwise.
	 * 
	 * @return true if this table contains no entries
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns a string representation of this <tt>SimpleHashtable</tt>.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		
		// Go through all table entries
		for (TableEntry<K, V> entry : this) {
			sb.append(entry).append(", ");
		}
		// Erase the last comma and space
		sb.setLength(sb.length()-2);
		
		sb.append(']');
		return sb.toString();
	}
	
	/**
	 * Removes all of the mappings from this map. The map will be empty after
	 * this call returns.
	 */
	public void clear() {
		// "Detach" all linked lists from table entries
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Copies all of the mappings from the specified hash table to this hash
	 * table. These mappings will replace any mappings that this hash table had
	 * for any of the keys currently in the specified hash table.
	 * 
	 * @param hashtable mappings of the table to be stored in this table
	 */
	public void putAll(SimpleHashtable<? extends K, ? extends V> hashtable) {
		putAllTable(hashtable.table);
	}
	
	/**
	 * Copies all of the elements from the specified table to the table of this
	 * <tt>SimpleHashtable</tt>. These mappings will replace any mappings that
	 * this hash table had for any of the keys currently in the specified table.
	 * 
	 * @param table mappings of the table to be stored into the inner table
	 */
	private void putAllTable(TableEntry<? extends K, ? extends V>[] table) {
		for (int i = 0; i < table.length; i++) {
			TableEntry<? extends K, ? extends V> currentInList = table[i];
			
			while (currentInList != null) {
				put(currentInList.key, currentInList.value);
				currentInList = currentInList.next;
			}
		}
	}
	
	/**
	 * A class that represents a table entry for the <tt>SimpleHashtable</tt>
	 * class. Used to store entries by their key and value.
	 * 
	 * @author Mario Bobic
	 * @param <K> key argument
	 * @param <V> value argument
	 */
	public static class TableEntry<K, V> {
		
		/** Key of the table entry. */
		private K key;
		/** Value of the table entry. */
		private V value;
		/** The next element of a singly linked list. */
		private TableEntry<K, V> next;
		
		/**
		 * Constructs a new table entry for its superclass
		 * {@code SimpleHashtable} with given key, value and next table entry in
		 * a singly linked list.
		 * 
		 * @param key key of the entry
		 * @param value value of the entry
		 * @param next next table entry in a singly linked list
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Returns the key of the entry.
		 * 
		 * @return the key of the entry
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Returns the value of the entry.
		 * 
		 * @return the value of the entry
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Sets the value of the entry.
		 * 
		 * @param value value of the entry to be set
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Returns a string representation of this table entry.
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}
	
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Iterates through all elements in <tt>SimpleHashtable</tt> by finding a
	 * non-empty slot first, then iterating through elements of a singly linked
	 * list.
	 * 
	 * @author Mario Bobic
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>>{
		
		/** The last returned element. */
		private TableEntry<K, V> lastReturned;
		/** The next element to be returned. */
		private TableEntry<K, V> next;
		/** Current slot of the table. */
		private int currentSlot;
		/** Remaining elements in this iterator. */
		private int remainingElements;
		/** The expected number of modifications. */
		private int expectedModCount = modificationCount;
		
		/**
		 * Constructs a new iterator for class <tt>SimpleHashtable</tt>. Sets
		 * the total number of remaining elements initially at size of this
		 * <tt>SimpleHashtable</tt>.
		 */
		public IteratorImpl() {
			lastReturned = null;
			currentSlot = 0;
			remainingElements = SimpleHashtable.this.size();
		}
		
		@Override
		public boolean hasNext() {
			checkForConcurrentModification();
			return remainingElements > 0;
		}
		
		@Override
		public TableEntry<K, V> next() {
			checkForConcurrentModification();
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements are available.");
			}
			
			setNext();
			lastReturned = next;

			remainingElements--;
			return lastReturned;
		}
		
		/**
		 * Sets the <tt>next</tt> table entry that should be returned in the
		 * next iteration.
		 */
		private void setNext() {
			if (next != null) {
				next = next.next;
			} else {
				next = table[currentSlot];
			}
			
			// Skip all empty slots
			while (next == null) {
				currentSlot++;
				next = table[currentSlot];
			}
		}
		
		@Override
		public void remove() {
			checkForConcurrentModification();
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			
			SimpleHashtable.this.remove(lastReturned.key);
			lastReturned = null;
			expectedModCount++;
		}
		
		/**
		 * Check if the modification count is as expected. If not, a
		 * {@linkplain ConcurrentModificationException} is thrown.
		 */
		private void checkForConcurrentModification() {
			if (modificationCount != expectedModCount) {
                throw new ConcurrentModificationException();
			}
		}
	}

}