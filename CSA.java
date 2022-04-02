/*
# TITLE: Ancestral Colorings of Perfect Binary Trees With Applications in Private Retrieval of Merkle Proofs
# PURPOSE: In this paper, we develop a divide-and-conquer algorithm called Color-Splitting Algorithm (CSA)
#          that takes h as input and generates a balanced ancestral coloring for the perfect binary tree T (h)
#          of height h in time almost linear in the number of tree nodes. In fact, this algorithm can generate
#          not only a balanced ancestral coloring but also any ancestral coloring (color classes having heterogeneous
#          sizes) feasible for 𝑇 (ℎ). It is worth noting that this flexibility of our algorithm establishes the
#          existence of optimal combinatorial patterned-batch codes corresponding to the case of servers with
#          heterogeneous storage capacities as well. At the high level, the algorithm colors two sibling nodes
#          at a time and proceeds recursively down to the two subtrees and repeats the process while maintaining
#          the Ancestral Property: if a color is used for a node then it will not be used for the descendants of that node.
#          Finding a way to split the colors for the two subtrees to guarantee that the algorithm runs smoothly until the end
#          is a highly nontrivial task. Using our algorithm implemented in Java, we can generate a balanced ancestral coloring
#          for the tree T (30) (around two billion nodes) in under five minutes (with 16GB allocated for Java’s heap memory).
*/

import java.io.*;
import java.util.*;


//-------------------------------------------Color-Spliting-Algorithm (CSA) Main class----------------------------------------
public class CSA {

    public static void main(String[] args) throws IOException {

        ColorSplittingAlgorithm CSA = new ColorSplittingAlgorithm();

        //"ON"/"OFF" test CSA output functions
        String CHECK = "OFF";
        //Change manual = false to check conditions of all possible feasible sequences. Default manual = true.
        boolean manual = true;
        //"ON"/"OFF" to export CSA results
        String EXP = "OFF";
        //You should change this PATH to save your output when EXP = "ON"
        Data export = new Data(EXP, "/Color-Spliting-Algorithm-CSA-main/Database/test");

        //Array of balanced/unbalanced sets
        NodesSet[] NodesSets;
        CheckCSA test = new CheckCSA();
        //List of all feasible sequences
        ArrayList<ArrayList<NumColor>> F;
        //List of color sequence c
        List<NumColor> c = new ArrayList<>();
        //The color sequence c = [c1,...,ch]
        ArrayList<Integer> vectorC = new ArrayList<>();

        //h is the height of a tree
        byte h = 0;
        //A for Automatic Balanced Ancestral Coloring; B for Manual Feasible Color sequence
        String option;
        boolean normal;
        Scanner input = new Scanner(System.in);
        char[] color = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'
                , 'R','S', 'T', 'U', 'V','W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7','8', '9'};

