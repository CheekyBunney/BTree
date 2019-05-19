import java.util.*;

public class BTree
{
    private BNode root; //root of B-tree
    private int treeHeight; //height of B-tree
    private int treeSize; //size of B-tree
    private int treeDegree; //degree of tree
    private int maxKey; //Atleast 3, must be odda
    private int maxChild;
    
    private class BNode
    {
        /*Author : Thomas H. Cormen et al.
          Obtained from : https://turing.plymouth.edu/~zshen/Webfiles/notes/CS322/CodeSample/com/mhhe/clrs2e/BTree.java
          Date visited : 10-05-2019 */
        
        /*Author : nil
          Obtained from : https://www.chegg.com/homework-help/questions-and-answers/java-write-method-receives-reference-root-b-tree-t-integer-d-prints-descending-order-keys--q27860359
          Date visited : 10-05-2019 */
        
        /*Author : ParkChangHan
          Obtained from : https://github.com/phishman3579/java-algorithms-implementation/blob/master/src/com/jwetherell/algorithms/data_structures/BTree.java#L126
          Date visited : 13-05-2019 */
        
        private int keys = 0;  //actual number of keys on node
        private Entry[] keyArray = null; // keys stored in node sorted ascendingly
        private BNode[] childArray = null; //array of child nodes
        private int children = 0; //number of children
        private BNode parent = null;
        private BNode child = null;
        private boolean isLeaf;   
        
        public BNode(BNode parent, int maxKey, int maxChild) // alt constructor for BNode
        {
            this.parent = parent;
            this.keyArray = new Entry[maxKey + 1]; 
            this.keys = 0;
            this.childArray = new BNode[maxChild + 1];
            this.isLeaf = true;
        }
    
        private Entry getNode(int index)
        {
            return keyArray[index];
        }
        
        private void addKey(Entry newKey)
        {
            
            keyArray[keys++] = newKey;
            String newNodeKey = newKey.getKey();
            keyInsSort();
        }
        
        private BNode getChild(int index)
        {
            return childArray[index];
        }
       
        private void addChild(BNode child)
        {
            child.parent = this;
            childArray[children++] = child;
            childInsSort();
        }
    
        private void childInsSort() //insertion sort for child nodes
        {
            int nn;
            for (nn = 1; nn < (childArray.length - 1); nn++)
            {
                children = nn;
                while (children > 0 && childArray[children - 1].getChild(0).childrenNo().compareTo(child.getChild(0).childrenNo()) > 0)
                {
                    childArray[children] = childArray[children - 1];
                    children--;
                }
            }
        }
        
        private void keyInsSort() //insertion sort for key nodes
        {
            int  nn;
            for (nn = 1; nn < (keyArray.length - 1); nn++)
            {
                keys = nn;
                while (keys > 0 && keyArray[keys - 1].keyNo().compareTo(newKey.keyNo()) > 0)
                {
                    keyArray[keys] = keyArray[keys - 1];
                    keys--;
                }
            }
        }

    
        private int keyNo() //number of keys in a node
        {
            return keys;    
        }
    
        private int childrenNo() //number of children that a node has
        {
            return children;
        }   
    }
        
    private class Entry //new key node entry
    {
        private String key;
        private Object value;
        
        public Entry(String key, Object value)
        {
            this.key = key;
            this.value = value;
        }
        
        private String getKey()
        {
            return key;
        }
        
        private Object getValue()
        {
            return value;
        }
    }

    public BTree(int degree)
    {
        root = null;
        this.maxKey = 2 * degree;
        this.maxChild = 2 * degree + 1;            
    }

    public int getSize() //size of B-Tree
    {
        return treeSize;
    }
    
    public int getHeight() //height of B-Tree
    {   
        return treeHeight;
    }
    
