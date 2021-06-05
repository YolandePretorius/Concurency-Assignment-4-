import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Friend extends Thread{

	private BlockingQueue<Friend> FriendList;
	private LinkedBlockingQueue replies;
	private boolean currentThreadPlan;

	public Friend(BlockingQueue<Friend> friends, int numberFriends, boolean b, int i) {
		this.FriendList = friends;
		replies = new LinkedBlockingQueue<>(numberFriends); // friends replying to person
		
	}
	
	public boolean getRandomReply() {
	    Random random = new Random();
	    return random.nextBoolean();
	}
	
	public void run() {
		currentThreadPlan = true; // true will be inside and false will be outside
		
		SendReplyToOther();
		
		
	}

	private void SendReplyToOther() {
		for (Friend friend : FriendList) {
			if (friend != this) {
				friend.AddToRepliesList(this); // send the current thread to the other threads and add the current thread to their replies list
			}
		}
			
	}
	
// adding thread to th other threads replies list
	private void AddToRepliesList(Friend friend) {
		System.out.println(friend + "replying to "+ this);
		replies.add(friend);
		System.out.println("=======");
		System.out.println(replies);
		
	}
		
	

}
