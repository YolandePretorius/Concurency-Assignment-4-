package Part1;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Friend extends Thread {

	private BlockingQueue<Friend> FriendList;
	private LinkedBlockingQueue<Friend> reportedPlan;
	private LinkedBlockingQueue<Boolean> MajorityPlanForOneFriend;
	private LinkedBlockingQueue<Boolean> finalMajorPlan;
	private boolean currentThreadPlan;
	public boolean sendReply = false;
	public long replyWaitingTime = 10000;
	public long receivedReplyWaitingTime = 10000;
	public boolean sendReceived = false;
	public boolean MajorityReceived = false;
	private boolean reported = false;
	private boolean decidePlan = true; // default to stay indoor if there is a draw

	private int inside;  // true is inside
	private int outside; // false is outside
	private String finalPlanDecided;
	private String finalPlanDecidedForAll;
	private LinkedBlockingQueue<Friend> replies;

	public Friend(BlockingQueue<Friend> friends, int numberFriends, boolean choice) {
		this.FriendList = friends;
		replies = new LinkedBlockingQueue<>(20); //
		reportedPlan = new LinkedBlockingQueue<>(100); //
		MajorityPlanForOneFriend = new LinkedBlockingQueue<>(100);
		finalMajorPlan = new LinkedBlockingQueue<>(100);
		this.currentThreadPlan = choice;

	}

	public void run() {

		System.out.println("FRIEND "+this.getName() + " CURRENT PLAN " + currentThreadPlan);

		if (sendReply == false) {
			System.out.println("FRIEND "+ this.getName() + " SEND REPLIES TO FRIENDS");
			SendReplyToOther();
			sendReply = true;
		}

		// waiting for replies from friends
		waitForRepliesOrTimer();

		// second round

		reportReceivedReplies();
		sendReceived = true;

		waitForReceivedRepliesOrTimer();

		System.out.println("FRIEND "+this.getName() + "- DONE WAITING FOR RECEIVED REPLIES...");

		// First vote
		firstVote();

		// Add my plan to the majority plan
		//MajorityPlanForOneFriend.add(currentThreadPlan);

		// this is the final plan what each friend will do
		finalPlanForOneFriend();

		waitForReceivedFinalPlan();
		MajorityPlanForAllFriends();
		MajorityReceived = true;

		finalPlanForAllFriends(); // over all final plan for all friends

//		System.out.println();
//
//		System.out.println("( "+ this + ")( REPLIES RECIEVED FROM ):" + replies);
//		
//		System.out.println("( "+ this.getName() + ") (FINAL PLAN FOR ME (FRIEND REPLIES + MYREPLY + REPORTED REPLIES ): " + MajorityPlanForOneFriend);

		// System.out.println("( "+ this + ") ( FINAL PLAN FOR ME (FRIEND REPLIES + MY
		// REPLY + REPORTED REPLIES ): " + finalPlanDecided);
//
//		System.out.println();

		System.out.println(this.getName() + "(ALL FRIENDS FINAL PLAN ---> ):  " + finalMajorPlan);

		System.out.println(this.getName() + "(ALL FRIENDS FINAL PLAN --> ):  " + finalPlanDecidedForAll);

		System.out.println();

		return;
	}

	// get the majority votes for one friend
	private void finalPlanForOneFriend() {
		outside = 0;
		inside = 0;

		for (Boolean finalVote : MajorityPlanForOneFriend) {
			if (finalVote == false) {
				outside++;
			} else {
				inside++;
			}
		}
		if (outside > inside) {
			finalPlanDecided = "OUTSIDE";
		} else {
			finalPlanDecided = "INSIDE";
		}
	}

	// get the majority votes for all friends
	private void finalPlanForAllFriends() {
		outside = 0;
		inside = 0;
		for (Boolean finalVote : finalMajorPlan) {
			if (finalVote == false) {
				outside++;
			} else {
				inside++;
			}
		}
		if (outside > inside) {
			finalPlanDecidedForAll = "OUTSIDE";
		} else {
			finalPlanDecidedForAll = "INSIDE";
		}
	}

	// first round
	private void firstVote() {
		//getMajorityOutOfReplies(replies); // get majority for this friend
		getMajorityOutOfReplies(reportedPlan);

	}

	private void getMajorityOutOfReplies(LinkedBlockingQueue<Friend> replies2) {
		for (Friend friendReplies : replies2) {
			MajorityPlanForOneFriend.add(friendReplies.currentThreadPlan);

		}

	}

	private void MajorityPlanForAllFriends() {
		for (Friend friend : FriendList) {
			finalMajorPlan.addAll(friend.MajorityPlanForOneFriend);

		}

	}

	// waiting for friends to finish replying
	private void waitForRepliesOrTimer() {
		long start = System.currentTimeMillis();

		while (true) {
			boolean allFriendsDone = true;

			for (Friend friend : FriendList) {
				if (friend != this) { // don't send replies to self
					allFriendsDone = allFriendsDone && friend.sendReply;
				}
			}

			if (allFriendsDone == true) {
				// all friends have send replies
				break;
			}

			if (System.currentTimeMillis() - start > replyWaitingTime) {
				// Waiting more than x seconds
				break;
			}

			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	}

	// waiting for receiving replies
	private void waitForReceivedRepliesOrTimer() {

		long start = System.currentTimeMillis();

		while (true) {
			boolean allFriendsDone = true;

			for (Friend friend : FriendList) { // verify if all friends have send replies
				if (friend != this) { // don't send replies to self
					allFriendsDone = allFriendsDone && friend.sendReceived;
				}
			}

			if (allFriendsDone == true) {
				// all friends have send replies
				break;
			}

			if (System.currentTimeMillis() - start > receivedReplyWaitingTime) {
				// Waiting more than x seconds
				break;
			}

			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

	// waiting for receiving replies
	private void waitForReceivedFinalPlan() {

		long start = System.currentTimeMillis();

		while (true) {
			boolean allFriendsDone = true;

			for (Friend friend : FriendList) { // verify if all friends have send replies
				if (friend != this) { // don't send replies to self
					allFriendsDone = allFriendsDone && friend.MajorityReceived;
				}
			}

			if (allFriendsDone == true) {
				// all friends have send replies
				break;
			}

			if (System.currentTimeMillis() - start > receivedReplyWaitingTime) {
				// Waiting more than x seconds
				break;
			}

			try {
				Thread.sleep(500); // waiting for other friends replies
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

	private void reportReceivedReplies() {

		for (Friend friend : FriendList) {
			if (friend != this) { // don't send replies to self
				for (Friend friendReplies : replies) {
					if (friend != friendReplies) { // dont send the threads own decided plan
						// System.out.println(this + ": - " + friend + " receiving replies from " +
						// friendReplies);
						friend.reportedPlan.add(friendReplies); // send reported plan received to other friends
						// System.out.println("Send replies from " + friendReplies + " to " + friend);

					}
				}
			}
		}
	}

	// first round sending replies to all generals
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
		replies.add(friend);

	}

}
