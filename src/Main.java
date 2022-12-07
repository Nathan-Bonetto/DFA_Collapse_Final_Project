import java.util.HashMap;
import java.util.Vector;

public class Main {
    static public class Node {
        // First state
        String val1;
        // Second state
        String val2;
        // Left child (or child "a")
        Node left;
        // Right child (or child "b")
        Node right;
        // Marked or unmarked
        boolean marked;

        Node(String str1, String str2) {
            this.val1 = str1;
            this.val2 = str2;
            this.left = null;
            this.right = null;
            this.marked = false;
        }
    }
    static HashMap<String, String[]> key = new HashMap<String, String[]>();
    // Vector to keep track of what state combinations have been made to avoid repetition
    static Vector<String> testCompare = new Vector<String>();
    static Vector<Node> rootNodes = new Vector<Node>();
    // How deep to go when expanding states
    static final int iterator = 2;

    public static void fillHashMap() {
        // Key = State, Value = array containing output of State based on "a" or "b" input
        // IMPORTANT: a = index 0, b = index 1
        String[] temp =  {"1", "0"};
        key.put("0", temp);

        String[] temp2 = {"1", "2"};
        key.put("1", temp2);

        String[] temp3 = {"3F", "0"};
        key.put("2", temp3);

        String[] temp4 = {"3F", "4F"};
        key.put("3F", temp4);

        String[] temp5 = {"3F", "5F"};
        key.put("4F", temp5);

        String[] temp6 = {"3F", "5F"};
        key.put("5F", temp6);
    }

    // Creates all the base root nodes (this is the equivalent to the chart you draw with the original states)
    public static void fillNodeArray() {
        for(String i : key.keySet()) {
            for(String j : key.keySet()) {
                // Test for any kind of repetition in state combinations
                String test = i + j;
                String test2 = j + i;
                if(!testCompare.contains(test) && !testCompare.contains(test2)) {
                    Node temp = new Node(i, j);
                    if((i.contains("F") && j.contains("F")) || (!i.contains("F") && !j.contains("F"))) {
                        temp.marked = false;
                    } else {
                        temp.marked = true;
                    }
                    rootNodes.add(temp);
                    testCompare.add(test);
                }
            }
        }
    }

    // Expands the root nodes recursively until a specified depth
    public static void fillNodeTree(Node node, int iterate) {
        // Stop iterating if any node of the tree is marked, or if completed the specified amount of iterations
        if(node.marked || iterate <= 0) {
            return;
        }
        // Pull the "a" outputs from states
        String str1 = key.get(node.val1)[0];
        String str2 = key.get(node.val2)[0];

        Node temp = new Node(str1, str2);
        if((str1.contains("F") && str2.contains("F")) || (!str1.contains("F") && !str2.contains("F"))) {
            temp.marked = false;
        } else {
            temp.marked = true;
        }
        // Set node as left, or "a", child. Then decrement iterator
        node.left = temp;
        fillNodeTree(node.left, iterate - 1);

        // Pull the "b" output from states
        String str3 = key.get(node.val1)[1];
        String str4 = key.get(node.val2)[1];

        Node temp2 = new Node(str3, str4);
        if ((str3.contains("F") && str4.contains("F")) || (!str3.contains("F") && !str4.contains("F"))) {
            temp2.marked = false;
        } else {
            temp2.marked = true;
        }
        // Set node as right, or "b", child. Then decrement iterator
        node.right = temp2;
        fillNodeTree(node.right, iterate - 1);
    }

    // Prints out node tree, the root node is printed in the center, while the left children are above
    // and the right children are below
    public static void inOrder(Node node) {
        if (node == null) {
            return;
        }

        inOrder(node.left);
        System.out.println(node.val1 + " " + node.val2 + ", " + node.marked);
        inOrder(node.right);
    }

    // Prints out the hashmap keys and values
    public static void printHashMap() {
        for (String name: key.keySet()) {
            String key2 = name.toString();
            String[] value = key.get(name);
            System.out.println(key2 + " " + value[0] + ", " + value[1]);
        }
    }

    public static void main(String[] args) {
        fillHashMap();
        fillNodeArray();

        for(Node i : rootNodes) {
            fillNodeTree(i, iterator);
        }

        // Print out each root node's tree with clear separation between the trees
        for(Node i : rootNodes) {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("------ " + "Printing out node: " + i.val1 + ", " + i.val2 + " ------");
            inOrder(i);
        }

        // TODO: if one node is marked, make sure all occurrences of it are marked
        //  (right now the nodes don't interact outside of their trees, so while one
        //  occurrence may be unmarked, another could be marked, invalidating the marked occurrence no matter what)
    }
}