/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

    int numEntries = 0;
    int numBuckets = 0;
    DList[] hash;
    int collisions = 0;
    DList keys = new DList();



    /**
    *  Construct a new empty hash table intended to hold roughly sizeEstimate
    *  entries.  (The precise number of buckets is up to you, but we recommend
    *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
    **/

    public HashTableChained(int sizeEstimate) {
        numBuckets = generatePrime(sizeEstimate);
        hash = new DList[numBuckets];
    }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

    public HashTableChained() {
        numBuckets = 103;
        hash = new DList[103];
    }


   /**
    * Generate the next closest prime number >= "num".
    **/

    public int generatePrime(int num) {
          if (isPrime(num)) {
            return num;
          } else {
            return generatePrime(num+1);
          }
    }

    /**
     * Returns true if "num" is a prime number, false otherwise.
     */
    public boolean isPrime(int num) {
        int divisor = 2;
        while (divisor < num) {
            if (num % divisor == 0) {
                return false;
            }
            divisor++;
        }
        return true;
    }

    /**
    *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
    *  to a value in the range 0...(size of hash table) - 1.
    *
    *  This function should have package protection (so we can test it), and
    *  should be used by insert, find, and remove.
    **/

    private int compFunction(int code) {
        int temp = generatePrime(numBuckets*23);
        int num = ((code*13 + (int)(0.342*temp)) % temp) % numBuckets;
        if (num < 0) {
            num += numBuckets;
        }
        return num;
    }

    /**
    *  Returns the number of entries stored in the dictionary.  Entries with
    *  the same key (or even the same key and value) each still count as
    *  a separate entry.
    *  @return number of entries in the dictionary.
    **/

    public int size() {
    // Replace the following line with your solution.
        return numEntries;
    }

    /**
     * Resizes the hashtable by double the previous numBuckets size.
     * Only called by the insert method in HashTableChained when the
     * load factor is > 0.7.
     */
    private void resize() {
        HashTableChained temp = new HashTableChained(numBuckets);
        temp.hash = hash;
        numBuckets = generatePrime(numBuckets*2);
        hash = new DList[numBuckets];
        DListNode d = keys.front();
        while (d != null) {
            Object key = d.item;
	    Object entry = temp.find(key);
            insert(key, ((Entry)entry).value);
            d = keys.next(d);
       }
    }


    /**
    *  Tests if the dictionary is empty.
    *
    *  @return true if the dictionary has no entries; false otherwise.
    **/

    public boolean isEmpty() {
        if (numEntries == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of collisions in "this" HashTableChained.
     */
    public int numCollisions() {
        return collisions;
    }

    /**
    *  Create a new Entry object referencing the input key and associated value,
    *  and insert the entry into the dictionary.  Return a reference to the new
    *  entry.  Multiple entries with the same key (or even the same key and
    *  value) can coexist in the dictionary.
    *
    *  This method should run in O(1) time if the number of collisions is small.
    *
    *  @param key the key by which the entry can be retrieved.
    *  @param value an arbitrary object.
    *  @return an entry containing the key and value.
    **/

    public Entry insert(Object key, Object value) {
        if (((double)numEntries / numBuckets) > 0.7) {
            //if the load factor is > 0.7, resize.
            resize();
        }
        int hashCode = compFunction(key.hashCode());
        Entry enter = new Entry();
        enter.key = key;
        enter.value = value;
        if (hash[hashCode] == null) {
            hash[hashCode] = new DList();
        }
        if (!hash[hashCode].isEmpty()) {
            collisions++;
        }
        hash[hashCode].insertFront(enter);
        numEntries++;
        keys.insertFront(key);
        return enter;
    }

    /**
    *  Search for an entry with the specified key.  If such an entry is found,
    *  return it; otherwise return null.  If several entries have the specified
    *  key, choose one arbitrarily and return it.
    *
    *  This method should run in O(1) time if the number of collisions is small.
    *
    *  @param key the search key.
    *  @return an entry containing the key and an associated value, or null if
    *          no entry contains the specified key.
    **/

    public Entry find(Object key) {
        DList n = hash[compFunction(key.hashCode())];
        if (n == null) {
            return null;
        } else if (n.isEmpty()) {
            return null;
        } else {
            DListNode d = n.front();
		//d is the front of the DList that may contain the key we are looking for
            while (d != null) {
                if (((Entry) d.item).key.equals(key)) {
                    return (Entry) d.item;
                }
                d = n.next(d);
            }
            return null;
        }
    }

    /**
    *  Remove an entry with the specified key.  If such an entry is found,
    *  remove it from the table and return it; otherwise return null.
    *  If several entries have the specified key, choose one arbitrarily, then
    *  remove and return it.
    *
    *  This method should run in O(1) time if the number of collisions is small.
    *
    *  @param key the search key.
    *  @return an entry containing the key and an associated value, or null if
    *          no entry contains the specified key.
    */

    public Entry remove(Object key) {
        DList n = hash[compFunction(key.hashCode())];
        if (n == null) {
            return null;
        }
        else if (n.isEmpty()) {
            return null;
        } else {
            DListNode d = n.front();
            while (d != null) {
                if (((Entry) d.item).key.equals(key)) {
                    //First, remove d with the same key from the DList
                    n.remove(d);
                    DListNode dInKeys = keys.front();
                    while (dInKeys != null) {
                        //loop through keys to find key
                        if (dInKeys.item.equals(key)) {
                            keys.remove(dInKeys);
                            break;
                        }
                        dInKeys = keys.next(dInKeys);
                    }
                    return (Entry) d.item;
                }
                d = n.next(d);
            }
            return null;
        }
    }

    /**
    *  Remove all entries from the dictionary.
    */
    public void makeEmpty() {
        hash = new DList[numBuckets];
        numEntries = 0;
        collisions = 0;
        keys = new DList();
    }


}