        //Loop forever to get your input and run the CSA algorithm unless you quit the loop, choosing option "Q".
        while (true) {
            normal = true;
            System.out.print("Removing all the garbage collector .....");
            System.gc(); //runs the garbage collector
            printMenu();

            //Enter input option until option is valid from MENU (A, B, or Q)
            boolean ck;
            do {
                System.out.print("Enter your option (A or B or Q): ");
                option = input.nextLine();
                ck = validOptionMenu(option);
            } while (!ck);

            //Quit
            if (option.charAt(0) == 'Q' || option.charAt(0) == 'q') break;

            //A. * Automatic Balanced Ancestral Coloring *
            //You need only input the tree's height, which is greater than or equal to 2.
            if (option.charAt(0) == 'A' || option.charAt(0) == 'a') {
                h = h(); //Enter the height of a tree "h" from keyboard
                c = CSA.balancedColorSequence(h);
                Collections.sort(c);
            }
            //B. * Manual Feasible Color sequence
            //You can choose your Feasible Color sequence c = [c1,...,ch] (e.g, 4 4 6) which has to satisfy two conditions C1 and C2.
            else if (option.charAt(0) == 'B' || option.charAt(0) == 'b') {
                do {
                    c.clear();
                    h = h(); //Enter the height of a tree "h" from keyboard
                    input = new Scanner(System.in);
                    F = CSA.feasibleConfList(h);  //List of all feasible sequences

                    if(manual) {
                        System.out.println("---------------------------------------------------");
                        System.out.println("Suggested the List of all feasible sequences: ");
                        CSA.printAllFeasibleSeqs(F);
                        System.out.println("---------------------------------------------------");

                        System.out.print("Enter your Feasible Color sequence c = [c1,...,ch] (e.g, " + CSA.oneFeasibleSeq(F) + "):");
                        // Enter option from keyboard
                        String vc = input.nextLine();
                        String[] ci = vc.split(" ");
                        h = (byte) ci.length;

                        for (int i = 0; i < h; i++) {
                            c.add(new NumColor(color[i], Integer.parseInt(ci[i])));
                        }

                        Collections.sort(c);
                    }
                    /*Check if:
                        1. All nodes in the tree (except root) appear exactly once across S
                        2. All node IDs in the tree (except root) are inside the range of IDs (from 2 to (2^(h+1) - 2))
                        3. Total number of nodes in the tree is the same as total nodes in the perfect binary tree.
                        4. No ancestor-descendant pair appears in any set*/
                    else {
                        testAllCSA(CSA, F, test, h);
                        normal = false;
                        break;
                    }
                } while (!CSA.isFeasible(h, c));

            }
            //Invalid input option
            else System.out.println("Warning! Invalid input option. The option has to be a letter A, B or Q");

            if(normal) {
                c.forEach(x -> vectorC.add(x.getSize()));

                //Start Color-Splitting Algorithm ************************** START *************************
                long startTime = System.nanoTime();

                //Color-Splitting Algorithm
                NodesSets = CSA.ColorSplitting(h, c);

                //End Color-Splitting Algorithm ***************************** END **************************
                long endTime = System.nanoTime();
                long timeElapsed = (endTime - startTime) / 1000000;

                //Print input including the height of a tree and a color sequence c = [c1,...,ch]
                System.out.println("*** Input:");
                System.out.println("    Tree height: h = " + h);
                System.out.println("    c = " + vectorC);

                //Print Output
                System.out.println("*** Output:");
                System.out.println("    Execution time in milliseconds: " + timeElapsed);

                //Uncomment/Comment this block below if you want/not to print Output sets in Terminal.
                Arrays.stream(NodesSets).forEach(s -> {
                    System.out.print("    Color " + s.getColorSet() + ": ");
                    Arrays.stream(s.getAddNodes()).forEach(r -> System.out.print(r + " "));
                    System.out.println();
                });

                //Export Output on txt files.
                export.SaveTreeTXT(NodesSets);

                //Turn CHECK to "ON" if you want to test CSA Outputs
                testCSA(CHECK, test, NodesSets, h);

                c.clear();
                vectorC.clear();
            }
        }
    }

    public static void testCSA(String OnOff, CheckCSA test, NodesSet[] NodesSets, byte h) {
        if(OnOff.contains("ON")) {
            //Check if:
            // 1. All nodes in the tree (except root) appear exactly once across S
            // 2. All node IDs in the tree (except root) are inside the range of IDs (from 2 to (2^(h+1) - 2))
            // 3. Total number of nodes in the tree is the same as total nodes in the perfect binary tree.
            if(test.IsValidAllNodeID(NodesSets, h)) System.out.println("IsValidAllNodeID: PASSED!");
            else System.out.println("IsValidAllNodeID: FAILED!");

            //Check if sets have valid sizes
            if(test.IsBalancedSets(NodesSets, h)) System.out.println("IsBalancedSets: PASSED!");
            else System.out.println("IsBalancedSets: FAILED!");

            //Check if no ancestor-descendant pair appears in any set
            if(test.IsRelationship(NodesSets)) System.out.println("IsRelationship: PASSED!");
            else System.out.println("IsRelationship: FAILED!");
        }
    }

    public static void testAllCSA(ColorSplittingAlgorithm CSA, ArrayList<ArrayList<NumColor>> F, CheckCSA test, byte h) {
        NodesSet[] NodesSets;
        List<NumColor> c = new ArrayList<>();
        ArrayList<Integer> vectorC = new ArrayList<>();

        for (ArrayList<NumColor> numColors : F) {
            c.addAll(numColors);
            Collections.sort(c);
            //Color-Splitting Algorithm
            NodesSets = CSA.ColorSplitting(h, c);

            c.forEach(x -> vectorC.add(x.getSize()));
            System.out.print("    c = " + vectorC + ": ");

            /*Check if:
                1. All nodes in the tree (except root) appear exactly once across S
                2. All node IDs in the tree (except root) are inside the range of IDs (from 2 to (2^(h+1) - 2))
                3. Total number of nodes in the tree is the same as total nodes in the perfect binary tree.
                4. No ancestor-descendant pair appears in any set*/
            if(test.IsValidAllNodeID(NodesSets, h)&&test.IsRelationship(NodesSets)) {
                System.out.println("ALL PASSED!");
            }
            else {
                System.out.println("FAILED!");
            }

            c.clear();
            vectorC.clear();
        }
    }

    public static void printMenu() {
        String[] menu = {"\n" + "********* MENU *********",
                "A. * Automatic Balanced Ancestral Coloring *",
                "B. * Manual Feasible Color Sequences *",
                "Q. * Quit *"};

        for (String s : menu) {
            System.out.println(s);
        }
    }

    //Enter the height of a tree "h" from keyboard. h has to greater than or equal to 2.
    public static Byte h() {
        byte height; //the height of the tree
        Scanner input = new Scanner(System.in);

        do {
            System.out.print("Enter the height of a tree \"h\" = ");
            // The height "h" input have to an integer number
            while (!input.hasNextByte()) {
                System.out.print("NOTE! You have to put integer of the height \"h\" = ");
                input.next();
            }
            height = input.nextByte();
            if (height < 2) {
                System.out.println("NOTE! Enter an Integer >= 2");
            }
        } while (height < 2); //The height "h" input has to greater than or equal 2

        return height;
    }

    //Check valid Option in Menu
    public static boolean validOptionMenu(String opt) {
        if (opt.length() != 1) {
            System.out.println("You have to put a character option in MENU (A, B, or Q)");
            return false;
        } else if (opt.charAt(0) != 'A' && opt.charAt(0) != 'a'
                && opt.charAt(0) != 'B' && opt.charAt(0) != 'b'
                && opt.charAt(0) != 'Q' && opt.charAt(0) != 'q') {
            System.out.println("You have to put a character option in MENU (A, B, or Q)");
            return false;
        } else {
            return true;
        }
    }
}


