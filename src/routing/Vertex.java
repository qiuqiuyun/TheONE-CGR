package routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vertex {

	    //�ڵ�����
		private Integer Node;
		//·������
		//HashMap<Integer,Long> PathLength_HashMap = new HashMap<Integer,Long>();
		//�ڵ�֮��ĽӴ�ʱ��
		HashMap<Integer,Long> PathDist_HashMap = new HashMap<Integer,Long>();
		//�ڵ��Ƿ��Ѿ����У��Ƿ��Ѿ�������ϣ�
		HashMap<Integer,Boolean> Visited = new HashMap<Integer,Boolean>();
		//����ʱ�����·���㷨�Ľڵ㼯��
		List<Integer> NodeSet = new ArrayList<Integer>();
		//�����鲥·���㷨�Ľڵ㼯��
		List<Integer> VertexSet = new ArrayList<Integer>();
		//���·��
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
