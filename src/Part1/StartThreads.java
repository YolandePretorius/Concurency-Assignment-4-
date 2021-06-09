package Part1;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StartThreads {

	public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;

		BlockingQueue<Friend> friends= new LinkedBlockingQueue<>(numberFriends);
		
		System.out.println("###########################");
		System.out.println("part 1:  No failures at all during any of the runs of the algorithm.");
		System.out.println("Welocome to the Chat group");
		System.out.println("###########################");
		System.out.println(" ");
		
		System.out.println("TRUE : -----> INSIDE ");
		System.out.println("FALSE : -----> OUTSIDE ");
		
				

		
		
		Friend friend1 = new Friend(friends,numberFriends,true);
		friend1.setName("Ally");
		friends.add(friend1);
		Friend friend2 = new Friend(friends,numberFriends, false);
		friend2.setName("Bob");
		friends.add(friend2);
		Friend friend3 = new Friend(friends,numberFriends, true);
		friend3.setName("Carl");
		friends.add(friend3);
		Friend friend4 = new Friend(friends,numberFriends, false);
		friend4.setName("Dan");
		friends.add(friend4);
		
		
		for (Friend friend : friends) {
			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			friend.start();
		}
	
		

		// interupts the friends threads that is in the threads list and stops them
		for (Friend thread : friends) {
				thread.interrupt();
				thread.join();
			    //friends.remove(thread);
			
		}
		
				
		System.out.println("*******************See you soon ************************");
		

	}

}