//-----------------------------------------Color-Splitting Algorithm class----------------------------------------------
class ColorSplittingAlgorithm {

    NodesSet[] NodesSets;
    HashMap<List<NumColor>, List<NumColor>> C = new HashMap<>();
    char[] color = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'
            , 'R','S', 'T', 'U', 'V','W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7','8', '9'};

    //Given a feasible sequence 𝑐 = [𝑐1, . . . , 𝑐ℎ], the algorithm finds a 𝑐-coloring of 𝑇 (ℎ)
    public NodesSet[] ColorSplitting(byte h, List<NumColor> c) {
        //the root of 𝑇 (ℎ) is 1
        int R = 1;
        NodesSets = new NodesSet [h];

        for (byte q = 0; q < h; q++) {
            int size = c.get(q).getSize();
            NodesSets[q] = new NodesSet(c.get(q).getColor(), size);
        }

        ColorSplittingRecursive(R, h, c);

        return NodesSets;
    }

    //𝑅 is the root node of the current subtree 𝑇 (ℎ) of height ℎ
    //Either 𝑅 needs no color (𝑅 = 1) or 𝑅 has already been colored in the previous call
    //𝑐 = [𝑐1, . . . , 𝑐ℎ] is a feasible color sequence, which implies that 2 ≤ 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ
    //This procedure colors the two children of 𝑅 and create feasible color sequences for its
    //left and right subtrees
    public void ColorSplittingRecursive(int R, byte h, List<NumColor> c) {
        int A, B;
        List<NumColor> a = new ArrayList<>(h - 1);
        List<NumColor> b = new ArrayList<>(h - 1);
        C.clear();

        //Uncomment this line to check if a sequence c is h-feasible
        //if (!isFeasible(h, c)) return;

        if (h > 0) {
            A = 2 * R; //left child of 𝑅
            B = 2 * R + 1; //right child of 𝑅

            //Assign Color 1 to both 𝐴 and 𝐵; And add A and B to the same set.
            if (c.get(0).getSize() == 2) {
                for (NodesSet s : NodesSets) {
                    if (s.getColorSet() == c.get(0).getColor()) {
                        s.addNode(A);
                        s.addNode(B);
                    }
                }
            }
            //Assign Color 1 to 𝐴 and Color 2 to 𝐵; And add A and B to 2 different sets.
            else {
                for (NodesSet s : NodesSets) {
                    if (s.getColorSet() == c.get(0).getColor()) {
                        s.addNode(A);
                    }
                    if (s.getColorSet() == c.get(1).getColor()) {
                        s.addNode(B);
                    }
                }
            }

            //Split the feasible sequence c to a feasible sequence a and a feasible sequence b
            if (h > 1) {
                C = FeasibleSplit(h, c);
                C.forEach((n, m) -> {
                    a.addAll(n);
                    b.addAll(m);
                });

                Collections.sort(a);
                ColorSplittingRecursive(A, (byte) (h - 1), a);

                Collections.sort(b);
                ColorSplittingRecursive(B, (byte) (h - 1), b);
            }
        }
    }

    /* This algorithm splits a ℎ-feasible sequence into two (ℎ − 1)-feasible ones, which will be used for coloring the subtrees; only works when ℎ ≥ 2.
    Note that the splitting rule (see FeasibleSplit(ℎ, 𝑐)) ensures that if Color 𝑖 is used for a node then it will no longer be used in the subtree rooted at that node,
    hence guaranteeing the Ancestral Property.*/
    //key = a; value = b which are two (ℎ − 1)-feasible of two subtrees following "Procedure FeasibleSplit(ℎ, 𝑐)"
    public HashMap<List<NumColor>, List<NumColor>> FeasibleSplit(byte h, List<NumColor> c) {
        List<NumColor> a = new ArrayList<>(h - 1);
        List<NumColor> b = new ArrayList<>(h - 1);
        C.clear();

        if (h == 2) {
            byte i = 1; //Position 2
            int aValue, bValue;

            if (c.get(0).getSize() == 2) {
                aValue = c.get(i).getSize() / 2;
                bValue = c.get(i).getSize() / 2;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(1).getColor(), bValue));
            }
            else {
                aValue = c.get(i).getSize() - 1;
                bValue = c.get(0).getSize() - 1;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(0).getColor(), bValue));
            }

            Collections.sort(a);
            Collections.sort(b);
            C.put(a, b);

            return C;
        }
        else if (h > 2) {
            //Case 1: 𝑐1 = 2
            if (c.get(0).getSize() == 2) {
                byte i = 1; //Position 2
                int aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                int bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                a.add(new NumColor(c.get(i).getColor(), aValue));
                b.add(new NumColor(c.get(i).getColor(), bValue));
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                //change for original algorithm
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                    }
                    else {
                        aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                    }
                    a.add(new NumColor(c.get(i).getColor(), aValue));
                    b.add(new NumColor(c.get(i).getColor(), bValue));
                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            //Case 2: 𝑐1 > 2; note that 𝑐1 ≥ 2 due to the feasibility of 𝑐;
            else {
                byte i = 1; //Position 2
                int aValue = c.get(i).getSize() - 1;
                int bValue = c.get(0).getSize() - 1;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(0).getColor(), bValue));
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                aValue = (int) Math.ceil((c.get(i).getSize() + c.get(0).getSize() - c.get(i - 1).getSize()) / 2.0);
                bValue = c.get(i - 1).getSize() - c.get(0).getSize() + (int) Math.floor((c.get(i).getSize() + c.get(0).getSize() - c.get(i - 1).getSize()) / 2.0);
                a.add(new NumColor(c.get(i).getColor(), aValue));
                b.add(new NumColor(c.get(i).getColor(), bValue));
                Sa += aValue;
                Sb += bValue;
                i++;
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                    }
                    else {
                        aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                    }
                    a.add(new NumColor(c.get(i).getColor(), aValue));
                    b.add(new NumColor(c.get(i).getColor(), bValue));

                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            Collections.sort(a);
            Collections.sort(b);
            C.put(a, b);
            return C;
        }
        else {
            System.out.println("Warming! h should be greater than or equal 2"); //𝑐 ≥ 2 due to the feasibility of 𝑐;
            return null;
        }
    }

    //Corollary 2.12 (Balanced Color sequence) return 𝑐 = [𝑐1, 𝑐2, . . . , 𝑐ℎ]
    public List<NumColor> balancedColorSequence(byte h) {
        int cValue;
        byte u = (byte) ((Math.pow(2, h + 1) - 2) % h);
        List<NumColor> c = new ArrayList<>(h);

        for (byte i = 0; i < h; i++) {
            double a = (Math.pow(2, h + 1) - 2) / h;
            if (i < (h - u)) {
                cValue = (int) Math.floor(a);
                c.add(new NumColor(color[i], cValue));
            }
            else {
                cValue = (int) Math.ceil(a);
                c.add(new NumColor(color[i], cValue));
            }
        }

        return c;
    }

    //Definition 2.3 (Feasible Color Sequence). A color sequence 𝑐 of dimension ℎ is called
    //"ℎ-feasible" if after being sorted in a non-decreasing order (so that 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ), it satisfies
    //the following two conditions: (C1) and (C2)
    public boolean isFeasible(byte h, List<NumColor> c) {
        for (byte m = h; m > 0; m--) {
            int sum;
            sum = getTotalColorSize(m, c);
            //check (C2)
            if (m == h && sum != (int) (Math.pow(2, m + 1) - 2)) {
                System.out.println("\n" + " ***** WARNING! *****");
                System.out.println("The color sequence is NOT feasible");
                System.out.println("Conflict with C2: The total size of 𝑐 is equal to the number of nodes in 𝑇 (ℎ)");
                return false;
            }
            //check (C1)
            if (sum < (int) Math.pow(2, m + 1) - 2) {
                System.out.println("\n" + " ***** WARNING! *****");
                System.out.println("The color sequence is NOT feasible");
                System.out.println("Conflict with C1: Colors 1, 2, . . . , ℓ can be used to color all nodes in Layers 1, 2, . . . , ℓ of the perfect binary tree 𝑇 (ℎ)");
                return false;
            }
        }

        return true;
    }

    //return the list of all feasible sequences
    public ArrayList<ArrayList<NumColor>> feasibleConfList(byte h){
        int m = 0;  //the current position of c to be filled in
        ArrayList<NumColor> c = new ArrayList<>(h);
        ArrayList<ArrayList<NumColor>> F = new ArrayList<>();

        feasibleConfListRecursive(F, c, m, h);

        //test feasibility of sequences in F
        for (ArrayList<NumColor> numColors : F) {
            if (!isFeasible(h, numColors)) {
                System.out.println("c = " + numColors + " is NOT feasible \n");
                return null;
            }
        }

        return F;
    }

    //given c = [c0,...,cm-1], try all possibilities for c_m that still guarantees feasibility
    public void feasibleConfListRecursive(ArrayList<ArrayList<NumColor>> F, ArrayList<NumColor>  c, int m, byte h){
        char[] color = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'
                , 'R','S', 'T', 'U', 'V','W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7','8', '9'};

        //if we have filled c then add c to the list F
        if (m == h) {
            //ArrayList<Integer> conf = new ArrayList<>(h);
            ArrayList<NumColor> conf = new ArrayList<>(h);
            for (int i = 0; i < h; i++) conf.add(i, new NumColor(color[i], c.get(i).getSize()));
            //conf.add(i, c.get(i).getSize());
            F.add(conf);
            if (!isFeasible(h, conf)) System.out.println("INFEASIBLE");
            return;
        }

        int current_sum = 0;
        int sum = (int) Math.pow(2,h+1)-2;

        for (int i = 0; i <= m-1; i++) {
            current_sum += c.get(i).getSize();
        }

        int remaining_sum = sum-current_sum;
        int upper = (int) Math.floor(remaining_sum/(h-m)); //largest possible value for c_m
        int lower = (m==0)? 2 : (int) Math.max(c.get(m-1).getSize(), (Math.pow(2,m+2)-2)-current_sum); //smallest possible value for c_m

        for (int cm = lower; cm <= upper; cm++) {
            if (c.size() <= m) {
                c.add(m, new NumColor(color[m], cm));
            }
            else c.set(m, new NumColor(color[m], cm));
            feasibleConfListRecursive(F, c, m+1, h);
        }
    }

    //Print the list of all feasible sequences
    public void printAllFeasibleSeqs(ArrayList<ArrayList<NumColor>> F) {
        for (ArrayList<NumColor> numColors : F) {
            for (NumColor numColor : numColors) System.out.print(numColor.getSize() + " ");
            System.out.println();
        }
    }

    //Print the one random feasible sequences
    public String oneFeasibleSeq(ArrayList<ArrayList<NumColor>> F) {
        Random rand = new Random(); //instance of random class
        //generate random values from 0-F.size()
        int int_random = rand.nextInt(F.size());
        ArrayList<NumColor> numColors = F.get(int_random);
        StringBuilder s = new StringBuilder();

        for (NumColor numColor : numColors) {
            s.append(numColor.getSize()).append(" ");
        }

        return s.toString();
    }

    //Sum all color values
    public int getTotalColorSize(byte m, List<NumColor> c) {
        if (0 < m && m <= c.size()) {
            int sum = 0;
            for (byte i = 0; i < m; i++) {
                sum += c.get(i).getSize();
            }
            return sum;
        }
        else {
            System.out.println("Warming! m should be positive and less than or equal h");
            return 0;
        }
    }
}


