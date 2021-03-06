package OD;
import java.util.ArrayList;

import BplusTree.InstanceKey;
import Data.Cmp;

public class OrderDependency {
	private ArrayList<String> LHS=new ArrayList<String>(),RHS=new ArrayList<String>();
	static final String lr_separator="->";
	static final String attr_separator=","; 
	
	public OrderDependency() {
		
	}
	
	public OrderDependency(OrderDependency cp) {
		
		for(String lhs:cp.LHS) {
			LHS.add(lhs);
		}
		for(String rhs:cp.RHS) {
			RHS.add(rhs);
		}
	}
	public void copy(OrderDependency d) {
		this.LHS.clear();
		this.RHS.clear();
		for(String it:d.LHS) LHS.add(it);
		for(String it:d.RHS) RHS.add(it);
	}
	public void addLHS(String s) {
		LHS.add(s);
	}
	public void addArray2LHS(String[] as) {
		for(int i=0;i<as.length;i++) {
			LHS.add(as[i]);
		}
	}
	public void addRHS(String s) {
		RHS.add(s);
	}
	public void addArray2RHS(String[] as) {
		for(int i=0;i<as.length;i++) {
			this.RHS.add(as[i]);
		}
	}
	
	public void addLHS(int it,String s) {
		LHS.add(it,s);
	}
	
	
	
	public void refreshRHS(ArrayList<String> rhs) {
		this.RHS.clear();
		if(rhs!=null&&rhs.size()!=0) {
			for(String s:rhs) {
				this.RHS.add(s);
			}
		}
		
	}
	
	public void deleteRHSTail() {
		RHS.remove(RHS.size()-1);
	}
	
	public ArrayList<String> getLHS(){
		return this.LHS;
	}
	
	public ArrayList<String> getRHS(){
		return this.RHS;
	}
	//检查当前od是否包含cod
	public int isContain(OrderDependency cod) {
		int cit=0,it=0;

		//找到this中与cod第一个相同的属性
		while(it<this.LHS.size()&&this.LHS.get(it).equals(cod.getLHS().get(0))==false) {
			it++;
		}
		
		while(it<this.LHS.size()&&cit<cod.getLHS().size()&&this.LHS.get(it).equals(cod.getLHS().get(cit))) {
			it++;
			cit++;
		}
		
		if(cit==cod.getLHS().size()) return it;
		return -1;
	}
	
	//判断两个OD，this的右边是否有cod中的一部分
	public boolean rightContain(OrderDependency cod) {
		for(String s:cod.getRHS()) {
			if(listContain(this.getRHS(),s)) return true;
		}
		return false;
	}
	
	
	//判断两个OD是否相等
	public boolean isEqual(OrderDependency cod) {
		if(this.LHS.size()!=cod.getLHS().size()) return false;
		for(int i=0;i<this.LHS.size();i++) {
			if(this.LHS.get(i).compareTo(cod.getLHS().get(i))!=0) return false;
		}
		return true;
	}
	
	//判断l1中是否包含s
	private boolean listContain(ArrayList<String> l1,String s) {
		for(String sl:l1) {
			if(sl.equals(s)) return true;
		}
		return false;
	}
	
	
	 @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof InstanceKey))
        {
            return false;
        }
        OrderDependency pn = (OrderDependency)o;
        
		return this.isEqual(pn);
    }

	    @Override
	    public int hashCode()
	    {
	       int result=0;
	       for(int i=0;i<LHS.size();i++) {
	    	   result+=LHS.get(i).hashCode();
	       }
	       
	       for(int i=0;i<RHS.size();i++) {
	    	   result+=RHS.get(i).hashCode();
	       }
	       return result;
	    }
	
	
	public void printOD() {
		System.out.print(LHS.get(0));
		for(int i=1;i<LHS.size();i++) {
			System.out.print(attr_separator+LHS.get(i));
		}
		
		System.out.print(lr_separator+RHS.get(0));
		for(int i=1;i<RHS.size();i++) {
			System.out.print(attr_separator+RHS.get(i));
		}
		System.out.print("\n");
	}
	
}
