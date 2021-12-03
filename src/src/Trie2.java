import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Trie2 extends TrieNode{

    protected TrieNode root;

    public Trie2() {
        root = new TrieNode();
    }

    public void insert(String word, long prefixFrequency) {

        //Start at the root
        TrieNode [] children = root.getChildren();
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node;
            //If there already exists a child with the character c in the trie
            if(children[c - 97] != null) {
                node = children[c - 97];
            }
            //Make a new node with the character value c and link it to the children of the parent
            else {
                node = new TrieNode(c);
                children[c - 97] = node;
            }
            //Either way, find the children of the node to keep looping through,
            //either creating a new path in the trie or following along a pre-existing one
            children = node.getChildren();
            //When you get to the end of the word, since we know that it's a word, set the last
            //character node to be the end of a word, i.e. set isEndOfWord to true
            if(i == word.length() - 1) {
                node.setEndOfWord(true);
                //Set the prefix frequency of the node so we know how many times it comes up in our .csv file
                node.setPrefixFrequency(prefixFrequency);

            }
        }
    }

    public boolean search(String word) {
        //Start at the root
        TrieNode [] children = root.getChildren();
        TrieNode node = null;
        //Iterate through the trie until you come across a break in the path
        //Outcome 1: You've reached a leaf node, so there are no more character nodes beyond that path
        //This is fine because we'll check to see if the node we stopped at has isEndOfWord set to true
        //If it is true, then we have a valid word
        //Outcome 2: The search function comes across a break in the path, i.e., all the letters "ran out"
        //of the word, or it couldn't find the right child the parameter was looking for. If isEndOfWord is not true,
        //and we reached the end of our search, the word does not exist in our trie.
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(children[c - 97] != null) {
                node = children[c - 97];
                children = node.getChildren();
            } else {
                node = null;
                break;
            }
        }
        //Tests the node we're left with to see if it makes a valid word
        if(node != null && node.isEndOfWord()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean startsWith(String prefix) {

        TrieNode [] children = root.getChildren();
        TrieNode node = null;
        for (int i = 0; i < prefix.length(); i++) {
            int j = prefix.charAt(i) - 'a';
            if (children[j] == null)
                return false;
            node = children[j];
        }
        return true;
    }

    public ArrayList<ArrayList<String>> getPrefixStrings(String prefix, HashMap<String, Long> frequencyMap){

        TrieNode [] children = root.getChildren();
        TrieNode node = null;
        String word = "";
        ArrayList<ArrayList<String>> prefixStrings = new ArrayList<>();
        ArrayList<String> onePrefixListStrings = new ArrayList<>();

        for (int i = 0; i < prefix.length(); i++) {
            int j = prefix.charAt(i) - 'a';
            if(children[j] != null){
                word += children[j].getChar();
                node = children[j];
                //restOfWord gets you an ArrayList of Strings, and you'll add that ArrayList to a
                //list of ArrayLists of Strings, all why filling the frequency hashmap
                prefixStrings.add(restOfWord(word, node, frequencyMap, onePrefixListStrings));
            }
        }
        return prefixStrings;
    }

    public ArrayList<String> restOfWord(String prefix, TrieNode node, HashMap<String, Long> frequencyMap, ArrayList<String> listOfPrefixStrings){


        TrieNode [] children = node.getChildren();
        //We're at the last letter of our prefix's node in the trie
        //For example, if our prefix was "th" , we'd be looking at all the children of
        //the 'h' node in this particular trie path. The goal is to look at each of the 26
        //possible children of this node and follow a path until we reach a node that has
        //isEndOfWord set to true. Once we find one of those nodes, we add the word to our
        //ArrayList, and go back to our 'h' node and look at the next child.

        //Base Case
        if(node.isEndOfWord()){
            //We use a Hashmap to store the key value pairs of the words, we just added
            //to the arrayList, with their frequency. This way, when we pass through our
            //frequencyMap in Spelling, not only will we get an ArrayList of all the words
            //starting with the prefix, but we'll also link them to their corresponding
            //frequencies in the map for lookup later
            frequencyMap.put(prefix, node.prefixFrequency());
            listOfPrefixStrings.add(prefix);
        }
        //Recursive Case
        for (int i = 0; i < 26; i++) {

            //Start at the prefix and look to see if the last node in the prefix has a child
            //in position i. If it does, add the character value of the child to word and
            //call restOfWord recursively, each time checking to see if the base case is met
            String word = prefix;
            if (children[i] != null) {
                word += children[i].getChar();
                restOfWord(word, children[i], frequencyMap, listOfPrefixStrings);
            }
        }
        return listOfPrefixStrings;

    }

}



