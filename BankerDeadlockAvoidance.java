//Banker's Deadlock Avoidance Algorithm Project

import java.util.Scanner;

public class BankerDeadlockAvoidance {
	  static int nop=5;      //number of processes
	  static int nor=3;      //number of resources
	  static int resources[][];          //resource instances matrix
	  static String strresources[][];
	  static int allocation[][];         //Allocation matrix
	  static String strallocation[][];
	  static int maximum[][];            //maximum matrix
	  static String strmaximum[][];
	  static int sumallocation;
	  
	  static int available[][];          //available matrix
	  static int need[][];               //need matrix
	  
	  static int seq[];                 //process sequence array
	  static String strseq[];
	  static int work[];                //work array, initialized as work[i] = avail[i];
	  static Scanner scan;
	  static boolean isSafe=false;
	  
	  
	  public static void main(String[] args) throws Exception{
	    scan = new Scanner(System.in);
	    
	    BankerDeadlockAvoidance.heading();
	    BankerDeadlockAvoidance.askUserInputresource();
	    BankerDeadlockAvoidance.askUserAllocation();
	    BankerDeadlockAvoidance.askUserMaximum();
	    available = Available(resources, allocation);
	    // Display Snapshot at time T0
	    DisplayStateInfo(allocation,maximum,available);
	    // Need array
	    need = NeedFunction(maximum,allocation);
	    BankerDeadlockAvoidance.askUserSequence();
	    BankerDeadlockAvoidance.isSafe();
	    if(isSafe)askAdditionalResource();
	    scan.close();
	  }
	  // This method is to ask for allocation of additional resources and let user know if there is any additional resource available
	  // which will still keep the system by checking the bankers algorithm.
	  private static void askAdditionalResource() throws Exception{
	    	  System.out.println("This Part describes the Resource-Request Algorithm.");
		  System.out.println("It determines whether request can be safely granted");
		  System.out.print("Enter the ID (");
		  for(int i=0;i<nop;i++)
		  System.out.print(i+" ");
		  System.out.print(" )of the process requesting additional resources: ");
		  System.out.println();
		  int id;
		  while(true){
			  try{
				  id = Integer.parseInt(scan.next());
				  break;
			  }catch(Exception e){
				  System.out.println("Enter Valid Input : ");
				  continue;
			  }
		  }
		  System.out.println("Resource-Request for the process P"+id);
		  int tempNeed[] = new int[nor];
		  for (int i = 0; i < nor; i++) {
			  System.out.print("    Enter the request for resource type R"+i+" ");
			  try{
				  tempNeed[i]=Integer.parseInt(scan.next());
			  }catch(Exception e){
				  i=i-1;
				  continue;
			  }
		  }
		  System.out.println();
		  StringBuffer requestP = new StringBuffer();
		  StringBuffer needR = new StringBuffer();
		  StringBuffer availableR = new StringBuffer();
		  requestP.append("(");
		  needR.append("(");
		  availableR.append("(");
		  for (int i = 0; i < tempNeed.length; i++) {
			  requestP.append(tempNeed[i]);
			  needR.append(need[id][i]);
			  availableR.append(available[0][i]);
			  if(i!=tempNeed.length-1) {
				  requestP.append(",");
				  needR.append(",");
				  availableR.append(",");
			  }
		  }
		  requestP.append(")");
		  needR.append(")");
		  availableR.append(")");
		  System.out.println("Process P"+id+" request vector is RequestP"+id+requestP.toString());
		  System.out.println();
		  System.out.println("The Resource-Request Algorithm is being executed...");
		  System.out.println("First test if RequestP"+id+" <= NeedP"+id);
		  for(int i=0;i<nor;i++){
			  if(tempNeed[i]>need[id][i]){
				  System.out.println("RequestP"+id+requestP+" > NeedP"+id+needR);
				  System.out.println();
				  System.out.println("Sorry but process P"+id+" has exceeded its maximum claim.");
				  System.out.println();
				  System.out.println("Run the program again.");
				  return;
			  }
		  }
		  
		  System.out.println("Second test if RequestP"+id+" <= Available");
		  for(int i=0;i<nor;i++){
			  if(tempNeed[i]>available[0][i]){
				  System.out.println("RequestP"+id+requestP+" > Available"+availableR);
				  System.out.println();
				  System.out.println("Sorry but process P"+id+" has exceeded its maximum claim.");
				  System.out.println();
				  System.out.println("Run the program again.");
				  return;
			  }
		  }
		  System.out.println("RequestP"+id+requestP+" <= NeedP"+id+needR);
		  System.out.println("RequestP"+id+requestP+" <= Available"+availableR);
		  System.out.println("System pretend to have allocated the requested resources to process P"+id+" by modifying the state.");
		  System.out.println("System state has been modified by applying step 3 of the Resource-Request Algorithm, which update the contents of the vectors:Available, Allocation, and Need.");
		  
		  for(int i=0;i<nor;i++){
			  available[0][i]=available[0][i]-tempNeed[i];
			  allocation[id][i]=allocation[id][i]+tempNeed[i];
			  need[id][i]=need[id][i]-tempNeed[i];
		  }
		  
		  BankerDeadlockAvoidance.askUserSequence();
		  BankerDeadlockAvoidance.isSafe();
	          System.in.read();
	          
	  }
	  
