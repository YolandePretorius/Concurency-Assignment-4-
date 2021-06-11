package Part3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Friend3 extends Thread {

	private BlockingQueue<Friend3> FriendList;
	private int[] reportedPlan;
	private LinkedBlockingQueue<Integer> MajorityPlanForOneFriend;
	private LinkedBlockingQueue<Integer> finalMajorPlan;
	private int currentThreadPlan;
	public boolean sendReply = false;
	public long replyWaitingTime = 10000;
	public long receivedReplyWaitingTime = 10000;
	public boolean sendReceived = false;
	public boolean MajorityReceived = false;

	private int inside;
	private int outside;
	private String finalPlanDecided;
	private String finalPlanDecidedForAll;
	int[] replies;

	public Friend3(BlockingQueue<Friend3> friends, int numberFriends, int choice) {
		this.FriendList = friends;
		replies = new int[4]; //
		reportedPlan = new int[4];
		MajorityPlanForOneFriend = new LinkedBlockingQueue<>(100);
		finalMajorPlan = new LinkedBlockingQueue<>(100);
		this.currentThreadPlan = choice;

		setArraytoZero(replies);

	}

	private void setArraytoZero(int[] replies2) {
		for (int i = 0; i < 4; i++) {
			replies2[i] = 0;
		}
	}
	//PART 2
	// randomly stop thread from sending replies
	public void crashfailures() {
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
	//PART 3
	// get random replies for friend 0
	public void Byzantine() {
		int rand = 0;
		Random random = new Random();
		rand = random.nextInt(3 - 1) + 1;
		currentThreadPlan = rand;
		System.out.println();
		System.out.println("BYZANTINE: " +this.getName()+" PLAN: "+ currentThreadPlan);
		System.out.println();
	}
	
	//----------------------------------------------------RUN STARTS------------------------------------------------------

	public void run() {

		System.out.println(this.getName() + " CURRENT PLAN " + currentThreadPlan);

		if (sendReply == false) {
			System.out.println(this.getName() + " SEND REPLY TO FRIENDS");
			SendReplyToOther();
			sendReply = true;
		}

		// System.out.println(this + " - WAITING FOR REPLIES...");
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

		// =================================================================================================
		// Output of friends results
		// =================================================================================================

		System.out.println();

		for (int i = 0; i < reportedPlan.length; i++) {
			System.out.println(" (" + this.getName() + ")(REPORTED PLAN): " + reportedPlan[i] + "From Friend" + i);
		}

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

		System.out.println("FRIEND " + this.getName() + " (ALL FRIENDS FINAL PLAN ): " + finalMajorPlan); // Array with
																											// final
																											// true and
																											// false
																											// values

		System.out.println(
				" FINAL----------> FRIEND " + this.getName() + " (ALL FRIENDS FINALPLAN ):---> " + finalPlanDecidedForAll);

		System.out.println();

		return;
	}//---------------------------------------------------------------RUN ENDS------------------------------------------------
	
	
	
	//=========================================================COUNTING VOTES ADD TO FINAL PLAN ================================================

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

	// first round: combine the replies and reported plans into one
	private void firstVote() {
		getMajorityOutOfReplies(replies); // get majority for this friend
		getMajorityOutOfReplies(reportedPlan);

	}

	private void getMajorityOutOfReplies(int[] replies2) {
		for (Integer friendReplies : replies2) {
			MajorityPlanForOneFriend.add(friendReplies);

		}

	}

	private void MajorityPlanForAllFriends() {
		for (Friend3 friend : FriendList) {
			finalMajorPlan.addAll(friend.MajorityPlanForOneFriend);

		}

	}
	
	
	//=========================================================WAITING FOR REPLIES FUCTIONS=====================================

	// waiting for friends to finish replying
	private void waitForRepliesOrTimer() {
		long start = System.currentTimeMillis();

		while (true) {
			boolean allFriendsDone = true;

			for (Friend3 friend : FriendList) {
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

			for (Friend3 friend : FriendList) { // verify if all friends have send replies
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

			for (Friend3 friend : FriendList) { // verify if all friends have send replies
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

	

	
	//========================================================REPORTING FUNCTION ===============================================
	private void reportReceivedReplies() {
		
		if(currentThreadPlan != 0){ // 0 will indicate crash failures

		for (Friend3 friend : FriendList) {
			if (friend != this) { // sending report to friend
				for (int i = 0; i < replies.length; i++) {
					if ((Integer.parseInt(friend.getName()) != i) && (replies[i] != 0)) {
						if (friend.reportedPlan[i] == 0) {
							friend.reportedPlan[i] = replies[i]; // send reported plan received to other friends
						} else {
							if (friend.reportedPlan[i] == replies[i]) {
								friend.reportedPlan[i] = replies[i];
							} else {
								friend.reportedPlan[i] = 99; // 99 will represent that there is a difference in answers
								// reported by two friends and it can't be taken into
								// consideration
							}
							System.out.println("FRIEND " + this.getName() + " IS REPORTING FRIEND " + i + "'S REPLY OF "
									+ replies[i] + " TO FREIND " + friend.getName());

						}
					}
				}
			}

		  }
		}

	}
	
	
	
	

	// ******************************************************************************
	// first round sending replies to all generals
	// first round sending replies to all friends, friend 0 will crash or send random values 
	// between 1 and 2 
	// *******************************************************************************
	private void SendReplyToOther() {
		for (Friend3 friend : FriendList) {
			if (friend != this) {
				
				//===================uncomment for part 2 crashfailures and part 3 Byzantine
				if (this.getName() == "0") { 
					
					//crashfailures(); // Part 2 call function to randomly stop thread 
												
					Byzantine(); // Part3 

					
				}

				friend.AddToRepliesList(this); // send the current thread to the other threads and add
												// the current
												// thread to their replies list
			}
		}

	}

// adding thread to the other threads replies list
	private void AddToRepliesList(Friend3 friend2) {
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
	// ==================================================================================================================================

}
