import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Friend extends Thread {

	private BlockingQueue<Friend> FriendList;
	private LinkedBlockingQueue<Boolean> replies;
	private LinkedBlockingQueue<Boolean> planReceived;
	private LinkedBlockingQueue<Boolean> reportedPlan;
	private LinkedBlockingQueue majorityPlan;
	private boolean currentThreadPlan;
	private boolean sendReply = false;
	private boolean reported = false;
	private boolean decidePlan = true; // default to stay indoor

	public Friend(BlockingQueue<Friend> friends, int numberFriends, boolean b, int i) {
		this.FriendList = friends;
		replies = new LinkedBlockingQueue<>(numberFriends-1); // friends replying to person
	    reportedPlan = new LinkedBlockingQueue<>(1000000); // 

	}

	public boolean getRandomReply() {
		Random random = new Random();
		return random.nextBoolean();
	}

	public void run() {
		currentThreadPlan = true; // true will be inside and false will be outside

		if (sendReply == false) {

			SendReplyToOther();
			sendReply = true;
		}

		try {
			Thread.sleep(5000); // waiting for other friends replies
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		planReceived = replies; // plan array from first round

		// second round
		reportRecievedReplies();

		try {
			Thread.sleep(5000); // waiting for other friends replies
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		// First vote
		firstFote();

	}

	private void firstFote() {

	}

	private void waitingForRepliesSleep() {
		try {
			Thread.sleep(5000); // waiting for other friends replies
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	private void reportRecievedReplies() {
		for (Friend friend : FriendList) {
			for (Friend friend2list : FriendList) {
				if (friend != friend2list & friend.reported == false ) { // dont send the threads own decided plan
					friend2list.reportedPlan.addAll(friend.planReceived); // send reported plan received to other friends
					friend.reported = true;
					System.out.println("friend " + friend + " reportedPlan " + friend.planReceived + " to "+ friend2list);
					try {
						Thread.sleep(2000); // waiting for other friends replies
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				}
			}
		}
	}

//	private void sendReportedPlan(LinkedBlockingQueue replies2, Friend friend) {
//		// TODO Auto-generated method stub
//		for(Friend friend1 : FriendList) {
//			if(friend1 != friend) {
//				System.out.println("replies"+ replies);
//				friend1.reportedPlan.addAll(replies); // problem here 
//			}
//		}
//		
//	}

//first round sending replies to all generals
	private void SendReplyToOther() {
		for (Friend friend : FriendList) {
			if (friend != this) {
				friend.AddToRepliesList(this); // send the current thread to the other threads and add the current
												// thread to their replies list
			}
		}

	}

// adding thread to the other threads replies list
	private void AddToRepliesList(Friend friend) {
		System.out.println(friend + "replying to " + this);
		replies.add(friend.decidePlan);
		System.out.println("=======");
		System.out.println(replies);

	}

}