//-----------------------------------------Set of Nodes have the same color class------------------------------------------------------------
/* An example of a perfect binary tree with h = 2
                       1
                    /    \
                  2       3
                /  \    /  \
               4   5   6    7
*/
//Each set will contain all node with the same color
class NodesSet {

    private int count = 0;
    private final char colorSet;
    //Array of node IDs (R)
    private final int [] addNodes;

    public NodesSet(char colorSet, int size) {
        this.colorSet = colorSet;
        addNodes = new int [size];
    }

    public void addNode(int root) {
        //array start from 0, whereas node colored start from 2
        addNodes[count] = root;
        count++;
    }

    public int [] getAddNodes() {
        return addNodes;
    }

    public char getColorSet() {
        return colorSet;
    }

    public int getSize() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder roots = new StringBuilder();
        for (int addNode : addNodes) {
            roots.append(addNode).append(" ");
        }

        return roots.toString();
    }
}


//-----------------------------------------NumColor class---------------------------------------------------------------
//Stored and Sorted vector c.
class NumColor implements Comparable<NumColor> {

    private final int size;
    private final char color;

    public NumColor(char color, int size) {
        this.color = color;
        this.size = size;
    }

    public char getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int compareTo(NumColor otherNumColor) {
        return Integer.compare(getSize(), otherNumColor.getSize());
    }
}


