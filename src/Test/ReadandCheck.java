package Test;

import Data.*;
import EquivalenceClass.*;
import OD.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import BplusTree.InstanceKey;


public class ReadandCheck {
	public static boolean debug;
	public final static int order = 5;
	public static Index indexes=new Index();
	private static ArrayList<OrderDependency> odList=new ArrayList<>();
	private static ArrayList<DataStruct> objectList,iObjectList;
	private static ArrayList<OrderDependency> originalODList=new ArrayList<OrderDependency>(),
			incorrectODList=new ArrayList<OrderDependency>(),
			enrichODList=new ArrayList<OrderDependency>();
	public static long dtime=0;
	public static long etime=0;
	private static int incorrect_od_num=0;
	public static int split_od_num=0;
	
public static void main(String[] args) {
		
		initial();
		
		listClear();
		
		if(Debug.debug) indexes.printIndex();
		/*将读入的od输出*/
		System.out.println("==================\nThe original od is:(共 "+originalODList.size()+" 条)");
		for(OrderDependency od:odList) od.printOD();
		System.out.println("共有"+objectList.size()+"条数据\n共有"+iObjectList.size()+"条增量数据");

//		indexes.print();
//		indexes.printECChanged();
		long start = System.currentTimeMillis();
		checkandRepair(originalODList);
		
		
		System.out.println("==================\n对等索引");
		System.out.println("原OD失效 "+incorrect_od_num+" 条");
		System.out.println("做了Expand的OD数目 "+split_od_num+" 条");
		incorrect_od_num=0;
		split_od_num=0;
		
		enrichment();
		
		long t = System.currentTimeMillis( );
		long diff = t - start;
		//在enrich之前需要更新索引结构
		indexes.buildIndexes(enrichODList);
		indexes.updateIndexes(enrichODList);
		
		if(!enrichODList.isEmpty()) odList.addAll(enrichODList);
		t = System.currentTimeMillis( );
		checkandRepair(enrichODList);
		
		
		long end = System.currentTimeMillis( );
        diff+=(end-t);
        System.out.println(objectList.size()+"条数据"+iObjectList.size()+"条增量数据 共耗时"+diff+"毫秒");
        System.out.println("其中Detect耗时 "+ dtime+"ms, Expand 耗时"+etime+"ms\n");
		System.out.println("The last od is:(共 "+odList.size()+" 条)");
		if(!odList.isEmpty()) 
			for(OrderDependency od:odList) od.printOD();
		
	}
	
	public static void checkandRepair(ArrayList<OrderDependency> ODLi) {
		
		//对于每个等价类
		for(OrderDependency od:ODLi) {
			int ecid=getECId(od);
			Handle handle=new Handle();
			
			long t1 = System.currentTimeMillis( );
			
			String violation_type=handle.detectOD(ecid);
			
			
			long t2 = System.currentTimeMillis( );
			
			dtime+=(t2-t1);
			
			
			if(Debug.time_test2) {
				System.out.println("detect");
				od.printOD();
				System.out.println("花费"+(t2-t1)+"ms\n");
				System.out.println("检测结果： "+violation_type+"\n\n------------------");
			}
			
			//boolean not_valid=false;
			
			//ArrayList<String> new_rhs=indexes.ECIndexList.get(ecid).getRHSName();
//			if(new_rhs.size()!=od.getRHS().size()&&!new_rhs.isEmpty()) {
//				//incorrectODList.add(new OrderDependency(od));
//				//not_valid=true; 
//				od.refreshRHS(new_rhs);
//			}
			
			if(violation_type.equals("swap")) {
				incorrect_od_num++;
				odList.remove(od);
			}else if(violation_type.equals("split")) {
				incorrect_od_num++;
				split_od_num++;
				long t3 = System.currentTimeMillis( );
				
				ArrayList<OrderDependency> res=handle.repairSplit(indexes.ECIndexList.get(ecid).splitECBlock,od);
				
				long t4 = System.currentTimeMillis();
				etime+=(t4-t3);
				if(Debug.time_test2) {
					System.out.println("Expand(split)");
					od.printOD();
					System.out.println("花费"+(t4-t3)+"ms\n====================\n");
				}
				
			
				incorrectODList.add(new OrderDependency(od));
				odList.remove(od);
				if(res!=null) {
					odList.addAll(res);
				}
					
			}
		}
		
	}
	
	
	public static void enrichment() {
		if(incorrectODList.isEmpty()) return;
		for(OrderDependency iod:incorrectODList) {
			for(OrderDependency ood:originalODList) {
				if(iod.getLHS().size()<ood.getLHS().size()&&ood.isEqual(iod)==false&&ood.isContain(iod)!=-1&&!ood.rightContain(iod)) {
					enrichSingleOD(ood,iod,ood.isContain(iod));
				}
			}
		}
	}
	
	//od:需要被扩展的od，iod：错误的od，it：需要插入iod右边的起始index.最后都放到enrichODList中
	public static void enrichSingleOD(OrderDependency od,OrderDependency iod,int it) {
		OrderDependency tmp;
		if(it==od.getLHS().size()) return;//如果iod正好在od的尾巴上，没必要扩展
		while(it<od.getLHS().size()) {
			tmp=new OrderDependency(od);
			int iter=it;
			for(String r:iod.getRHS()) {
				tmp.addLHS(iter++,r);
			}
			enrichODList.add(tmp);
			
			it++;
		}
		
	}

	
	
	public static void initial() {
		debug=Debug.debug;
		DataInitial.readData();
		objectList=DataInitial.objectList;
		iObjectList=DataInitial.iObjectList;
		odList=DataInitial.odList;
		indexes.buildIndexes(odList);
		indexes.updateIndexes(odList);
	}
	
	private static void listClear() {
		incorrectODList.clear();
		enrichODList.clear();
		originalODList.clear();
		//存储所有原有的od
		
		if(odList!=null) originalODList.addAll(odList);
		
	}
	public static int getECId(OrderDependency todo) {
		
		return indexes.indexMap.getOrDefault(todo,-1);
		
	}
	public static void printList(ArrayList<OrderDependency> list,String sentence) {
		if(list.isEmpty()==false) System.out.println(sentence);
		for(OrderDependency od:list) {
			od.printOD();
		}
	}
	
}




