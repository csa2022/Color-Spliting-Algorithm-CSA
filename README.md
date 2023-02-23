# Ancestral Colorings of Perfect Binary Trees With Applications in Private Retrieval of Merkle Proofs

In this paper, we develop a divide-and-conquer algorithm called Color-Splitting Algorithm (CSA) that takes h as input and generates a balanced ancestral coloring for the perfect binary tree T (h) of height h in time almost linear in the number of tree nodes. In fact, this algorithm can generate not only a balanced ancestral coloring but also any ancestral coloring (color classes having heterogeneous sizes) feasible for 𝑇 (ℎ). It is worth noting that this flexibility of our algorithm establishes the existence of optimal combinatorial patterned-batch codes corresponding to the case of servers with heterogeneous storage capacities as well. At the high level, the algorithm colors two sibling nodes at a time and proceeds recursively down to the two subtrees and repeats the process while maintaining the *Ancestral Property: if a color is used for a node then it will not be used for the descendants of that node*. Finding a way to split the colors for the two subtrees to guarantee that the algorithm runs smoothly until the end is a highly nontrivial task. Using our algorithm implemented in Java, we can generate ***a balanced ancestral coloring for the tree T (30) (around two billion nodes) in under five minutes*** (with 16GB allocated for Java’s heap memory). You can find a copy of the paper [here](https://arxiv.org/abs/2205.05211)

## Experimental setup

We run our experiments on Linux server (Intel(R) Xeon(R) CPU E5-2690 v2 @ 3.00GHz) based on Red Hat Enterprise Linux Server version 7.9 (Maipo)  all running Linux 3.10.0-1160.59.1.el7.x86_64 x86_64. We compile our Java code with Java OpenJDK version "1.8.0_322" and Javac version 1.8.0_322. Time generates a balanced ancestral coloring for the tree T (h) as below:


    T(20) = 224 milliseconds
    T(25) = 7523 milliseconds   = 7.52 seconds
    T(30) = 241847 milliseconds = 4.03 minutes      (Recommended the maximum Java heap size: 16GB)
    T(31) = 490781 milliseconds = 8.18 minutes      (Recommended the maximum Java heap size: 32GB)
    T(32) = 978274 milliseconds = 16.30 minutes     (Recommended the maximum Java heap size: 64GB)
    T(33) = 1994432 milliseconds = 33.24 minutes    (Recommended the maximum Java heap size: 120GB)
    T(34) = 4378425 milliseconds = 1.22 hours       (Recommended the maximum Java heap size: 240GB)
    T(35) = 8691602 milliseconds = 2.41 hours       (Recommended the maximum Java heap size: 500GB)

## Compiling CSA
Once Java and Javac are installed, to build CSA simply run:

    javac CSA.java
    java CSA

To build CSA with recommended max heap size simply run:

    javac CSA.java
    java -Xmx32g CSA

## Using CSA

Take a look at the pictures below, guidelines and in CSA.java comments for how to use CSA.  

<img width="415" alt="h5auto" src="https://user-images.githubusercontent.com/102839948/161372568-85df8aed-6424-4977-9853-722879624efe.png">

*Fig 1: An example of the CSA algorithm running option A (Automatic Balanced Ancestral Coloring) when h = 5.*


<img width="575" alt="h3manual" src="https://user-images.githubusercontent.com/102839948/161372572-773c693e-bd18-4a97-b979-00bbc393fce9.png">

*Fig 2: An example of the CSA algorithm running option B (Manual Feasible Color Sequences) with c = [3 3 8].*