	  //This method will be called by askUserAllocation() method to check and warn the user
	  //if individual  values entered are more than resource instances.
	  public static int allocationInstanceCheck(int d , int e){
	    if (allocation[d][e] > resources[0][e]){
	      System.out.println("\n");
	      System.out.print("The Allocation for process P"+ d + " of resource type R" + e+ "\n");
	      System.out.print("cannot be greater than the total instances " + resources[0][e]+ "\n");
	      System.out.print("of this resource type.");
	      System.out.println("\n");
	      return 1;
	    }
	    return 0;	
	  }
	  
	  //This method will be called by askUserMaximum() method to check and warn the user
	  //if individual values entered are more than resource instances.
	  public static int allocationMaximumCheck(int d , int e){
	    if (maximum[d][e] > resources[0][e]){
	      // System.out.println("\n");
	      System.out.print("There are "+ resources[0][e]+ " Instances of this resources type."+ "\n");
	      System.out.print("Therefore , your need is more than" + "\n");
	      System.out.print("the system has.");
	      System.out.println("\n");
	      return 1;
	    }
	    return 0; 
	  }

	  //This method will be called by askUserMaximum() method to check and warn the user
	  //if individual values entered for maximum need is less than the Allocation. 
	  public static int maximumNeedAllocationCheck(int d , int e){
	    if (maximum[d][e] < allocation[d][e]){
	      System.out.println("\n");
	      System.out.print("The maximum need "+ maximum[d][e]+" for process P"+ d +" cannot be less than " + "\n");
	      System.out.print("The process' allocation " +allocation[d][e]+ " for the resource type R"+e+"."+"\n");
	      System.out.print("the system has.");
	      System.out.println("\n");
	      return 1;
	    }
	    return 0;
	  }    	
	  
	  //This methods asks the user to enter the instances for each resource
	  //all input validations will be done and finally prints valid input resource matrix.
	  public static void askUserInputresource() throws Exception{
	    resources=new int[1][nor]; 
	    strresources=new String [1][nor];
	    //setting instances for all resources from user input
	    for(int j=0;j<nor;j++){
	      System.out.print("Enter the number of instances for resource R" + j + ":");
	      strresources[0][j]=scan.next();  
	      // check for forcing the user not to give non- integer values.
	      try {
	        resources[0][j]=Integer.parseInt(strresources[0][j]);
	      } 
	      catch (Exception e){ 
	        System.out.println("\n");
	        System.out.println("The number of instances of a resource type you have"+"\n"+
	        "entered is not a valid number. Only numbers greater than zero"+ "\n"+
	        "are allowed.");
	        System.out.println("\n");
	        j=j-1;
	        continue;	    	   
	      }
	      
	      // check for forcing the user to give positive numbers
	      if(resources[0][j]<=0){
	        System.out.println("\n");
	        System.out.println("The number of instances of a resource type you have"+"\n"+
	        "entered is not allowed. Only numbers greater than zero"+ "\n"+
	        "are allowed.");
	        System.out.println("\n");
	        j=j-1;
	        continue;
	      }
	    }
	    
	    //printing resources for each resource type
	    System.out.println("\n");
	    System.out.println("The number of resources for each resource type");
	    System.out.println("in the system is : ");
	    System.out.println("\n");

	    for(int j1=0;j1<nor;j1++){
	      System.out.print("\t");
	      System.out.print("R"+j1);
	      System.out.print("  ");
	    }
	    System.out.println("\n");
	    
	    for(int j2=0;j2<nor;j2++){
	      System.out.print("\t");
	      System.out.print(resources[0][j2]);
	      System.out.print("  ");
	    } 	
	    
	  }
	  
	  
	  //This methods asks the user to enter the values for matrix allocation
	  //all input validations will be done and finally prints Allocation matrix.
	  public static void askUserAllocation() throws Exception{
	    //setting Allocation from user input
	    System.out.println("\n");
	    allocation=new int[nop][nor];
	    strallocation=new String [nop][nor];
	    System.out.println("Initializing the contents of matrix Allocation");
	    System.out.println("\n");
	    for(int i=0;i<nop;i++){
	      System.out.println("For Process P"+i+":");
	      for(int j=0;j<nor;j++){
	        System.out.print("\t");
	        System.out.print("Enter the allocation for resources type R"+j+":");
	        //forcing the user not to enter negative or non integer values for allocation.
	        strallocation[i][j]=scan.next();   	  
	        try{	  
	          allocation[i][j]=Integer.parseInt(strallocation[i][j]);   
	        }
	        
	        catch (Exception e ){	
	          System.out.println("\n");
	          System.out.println("The Allocation for this resource type is not a number.");
	          System.out.println("\n");
	          j=j-1;
	          continue;
	        } 
	        int g=BankerDeadlockAvoidance.allocationInstanceCheck(i, j);
	        
	        if (g==1){	 
	          j=j-1;
	          continue; 
	        }
	        if( allocation[i][j]<0){
	          System.out.println("\n");
	          System.out.println("The Allocation for this resource type is not a positive number.");
	          System.out.println("\n");
	          j=j-1;
	          continue;
	        } 
	      }
	    }
	    System.out.println("\n");
	    
	    //printing allocation matrix
	    System.out.println("\t");
	    System.out.println("    " + "Allocation");
	    System.out.println("   "+ "------------");
	    for(int j=0;j<nor;j++){
	      System.out.print("   ");
	      System.out.print("R" + j);  
	    }
	    System.out.println("\n");
	    for(int i=0;i<nop;i++){
	      System.out.print("P"+i);
	      for(int j=0;j<nor;j++){
	        System.out.print("   ");
	        System.out.print(allocation[i][j]);
	      }
	      System.out.println("\n");
	    }
	    System.out.println("\n");  
	  }
	  
