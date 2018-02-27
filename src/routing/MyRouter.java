package routing;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import core.*;

import static core.Constants.DEBUG;

import util.Tuple;


public class MyRouter extends ActiveRouter{
	public static final String MYROUTER_NS = "MyRouter";
	public static final String MSG_PATH = MYROUTER_NS + "." + "path";
	public static final String MSG_NEXTHOP = MYROUTER_NS + "." + "nexthop";
	private static List<MyRouter> allRouters;

	static {
		DTNSim.registerForReset(MyRouter.class.getCanonicalName());
		reset();
	}
	//protected string initialPaths;
	public MyRouter(Settings s) {
		super(s);
		//Settings mySettings = new Settings(MYROUTER_NS);
		//initialPaths = mySettings.getString(NROF_COPIES);
	}

	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected MyRouter(MyRouter r) {
		super(r);
		allRouters.add(this);
		
		
	}
	
	public boolean createNewMessage(Message msg) {
		boolean ok = super.createNewMessage(msg);

		if (!ok) {
			throw new SimError("Can't create message " + msg);
		}
		//String paths= T_Dijkstra.read_one((long)msg.getCreationTime(),msg.getFrom().getAddress(),msg.getTo().getAddress());
		String paths= CGR_Dijkstra.read_one((long)msg.getCreationTime(),msg.getFrom().getAddress(),msg.getTo().getAddress());
		if(msg.getId().equals("M240")){
			System.out.println(paths);
			System.out.println(msg.getCreationTime()+" "+msg.getId()+paths+"r");}
		Integer nexthop=getNextHop(paths,getHost().getAddress());
		msg.addProperty(MSG_PATH,new String(paths)); //添加字段
	    sendMessageToConnected(msg);
	    return true;
	}
	
	public void update() {
		super.update();
		
		if (isTransferring() || !canStartTransfer()) {
			return; // can't start a new transfer
		}

		// Try only the messages that can be delivered to final recipient
		
		
		/*if (exchangeDeliverableMessages() != null) {
			return; // started a transfer
		}*/
		tryOtherMessages();
	}
	
	private Tuple<Message, Connection> tryOtherMessages() {
		List<Tuple<Message, Connection>> messages =
			new ArrayList<Tuple<Message, Connection>>();

		Collection<Message> msgCollection = getMessageCollection();

		
		for (Connection con : getConnections()) {
			DTNHost other = con.getOtherNode(getHost());
			MyRouter othRouter = (MyRouter)other.getRouter();

			if (othRouter.isTransferring()) {
				continue; // skip hosts that are transferring
			}
			
			for (Message m : msgCollection) {
				if (othRouter.hasMessage(m.getId())) {
					continue; // skip messages that the other one has
				}
				
				String Paths = (String)m.getProperty(MSG_PATH);
				int getto=getNextHop(Paths,getHost().getAddress());//获取下一跳节点信息
				
				if (getto!=Integer.MAX_VALUE && getto == other.getAddress()) { //消息的下一跳节点是邻居节点
					
	                messages.add(new Tuple<Message, Connection>(m,con));
	            }
			}
		}

		if (messages.size() == 0) {
			return null;
		}
		this.sortByQueueMode(messages);
		return tryMessagesForConnected(messages);	// try to send messages
	}
	
	
	private void sendMessageToConnected(Message m) {
		String Paths = (String)m.getProperty(MSG_PATH);
		int getto=getNextHop(Paths,getHost().getAddress());//获取下一跳节点信息
		if(m.getId().equals("M240")){
			System.out.println(m.getCreationTime()+" "+m.getId()+" "+this.getHost().getAddress()+" "+getto+" "+Paths+"a");}
		for (Connection c : getConnections()) {
			DTNHost other = c.getOtherNode(getHost());
			
			if (c.isUp() && getto!=Integer.MAX_VALUE && getto == other.getAddress()) {		
				//messages.add(new Tuple<Message, Connection>(m,c)); //and finalize it right away 
				if(m.getId().equals("M240")) {
				System.out.println(m.getId()+" "+this.getHost().getAddress()+" "+getto+" "+other.getAddress()+"b");}
				startTransfer(m,c);
			}
		}
	}
	
	//更新下一跳节点信息
	private void updateNextHop(Message msg,DTNHost host) {
		//步骤1：取得消息的paths
	    String Paths = (String)msg.getProperty(MSG_PATH);
		int getto=getNextHop(Paths,host.getAddress());//获取下一跳节点信息
	    //步骤2：更新消息的nexthop 
	    msg.updateProperty(MSG_NEXTHOP, getto);
	}
	
	
	@Override
	public Message messageTransferred(String id, DTNHost from) {
	    Message msg = super.messageTransferred(id, from);
	    //updateNextHop(msg,this.getHost());
	    if(msg.getId().equals("M240")){
			System.out.println(msg.getId()+" "+this.getHost()+"d");}
	    sendMessageToConnected(msg);
	    if (msg.getTo() == this.getHost()) {
			for (MyRouter r : allRouters) {
				if (r != this && r != from.getRouter()) {
					r.removeDeliveredMessage(id);
				}
			}
		}
	    return msg;
	}
	
	public void removeDeliveredMessage(String id) {
		if (this.hasMessage(id)) {
			/*for (Connection c : this.sendingConnections) {
				//if sending the message-to-be-removed, cancel transfer 
				if (c.getMessage().getId().equals(id)) {
					c.abortTransfer();
				}
			}*/
			this.deleteMessage(id, false);
		}
	}

	
	
	
	protected void transferDone(Connection con) {
		Message m = con.getMessage();
		if (m == null) {
			if (DEBUG) core.Debug.p("Null message for con " + con);
			return;
		}
		/* was the message delivered to the final recipient? */
		if (m.getTo() == con.getOtherNode(getHost())) {
			this.deleteMessage(m.getId(), false);
		}
		
	}
	
	
	/*public static int getNextHop(String paths,int node) {
		int nextHop=0;
		if(paths=="") {
			nextHop=Integer.MAX_VALUE;
			return nextHop;
		}else {
		for (int x = 0; x < paths.length()-1; x++) {
			
			if((paths.charAt(x)-'0')==node ){
				nextHop=(int)(paths.charAt(x+1)-'0');
			}else{
				continue;
			}
		}
		return nextHop;	
		}
			
	}*/
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
	
	public static void reset() {
		allRouters = new ArrayList<MyRouter>();
	}
	@Override
	public MyRouter replicate() {
		return new MyRouter(this);
	}
	
}
