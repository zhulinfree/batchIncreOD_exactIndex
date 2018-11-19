package Data;

import java.util.ArrayList;
import java.util.Scanner;

import OD.*;

public class DataInitial {
	
	private static String dataFileName=new String("flight_9/flights_9_30w_cleaned_int.csv");
	private static String increFileName=new String("flight_9/flights_9_9w_incre_noiseF.csv");
	private static String odFileName=new String("flight_9/flight_9_20w_od.txt");
//
//	private static String dataFileName=new String("flight_8/flights_8_40w_concise.csv");
//	private static String increFileName=new String("flight_8/flights_8_10w_incre_noiseF2E2.csv");
//	private static String odFileName=new String("flight_8/FLIGHT_8_40W_OD.txt");

//	private static String dataFileName=new String("flight_7/flights_7_40w_concise.csv");
//	private static String increFileName=new String("flight_7/flights_7_10w_noise_int_moreEC_v3.csv");
//	private static String odFileName=new String("flight_7/FLIGHT_7_40W_OD.txt");	
//	
	
//	private static String dataFileName=new String("flight_6/flights_6_30w_cleaned_int.csv");
//	private static String increFileName=new String("flight_6/flights_6_10w_noise_int.csv");
//	private static String odFileName=new String("flight_6/flight_6_20w_od.txt");	
	
//	private static String dataFileName=new String("f_6/flights_6_20w_concise.csv");
//	private static String increFileName=new String("f_6/flights_6_2w_noise_int.csv");
//	private static String odFileName=new String("f_6/flight_6_od.txt");	

	
//	private static String dataFileName=new String("ncvoter_7/ncvoter_7_30w_F2H2.csv");
//	private static String increFileName=new String("ncvoter_7/ncvoter_7_incre_noiseF2H2.csv");
//	private static String odFileName=new String("ncvoter_7/ncvoterOD.txt");	
//	
	
	
	
	public static CSVtoDataObject cdo = new CSVtoDataObject();
	private static CSVtoDataObject ind=new CSVtoDataObject();
	private static TXTtoOD ods=new TXTtoOD();
	public static ArrayList<OrderDependency> odList=new ArrayList<>();
	public static ArrayList<DataStruct> objectList=new ArrayList<DataStruct>(),
			iObjectList=new ArrayList<DataStruct>();
	
	public static void readData() {
		
//		Scanner sc = new Scanner(System.in); 
//        System.out.println("请输入原始数据集文件名称："); 
//        dataFileName = sc.nextLine(); 
//        System.out.println("请输入增量数据集文件名称："); 
//        increFileName = sc.nextLine(); 
//        System.out.println("请输入od文件名称："); 
//        String odFileName = sc.nextLine();
//        
		try{
			odList=ods.storeOD(odFileName);
			cdo.readCSVData(dataFileName);
			ind.readCSVData(increFileName);
		}catch(Exception e) {
			System.out.println("read fail!");
		}
		objectList = cdo.datatoObject();
		iObjectList=ind.datatoObject();
	}
	
}
