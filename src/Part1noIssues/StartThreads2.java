package Part1noIssues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//-----------------------------------------------------------------------------------------------------
//All friends replies
//during the run of program friends will be assigned a choice 1: inside and 2: outside
//All friends will reply their choice to their friends
//All friends will report their friends choices received
//If there is a draw the default decision is inside
//-------------------------------------------------------------------------------------------------------
public class StartThreads2 { 
	
	
public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;

		BlockingQueue<Friend1> friends= new LinkedBlockingQueue<>(numberFriends);
		
		System.out.println("############################################################################################################");
		System.out.println();
		System.out.println("             Part 1:  No failures at all during any of the runs of the algorithm.");
		System.out.println("             Welocome to the Chat group");
		System.out.println("             Please be patient as this may take a few second");
		System.out.println();
		System.out.println("############################################################################################################");
		System.out.println(" ");
		
		
		System.out.println("1 : -----> INSIDE ");
		System.out.println("2 : -----> OUTSIDE ");
		
			
		// creating each friend as well as assign their choices 1: inside,  2:outside
		Friend1 friend1 = new Friend1(friends,numberFriends,1); 
		friend1.setName("0");
		friends.add(friend1);
		Friend1 friend2 = new Friend1(friends,numberFriends, 1);
		friend2.setName("1");
		friends.add(friend2);
		Friend1 friend3 = new Friend1(friends,numberFriends, 1);
		friend3.setName("2");
		friends.add(friend3);
		Friend1 friend4 = new Friend1(friends,numberFriends, 2);
		friend4.setName("3");
		friends.add(friend4);
		
		
		for (Friend1 friend : friends) {
			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			friend.start();
		}
	
		

		// interupts the friends threads that is in the threads list and stops them
		for (Friend1 thread : friends) {
				thread.interrupt();
				thread.join();
			    //friends.remove(thread);
			
		}
		
				
		System.out.println("*******************See you soon ************************");
		

	}

}



