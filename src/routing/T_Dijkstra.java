package routing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 


public class T_Dijkstra {

	//private Vertex vertexs;
	//static Vertex vertexs = new Vertex();
	private static final int vertex_num = 9;
	//static 
	

		
	public static int read_source(String str) {
		  String sourcenode = "(\\s+\\d{1,2})";
		  Pattern pattern_sourcenode = Pattern.compile(sourcenode);
		  Matcher matcher_sourcenode = pattern_sourcenode.matcher(str);
		  int source=1;
		  if(matcher_sourcenode.find())
      {
			  String str_source=matcher_sourcenode.group();
			//ȥ���ַ�����ͷ�ͽ�β�Ŀո�
			  str_source = str_source.trim();
			  source = Integer.parseInt(str_source);
			  
			  }
		  return source;
		 
	}
	
	
	public static int read_destination(String str) {
		  String destinationnode = "(\\s+\\d{1,2}+\\s+[A-Z])";
		  Pattern pattern_destinationnode = Pattern.compile(destinationnode);
		  Matcher matcher_destinationnode = pattern_destinationnode.matcher(str);
		  int destination=0;
		  if(matcher_destinationnode.find())
    {
			  String str_destination=matcher_destinationnode.group();
			  str_destination = str_destination.trim();
			  str_destination = str_destination.substring(0,str_destination.length()-1);
			  str_destination = str_destination.trim();
			  destination = Integer.parseInt(str_destination);			  
			  }
		  return destination;
	}
	
	//�жϽڵ��ǽ������ӻ��ǶϿ�����
	public static int judgment_state(String str) {
		String upstate = "(UP)";
		String downstate = "(DOWN)";
		Pattern pattern_upstate = Pattern.compile(upstate);
		Matcher matcher_upstate = pattern_upstate.matcher(str);
		Pattern pattern_downstate = Pattern.compile(downstate);
		Matcher matcher_downstate = pattern_downstate.matcher(str);
		int state=0;
		 if(matcher_upstate.find())
	      {
			 state=1;
	      }
		 if(matcher_downstate.find())
	      {
			 state=0;
	      }	
		return state;		
	}
	
	//��ȡʱ��
	public static long read_time(String str) {
		String times = "([0-9]{2,6})";
		Pattern pattern_time = Pattern.compile(times);
	    Matcher matcher_time = pattern_time.matcher(str);
		long time=0;
		  if(matcher_time.find())
  {
			  String str_time=matcher_time.group();
			  time = Long.parseLong(str_time);			  
			  }
		  return time;
		
	}
	
	
	//ɾ��list��ʱ���time֮ǰ������
		public static List<HashMap<String, Object>> cut_list(List<HashMap<String, Object>> list,long time) {
			List<HashMap<String, Object>> listtemp=new ArrayList<HashMap<String, Object>>(list);
			//listtemp=list;
			Iterator<HashMap<String, Object>> it = listtemp.iterator();
		       while(it.hasNext()) {  
		       HashMap<String, Object> stuMap = it.next();    
		       long t=Long.parseLong(stuMap.get("time").toString()); 
		       int s=Integer.parseInt(stuMap.get("state").toString());
		       if(t<=time && s==0 ) {     
		    	   
		    	   
		    	   //int flag=0;
		    	   Iterator<HashMap<String, Object>> ito = list.iterator();
		    	   while(ito.hasNext()) {
		    		  
		    	       HashMap<String, Object> stuMaper = ito.next();  
		    	       int e=Integer.parseInt(stuMap.get("source").toString());
				       int d=Integer.parseInt(stuMap.get("destination").toString());
		    	       int sr=Integer.parseInt(stuMaper.get("state").toString());
		    	       int er=Integer.parseInt(stuMaper.get("source").toString());
		    	       int dr=Integer.parseInt(stuMaper.get("destination").toString());
		    	       //long tr=Long.parseLong(stuMaper.get("time").toString());
		    	       //flag++;
		    	       if((sr == 1) && (er==e) && (dr==d) ) {
		    	    	   ito.remove(); 
		    	    	   break;
		    	       }
		    	       
		    	       
		       }
		    	   //list.remove(flag);  
		       
		       }else if(t>time){
		    	   break;
		    	   }
		       }
		       //list=list.subList(flag-1, list.size());
		       return list;
		}
		
