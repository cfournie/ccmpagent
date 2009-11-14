package agent;
import testbed.agent.Agent;
import learning.LearningInterface;
import trust.TrustInterface;

import testbed.messages.CertaintyReplyMsg;
import testbed.messages.CertaintyRequestMsg;
import testbed.messages.OpinionOrderMsg;
import testbed.messages.OpinionReplyMsg;
import testbed.messages.OpinionRequestMsg;
import testbed.messages.ReputationAcceptOrDeclineMsg;
import testbed.messages.ReputationReplyMsg;
import testbed.messages.ReputationRequestMsg;
import testbed.messages.WeightMsg;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author cfournie
 *
 */
public class CCMPAgent extends Agent {

	private LearningInterface mDecisionTree;
	private TrustInterface  mTrustNetwork;
    private List<ReputationRequestMsg>  mReputationRequestsToAccept;	
	
	/**
	 * 
	 */
	public CCMPAgent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param paramFile
	 */
	public CCMPAgent(String paramFile) {
		super(paramFile);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#initializeAgent()
	 */
	@Override
	public void initializeAgent() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareCertaintyReplies()
	 */
	@Override
	public void prepareCertaintyReplies() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareCertaintyRequests()
	 */
	@Override
	public void prepareCertaintyRequests() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionCreationOrders()
	 */
	@Override
	public void prepareOpinionCreationOrders() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionProviderWeights()
	 */
	@Override
	public void prepareOpinionProviderWeights() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionReplies()
	 */
	@Override
	public void prepareOpinionReplies() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionRequests()
	 */
	@Override
	public void prepareOpinionRequests() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationAcceptsAndDeclines()
	 */
	@Override
	public void prepareReputationAcceptsAndDeclines() {
		List<ReputationRequestMsg> reputationRequests = getIncomingMessages();
		mReputationRequestsToAccept = new ArrayList<ReputationRequestMsg>();		

        for (ReputationRequestMsg receivedMsg: reputationRequests) {
        	if( mDecisionTree.respondToReputationRequest( receivedMsg.getSender(), receivedMsg.getAppraiserID(), receivedMsg.getEra() ) )
        	{
        		mReputationRequestsToAccept.add(receivedMsg);
        		ReputationAcceptOrDeclineMsg msg = receivedMsg.reputationAcceptOrDecline(true);
            	sendOutgoingMessage(msg);
        	}
        	else
        	{
        		mReputationRequestsToAccept.add(receivedMsg);
        		ReputationAcceptOrDeclineMsg msg = receivedMsg.reputationAcceptOrDecline(false);
            	sendOutgoingMessage(msg);
        	}        		
        }		
		
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationReplies()
	 */
	@Override
	public void prepareReputationReplies() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationRequests()
	 */
	@Override
	public void prepareReputationRequests() {
		// TODO Auto-generated method stub

	}

}
