package Part2;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Friend2 extends Thread {

	private BlockingQueue<Friend2> FriendList;
	private LinkedBlockingQueue<Integer> reportedPlan;
	private LinkedBlockingQueue<Integer> MajorityPlanForOneFriend;
	private LinkedBlockingQueue<Integer> finalMajorPlan;
	private int currentThreadPlan;
	public boolean sendReply = false;
	public long replyWaitingTime = 10000;
	public long receivedReplyWaitingTime = 10000;
	public boolean sendReceived = false;
	public boolean MajorityReceived = false;
//	private boolean reported = false;
//	private boolean decidePlan = true; // default to stay indoor if there is a draw

	private int inside;
	private int outside;
	private String finalPlanDecided;
	private String finalPlanDecidedForAll;
	int[] replies;

	public Friend2(BlockingQueue<Friend2> friends, int numberFriends, int choice) {
		this.FriendList = friends;
		replies = new int[4]; //
		reportedPlan = new LinkedBlockingQueue<>(100); //
		MajorityPlanForOneFriend = new LinkedBlockingQueue<>(100);
		finalMajorPlan = new LinkedBlockingQueue<>(100);
		// this.currentThreadPlan = getRandomReply();// true will be inside and false
		// will be outside
		this.currentThreadPlan = choice;

		setArraytoZero(replies);

	}

	private void setArraytoZero(int[] replies2) {
		for (int i = 0; i < 4; i++) {
			replies2[i] = 0;
		}
	}

	// randomly stop thread from sending replies
	public void RandomlyStop() {
		Random random = new Random();
		int rand = 0;
		while (currentThreadPlan != 0) {
			rand = random.nextInt(2);

			if (rand == 0) {
				currentThreadPlan = 0;
				

			} else {
				break;
			}
		}

	}

	// ----------------------------------------------------------run-----------------------------------------------------------
	public void run() {

//		currentThreadPlan = getRandomReply(); // true will be inside and false will be outside

		System.out.println("Friend "+this.getName() + " CURRENT PLAN " + currentThreadPlan);

		if (sendReply == false) {
			System.out.println("Friend "+this.getName() + " SEND REPLIES TO FRIENDS");
			SendReplyToOther();
			sendReply = true;
		}


		waitForRepliesOrTimer();

		// second round

		reportReceivedReplies();
		sendReceived = true;

		waitForReceivedRepliesOrTimer();

		System.out.println(this.getName() + "- DONE WAITING FOR RECEIVED REPLIES...");

		// First vote
		firstVote();

		// Add my plan to the majority plan
		MajorityPlanForOneFriend.add(currentThreadPlan);

		// this is the final plan what each friend will do
		finalPlanForOneFriend();

		waitForReceivedFinalPlan();
		MajorityPlanForAllFriends();
		MajorityReceived = true;

		finalPlanForAllFriends();

		System.out.println("///////// Friend " + this.getName() + " CURRENT PLAN " + currentThreadPlan + " /////////");

		// =================================================================================================
		// Output of friends results
		// =================================================================================================
		System.out.println();

		// System.out.println(" (" + this.getName() + ")(REPLIES RECIEVED):" + replies);
		// // Array with replies from
		// friends

//		System.out.println("( " + this.getName() + ") (FINAL PLAN FOR ME (FRIEND REPLIES + MYREPLY + REPORTED REPLIES ): " + MajorityPlanForOneFriend);
//
//		 System.out.println(" ("+ this.getName() + ") (FINAL PLAN FOR ME (FRIEND REPLIES + MYREPLY + REPORTED REPLIES ): " + finalPlanDecided+"<-------------------");
//
//		 System.out.println();
//
//		 System.out.println();

		System.out.println("FRIEND" + this.getName() + "(ALL FRIENDS FINAL PLAN ): " + finalMajorPlan); // Array with
																										// final true
																										// and false
																										// values

		System.out.println(" FINAL----------> FRIEND" + this.getName() + "(ALL FRIENDS FINALPLAN ):---> "
				+ finalPlanDecidedForAll);

		System.out.println();

		return;
	}//--------------------------------------------------run ends-------------------------------------------------------

	// get the majority votes for one friend
	private void finalPlanForOneFriend() {
		outside = 0;
		inside = 0;

		for (Integer finalVote : MajorityPlanForOneFriend) {
			if (finalVote == 1) {
				inside++;
			}
			if (finalVote == 2) {
				outside++;
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
		for (Integer finalVote : finalMajorPlan) {
			if (finalVote == 1) {
				inside++;
			}
			if (finalVote == 2) {
				outside++;
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
		getMajorityOutOfReplies(replies); // get majority for this friend
		getMajorityOutOfReplies2(reportedPlan);

	}

	private void getMajorityOutOfReplies(int[] replies2) {
		for (Integer friendReplies : replies2) {
			MajorityPlanForOneFriend.add(friendReplies);

		}

	}

	private void getMajorityOutOfReplies2(LinkedBlockingQueue<Integer> reportedPlan2) {
		for (Integer friendReplies : reportedPlan2) {
			MajorityPlanForOneFriend.add(friendReplies);

		}

	}

	private void MajorityPlanForAllFriends() {
		for (Friend2 friend : FriendList) {
			finalMajorPlan.addAll(friend.MajorityPlanForOneFriend);

		}

	}

	// waiting for friends to finish replying
	private void waitForRepliesOrTimer() {
		long start = System.currentTimeMillis();

		while (true) {
			boolean allFriendsDone = true;

			for (Friend2 friend : FriendList) {
				if (friend != this) {
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

			for (Friend2 friend : FriendList) { // verify if all friends have send replies
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

			for (Friend2 friend : FriendList) { // verify if all friends have send replies
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

	
	// reporting replies to other friends
	private void reportReceivedReplies() {

		if (currentThreadPlan != 0) {

			for (Friend2 friend : FriendList) {
				if (friend != this)// if friend plan is 0 it means the thread has failed
					for (int i = 0; i < replies.length; i++) { // i is the reply 1 or 2 and the position is the friend,
																// position 0 is friend 0
						if ((Integer.parseInt(friend.getName()) != i) && (replies[i] != 0)) {
							friend.reportedPlan.add(replies[i]); // send reported plan received to
																	// other friends
							System.out.println("Friend " + this.getName() + " reports to friend " + friend.getName()
									+ " what friend " + i + " reply was " + replies[i]);

						}
					}

			}
		}
	}

	// ********************************************
	// first round sending replies to all generals
	// ********************************************

	private void SendReplyToOther() {
		for (Friend2 friend : FriendList) {
			if (friend != this) {

				if (this.getName() == "0") { // -----------------------> Friend 0 is the traitor stops randomly sending
												// plan by sending 0
					RandomlyStop(); //determines when friend will stop sending messages

					System.out.println("++++++++++++ Friend 0 will stop randomly : his curent thread plan is "
							+ currentThreadPlan + " to friend :" + friend.getName());

					// currentThreadPlan = getRandomReply();

				}

				friend.AddToRepliesList(this); // send the current thread to the other threads and add
												// the current
												// thread to their replies list

				System.out.println(this.getName() + "************** is sending his plan " + currentThreadPlan
						+ " to friend :" + friend.getName());

			}
		}

	}

	// adding thread to the other threads replies list
	private void AddToRepliesList(Friend2 friend2) {
		if (friend2.getName() == "0") {
			replies[0] = friend2.currentThreadPlan;

		}
		if (friend2.getName() == "1") {
			replies[1] = friend2.currentThreadPlan;

		}
		if (friend2.getName() == "2") {
			replies[2] = friend2.currentThreadPlan;

		}
		if (friend2.getName() == "3") {
			replies[3] = friend2.currentThreadPlan;

		}

	}

}