		//�Ƴ�״̬��down������
		public static List<HashMap<String, Object>> remove_down(List<HashMap<String, Object>> list,long time) {
			Iterator<HashMap<String, Object>> it = list.iterator();
			//int flag=0;
		       while(it.hasNext()) {  
		       HashMap<String, Object> stuMap = it.next(); 
		       long t=Long.parseLong(stuMap.get("time").toString());
		       int s=Integer.parseInt(stuMap.get("state").toString());
		       //flag++;
		       if(s == 0 && t<=time) {
		    	   it.remove();
		       }
		       }
		       
		       return list;
		}
		
		//���������ڵ㣬���������ڵ���С�����ӿ�ʼʱ��
		public static long search_time(List<HashMap<String, Object>> list,int s,int d) {
			 Iterator<HashMap<String, Object>> it = list.iterator();
			 int flag=0;
			 long time=0;
		       while(it.hasNext()) {  
		       HashMap<String, Object> stuMap = it.next();
		       if((s==Integer.parseInt(stuMap.get("source").toString())) && (d==Integer.parseInt(stuMap.get("destination").toString())) && (Integer.parseInt(stuMap.get("state").toString())==1)) {
		    	   time=Long.parseLong(stuMap.get("time").toString());
		    	   flag=1;
		    	   break;
		       }
		        }
		       if(flag==1) {
		    	   return time;
		       }else {return Long.MAX_VALUE;}
		       
		}
		
		
		//��ʼ���ڵ���Ϣ
		public static void Initial_Vertexs(Vertex vertexs,List<HashMap<String, Object>> list,int from) {
			vertexs.Initial_Node(from);
			for(int i=0;i<vertex_num;i++) {
				vertexs.PathDist_HashMap.put(i,search_time(list,from,i));
			}
		}
		
		//���½ڵ���Ϣ
		public static void Update_Path(Vertex vertexs,List<HashMap<String, Object>> list,int from,int min) {
			for(int nodeset : vertexs.NodeSet) {
				  if(search_time(list,min,nodeset)<vertexs.PathDist_HashMap.get(nodeset)) {
					  //System.out.println(min+","+nodeset+":"+search_time(list,min,nodeset)+"<"+vertexs.PathDist_HashMap.get(nodeset));
					  vertexs.PathDist_HashMap.put(nodeset,search_time(list,min,nodeset));	
					  Add_Path(vertexs,nodeset,vertexs.Path_HashMap.get(min));
				  }			
			}
		}
		
		//������̾���Ľڵ�
		public static int Min_PathDist(Vertex vertexs) {
			 //if (vertexs.PathDist_HashMap == null) return null;
			List<Long> c = new ArrayList<Long>();  
			for(int i=0;i<vertex_num;i++) {
				if(vertexs.Visited.get(i)==false)
					c.add(vertexs.PathDist_HashMap.get(i));
					
			}
		        Object[] obj = c.toArray();
		        Arrays.sort(obj);
		        return Find_Vertex(vertexs,Long.valueOf(obj[0].toString()));
		}
		
		
		//����distֵ�ҵ���Ӧ�Ľڵ�
		public static int Find_Vertex(Vertex vertexs,long value) {
			  int key=0;
			  for(int getKey: vertexs.PathDist_HashMap.keySet()){
				  if(vertexs.PathDist_HashMap.get(getKey).equals(value)){
					  key =getKey;
					  }
				  }
			  return key;  
			  //���key�����һ�������������key.  
			  }

		
		//��ʼ���ڵ����״̬
		public static void Initial_Visited(Vertex vertexs,int s) {
			for(int i=0;i<vertex_num;i++) {
				if(i==s) {
					vertexs.Visited.put(i,true);
				}else {
					vertexs.Visited.put(i,false);
				}
			}
		}
		
		//�ҵ����·���Ľڵ�ı������װ̬
		public static void Change_Visited(Vertex vertexs,int k) {
			vertexs.Visited.put(k,true);
		}
		
		//��ʼ���ڵ㼯��
		public static void Initial_Set(List<Integer> set,int s) {
			for(int i=0;i<vertex_num;i++) {
				if(i==s) {
					continue;
				}
				set.add(i);
			}
		}
		
		
		//�ҵ����·���Ľڵ����
		public static void Remove_NodeSet(Vertex vertexs,int d) {
			Iterator<Integer> it = vertexs.NodeSet.iterator();
			while(it.hasNext()){
			    int x = it.next();
			    if(x==d){
			        it.remove();
			    }
			}
		}
		