    public void insert(String key, Object value) //inserting a new key node into a B-Tree
    {
        System.out.println("\nInserting Data : " + key + " " + value);
        Entry newKey = new Entry(key,value);
        if (root == null) // root doesn't exist
        {
            BNode root = new BNode(null, maxKey, maxChild); //new root created
            root.addKey(newKey); //new entry key node inserted into new root
        }
        
        else if (root.childrenNo() == 0)  // root exists but has no children
        {
            BNode node = root; // let current node be root node
            if (node.keyNo() == maxKey) //root node exceeds max key limit
            {
        /*        splitChild(node); //current node will be split, a new root will be created
                height++; //height of tree increased  */
            }
            
            else //root within max key limit
            {
                root.addKey(newKey); //insert entry key node into root node
            }
           
        } 
        else // root exists and has children
        { 
            insertRecursive(root,key,value); // traverse the tree to find the right position to insert the key node
        }
    }            
        
    private BNode insertRecursive(BNode node, String key, Object value)
    {
        //first key node comparison
        int idx = 0;
        String compareKey = node.getNode(idx).getKey(); 
        if (idx == 0 && key.compareTo(compareKey(0)) < 0) //compare new entry key node's key to first key node's key
        {
            node = node.getChild(0); 
            node = insertRecursive(node, key, value);  
            idx++;      
        }
        //last key node comparison
        else if (idx == maxKey - 1 && key.compareTo(compareKey(0)) > 0) //compare new entry key node's key to last key node's key
        {
            node = node.getChild(maxKey - 1);
            node = insertRecursive(node, key, value);
            idx++;
        }
        //inbtwn key nodes comparison
        else
        { 
            for (int i = 1; i < node.keyNo(); i++) //traverse through remaining key nodes besides first and last 
            {
                String prevNodeKey = node.getKey(i - 1);
                String nextNodeKey = node.getKey(i);
                
                if ((node.getKey(i - 1).compareTo(prevNodeKey) > 0) && (node.getKey(i).compareTo(nextNodeKey) < 0)) //new entry node found its position in current child node
                {
                    node = node.getChild(i); 
                }   
            }
        }
    }       
   /* private void splitChild(BNode splitNode)
    {
        System.out.println("Splitting Node");
        int keyNumber = splitNode.keyNo();
        int medIdx = keyNumber / 2;
        Object medValue = newChildNode.getKey(medIdx);
        BNode splitNodeParent = node.parent;
        
        BNode left = new BNode(null, maxKey, maxChild);
        for (int i = 0; i < medIdx; i++)
        {
            left.getKey(i) = splitNode.getKey(i);
            left.getChild(i) = splitNode.getChild(i);
        }
        
        left.getChild[medIdx] = splitNode.getChild[medIdx];
           
        int j = 0;
        BNode right = new BNode(null, maxKey, maxChild);
        if (node.childrenNo() > 0)
        {
            for (int j = medIdx + 1; j < node.childrenNo(); j++)
            {
                right.getKey(i) = splitNode.getKey(i);
                right.getChild(i) = splitNode.getChild(i);
            }
        }

        right.getChild(medIdx) = splitNode.getChild(splitNode.keyNo());
        
        left.isLeaf = splitNode.isLeaf;
        right.isLeaf = splitNode.isLeaf;

        if (splitNode.parent == null)
        {
            BNode newRoot = new BNode(null, maxKey, maxChild);
            newRoot.addKey(medIdx);
            splitNode.parent = newRoot;
            root = newRoot;
            splitNode = root;
            splitNode.addChild(left);
            splitNode.addChild(right);      
        }
        
        else 
        {
            BNode parent = node.parent;
            parent.addKey(medVal);
            parent.removeChild(node);
            parent.addChild(left);
            parent.addChild(right);
        
            if (parent.keyNo() > maxKey)
            {
                splitChild(parent);
            }
        }
    }
    
    public void find(String key, Object value)
    {
        BNode node = root;
        while (node != null)
        {
            Object lesserValue = node.getKey(0);
            if (value.compareTo(lesserValue) < 0)
            {
                if (node.childrenNo() > 0)
                {
                    node = node.getChild(0);
                }
                
                else
                {
                    node = null;
                }
                   
            
            
    */
}
