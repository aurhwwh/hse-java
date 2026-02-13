package hse.java.lectures.lecture3.practice.randomSet;
import java.util.Random;

public class RandomSet<T extends Comparable<T>> {

    static Random rand=new Random();
    private int root = 0;
    private int amountOfInts = 0;
    private int amountOfNodes = 0;
    private int capacity = 8;

    @SuppressWarnings("unchecked")
    T[] values = (T[]) new Comparable[8];
    private static class Node{
        public int valueIndex;
        public int left=-1;
        public int right=-1;
        public int priority;
        Node(int valueIndex){
            this.valueIndex=valueIndex;
            this.priority = rand.nextInt();
        }
    }

    Node[] nodes = new Node[8];
    int[] numberNodeForValue = new int[8];
    private void resize(){

        @SuppressWarnings("unchecked")
        T[] newValues = (T[]) new Comparable[capacity*2];
        for (int i = 0; i < values.length; i++) newValues[i] = values[i];
        values = newValues;

        Node[] newNodes = new Node[capacity*2];
        for (int i = 0; i < nodes.length; i++) newNodes[i] = nodes[i];
        nodes=newNodes;

        int[] newNumberNodeForValue = new int[capacity*2];
        for (int i = 0; i < numberNodeForValue.length; i++) newNumberNodeForValue[i] = numberNodeForValue[i];
        numberNodeForValue = newNumberNodeForValue;

        capacity = capacity * 2;
    }
    private int merge(int a, int b){
        if(a==-1) return b;
        if(b==-1) return a;
        if(nodes[a].priority>nodes[b].priority || (nodes[a].priority==nodes[b].priority && values[nodes[a].valueIndex].compareTo(values[nodes[b].valueIndex])>0)){
            nodes[a].right=merge(nodes[a].right, b);
            return a;
        }
        nodes[b].left=merge(a,nodes[b].left);
        return b;
    }
    class Pair {
        int a;
        int b;
    }
    private Pair split(int n, T key) {
        if (n == -1) {
            Pair p = new Pair();
            p.a = -1;
            p.b = -1;
            return p;
        }
        if (values[nodes[n].valueIndex].compareTo(key) < 0) {
            Pair res = split(nodes[n].right, key);
            nodes[n].right = res.a;
            res.a = n;
            return res;
        } else {
            Pair res = split(nodes[n].left, key);
            nodes[n].left = res.b;
            res.b = n;
            return res;
        }
    }


    public boolean insert(T value) {
        if (contains(value)) return false;
        amountOfInts+=1;
        if(amountOfNodes>=capacity) resize();
        values[amountOfInts-1]=value;
        int newNode = amountOfNodes;
        nodes[newNode] = new Node(amountOfInts-1);
        numberNodeForValue[amountOfInts-1]=amountOfNodes;
        if (amountOfNodes!=0){
        Pair p = split(root, value);
        root=merge(merge(p.a,newNode),p.b);
        }
        amountOfNodes++;
        return true;
    }

    public boolean remove(T value) {
        if (!contains(value)) return false;
        Pair p = split(root, value);
        int current = p.b;
        int parent = -1;
        while (nodes[current].left != -1) {
            parent = current;
            current = nodes[current].left;
        }
        int valueIndexToRemove = nodes[current].valueIndex;
        int newRightTree;
        if (parent == -1) {
            newRightTree = nodes[current].right;
        } else {
            nodes[parent].left = nodes[current].right;
            newRightTree = p.b;
        }
        root = merge(p.a, newRightTree);

        int lastIndex = amountOfInts - 1;
        if (valueIndexToRemove != lastIndex) {
            T lastValue = values[lastIndex];
            values[valueIndexToRemove] = lastValue;
            int movedNode = numberNodeForValue[lastIndex];
            nodes[movedNode].valueIndex = valueIndexToRemove;
            numberNodeForValue[valueIndexToRemove] = movedNode;
        }

        values[lastIndex] = null;
        amountOfInts--;

        return true;
    }


    public boolean contains(T value) {
        if(amountOfInts==0) return false;
        int t = root;
        while (t!=-1){
            if(values[nodes[t].valueIndex].equals(value)) return true;
            else if (values[nodes[t].valueIndex].compareTo(value)<0) {
                t=nodes[t].right;
            }
            else t = nodes[t].left;
        }
        return false;
    }

    public T getRandom() {
        if(amountOfInts == 0) throw new EmptySetException("");
        return values[rand.nextInt(amountOfInts)];
    }

}
