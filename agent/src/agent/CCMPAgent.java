package agent;
import testbed.agent.Agent;

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

import agent.decision.DecisionTree;
import agent.trust.TrustNetwork;
import testbed.sim.Era;
import testbed.sim.Weight;
import testbed.sim.Opinion;
import testbed.sim.AppraisalAssignment;
import testbed.sim.Appraisal;


/**
 * 
 */

/**
 * @author cfournie
 *
 */
public abstract class CCMPAgent extends Agent {

	private DecisionTree 						mDecisionTree;
	private TrustNetwork	  					mTrustNetwork;
    private List<ReputationRequestMsg>  		mReputationRequestsToAccept;	
    private List<CertaintyReplyMsg>  			mCertaintyReplysProvided;
    private List<OpinionRequestMsg>         	mOpinionRequests;
    private List<ReputationRequestMsg>  		mReputationsRequested;
    private List<ReputationAcceptOrDeclineMsg>	mReputationRequestsAcceptedOrDeclined;
    private List<CertaintyRequestMsg>			mCertaintiesRequested;
    private List<OpinionRequestMsg>				mOpinionsRequested;
    	
	
	/**
	 * 
	 */
	public CCMPAgent()
	{
		mDecisionTree = createDecisionTree();
		mTrustNetwork = createTrustNetwork();
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
	public void initializeAgent()
	{
        mDecisionTree.init();
        mTrustNetwork.init();
        
        mDecisionTree.setAgent(this);
        mTrustNetwork.setAgent(this);
        
        for (String name: agentNames)
        {
        	mTrustNetwork.addAgent(name);
        	mDecisionTree.addAgent(name);
        	for( Era era: eras )
        	{
        		updateDecisionTreeTrustValues(name, era);
        	}
        }

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareCertaintyReplies()
	 */
	@Override
	public void prepareCertaintyReplies()
	{
		/* Assumptions for this behaviour
		 * 1. DecisionTree may or may not decide to provide a certainty
		 * 2. DecisionTree provides the certainty in the format required by the message (1 - e*alpha ?)
		 */		
		List<CertaintyRequestMsg> certRequests = getIncomingMessages();
		//Store the list of certainty replies we provided to others.
		mCertaintyReplysProvided = new ArrayList<CertaintyReplyMsg>();
				
        for (CertaintyRequestMsg receivedMsg: certRequests)
        {
            Era era = receivedMsg.getEra();
            String fromAgent = receivedMsg.getSender();
            
            if( mDecisionTree.provideCertaintyReply(fromAgent, era) )
            {
            	double myExpertise = mDecisionTree.getCertaintyRequestValue(fromAgent, era);
            	CertaintyReplyMsg msg = receivedMsg.certaintyReply(myExpertise);
                sendOutgoingMessage(msg);
            	mCertaintyReplysProvided.add(msg);
                
                //Tell our trust network we provided a certainty value in our reply
                mTrustNetwork.providedCertaintyReply(fromAgent, era, myExpertise);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);                
            }
            else
            {
                //Tell our trust network we did not accept a certainty request
                mTrustNetwork.didNotAcceptCertaintyRequest(fromAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);           	
            }
        }
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareCertaintyRequests()
	 */
	@Override
	public void prepareCertaintyRequests() {
		// first collect the replies to the previous reputation transactions
	    List<ReputationReplyMsg> reputationReplies = getIncomingMessages();
	    mCertaintiesRequested = new ArrayList<CertaintyRequestMsg>();
	    
	    //determine if everyone who accepted a request, provided a reputation.
	    //go through each accept msg we stored previously.
	    for( ReputationAcceptOrDeclineMsg acceptMsg: mReputationRequestsAcceptedOrDeclined )
	    {
	    	if( acceptMsg.getAccept() )
	    	{
		    	boolean providedReputation = false;
		    	//then go through the reply messages to see if we got the reply for the request.
		    	for( ReputationReplyMsg replyMsg: reputationReplies )
		    	{
		    		if( acceptMsg.getTransactionID() == replyMsg.getTransactionID() )
		    		{
		    			providedReputation = true;
		    		}
		    	}
		    	
		    	if( !providedReputation )
		    	{
		    		//We need to find the era associated with this request
					for( ReputationRequestMsg requestMsg: mReputationsRequested )
					{
						if( requestMsg.getTransactionID() == acceptMsg.getTransactionID() )
						{
							mTrustNetwork.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
						}
					}
		    	}	    		
	    	}
	    }
	    
	    //Now go through the reputation replies and update our trust database.
    	for( ReputationReplyMsg replyMsg: reputationReplies )
    	{
    		mTrustNetwork.receiveAgentReputationUpdate(replyMsg.getSender(), replyMsg.getAppraiserID(), replyMsg.getEra(), replyMsg.getReputation() );
    	}
    	for( String name: agentNames )
    	{
        	for( Era era: eras )
        	{
        		updateDecisionTreeTrustValues(name, era);
        	}
    	}
    	
    	//Now handle the certainty requests.
    	//Go through each painting we are assigned and determine which agents we want to send certainty requests to
    	for( AppraisalAssignment appraisal: assignedPaintings)
    	{
    		for( String name: agentNames )
    		{
    			if( mDecisionTree.requestAgentCertainty(name, appraisal.getEra()) )
    			{
    		        CertaintyRequestMsg msg = new CertaintyRequestMsg( name, null, appraisal.getEra() );
    		        sendOutgoingMessage(msg); 
    		        mCertaintiesRequested.add(msg);
    		        mDecisionTree.sentCertaintyRequest(name, appraisal.getEra());
    			}
    		}
    	} 
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionCreationOrders()
	 */
	@Override
	public void prepareOpinionCreationOrders()
	{
		/* Assumptions for this behaviour
		 * 1. The agent requesting the opinion is the agent appraising the painting.
		 *    getSender() == getAppraisalAssignment().getAppraiser() - for trust purposes
		 * 2. Only one opinion will be generated per request.
		 * 3. Appraisal cost is solely based on information the DecisionTree has about the Sender Agent (and era)
		 */
        // Get opinion request confirmation messages from the message inbox
		List<OpinionRequestMsg> opinionRequests = getIncomingMessages();
		mOpinionRequests = new ArrayList<OpinionRequestMsg>(); 
		
		//First find out who didn't like our certainty values and didn't ask us for
		//an opinion
        for (CertaintyReplyMsg previousCertaintyMsg: mCertaintyReplysProvided)
        {
        	boolean acceptedCertainty = false;
	        for (OpinionRequestMsg receivedMsg: opinionRequests)
	        {
	        	//Check to see if the opinion message is the same as the certain message.
	        	if( receivedMsg.getTransactionID() == previousCertaintyMsg.getTransactionID() )
	        	{
	        		acceptedCertainty = true;
	        	}
	        }
	        if( !acceptedCertainty )
	        {
	        	//the agent we sent a certainty to, didn't continue with the opinion transaction, this might affect our internal trust reps.
	        	mTrustNetwork.agentDidNotAcceptCertainty(previousCertaintyMsg.getSender(), previousCertaintyMsg.getEra(), previousCertaintyMsg.getCertainty());
	        	updateDecisionTreeTrustValues(previousCertaintyMsg.getSender(), previousCertaintyMsg.getEra());
	        }
        }

        // Order an opinion (from the sim) for each opinion request message received
        for (OpinionRequestMsg receivedMsg: opinionRequests)
        {
            Era era = receivedMsg.getAppraisalAssignment().getEra();
            String fromAgent = receivedMsg.getSender();
            
            if( mDecisionTree.generateOpinion(fromAgent, era) )
            {
            	double appraisalCost = mDecisionTree.getAppraisalCost(fromAgent, era);
                OpinionOrderMsg msg = receivedMsg.opinionOrder(appraisalCost);
                sendOutgoingMessage(msg);
                mOpinionRequests.add(receivedMsg);
                
                //Tell our trust network we generated an opinion
                mTrustNetwork.generatedOpinion(fromAgent, receivedMsg.getAppraisalAssignment(), appraisalCost);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);             
                
            }
            else
            {
                //Tell our trust network we did not accept a certainty request
                mTrustNetwork.didNotProvideOpinionAfterPayment(fromAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);           	
            }        	
        }
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionProviderWeights()
	 */
	@Override
	public void prepareOpinionProviderWeights()
	{
        for (String agentToWeight: agentNames)
        {
            for (Era thisEra: eras)
            {
            	if( mDecisionTree.provideWeight(agentToWeight, thisEra) )
            	{
	            	double weight = mTrustNetwork.getReputationWeight(agentToWeight, thisEra);
	                WeightMsg msg = new WeightMsg(new Weight(getName(), agentToWeight, weight, thisEra.getName()));
	                sendOutgoingMessage(msg);
            	}
            }
        }  
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionReplies()
	 */
	@Override
	public void prepareOpinionReplies()
	{
		/* Assumptions for this behaviour
		 * 1. The agent requesting the opinion is the agent appraising the painting.
		 *    getSender() == getAppraisalAssignment().getAppraiser() - for trust purposes
		 * 2. DecisionTree may decide to adjust the appraisal value we got form the sim
		 */		
        for (OpinionRequestMsg receivedMsg: mOpinionRequests)
        {
        	String toAgent = receivedMsg.getSender();
        	Era  era = receivedMsg.getAppraisalAssignment().getEra();
        	
            Opinion op = findOpinion(receivedMsg.getTransactionID());
            int updateAppraisal = mDecisionTree.adjustAppraisalValue(toAgent, era, op.getAppraisedValue() );
            op.setAppraisedValue(updateAppraisal);
            // Use convenience method for generating an opinion reply message
            OpinionReplyMsg msg = receivedMsg.opinionReply(op);
            sendOutgoingMessage(msg);
            
            //Tell our trust network we generated an opinion
            mTrustNetwork.providedOpinion(toAgent, receivedMsg.getAppraisalAssignment(), updateAppraisal);
        	//The trust network may have updated its trust values based on this action,
        	//propagate the new values to our decision tree.
        	updateDecisionTreeTrustValues(toAgent, era);                         
        }

	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionRequests()
	 */
	@Override
	public void prepareOpinionRequests()
	{
		List<CertaintyReplyMsg> certaintyResponses = getIncomingMessages();
		
		//First go through and determine who didn't give us a certainty response.
		for( CertaintyRequestMsg requestMsg: mCertaintiesRequested )
		{
			boolean providedCertainty = false;
			for( CertaintyReplyMsg replyMsg: certaintyResponses )
			{
				if( replyMsg.getTransactionID() == requestMsg.getTransactionID() )
				{
					providedCertainty = true;
					break;
				}
			}
			if( !providedCertainty )
			{
				mTrustNetwork.agentDidNotProvideCertainty(requestMsg.getSender(), requestMsg.getEra());
			}
		}
		
		for( CertaintyReplyMsg replyMsg: certaintyResponses )
		{
			mDecisionTree.setAgentEraCertainty(replyMsg.getSender(), replyMsg.getEra(), replyMsg.getCertainty());
			mTrustNetwork.setAgentEraCertainty(replyMsg.getSender(), replyMsg.getEra(), replyMsg.getCertainty());
		}
		
		//perhaps changing the era certainties updated our trust network.
    	for( String name: agentNames )
    	{
        	for( Era era: eras )
        	{
        		updateDecisionTreeTrustValues(name, era);
        	}
    	}
    	
    	//Now handle the opinion requests.
    	//Go through each painting we are assigned and determine which agents we want to send opinion requests to
    	for( AppraisalAssignment appraisal: assignedPaintings)
    	{
    		for( String name: agentNames )
    		{
    			if( mDecisionTree.requestAgentOpinion(name, appraisal) )
    			{
    		        OpinionRequestMsg msg = new OpinionRequestMsg( name, null, appraisal );
    		        sendOutgoingMessage(msg); 
    		        mOpinionsRequested.add(msg);
    		        mDecisionTree.sentOpinionRequest(name, appraisal);
    			}
    		}
    	} 		
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationAcceptsAndDeclines()
	 */
	@Override
	public void prepareReputationAcceptsAndDeclines()
	{
		List<ReputationRequestMsg> reputationRequests = getIncomingMessages();
		mReputationRequestsToAccept = new ArrayList<ReputationRequestMsg>();
		
		
        for (ReputationRequestMsg receivedMsg: reputationRequests)
        {
        	String toAgent = receivedMsg.getSender();
        	String aboutAgent = receivedMsg.getAppraiserID();
        	Era  era = receivedMsg.getEra();
        	
        	if( mDecisionTree.respondToReputationRequest( toAgent, aboutAgent, era ) )
        	{
        		mReputationRequestsToAccept.add(receivedMsg);
        		ReputationAcceptOrDeclineMsg msg = receivedMsg.reputationAcceptOrDecline(true);
            	sendOutgoingMessage(msg);
            	
            	//Tell our trust network we accept the request
            	mTrustNetwork.providedAcceptReputationRequest(toAgent, aboutAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(toAgent, era);
        	}
        	else
        	{
        		ReputationAcceptOrDeclineMsg msg = receivedMsg.reputationAcceptOrDecline(false);
            	sendOutgoingMessage(msg);
            	//Tell our trust network we did not accept the request
            	mTrustNetwork.didNotProvideAcceptReputationRequest(toAgent, aboutAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(toAgent, era);            	
        	}        		
        }		
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationReplies()
	 */
	@Override
	public void prepareReputationReplies()
 {
		//Stored the accept/decline messages we received from OUR requests;
		mReputationRequestsAcceptedOrDeclined = getIncomingMessages();
		
		//determine who declined our request for reputations, used in prepareCertainties to update
		//reputations/trust
		for( ReputationAcceptOrDeclineMsg acceptMsg: mReputationRequestsAcceptedOrDeclined )
		{
			if( !acceptMsg.getAccept() )
			{
				for( ReputationRequestMsg requestMsg: mReputationsRequested )
				{
					if( requestMsg.getTransactionID() == acceptMsg.getTransactionID() )
					{
						mTrustNetwork.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
					}
				}
			}
		}

		//Now go through the reputation requests we accepted and generate the results.
        for (ReputationRequestMsg receivedMsg: mReputationRequestsToAccept)
        {
        	String toAgent = receivedMsg.getSender();
        	String aboutAgent = receivedMsg.getAppraiserID();
        	Era  era = receivedMsg.getEra();

        	if( mDecisionTree.provideReputationReply( toAgent, aboutAgent, era ) )
        	{
        		double repValue = mDecisionTree.getReputationRequestValue(toAgent,
        																  aboutAgent,
        																  era);
                ReputationReplyMsg msg = receivedMsg.reputationReply(repValue);
                sendOutgoingMessage(msg);
                
            	//Tell our trust network we provided a reputation and it's value
            	mTrustNetwork.providedReputationReply(toAgent, aboutAgent, era, repValue);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(toAgent, era);                
        	}
        	else
        	{
            	//Tell our trust network we accept the request
            	mTrustNetwork.didNotProvideReputationAfterPayment(toAgent, aboutAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(toAgent, era);       		
        	}
        }	
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareReputationRequests()
	 */
	@Override
	public void prepareReputationRequests()
	{
		/* Assumptions for this behaviour
		 * 1. Requesting an reputation update does not depend on the paintings we
		 *    are currently being asked to appraise.
		 */
		
		//This is the first method in a time step, update our trust values based on results from previous time step.
		processFrameResults();
		
		mReputationsRequested = new ArrayList<ReputationRequestMsg>();			
        for (String toAgent: agentNames)
        {
        	for( Era era: eras )
        	{
        		for( String aboutAgent: agentNames )
        		{
        			if( toAgent != aboutAgent && mDecisionTree.requestAgentReputationUpdate(toAgent, aboutAgent, era, currentTimestep) )
        			{
                        ReputationRequestMsg msg = new ReputationRequestMsg(toAgent, null, era, aboutAgent);
                        sendOutgoingMessage(msg);
                        mReputationsRequested.add(msg);
                        mDecisionTree.sentReputationRequest(toAgent, aboutAgent);
        			}
        		}
        	}
        }
	}
	
	private void updateDecisionTreeTrustValues( String toAgent, Era era )
	{
    	double ourNewTrust = mTrustNetwork.getTrustValue(toAgent, era);
    	double ourNewInferredTrust = mTrustNetwork.getInferredTrustValue(toAgent, era);
    	mDecisionTree.setAgentTrust(toAgent, era, ourNewTrust);
    	mDecisionTree.setAgentPerceivedTrust(toAgent, era, ourNewInferredTrust);		
	}

    // Use this method to sort through ordered opinions the sim delivers, by transactionID.
    private Opinion findOpinion(String _transactionID)
    {
        if (createdOpinions != null) {
            for (Opinion op: createdOpinions) {
                if (op.getTransactionID().equals(_transactionID)) {
                    return op;
                }
            }
        }
        return null;
    }

    private void processFrameResults( )
    {
    	List<OpinionReplyMsg> opinionReplies = getIncomingMessages();
    	
    	mDecisionTree.frameReset();
    	mTrustNetwork.frameReset();
    	
    	//There may have been era expertise change. Should we tell our decision tree about it?
    	
        if(finalAppraisals != null)
        {
        	for(Appraisal appraisal: finalAppraisals)
        	{
        	    //System.out.print("ID: " + appraisal.getPaintingID() + ", real: " + appraisal.getTrueValue());
        		for(OpinionReplyMsg msg: opinionReplies)
        		{
        			if(msg.getAppraisalAssignment().getPaintingID().equals(appraisal.getPaintingID()))
        			{
        				mTrustNetwork.updateAgentTrustFromFinalAppraisal(msg.getOpinion().getOpinionProvider(),
        						 										 appraisal, msg.getOpinion());
        			}
        		}
        	}
        }
    }
    
    public int getMaxCertaintyRequests()
    {
    	return super.maxNbCertaintyRequests;
    }
    
    public int getMaxOpinionRequests()
    {
    	return super.maxNbOpinionRequests;
    }
    
    public double getExpertise( String eraName )
    {
    	return myExpertiseValues.get(eraName);
    }
    
    public double getOpinionCost()
    {
    	return opinionCost;
    }
    
    public double getBankBalance()
    {
    	return bankBalance;
    }
    
    public double getEraCertainty( Era era )
    {
        return myExpertiseValues.get(era.getName());
    }
    
    abstract DecisionTree createDecisionTree();
    abstract TrustNetwork createTrustNetwork();
}