//---------------------------------------------------------Data export class---------------------------------------------------------------
//Export Output. An example of T(2) as below: (first line is the height of the tree; second line and third line are nodes of the same color
//2
//2 6 7
//3 4 5
class Data {

    private final String PATH;
    private String EXP;

    public Data(String EXP, String PATH) {
        this.PATH = PATH;
        this.EXP = EXP;
    }

    public void SaveTreeTXT(NodesSet[] NodesSets) throws IOException {
        if(this.EXP.contains("ON")) {
            File file = new File(PATH + NodesSets.length + ".txt"); //Create testh.txt
            FileWriter out = new FileWriter(file, false); //Set true for append mode
            PrintWriter output = new PrintWriter(out);

            output.println(NodesSets.length);

            Arrays.stream(NodesSets).forEach(s -> {
                Arrays.stream(s.getAddNodes()).forEach(r -> output.print(r + " "));
                output.println();
            });

            output.close();
        }
    }
}


//-----------------------------------------CHECK the CSA outputs class---------------------------------------------------------------
class CheckCSA{

    //Check if:
    // 1. All nodes in the tree (except root) appear exactly once across S
    // 2. All node IDs in the tree (except root) are inside the range of IDs (from 2 to (2^(h+1) - 2))
    // 3. Total number of nodes in the tree is the same as total nodes in the perfect binary tree.
    public boolean IsValidAllNodeID(NodesSet[] S, int h) {
        int nodeID;
        int numNodes = 0;
        int n = (int) Math.pow(2, h);
        int minNode = 2, maxNode = 2 * n - 1;

        if (S.length !=h ) {
            System.out.println("The size of S is not " + h);
            return false;
        }

        //true if the corresponding node has been allocated to some sets before
        boolean[] isAllocated = new boolean[2 * n];

        for (int i = 0; i < isAllocated.length; i++) {
            isAllocated[i] = false;
        }

        for (NodesSet nodesSet : S) {
            numNodes += nodesSet.getSize();
            for (int j = 0; j < nodesSet.getSize(); j++) {
                nodeID = nodesSet.getAddNodes()[j];
                if ((nodeID < minNode) || (nodeID > maxNode)) {
                    System.out.println("ERROR: Nodes are out of range: " + nodeID);
                    return false;
                }
                if (isAllocated[nodeID]) {
                    System.out.println("ERROR: Overlapping sets");
                    return false;
                }
                isAllocated[nodeID] = true;
            }
        }

        if (numNodes != 2 * n - 2) {
            System.out.println("ERROR: The total number of tree nodes is not correct");
            return false;
        }

        return true;
    }

