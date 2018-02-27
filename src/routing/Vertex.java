package routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vertex {

	    //节点名称
		private Integer Node;
		//路径长度
		//HashMap<Integer,Long> PathLength_HashMap = new HashMap<Integer,Long>();
		//节点之间的接触时间
		HashMap<Integer,Long> PathDist_HashMap = new HashMap<Integer,Long>();
		//节点是否已经出列（是否已经处理完毕）
		HashMap<Integer,Boolean> Visited = new HashMap<Integer,Boolean>();
		//用于时序最短路径算法的节点集合
		List<Integer> NodeSet = new ArrayList<Integer>();
		//用于组播路由算法的节点集合
		List<Integer> VertexSet = new ArrayList<Integer>();
		//最短路径
		HashMap<Integer,String> Path_HashMap = new HashMap<Integer,String>();
		
		public Vertex() {
			this.Node=null;
		}
		public Vertex(int node) {
			this.Node=node;	
		}
		
		public void Initial_Node(int s) {
			this.Node=s;
		}
		
		
		
		
}
