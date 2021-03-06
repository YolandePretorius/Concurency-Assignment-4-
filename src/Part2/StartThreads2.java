package Part2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//-----------------------------------------------------------------------------------------------------
//crash failures
// during the run of program friend 0 will randomly crash and will no longer send any replies or reports to friends 
// crash can occur before first reply is send, after first reply, after first reply or no crash
// When friend 0 crashes he sends 0 as a reply to friends
//since its a random crash there is a chance that when the program is run that there is no crashes as well
//-------------------------------------------------------------------------------------------------------
public class StartThreads2 { 
	
	
public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;

		BlockingQueue<Friend2> friends= new LinkedBlockingQueue<>(numberFriends);
		
		
		System.out.println("############################################################################################################");
		System.out.println();
		System.out.println("             Part 2: Crash failures during runs of the algorithm");
		System.out.println("             Welocome to the Chat group");
		System.out.println("             Please be patient as this may take a few second");
		System.out.println();
		System.out.println("############################################################################################################");
		System.out.println(" ");
		
		System.out.println("0 : -----> NO REPLY ");
		System.out.println("1 : -----> INSIDE ");
		System.out.println("2 : -----> OUTSIDE ");
		System.out.println();
		
			
		
		Friend2 friend1 = new Friend2(friends,numberFriends,1); // friend will crash  during the run of the program
		friend1.setName("0");
		friends.add(friend1);
		Friend2 friend2 = new Friend2(friends,numberFriends, 2);
		friend2.setName("1");
		friends.add(friend2);
		Friend2 friend3 = new Friend2(friends,numberFriends, 1);
		friend3.setName("2");
		friends.add(friend3);
		Friend2 friend4 = new Friend2(friends,numberFriends, 2);
		friend4.setName("3");
		friends.add(friend4);
		
		
		for (Friend2 friend : friends) {
			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			friend.start();
		}
	
		

		// interupts the friends threads that is in the threads list and stops them
		for (Friend2 thread : friends) {
				thread.interrupt();
				thread.join();
			    //friends.remove(thread);
			
		}
		
				
		System.out.println("*******************See you soon ************************");
		

	}

}



