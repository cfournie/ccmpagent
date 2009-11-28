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
import java.util.logging.*;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.apache.commons.digester.Digester;

import agent.CCMPConfigInfo;

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
    private List<OpinionRequestMsg>         	mOpinionsCreated;
    private List<ReputationRequestMsg>  		mReputationsRequested;
    private List<ReputationAcceptOrDeclineMsg>	mReputationRequestsAcceptedOrDeclined;
    private List<CertaintyRequestMsg>			mCertaintiesRequested;
    private List<OpinionRequestMsg>				mOpinionsRequested;
    private List<Era> 							mCurrentEras;
    protected Logger							mLogger;
    protected Digester							mDigester;
    protected CCMPConfigInfo					mConfigInfo;
    boolean										mLogging;
	
	/**
	 * 
	 */
	public CCMPAgent()
	{		
		mDecisionTree = createDecisionTree();
		mTrustNetwork = createTrustNetwork();
	
		//We shouldn't have to call this here, and use the
		//pass param argument..but that doesn't seem to work with JAR
		//I get an missing method exception.
		//So we'll do it ourselves.
		parseConfigFile(getConfigFile());
	}

	/**
	 * @param paramFile
	 */
	public CCMPAgent(String paramFile)
	{
		super(paramFile);
		
		//This should work, but it doesn't
		//if we set passParam to true, we get a missing method function error..wtf?
		parseConfigFile(paramFile);
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#initializeAgent()
	 */
	@Override
	public void initializeAgent()
	{
	    try
	    {
	        boolean append = false;
	        mLogger = Logger.getLogger(getName());
	        
	        if( mLogging )
	        {
		        FileHandler fh = new FileHandler(getName()+"_log.txt", append);
		        fh.setFormatter(new Formatter() {
		            public String format(LogRecord rec) {
		               StringBuffer buf = new StringBuffer(1000);
		               buf.append(formatMessage(rec));
		               buf.append('\n');
		               return buf.toString();
		               }
		             });		        
	        	mLogger.addHandler(fh);
	        }
	       	mLogger.setUseParentHandlers(false);
	    }
	    catch (IOException e)
	    {
	    	 e.printStackTrace();
        }
		
	    mLogger.warning("Initializing CCMPAgent");
        mDecisionTree.init();
        mTrustNetwork.init();
        
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
		
		mLogger.info("T="+currentTimestep+": Prepare Certainty Replies");
				
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
            	mLogger.info("\t to="+fromAgent+" era="+era+" certainty="+myExpertise);
            }
            else
            {
                //Tell our trust network we did not accept a certainty request
                mTrustNetwork.didNotAcceptCertaintyRequest(fromAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);
            	mLogger.info("\t CCMP did not accept certainty request from agent="+fromAgent+" era="+era);
            }
        }
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareCertaintyRequests()
	 */
	@Override
	public void prepareCertaintyRequests()
	{
		// first collect the replies to the previous reputation transactions
	    List<ReputationReplyMsg> reputationReplies = getIncomingMessages();
	    mCertaintiesRequested = new ArrayList<CertaintyRequestMsg>();

		mLogger.info("T="+currentTimestep+" Parse Reputation Returns:");		
	    
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
							mLogger.info("\t Agent did not accept reputation request agent="+acceptMsg.getSender()+" era="+requestMsg.getEra());
							mTrustNetwork.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
						}
					}
		    	}	    		
	    	}
	    }
	    
	    //Now go through the reputation replies and update our trust database.
    	for( ReputationReplyMsg replyMsg: reputationReplies )
    	{
    		mLogger.info("\t from="+replyMsg.getSender()+" about="+replyMsg.getAppraiserID()
    				                    +" era="+replyMsg.getEra()+ " reputation="+replyMsg.getReputation());
    		mTrustNetwork.receiveAgentReputationUpdate(replyMsg.getSender(), replyMsg.getAppraiserID(), replyMsg.getEra(), replyMsg.getReputation() );
    	}
    	for( String name: agentNames )
    	{
        	for( Era era: eras )
        	{
        		updateDecisionTreeTrustValues(name, era);
        	}
    	}
    	
		mLogger.info("T="+currentTimestep+" Prepare Certainty Requests:");		    	
    	
    	//Now handle the certainty requests.
    	//Go through each painting we are assigned get the list of eras we have to evaluate:    			
    	for( Era era: mCurrentEras )
    	{
	    	mLogger.info("\t For era="+era);
	   		for( String name: agentNames )
	   		{
	   			if( name != getName() && mDecisionTree.requestAgentCertainty(name, era ) )
	   			{
	   				mLogger.info("\t\t to="+name);
	   		        CertaintyRequestMsg msg = new CertaintyRequestMsg( name, null, era );
	   		        sendOutgoingMessage(msg); 
	   		        mCertaintiesRequested.add(msg);
	   		        mDecisionTree.sentCertaintyRequest(name, era);
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
		mOpinionsCreated = new ArrayList<OpinionRequestMsg>(); 
		
		mLogger.info("T="+currentTimestep+" Parse Opinion Requests:");		
		//First find out who didn't like our certainty values and didn't ask us for
		//an opinion
		int numAccepted = 0;
		int numDeclined = 0;
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
	        	mLogger.info("\t "+previousCertaintyMsg.getSender()+" did not request opinion after certainty era="
	        			          +previousCertaintyMsg.getEra()+" certainty="+previousCertaintyMsg.getCertainty());
	        	//the agent we sent a certainty to, didn't continue with the opinion transaction, this might affect our internal trust reps.
	        	mTrustNetwork.agentDidNotAcceptCertainty(previousCertaintyMsg.getSender(), previousCertaintyMsg.getEra(), previousCertaintyMsg.getCertainty());
	        	updateDecisionTreeTrustValues(previousCertaintyMsg.getSender(), previousCertaintyMsg.getEra());
	        	numDeclined++;
	        }
	        else
	        {
	        	numAccepted++;
	        }
        }
        mLogger.info("\t numReceived="+opinionRequests.size()+" numAccepted="+numAccepted+" numDeclined="+numDeclined);

		mLogger.info("T="+currentTimestep+" Prepare Opinion Orders:");	
		
		//First generate an opinion request for ourselves.
		if( !denyUseOfSelfOpinions )
		{
	    	for( AppraisalAssignment appraisal: assignedPaintings)
	    	{	
	            OpinionOrderMsg msg = new OpinionOrderMsg(null, appraisal, getOpinionCost()*2);
	            msg.setAppraisalAssignment(appraisal);
	            sendOutgoingMessage(msg);
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
                
                mOpinionsCreated.add(receivedMsg);
                
                //Tell our trust network we generated an opinion
                mTrustNetwork.generatedOpinion(fromAgent, receivedMsg.getAppraisalAssignment(), appraisalCost);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);             

            	mLogger.info("\t to="+fromAgent+" era="+era+" appraisalCost="+appraisalCost+" appraisalId="+receivedMsg.getAppraisalAssignment().getPaintingID());               
            }
            else
            {
                //Tell our trust network we did not accept a certainty request
                mTrustNetwork.didNotProvideOpinionAfterPayment(fromAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(fromAgent, era);   
            	
            	mLogger.info("\t CCMP don't provide opinion after payment to="+fromAgent+" era="+era);
            }        	
        }
	}

	/* (non-Javadoc)
	 * @see testbed.agent.Agent#prepareOpinionProviderWeights()
	 */
	@Override
	public void prepareOpinionProviderWeights()
	{
		mLogger.info("T="+currentTimestep+" Prepare Opinion Provider Weights:");			
        for (String agentToWeight: agentNames)
        {
            for (Era thisEra: eras)
            {
            	if( mDecisionTree.provideWeight(agentToWeight, thisEra) )
            	{
	            	double weight = mTrustNetwork.getReputationWeight(agentToWeight, thisEra);
	                WeightMsg msg = new WeightMsg(new Weight(getName(), agentToWeight, weight, thisEra.getName()));
	                sendOutgoingMessage(msg);
            		mLogger.info("\t who="+agentToWeight+" era="+thisEra+" weight="+weight);
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
		mLogger.info("T="+currentTimestep+" Prepare Opinion Replies:");			
        for (OpinionRequestMsg receivedMsg: mOpinionsCreated)
        {
        	String toAgent = receivedMsg.getSender();
        	Era  era = receivedMsg.getAppraisalAssignment().getEra();
        	
            Opinion op = findOpinion(receivedMsg.getTransactionID());
            int updateAppraisal = mDecisionTree.adjustAppraisalValue(toAgent, era, op.getAppraisedValue() );
            op.setAppraisedValue(updateAppraisal);
            // Use convenience method for generating an opinion reply message
            OpinionReplyMsg msg = receivedMsg.opinionReply(op);
            sendOutgoingMessage(msg);
            
            mLogger.info("\t to="+toAgent+" simValue="+op.getAppraisedValue()+" sentValue="+updateAppraisal+" era="+era);
            
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
		
		mLogger.info("T="+currentTimestep+": Parse certainty reply messages:");		
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
				mLogger.info("\t Agent Did not provide certainty agent="+requestMsg.getSender()+" era="+requestMsg.getEra());
				mTrustNetwork.agentDidNotProvideCertainty(requestMsg.getSender(), requestMsg.getEra());
			}
		}
		
		for( CertaintyReplyMsg replyMsg: certaintyResponses )
		{
			mLogger.info("\tfrom="+replyMsg.getSender()+" era="+replyMsg.getEra()+" certainty="+replyMsg.getCertainty());
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
    	
    	mOpinionsRequested = new ArrayList<OpinionRequestMsg>();
    	
		mLogger.info("T="+currentTimestep+": Prepare Opinion Requests:");		    	
    	//Now handle the opinion requests.
    	//Go through each painting we are assigned and determine which agents we want to send opinion requests to
    	for( AppraisalAssignment appraisal: assignedPaintings)
    	{
			mLogger.info("\t For Appraisal="+appraisal.getPaintingID()+" era="+appraisal.getEra());

    		for( String name: agentNames )
    		{
    			if( name != getName() &&
    				mDecisionTree.requestAgentOpinion(name, appraisal) )
    			{
    				mLogger.info("\t\t to="+name);
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
		
		mLogger.info("T="+currentTimestep+": Prepare reputation accept/decline");
        for (ReputationRequestMsg receivedMsg: reputationRequests)
        {
        	String toAgent = receivedMsg.getSender();
        	String aboutAgent = receivedMsg.getAppraiserID();
        	Era  era = receivedMsg.getEra();
        	
        	if( mDecisionTree.respondToReputationRequest( toAgent, aboutAgent, era ) )
        	{
        		mLogger.info("\taccept to="+toAgent+" about="+aboutAgent+" era="+era);
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
        		mLogger.info("\tdecline to="+toAgent+" about="+aboutAgent+" era="+era);        		
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
		
		mLogger.info("T="+currentTimestep+" Parse returned reputation accept/decline:");
		
		//determine who declined our request for reputations, used in prepareCertainties to update
		//reputations/trust
		int numAccepted = 0;
		int numDeclined = 0;
		for( ReputationAcceptOrDeclineMsg acceptMsg: mReputationRequestsAcceptedOrDeclined )
		{
			if( !acceptMsg.getAccept() )
			{
				for( ReputationRequestMsg requestMsg: mReputationsRequested )
				{
					if( requestMsg.getTransactionID() == acceptMsg.getTransactionID() )
					{
        				mLogger.info("\t agent declined rep request: from="+acceptMsg.getSender()+" about="+requestMsg.getAppraiserID()+" era="+requestMsg.getEra());
						mTrustNetwork.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
					}
				}
			}
			else
			{
				numAccepted++;
			}
		}
		mLogger.info("\t num received replies="+mReputationRequestsAcceptedOrDeclined.size()+
				     " num accepted="+numAccepted+" num declined="+numDeclined);
		
		mLogger.info("T="+currentTimestep+" Prepare reputation replies");
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
            	
            	mLogger.info("\tto="+toAgent+" about="+aboutAgent+" rep="+repValue);
        	}
        	else
        	{
            	//Tell our trust network we accept the request
            	mTrustNetwork.didNotProvideReputationAfterPayment(toAgent, aboutAgent, era);
            	//The trust network may have updated its trust values based on this action,
            	//propagate the new values to our decision tree.
            	updateDecisionTreeTrustValues(toAgent, era); 
            	
            	mLogger.info("\tignored to="+toAgent+" about="+aboutAgent);
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
		
		mLogger.info("T="+currentTimestep+": Prepare Reputation Requests: ");
		
		mReputationsRequested = new ArrayList<ReputationRequestMsg>();			
        for (Era era: mCurrentEras)
        {
    		mLogger.info("\t Era="+era);
        	for( String toAgent: agentNames )
        	{
        		for( String aboutAgent: agentNames )
        		{
        			if( toAgent != getName() &&
        				toAgent != aboutAgent &&
        				mDecisionTree.requestAgentReputationUpdate(toAgent, aboutAgent, era, currentTimestep) )
        			{
        				mLogger.info("\t\t to="+toAgent+" about="+aboutAgent);
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
    	mLogger.info("Begin Frame: "+currentTimestep);

    	mDecisionTree.frameReset();
    	mTrustNetwork.frameReset();
    	
		mCurrentEras = new ArrayList<Era>();		
    	for( AppraisalAssignment appraisal: assignedPaintings)
    	{
    		if( !mCurrentEras.contains(appraisal.getEra()) )
    		{
    			mCurrentEras.add(appraisal.getEra());
    		}
    	}     	
    	
    	if( currentTimestep != 0 )
    	{
    		List<OpinionReplyMsg> opinionReplies = getIncomingMessages();
    		mLogger.info("T="+currentTimestep+": Parse opinion reply messages:");		
			//First go through and determine who didn't give us a certainty response.
			for( OpinionRequestMsg requestMsg: mOpinionsRequested )
			{
				boolean providedOpinion = false;
				for( OpinionReplyMsg replyMsg: opinionReplies )
				{
					if( replyMsg.getTransactionID() == requestMsg.getTransactionID() )
					{
						providedOpinion = true;
						break;
					}
				}
				if( !providedOpinion )
				{
					mLogger.info("\t Agent Did not provide opinion agent="+requestMsg.getSender()
							    +" era="+requestMsg.getAppraisalAssignment().getEra());
					mTrustNetwork.agentDidNotProvideOpinion(requestMsg.getSender(), requestMsg.getAppraisalAssignment().getEra());
				}
			}
			    	
	    	
	        if(finalAppraisals != null)
	        {
	        	for(Appraisal appraisal: finalAppraisals)
	        	{
	            	mLogger.info("Received final appraisal: ID="+appraisal.getPaintingID()+" Value="+appraisal.getTrueValue());
	        		
	        	    //System.out.print("ID: " + appraisal.getPaintingID() + ", real: " + appraisal.getTrueValue());
	        		for(OpinionReplyMsg msg: opinionReplies)
	        		{
	        			if(msg.getAppraisalAssignment().getPaintingID().equals(appraisal.getPaintingID()))
	        			{
	            			mLogger.info("\t from="+msg.getOpinion().getOpinionProvider()+" opinion="+msg.getOpinion().getAppraisedValue());
	        				mTrustNetwork.updateAgentTrustFromFinalAppraisal(msg.getOpinion().getOpinionProvider(),
	        						 										 appraisal, msg.getOpinion());
	        			}
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
    
    public List<Era> getEras()
    {
    	return eras;
    }
    
    public List<AppraisalAssignment> getAppraisalAssignments()
    {
    	return assignedPaintings;
    }
    
    public String getConfigFile()
    {
    	return "CCMPAgent_Config.xml";
    }
    
    protected void parseConfigFile( String paramFile )
    {
		mConfigInfo = new CCMPConfigInfo();
        try
        {
            mDigester = new Digester();            
            mDigester.push(mConfigInfo);            
            mDigester.addCallMethod("agentConfig/CCMPParams/log", "setLogging", 0); 
            mDigester.parse(paramFile);            
        } catch (IOException e1) {
          System.out.println("File not found exception: " + paramFile);
          System.out.println(e1);
        } catch (SAXException e2) {
          System.out.println("Error parsing file: " + paramFile);
          System.out.println(e2);
        }
        
        mLogging = mConfigInfo.getLogging();
    }
    
    public void writeToLogFile( String toLog )
    {
    	if( mLogging )
    	{
    		mLogger.info(toLog);
    	}
    }
    
    public CCMPConfigInfo getConfigInfo()
    {
    	return mConfigInfo;
    }
    
    abstract DecisionTree createDecisionTree();
    abstract TrustNetwork createTrustNetwork();
}
