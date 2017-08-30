# Banker-algorithm
Banker's algorithm for deadlock avoidance. Both safety and resource-request implemented.
Operating Systems Concept (Deadlock Avoidance)
Simulating Banker's Deadlock Avoidance Algorithm in Java
	
Description: 
This program implements the Banker's deadlock avoidance algorithms. Both the Safety and Resource-Request algorithms are implemented in this project.  
Assumptions: This simulation considers the case of five(5) processes named P0, P1, P2, P3, and P4 and three (3) resource types named R0, R1, and R2.
Inputs Validity: The contents of the vectors Allocation, Maximum, Available, Request, Process IDs, Finish, and sequence numbers must be at least zero. 
No duplicate in the sequence. Also the number of processes and resources must be positive. The maximum of each resource type must be greater than zero. 

Available: a 1-d array of size m (m = 3) indicating the number of available resources
Max: a 2-d array of size nxm that defines the maximum demands of each process in a system (n = 5, is the number of processes)
Need: a 2-d array of size nxm that indicate the remaining resource need of each process.
Need[i,j] = Max[i,j] - Allocation[i,j]

Safety Algorithm:
The algorithm for finding out whether or not a system is in a safe state can be described as follows:
1. Let Work = Available
   Finish[i] = false; for i=1,2,.......,n
2. Find an i such that both 
   a) Finish[i] = false
   b) Needi <= Work
        if no such i exists goto (4)
3. Work = Work + Allocationi
   Finish[i] = true
   goto (2)
4. if Finish[i] = true for all i
   then the system is in safe state.

Resource-Request Algorithm
Let Requesti be the request array for process Pi.
1. if Requesti <= Needi
   goto step (2); otherwise, raise an error condition, since the process has exceeded its maximum claim.
2. if Requesti <= Available
   goto step (3); otherwise, raise an exception since the resources are not available.
3. Have the system pretend to have allocated the requested resources to process Pi by modifying the state as follows:
   Available = Available -Requesti
   Allocationi = Allocationi + Requesti
   Needi = Needi - Requesti

Testing: to run the program: build and run BankerDeadlockAvoidance.java
prompt requesting instances for resources:  Resources[10, 5, 7]
prompt requesting matrix allocation: Allocation[[0, 1, 0]
                                                [2, 0, 0]
                                                [3, 0, 2]
                                                [2, 1, 1]
                                                [0, 0, 2]]
prompt requesting Maximum need: Max[[7, 5, 3]
                                    [3, 2, 2]
                                    [9, 0, 2]
                                    [2, 2, 2]
                                    [4, 3, 3]]
prompt for process execution sequence that must be numeric between 0 and 4:  1, 3, 4, 0, 2
If in safe state (Note: the above given values and sequence order leads to safe state), proceeds to Resource-request part
prompt user to enter process ID (0, 1, 2, 3 or 4) of the process requesting additional resources: 1
for P1: prompt for resources requested [1, 0, 2]
if process request granted sucessfully 
you will be prompt to re-enter the process execution sequence that must be numeric between 0 and 4:  1, 3, 4, 0, 2
The system should be in safe state.