		//�ж����·���㷨�Ľڵ㼯���Ƿ�Ϊ�գ�Ϊ�շ���-1����Ϊ�շ���1
		public static int Judge_NodeSet(Vertex vertexs) {
			int i=0;
			if (vertexs.NodeSet.isEmpty()) {
			    i=-1;
			}else {
			    i=1;
			}
			return i;
		}
		
		//��ʼ��·��
		public static void Initial_Path(Vertex vertexs,int from) {
			for(int i=0;i<vertex_num;i++)
			vertexs.Path_HashMap.put(i,String.valueOf(from));
		}
		
		//����Path_HashMap�����ҵ������·�����
		public static void Add_Path(Vertex vertexs,int k,String path) {
			vertexs.Path_HashMap.put(k,path);
		}
		
		//���·����Ϣ
		public static void Print_Path(Vertex vertex) {
			System.out.println("Print_Path");
			Iterator it = vertex.Path_HashMap.entrySet().iterator();   
		       while(it.hasNext()) {  
		    	   Map.Entry entry = (Map.Entry) it.next();    
		    	   Object key = entry.getKey();  
		            Object value = entry.getValue();  
		            System.out.println(key + ":" + value);   
		        }
		}
		
		//����ڵ㼯����Ϣ
		public static void Print_Set(List<Integer> set) {
			Iterator it = set.iterator();   
		       while(it.hasNext()) {  
		            System.out.print(it.next());   
		        }
		}
		
		//�ж��ַ������Ƿ���������еĽڵ㣬����String��list�����-1��1
		public static int IndexOf_String(String str,List<Integer> listPath) {
			int flag = -1;
			
			for (int node : listPath) {
			 if(str.indexOf(Integer.toString(node))!=-1) {
				 flag = 1;
				 break;
			 }else {
				 flag = -1;
			 }
			 
			}
			if(listPath.size()==1) {
				 flag = -1;
			 }
			return flag;
		}
		
		//������Path_HashMap�����·����Ӧ�Ķ���
		public static List<Integer> Long_Path(HashMap<Integer,Vertex> Vertex_HashMap,List<Integer> listPath) {
			int length = 0;
			int node_num = 0;
			int key_num = 0;
			//List<Integer> tempList = new ArrayList<Integer>();
			
			for(int node : Vertex_HashMap.keySet()) {
				System.out.println(node+":node");
				//Print_Path(Vertex_HashMap.get(node));
			for (int key : Vertex_HashMap.get(node).Path_HashMap.keySet())
			{
				//System.out.println(node+","+key+":"+Vertex_HashMap.get(node).Path_HashMap.get(key).length());
				//tempList = stringToList(Vertex_HashMap.get(node).Path_HashMap.get(key));
				System.out.println(Vertex_HashMap.get(node).Path_HashMap.get(key)+" "+IndexOf_String(Vertex_HashMap.get(node).Path_HashMap.get(key),listPath));
			    if((Vertex_HashMap.get(node).Path_HashMap.get(key).length() >length) )
			    {
			    	
			    	length = Vertex_HashMap.get(node).Path_HashMap.get(key).length();
			        key_num = key;
			        node_num = node;
			        System.out.println("Ŀǰ�·��Ϊ���ڵ�"+node_num+"��"+key_num+"����Ϊ��"+length);
			        System.out.println(Vertex_HashMap.get(node).Path_HashMap.get(key));
			    }
			}
			}
			System.out.println("ѡ����·��Ϊ���ڵ�"+node_num+"��"+key_num+"����Ϊ��"+length);
			List<String> list = new ArrayList<String>();
			list = Arrays.asList(Vertex_HashMap.get(node_num).Path_HashMap.get(key_num).split(","));		
			List<Integer> intList = new ArrayList<Integer>();
			for(String str : list) {
				  int i = Integer.parseInt(str);
				  intList.add(i);
				}
			return intList;
		}
		
		public static List<Integer> stringToList(String str){
			List<Integer> tempList = new ArrayList<Integer>();
			List<String> list = Arrays.asList(str.split(","));
			for(String strs : list) {
				  int i = Integer.parseInt(strs);
				  tempList.add(i);
				}
			return tempList;
			
		}
		
		//���붥����������Ӧ��·������
		public static List<Integer> Vertex_Path(Vertex vertexs,int key) {
			List<String> list = Arrays.asList(vertexs.Path_HashMap.get(key).split(","));		
			List<Integer> intList = new ArrayList<Integer>();
			for(String str : list) {
				  int i = Integer.parseInt(str);
				  intList.add(i);
				}
			return intList;
		}
		
