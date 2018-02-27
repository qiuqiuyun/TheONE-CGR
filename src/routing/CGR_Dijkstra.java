package routing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 


public class CGR_Dijkstra {

	//private Vertex vertexs;
	static Vertex vertexs = new Vertex();
	private static final int vertex_num = 9;


		
	public static int read_source(String str) {
		  String sourcenode = "(\\s+\\d{1,2})";
		  Pattern pattern_sourcenode = Pattern.compile(sourcenode);
		  Matcher matcher_sourcenode = pattern_sourcenode.matcher(str);
		  int source=1;
		  if(matcher_sourcenode.find())
      {
			  String str_source=matcher_sourcenode.group();
			//去除字符串开头和结尾的空格
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
	
	//判断节点是建立连接还是断开连接
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
	
	//读取时间
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
	
	
	//删除list中时间点time之前的数据
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
		
		//移除状态是down的数据
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
		
		//输入两个节点，返回两个节点最小的链接开始时间
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
		
		
		//初始化节点信息
		public static void Initial_Vertexs(List<HashMap<String, Object>> list,int from) {
			vertexs.Initial_Node(from);
			for(int i=0;i<vertex_num;i++) {
				vertexs.PathDist_HashMap.put(i,search_time(list,from,i));
			}
		}
		
		//更新节点信息
		public static void Update_Path(List<HashMap<String, Object>> list,int from,int min) {
			for(int nodeset : vertexs.NodeSet) {
				  if(search_time(list,min,nodeset)<vertexs.PathDist_HashMap.get(nodeset)) {
					  //System.out.println(min+","+nodeset+":"+search_time(list,min,nodeset)+"<"+vertexs.PathDist_HashMap.get(nodeset));
					  vertexs.PathDist_HashMap.put(nodeset,search_time(list,min,nodeset));	
					  Add_Path(nodeset,vertexs.Path_HashMap.get(min));
				  }			
			}
		}
		
		//返回最短距离的节点
		public static int Min_PathDist() {
			 //if (vertexs.PathDist_HashMap == null) return null;
			List<Long> c = new ArrayList<Long>();  
			for(int i=0;i<vertex_num;i++) {
				if(vertexs.Visited.get(i)==false)
					c.add(vertexs.PathDist_HashMap.get(i));
					
			}
		        Object[] obj = c.toArray();
		        Arrays.sort(obj);
		        return Find_Vertex(Long.valueOf(obj[0].toString()));
		}
		
		
		//根据dist值找到对应的节点
		public static int Find_Vertex(long value) {
			  int key=0;
			  for(int getKey: vertexs.PathDist_HashMap.keySet()){
				  if(vertexs.PathDist_HashMap.get(getKey).equals(value)){
					  key =getKey;
					  }
				  }
			  return key;  
			  //这个key是最后一个满足该条件的key.  
			  }

		
		//初始化节点访问状态
		public static void Initial_Visited(int s) {
			for(int i=0;i<vertex_num;i++) {
				if(i==s) {
					vertexs.Visited.put(i,true);
				}else {
					vertexs.Visited.put(i,false);
				}
			}
		}
		
		//找到最短路径的节点改变其访问装态
		public static void Change_Visited(int k) {
			vertexs.Visited.put(k,true);
		}
		
		//初始化节点集合
		public static void Initial_NodeSet(int s) {
			for(int i=0;i<vertex_num;i++) {
				if(i==s) {
					continue;
				}
				vertexs.NodeSet.add(i);
			}
		}
		
		//找到最短路径的节点出列
		public static void Remove_NodeSet(int d) {
			Iterator<Integer> it = vertexs.NodeSet.iterator();
			while(it.hasNext()){
			    int x = it.next();
			    if(x==d){
			        it.remove();
			    }
			}
		}
		
		//判断节点集合是否为空，为空返回-1，不为空返回1
		public static int Judge_NodeSet() {
			int i=0;
			if (vertexs.NodeSet.isEmpty()) {
			    i=-1;
			}else {
			    i=1;
			}
			return i;
		}
		
		//初始化路径
		public static void Initial_Path(int from) {
			for(int i=0;i<vertex_num;i++)
			vertexs.Path_HashMap.put(i,String.valueOf(from));
		}
		
		//更新Path_HashMap，将找到的最短路径添加
		public static void Add_Path(int k,String path) {
			vertexs.Path_HashMap.put(k,path);
		}
		
		//输出路径信息
		public static void Print_Path() {
			Iterator it = vertexs.Path_HashMap.entrySet().iterator();   
		       while(it.hasNext()) {  
		    	   Map.Entry entry = (Map.Entry) it.next();    
		    	   Object key = entry.getKey();  
		            Object value = entry.getValue();  
		            System.out.println(key + ":" + value);   
		        }
		}
		
		//输出节点集合信息
		public static void Print_NodeSet() {
			Iterator it = vertexs.NodeSet.iterator();   
		       while(it.hasNext()) {  
		            System.out.print(it.next());   
		        }
		}
		
		public static void temporal_dijkstra(List<HashMap<String, Object>> list,long timeCreated,int from) {
		Initial_NodeSet(from);
		Initial_Visited(from);
		Initial_Vertexs(list,from);
		Initial_Path(from);
		while(Judge_NodeSet()!=-1) {
			
			int TempMin = Min_PathDist();
			//System.out.println(TempMin+","+vertexs.PathDist_HashMap.get(TempMin));
			Remove_NodeSet(TempMin);
			list=cut_list(list,vertexs.PathDist_HashMap.get(TempMin));
			list=remove_down(list,vertexs.PathDist_HashMap.get(TempMin));
			
			Add_Path(TempMin,vertexs.Path_HashMap.get(TempMin)+","+TempMin);
			//System.out.println(vertexs.Path_HashMap.get(Min_PathDist())+","+Min_PathDist()+"1111");	
			//Print_Path();
			Update_Path(list,from,TempMin);	
			Change_Visited(TempMin);
			//Print_NodeSet();
			//Print_Path();
			
			
		}
		//return vertexs;
		
		}
		


	public static String read_one(long timeCreated,int from,int to) {
       	File af = new File("E:\\张馨云\\one\\akeranen-the-one-v1.6.0-72-g4ac896c\\akeranen-the-one-4ac896c\\data\\infocom.txt");
		  FileReader is = null;
		  BufferedReader br = null;
		  String str = "";
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
				   
					   temporal_dijkstra(list,timeCreated,from);
			       
			       
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
			  return vertexs.Path_HashMap.get(to);
			 
			  
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
	
	public static void main(String[] args) {
		//起始时间
		long timeCreated=17552;
		//起始节点
		int from=4;
		
		int to=6;
		Vertex vertexs = new Vertex(from);
		String paths = read_one(timeCreated,from,to);
		//System.out.println(w);
		//System.out.println(getNextHop(w,2));
		System.out.println(getNextHop(paths,6));
		
		
		
	
	}
}
