package routing;  
  
import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.List;  
  
public class MyTree<T> {  
    private final int DEFAULT_SIZE = 2;  
    private int size;  
    private int count;  
    private Object[] nodes;  
  
    public MyTree() {  
        this.size = this.DEFAULT_SIZE;  
        this.nodes = new Object[this.size];  
        this.count = 0;  
    }  
  
    public MyTree(Node<T> root) {  
        this();  
        this.count = 1;  
        this.nodes[0] = root;  
    }  
  
    public MyTree(Node<T> root, int size) {  
        this.size = size;  
        this.nodes = new Object[this.size];  
        this.count = 1;  
        this.nodes[0] = root;  
    }  
  
    //���һ���ڵ�  
    public void add(Node<T> node) {  
        for (int i = 0; i < this.size; i++) {  
            if (this.nodes[i] == null) {  
                nodes[i] = node;  
                break;  
            }  
        }  
        this.count++;  
    }  
  
    public void check(){  
        if(this.count >= this.size){  
            this.enlarge();  
        }  
    }  
    //���һ���ڵ㣬��ָ�����ڵ�  
    public void add(Node<T> node, Node<T> parent) {  
        this.check();  
        node.setParent(this.position(parent));  
        this.add(node);  
    }  
  
    //��ȡ�ڵ�������Ĵ洢λ��  
    public int position(Node<T> node) {  
        for (int i = 0; i < this.size; i++) {  
            if (nodes[i] == node) {  
                return i;  
            }  
        }  
        return -1;  
    }  
    
    public boolean hassame(Node<T> node) {  
    	List<Node<T>> list = new ArrayList<Node<T>>();
    	list=this.getAllNodes();
    	for (Node<T> temp : list) {  
            if (temp==node) {  
                return false;  
            }  
        }  
        return true;  
    }  
      
    //��ȡ�������ж��ٽڵ�  
    public int getSize(){  
        return this.count;  
    }  
      
    //��ȡ���ڵ�  
    @SuppressWarnings("unchecked")  
    public Node<T> getRoot(){  
        return (Node<T>) this.nodes[0];  
    }  
      
    //��ȡ���Խڵ㣬��List��ʽ����  
    @SuppressWarnings("unchecked")  
    public List<Node<T>> getAllNodes(){  
        List<Node<T>> list = new ArrayList<Node<T>>();  
        for(int i=0;i<this.size;i++){  
            if(this.nodes[i] != null){  
                list.add((Node<T>)nodes[i]);  
            }  
        }  
        return list;  
    }  
    
    
      
    //��ȡ������ȣ�ֻ�и��ڵ�ʱΪ1  
    @SuppressWarnings("unchecked")  
    public int getDepth(){  
          
        int max = 1;  
        if(this.nodes[0] == null){  
            return 0;  
        }  
          
        for(int i=0;i<this.count;i++){  
            int deep = 1;  
            int location = ((Node<T>)(this.nodes[i])).getParent();  
            while(location != -1 && this.nodes[location] != null){  
                location = ((Node<T>)(this.nodes[location])).getParent();  
                deep++;  
            }  
            if(max < deep){  
                max = deep;  
            }  
        }  
        return max;  
    }  
      
    public void enlarge(){  
        this.size = this.size + this.DEFAULT_SIZE;  
        Object[] newNodes = new Object[this.size];  
        newNodes = Arrays.copyOf(nodes, this.size);  
        Arrays.fill(nodes, null);  
        this.nodes = newNodes;  
        //System.out.println("enlarge");  
    }  
} 