    //Check if sets have valid sizes
    public boolean IsBalancedSets(NodesSet[] S, int h) {
        Set<Integer> validSizes = setSizes(h);
        //Second, test if sets have valid sizes
        for (int i = 0; i < S.length; i++) {
            if (!validSizes.contains(S[i].getSize())) {
                System.out.println("ERROR: Set " + i + " has an invalid size = " + S[i].getSize());
                return false;
            }
        }

        return true;
    }

    //Check if no ancestor-descendant pair appears in any set
    public boolean IsRelationship(NodesSet[] S) {
        for (int i = 0; i < S.length; i++) {
            Arrays.sort(S[i].getAddNodes());
            for (int j = 0; j < S[i].getSize(); j++) {
                if (IsDescendantOf(S[i].getAddNodes()[j], S[i])) {
                    System.out.println("ERROR: Set " + i + " contains an ancestor-descendant pair");
                    return false;
                }
            }
        }

        return true;
    }

    //Using the rule: leftchild = 2*parent; rightchild = 2*parent+1
    public static boolean IsDescendantOf(int descendant, int ancestor) {
        int parent = descendant / 2;

        while (parent > ancestor) {
            parent = parent / 2;
        }

        return parent == ancestor;
    }

    public static boolean IsDescendantOf(int descendant, NodesSet ancestorsList) {
        for (int i = 0; i < ancestorsList.getSize(); i++) {
            if (IsDescendantOf(descendant, ancestorsList.getAddNodes()[i])) return true;
        }
        return false;
    }

    public Set<Integer> setSizes(int h) {
        Set<Integer> validSizes = new LinkedHashSet<>(2);
        int n = (int) Math.pow(2, h);
        validSizes.add((2 * n - 2) / h);

        if ((2 * n - 2) % h != 0) {
            validSizes.add((2 * n - 2) / h + 1);
        }

        return validSizes;
    }
}