		//���붥���б��ڽڵ㼯����ɾ�������б���Ԫ��
		public static void Delete_vertex(Vertex vertexs,List<Integer> list) {
			vertexs.VertexSet.removeAll(list);
			
		}
		
		
		
		//�ж��鲥·���㷨�Ľڵ㼯���Ƿ�Ϊ�գ�Ϊ�շ���-1����Ϊ�շ���1
		public static int Judge_VertexSet(Vertex vertexs) {
			int i=0;
			if (vertexs.VertexSet.isEmpty()) {
			    i=-1;
			}else {
			    i=1;
			}
			return i;
		}
	
		public static Vertex temporal_dijkstra(Vertex vertex,List<HashMap<String, Object>> list,long timeCreated,int from) {
		//Initial_NodeSet(from);
		Vertex vertexs = new Vertex();
		//vertexs.PathDist_HashMap=(HashMap)vertex.PathDist_HashMap.clone();
		vertexs.VertexSet.addAll(vertex.VertexSet);
		Initial_Set(vertexs.NodeSet,from);  
		Initial_Visited(vertexs,from);
		Initial_Vertexs(vertexs,list,from);
		Initial_Path(vertexs,from);
		while(Judge_NodeSet(vertexs)!=-1) {
			
			int TempMin = Min_PathDist(vertexs);
			//System.out.println(TempMin+","+vertexs.PathDist_HashMap.get(TempMin));
			Remove_NodeSet(vertexs,TempMin);
			list=cut_list(list,vertexs.PathDist_HashMap.get(TempMin));
			list=remove_down(list,vertexs.PathDist_HashMap.get(TempMin));
			
			Add_Path(vertexs,TempMin,vertexs.Path_HashMap.get(TempMin)+","+TempMin);
			//System.out.println(vertexs.Path_HashMap.get(Min_PathDist())+","+Min_PathDist()+"1111");	
			//Print_Path();
			Update_Path(vertexs,list,from,TempMin);	
			Change_Visited(vertexs,TempMin);
			//Print_NodeSet();
			//Print_Path();
			
			
		}
		Print_Set(vertexs.VertexSet);
		//Print_Path(vertexs);
		return vertexs;
		
		}
		


	public static Vertex read_one(Vertex vertexs,long timeCreated,int from) {

		File af = new File("E:\\��ܰ��\\one\\akeranen-the-one-v1.6.0-72-g4ac896c\\akeranen-the-one-4ac896c\\data\\infocom.txt");
		  FileReader is = null;
		  BufferedReader br = null;
		  String str = "";
		  
		  //Vertex vertexs = new Vertex();
		  //CGR_Dijkstra test = new CGR_Dijkstra(Settings s); 
		  //CGR_Dijkstra testtemp = new CGR_Dijkstra(); 
			  try {
			   is = new FileReader(af);
			   br = new BufferedReader(is);
			   try {
				   List<HashMap<String, Object>> lister = new ArrayList<HashMap<String, Object>>();
				   List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				   	   
				   while ((str = br.readLine()) != null) {
				   int i=read_source(str);
				   int j=read_destination(str);
				   int state=judgment_state(str);
				   long time=read_time(str);
				   HashMap<String, Object> map = new HashMap<String, Object>();
				   map.put("source",i);    
			       map.put("destination",j);  
			       map.put("state",state);
			       map.put("time",time);
			       lister.add(map);				   
				   }
				   
				   list=lister;
				   	   
				   list=cut_list(list,timeCreated);
				   list=remove_down(list,timeCreated);
				   /*Iterator<HashMap<String, Object>> it = list.iterator();   
			       while(it.hasNext()) {  
			       HashMap<String, Object> stuMap = it.next();    
			       System.out.println(stuMap.get("source") + "    " + stuMap.get("destination") + "   " + stuMap.get("state")+"   " + stuMap.get("time"));  
			        }*/
				   
				   vertexs = temporal_dijkstra(vertexs,list,timeCreated,from);
			       
			       
				   } catch (IOException e) {
					    e.printStackTrace();
				   }
				  } catch (IOException e) {
					    e.printStackTrace();
			   }finally{
				   try {
					   if(br != null) br.close();   
				    if(is != null) is.close();
				   } catch (IOException e) {
				    e.printStackTrace();
				   }
	}
			  //System.out.println(vertexs.Path_HashMap.get(6));
			  //System.out.println(timeCreated+" "+test.paths);
			  //return vertexs.Path_HashMap;
			 return vertexs;
			  
	}
	
