import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StartThread {

	public static void main(String[] args) throws InterruptedException {
		
		int numberFriends = 4;

		BlockingQueue<Friend> friends= new LinkedBlockingQueue<>(numberFriends);
		
		 
		
		System.out.println("Welocome to the Chat group");
		System.out.println("###########################");
		System.out.println(" ");
		
	
		
		for (int i = 0; i < numberFriends; i++) {
			System.out.println("creating threads");
			Friend friend = new Friend(friends,numberFriends,i== 0, i);
			friends.add(friend);
			
		}
		
		for (Friend friend : friends) {
			try {
			
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
