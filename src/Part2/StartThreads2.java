package Part2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StartThreads2 { // crash failures
	
	
public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;

		BlockingQueue<Friend2> friends= new LinkedBlockingQueue<>(numberFriends);
		
		System.out.println("###########################");
		System.out.println("part 2:  Crash failures during runs of the algorithm.");
		System.out.println("Welocome to the Chat group");
		System.out.println("###########################");
		System.out.println(" ");
		
		System.out.println("1 : -----> INSIDE ");
		System.out.println("2 : -----> OUTSIDE ");
		
			
		
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



