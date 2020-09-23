/**
 * Team Name: El Cucharachas
 * 
 * Students: - Ahmed Jouda 18329393 - Sean Mcdonnell 18391961 - Lleno Anya
 * 18357493
 *
 */
public class Tree {

    Tree[] children;
    Character letter;
    boolean endOfWord;

    public Tree() {
        children = new Tree[26];
        letter = null;
        endOfWord = false;
    }

    //Sets the letter based on the given int
    void fill(int i){
        this.letter = (char) (i + 65);
    }
    
    //Fills a child node with the appropriate letter value
    public void set(int i) {
        if(children[i] == null){
            children[i] = new Tree();
        }

        children[i].fill(i);
    }

    @Override
    public String  toString() {
        return  String.valueOf(letter);
    }
    
    //To set the dictionary words into the tree data structure
    public void set(String cat) {
        int len = cat.length();

        if(len == 0){
            endOfWord = true;
        }
        else{
            int index = cat.charAt(0) - 65;
            set(index);
            String newCat = cat.substring(1);
            children[index].set(newCat);
        }
    }

    //Searches for a given word and returns true if its in the dictionary
    public boolean find(String cat) {
        int len = cat.length();

        if(len == 0){
            return endOfWord;
        }
        else{
            int index = cat.charAt(0) - 65;
            if (children[index] != null){
                String newCat = cat.substring(1);
                return children[index].find(newCat);
            }
            else
                return false;
        }
    }
}