	//ɾ��listPath�д��ڵĽڵ���Ϣ
	public static void Remove_vertexs(Vertex vertexs,List<Integer> listPath) {
		for (int node : listPath) {
			vertexs.Path_HashMap.remove(node);
		}
	}
	
	//ɾ��list�е��ظ�Ԫ��
	public static List removeDuplicate(List list) {   
		HashSet h = new HashSet(list);   
		list.clear();   
		list.addAll(h);   
		return list;   
		}  
	
	public static Vertex clearPathHashmap(Vertex vertexs) {
		vertexs.Path_HashMap.clear();
		return vertexs;
	}
	
	public static void multicastRouting(long timeCreated,int from,int to) {
		//Initial_VertexSet();
		//��ʼʱ��
		 
		Vertex vertexs = new Vertex();
		
		List<Integer> listPath = new ArrayList<Integer>();
		HashMap<Integer,Vertex> Vertex_HashMap = new HashMap<Integer,Vertex>();
		listPath.add(from);
		vertexs.PathDist_HashMap.put(from,timeCreated);
		Initial_Set(vertexs.VertexSet,from);
		while(Judge_VertexSet(vertexs)!=-1) {
			//Print_Set(vertexs.VertexSet);
			//System.out.println("11111111");
			//Print_Set(listPath);
			//System.out.println("11111111");
			for(int node:listPath) {
				System.out.println(node+"qqqqqq");
				//clearPathHashmap(vertexs);
				vertexs = read_one(vertexs,vertexs.PathDist_HashMap.get(node),node);
				Print_Path(vertexs);
				Vertex_HashMap.put(node,vertexs);
				//Print_Path(Vertex_HashMap.get(4));
				Remove_vertexs(vertexs,listPath);
			}
			listPath.addAll(Long_Path(Vertex_HashMap,listPath));
			removeDuplicate(listPath);
			Delete_vertex(vertexs,listPath);
			
			Print_Set(vertexs.VertexSet);
			System.out.println("δ�����Ľڵ�");
			Print_Set(listPath);
			System.out.println("�������Ľڵ�");
		}
		Print_Path(Vertex_HashMap.get(0));
		Print_Path(Vertex_HashMap.get(1));
		Print_Path(Vertex_HashMap.get(2));
		Print_Path(Vertex_HashMap.get(3));
	}
	
	
	public static int getNextHop(String paths,int node) {	
		int nextHop=0;
		if(paths=="") {
			nextHop=Integer.MAX_VALUE;
			return nextHop;
		}
		List<String> list = Arrays.asList(paths.split(","));
		String Node = node + "";
		int i=0;
		Iterator it = list.iterator();   
	       while(it.hasNext()) {
	    	   i++;
	    	   if(it.next().equals(Node))
	            break;    
	        }
	       if(i==list.size()) {
	    	   i=i-1;
	       }
	       nextHop = Integer.parseInt(list.get(i));
	       return nextHop;	
	}
	
	public static List<Integer> test(int i){
		List<Integer> list = new ArrayList<Integer>();
		list.add(i);
		list.add(i+1);
		list.add(i+2);
		return list;
	}
	
	public static void main(String[] args) {
		//��ʼʱ��
		long timeCreated=17552;
		//��ʼ�ڵ�
		int from=4;
		
		int to=6;
		
		//String paths = read_one(timeCreated,from,to);
		//System.out.println(getNextHop(paths,6));
		multicastRouting(timeCreated,from,to);
		/*Vertex vertexs = new Vertex();
		HashMap<Integer,Vertex> hash = new HashMap();
		List<Integer> listPath = new ArrayList<Integer>();
		listPath.add(0);
		listPath.add(1);
		listPath.add(2);
		for(int i:listPath) {
			System.out.println(i+"qqqqqq");
			vertexs.Path_HashMap.clear();
			Print_Path(vertexs);
			Vertex vertex = read_one(vertexs,0,i);
			
			List list = test(i);
			//Print_Path(vertex);
			hash.put(i,vertex);
			
		}
		for(int key:listPath) {
		       Print_Path(hash.get(key));
		       }
		Iterator it = hash.entrySet().iterator();   
	       while(it.hasNext()) {  
	    	   Map.Entry entry = (Map.Entry) it.next();    
	    	   Object key = entry.getKey();  
	            Object value = entry.getValue();  
	            System.out.println(key + ":" + value);
	            
	        }*/
	       
	
	}
}
