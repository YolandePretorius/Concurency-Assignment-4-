import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StartThread {

	public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;
		int maxNumber = 0;

		BlockingQueue<Friend> friends= new LinkedBlockingQueue<>(numberFriends);
		
		 
		
		System.out.println("Welocome to the Chat group");
		System.out.println("###########################");
		System.out.println(" ");
		
		System.out.println("TRUE : -----> INSIDE ");
		System.out.println("FALSE : -----> OUTSIDE ");
		
		
		
		
			
	
		

		
		
		Friend friend1 = new Friend(friends,numberFriends,true);
		friends.add(friend1);
		Friend friend2 = new Friend(friends,numberFriends, false);
		friends.add(friend2);
		Friend friend3 = new Friend(friends,numberFriends, true);
		friends.add(friend3);
		Friend friend4 = new Friend(friends,numberFriends, true);
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