	  //This methods asks the user to enter the values for matrix Maximum
	  //all input validations will be done and finally prints Maximum matrix.
	  public static void askUserMaximum() throws Exception{
	    //setting maximum matrix from user input
	    System.out.println("Initializing the contents of matrix Maximum");
	    System.out.println("\n");
	    maximum=new int[nop][nor];
	    strmaximum=new String[nop][nor];
	    need=new int[nop][nor]; 
	    for(int i=0;i<nop;i++){
	      System.out.println("For Process P"+i+":");
	      for(int j=0;j<nor;j++){
	        System.out.print("\t");
	        System.out.print("Enter the Maximum need for resources type R"+j+":");  
	        // forcing user not to enter negative and non-integers for maximum
	        strmaximum[i][j]=scan.next();
	        try{	  
	          maximum[i][j]=Integer.parseInt(strmaximum[i][j]);
	        }
	        catch (Exception e ){
	          System.out.println("\n");
	          System.out.println("The maximum need for this resource type is not a number.");
	          System.out.println("\n");
	          j=j-1;
	          continue;
	        }
	        int g=BankerDeadlockAvoidance.allocationMaximumCheck(i, j);
	        if (g==1){	 
	          j=j-1;
	          continue; 
	        }
	        int gg=BankerDeadlockAvoidance.maximumNeedAllocationCheck(i, j);
	        if (gg==1){	 
	          j=j-1;
	          continue; 
	        }
	        if( maximum[i][j]<0){
	          System.out.println("\n");
	          System.out.println("The maximum need for this resource type is not a number.");
	          System.out.println("\n");
	          j=j-1;
	          continue;
	        }
	      }
	      System.out.println("\n");
	    }
	    //printing maximum matrix
	    System.out.println("\t");
	    System.out.println("    " + "Maximum");
	    System.out.println("   "+ "------------");
	    
	    for(int j=0;j<nor;j++){
	      System.out.print("   ");
	      System.out.print("R" + j);   
	    }
	    System.out.println("\n");
	    for(int i=0;i<nop;i++){
	      System.out.print("P"+i);
	      for(int j=0;j<nor;j++){
	        System.out.print("   ");
	        System.out.print(maximum[i][j]);
	      }
	      System.out.println("\n");
	    }
	    // checking if the sum of all processes for a resource does not exceed no of instances
	    System.out.println("Verifying if the sum of all the allocations does not exceed" +  "\n" + 
	    "total number of resources of each type in the system.");
	    System.out.println("\n");
	    for(int j=0;j<nor;j++){
	      int resourceSum = 0;
	      for(int i=0;i<nop;i++){
	        resourceSum += allocation[i][j]; 
	      }
	      if (resourceSum > resources[0][j]){
	        System.out.println("\n");
	        System.out.println("Sorry. Processes' allocations for resource type R"+j);
	        System.out.println("cannot be greater than the total "+ resources[0][j]);
	        System.out.println("of this resource type in the system.");
	        System.out.println("\n");
	        System.out.println("Run the program again.");
	        System.out.println("\n\nPress any key to continue..");
	        if ( scan.next() != null);
	        System.exit(0);
	      }   
	    } 
	    //BankerDeadlockAvoidance.printAllMaxAvaiNeed();
	  }
	  //Need function
	  public static int[][] NeedFunction(int[][] max, int[][] alloc){
	    int [][] need = new int[nop][nor];
	    System.out.print("\n");
	    System.out.println("The contents of matrix Need after computation. ");
	    System.out.println("     \t Need");
	    System.out.println("\t------------------");
	    System.out.println("\tR0 \tR1 \tR2");
	    for (int i = 0; i < nop; i++){
	      System.out.printf("P%d", i);
	      for (int j = 0; j < nor; j++){
	        need[i][j] = max[i][j] - alloc[i][j];
	        System.out.print("\t");
	        System.out.print(need[i][j]);
	      }
	      System.out.println("\n");
	    }
	    return need;
	  }
	  // Available Method
	  public static int[][] Available(int[][] resarray, int[][] allocarray )
	  {
	    int[] numvalue = new int[nor];
	    int[][] avail = new int[1][nor];
	    for (int i = 0; i < nop; i++){
	      for (int j = 0; j < nor; j++){
	        numvalue[j] += allocarray[i][j];
	      }
	    }
	    
	    for (int k = 0; k < nor; k++)
	    avail[0][k] = resarray[0][k] - numvalue[k]; 

	    System.out.println("---------------------");
	    System.out.println("The allocation is Ok.");
	    System.out.println("The content of the vector Available has been updated");
	    System.out.println("after the allocation");
	    return avail;
	  }
	  //Method for Display state info
	  public static void DisplayStateInfo( int[][] Allocatearray, int[][] Maximumarray, int[][] available){
	    System.out.println("--------------------------------------------------------");
	    System.out.println("The snapshot of the system at time T0. ");
	    System.out.println("\t   Allocation\t\t   Maximum\t\t   Available");
	    System.out.println("\t------------------\t-------------------\t-----------------");
	    System.out.println("\tR0\tR1\tR2\tR0\tR1\tR2\tR0\tR1\tR2");
	    for (int i = 0; i < nop; i++)
	    {
	      // to print P0, P1, P2, P3, P4 on every row.
	      System.out.printf("P%d\t", i);
	      // For the actual matrix.
	      for (int j = 0; j < nor; j++){
	        System.out.print(Allocatearray[i][j] + "\t");
	      }
	      for (int k = 0; k < nor; k++){
	        System.out.print(Maximumarray[i][k] + "\t");
	      }
	      if (i == 0){
	        for (int m = 0; m < nor; m++){
	          if(m<nor-1)
	          System.out.print(available[0][m] + "\t");
	          else
	          System.out.print(available[0][m]);
	        }
	      }
	      System.out.println("\n");
	    } 
	  }
	  
	  
	  public static boolean checkDuplicates(int[] seq) 
	  {
	    for (int j=0;j<nop;j++){
	      for (int k=j+1;k<nop;k++){
	        if (k!=j && seq[k] == seq[j])
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  //Get Sequence method
	  public static void askUserSequence() throws Exception{
	    //setting maximum matrix from user input
	    System.out.println("Now you need to enter the execution sequence numbers of the " + 
	    nop + " processes");
	    System.out.println("\n");
	    System.out.println("The sequence numbers must be between 0 and " + (nop-1) );
	    seq = new int[nop];
	    strseq = new String[nop];

	    
	    for(int i = 0; i<nop; i++){
	      System.out.print("Enter the sequence number " + i + ": ");
	      strseq[i] = scan.next();
	      
	      try{	  
	        seq[i]=Integer.parseInt(strseq[i]);   
	      }
	      catch (Exception e ){	
	        System.out.println("\n");
	        System.out.println("The Allocation for this resource type is not a number.");
	        System.out.println("\n");
	        i=i-1;
	        continue;
	      }
	      
	      // check for forcing the user to give the number of sequence within the range
	      if( seq[i] < 0 || seq[i] > nop -1 ){
	        System.out.println("\n");
	        System.out.println("Oops. Invalid execution sequence number.");
	        System.out.println("The sequence number must be between 0 and " + (nop-1) + ".\n");
	        i=i-1;
	        continue;
	      } 
	    }
	    
	    if(checkDuplicates(seq))
	    {
	      System.out.println("\n");
	      System.out.println("Invalid sequence. No process can");
	      System.out.println("appear more than one time  in the sequence.");
	      System.out.println("\n" + "Run the program again." + "\n");
	      System.exit(0); 
	    }
	    
	    //Display number of sequence
	    System.out.print("\n" + "The sequence you entered is: <");
	    for(int k = 0; k<nop; k++){
	      System.out.printf("P%d ", seq[k]);
	    }
	    System.out.println(">" + "\n");
	  }
	  
	  public static boolean check(int j)
	  {
	    for (int i = 0; i < nor; i++) {
	      if (need[j][i] > work[i]) {
	        return false;
	      }
	    }
	    return true;
	  }
	  
	  public static void isSafe()
	  {
	    int[] finish = new int[nop];   
	    int i, j;

	    work = new int[nor];
	    for (i = 0; i < nor; i++) {
	      work[i] = available[0][i];
	    }

	    System.out.println("The Safety Algorithm is being executed..." + "\n");

	    for (i=0;i < nop;++i)
	    {
	      if ((finish[seq[i]] == 0) && (check(seq[i])))
	      {
	        for (j = 0; j < nor; j++)
	        {
	          work[j] += allocation[seq[i]][j];
	          finish[seq[i]] = 1;
	        }
	        System.out.println("Process " + seq[i]);
	        System.out.println("Contents of work");
	        for (j = 0; j < nor; j++) {
	          System.out.printf("%d ", work[j]);
	        }
	        System.out.printf("\n");
	        System.out.println("Contents of finish");
	        for (j = 0; j < nop; j++) {
	          System.out.printf("%d ", finish[j]);
	        }
	        System.out.printf("\n\n");
	      }
	      else
	      {
	        System.out.println("Process " + seq[i] + " cannot be allocated");
	        System.out.println("Contents of work");
	        for (j = 0; j < nor; j++) {
	          System.out.printf("%d ", work[j]);
	        }
	        System.out.printf("\n");
	        break;
	      }
	    }
	    if (i == nop) {
	      System.out.println("The system is safe");
	      isSafe = true;
	    } else {
	      System.out.println("The system is unsafe");
	    }
	  }
	  
	  //Heading method   
	  public static void heading ()
	  {
	    System.out.println("************************************************************"+
	    "\n" + "|" + " Simulating Banker's Deadlock Avoidance Algorithm in Java "+ "|" + 
	    "\n" + "|" + "                                                          "+ "|" + 
	    "\n" + "|" + " Description: This program implements the Banker's        "+ "|" + 
	    "\n" + "|" + " deadlock avoidance algorithms. Both the Safety and       "+ "|" + 
	    "\n" + "|" + " Resource-Request Algorithms are implemented.             "+ "|" + 
	    "\n" + "|" + "                                                          "+ "|" + 
	    "\n" + "|" + " Assumptions: This simulation considers the case of five  "+ "|" + 
	    "\n" + "|" + " (5) processes named P0, P1, P2, P3, and P4 and three (3) "+ "|" + 
	    "\n" + "|" + " resource types named R0, R1, and R2.                     "+ "|" + 
	    "\n" + "|" + "                                                          "+ "|" + 
	    "\n" + "|" + " Inputs Validity: The contents of the vectors Allocation, "+ "|" + 
	    "\n" + "|" + " Maximum, Available, Request, Process IDs, Finish, and    "+ "|" + 
	    "\n" + "|" + " sequence numbers must be at least zero. No duplicate in  "+ "|" + 
	    "\n" + "|" + " the sequence. Also the number of processes and resources "+ "|" + 
	    "\n" + "|" + " must be positive. The maximum of each resource type must "+ "|" + 
	    "\n" + "|" + " be greater than zero. Display the proper input  error    "+ "|" + 
	    "\n" + "|" + " messages.                                                "+ "|" + 
	    "\n" + "|" + "                                                          "+ "|" +  
	    "\n"  +"************************************************************"+"\n"+"\n"+
	    "The system will have 5 processes: P0  P1  P2  P3  P4"+"\n"+"\n"+

	    "The system has 3 resources: R0  R1  R2"+"\n"+"\n"+

	    "Getting the number of instances of each resource type."+"\n"+
	    "This initializes the vector Available."+"\n"+"\n");

	  }
	  
	}



