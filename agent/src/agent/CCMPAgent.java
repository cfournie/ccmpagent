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

	protected DecisionTree 						mDecisionTrees;
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
	 * Create the decision tree and trust network by calling the abstract function
	 * the specific type of CCMPAgent will then create their specific DT and TN
	 */
	public CCMPAgent()
	{
		super();
		mConfigInfo = new CCMPConfigInfo();	
		mLogging = false;
	}

	/**
	 * Create the decision tree and trust network by calling the abstract function
	 * the specific type of CCMPAgent will then create their specific DT and TN
	 * Then parse the config file using the param file string.
	 * @param paramFile
	 */
	public CCMPAgent(String paramFile)
	{
		super(paramFile);
		//use the passParam to add config file settings to the agent.xml
		parseConfigFile(paramFile);		
	}

	/** 
	 * Initialize the CCMPAgent by setting up the logger (agentName_log.txt)
	 * Call init on the DT and TN, then add all the agents to the TN.
	 * Since you've initialized the TN with default values, you should pass this to the DT
	 * @see testbed.agent.Agent#initializeAgent()
	 */
	@Override
	public void initializeAgent()
	{
		initLogging();
	    mLogger.warning("Creating DT and TN");
	    try
	    {
			mDecisionTrees = createDecisionTree();
	    }
	    catch(Exception e)
	    {
	    	mLogger.warning("Failed to create DT");
	    	mLogger.warning(e.toString());	    	
	    }
	    
	    try
	    {
			mTrustNetwork = createTrustNetwork();
	    }
	    catch(Exception e)
	    {
	    	mLogger.warning("Failed to create TN");
	    	mLogger.warning(e.toString());	    		    	
	    }	    
		
	    //Call init on the DT and TN, this creates the internal structures required 
	    //within these classes.
	    mLogger.warning("Initializing CCMPAgent");
        mDecisionTrees.init();
        mTrustNetwork.init();
        
        //Add all the agents to the TN and DT.
        //Since we've got default trust values in TN, pass the values
        //To the DT.
        for (String name: agentNames)
        {
        	mTrustNetwork.addAgent(name);
        	mDecisionTrees.addAgent(name);
        	for( Era era: eras )
        	{
        		updateDecisionTreeTrustValues(name, era);
        	}
        }

	}

	/** 
	 * Parse the incoming certainty requests from other agents.
	 * Ask the DT if we should respond to these requests, if so generate the messages.
	 * The TN (and DT?) may want to know that we did this, therefore pass the info to it.
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
            
            //Ask the DT if we should provide a certainty reply to this agent.
            if( mDecisionTrees.provideCertaintyReply(fromAgent, era) )
            {
            	//Get the value from the DT that we should send, it could be our actual value
            	//Or a value we change based on our trust and behaviour to that agent.
            	double myExpertise = mDecisionTrees.getCertaintyRequestValue(fromAgent, era);
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

	/**
	 * Parse the incoming reputation replies, based on our requests to other agents.
	 * Determine if an agent didn't send a response, even after we paid them and update the TN/DT
	 * Update the TN with the new values. Pass those new values to the DT
	 * Generate the certainty request messages by going through all the era we have paintings
	 * in then for each agent, ask the DT is we should request a message from that agent.
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
	    //go through each accept msg we stored previously and make sure there was a reply
		//if the agent didn't respond, even after we paid them..then tell the DT/TN about
		//the negative interaction.
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
		    	//We never got a reponse for our request/payment.
		    	if( !providedReputation )
		    	{
		    		//We need to find the era associated with this request
					for( ReputationRequestMsg requestMsg: mReputationsRequested )
					{
						if( requestMsg.getTransactionID() == acceptMsg.getTransactionID() )
						{
							mLogger.info("\t Agent did not accept reputation request agent="+acceptMsg.getSender()+" era="+requestMsg.getEra());
							mTrustNetwork.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
							mDecisionTrees.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
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
    	//For each era we have paintings for, go through the list of agents and ask the DT
		//if we should request a certainty.  The DT is responsible for keeping track of the
		//number of messages sent and the maximum number of messages we are allowed to send.
    	for( Era era: mCurrentEras )
    	{
	    	mLogger.info("\t For era="+era);
	   		for( String name: agentNames )
	   		{
	   			//Don't send a message to ourselves,
	   			if( name != getName() && mDecisionTrees.requestAgentCertainty(name, era ) )
	   			{
	   				mLogger.info("\t\t to="+name);
	   		        CertaintyRequestMsg msg = new CertaintyRequestMsg( name, null, era );
	   		        sendOutgoingMessage(msg); 
	   		        mCertaintiesRequested.add(msg);
	   		        mDecisionTrees.sentCertaintyRequest(name, era);
	   			}
	   		}
    	}
	}

	/**
	 * Parse the incoming request messages from the other agents.
	 * First determine if an agent didn't request an opinion, after we sent them our 
	 * certainty, tell this to the TN (and DT?) and update the trust values.
	 * Then go through all the opinion requests and ask the DT whether we should generate an opinion
	 * creation messages.  If we do create one, ask the DT how much we should spend on the creation order.
	 * Then tell the TN (and DT?) that we did or did not fulfill the opinion request.
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
	        	mDecisionTrees.agentDidNotAcceptCertainty(previousCertaintyMsg.getSender(), previousCertaintyMsg.getEra(), previousCertaintyMsg.getCertainty());
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
			//For each painting, create a request with twice the cost.
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
            
            //Ask the DT if we should generate an opinion for the agent.
            if( mDecisionTrees.generateOpinion(fromAgent, era) )
            {
            	//Ask the DT how much we should spend on the creation.
            	double appraisalCost = mDecisionTrees.getAppraisalCost(fromAgent, era);
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

	/**
	 * For each agent and era get the weight from the decision tree and generate a 
	 * weight message to the sim.
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
            	if( mDecisionTrees.provideWeight(agentToWeight, thisEra) )
            	{
	            	double weight = mTrustNetwork.getReputationWeight(agentToWeight, thisEra);
	                WeightMsg msg = new WeightMsg(new Weight(getName(), agentToWeight, weight, thisEra.getName()));
	                sendOutgoingMessage(msg);
            		mLogger.info("\t who="+agentToWeight+" era="+thisEra+" weight="+weight);
            	}
            }
        }  
	}

	/**
	 * Go through the list of opinions we got back from the Sim and ask the DT
	 * how much we should adjust the value (if we want to be mean to people we don't like)
	 * Then generate a opinion reply message.
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
            //Ask the DT how much we should adjust the appraisal value by.
            int updateAppraisal = mDecisionTrees.adjustAppraisalValue(toAgent, era, op.getAppraisedValue() );
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

	/**
	 * Parse the incoming certainty reply messages, from our requests for certainties.
	 * Determine if an agent did not provide a certainty reply and tell the TN (and DT)
	 * Update the certainty values in the TN and DT and then the trust values.
	 * Go through all the paintings we were assigned and then for each agent ask the DT
	 * whether we should ask them for an opinion.
	 * 
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
				mDecisionTrees.agentDidNotProvideCertainty(requestMsg.getSender(), requestMsg.getEra());
			}
		}
		
		//From the received messages, update the certainty values.
		for( CertaintyReplyMsg replyMsg: certaintyResponses )
		{
			mLogger.info("\tfrom="+replyMsg.getSender()+" era="+replyMsg.getEra()+" certainty="+replyMsg.getCertainty());
			mDecisionTrees.setAgentEraCertainty(replyMsg.getSender(), replyMsg.getEra(), replyMsg.getCertainty());
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
    			//we can't ask ourselves for an opinion.
    			if( name != getName() &&
    				mDecisionTrees.requestAgentOpinion(name, appraisal) )
    			{
    				mLogger.info("\t\t to="+name);
    		        OpinionRequestMsg msg = new OpinionRequestMsg( name, null, appraisal );
    		        sendOutgoingMessage(msg); 
    		        mOpinionsRequested.add(msg);
    		        mDecisionTrees.sentOpinionRequest(name, appraisal);
    			}
    		}
    	} 		
	}

	/**
	 * Parse the incoming reputation request messages, from other Agents.
	 * Ask the DT whether we should response to the requesting agent, about another agent and era.
	 * Create the appropriate message and then tell the TN (and DT) that responded.
	 * @see testbed.agent.Agent#prepareReputationAcceptsAndDeclines()
	 */
	@Override
	public void prepareReputationAcceptsAndDeclines()
	{
		List<ReputationRequestMsg> reputationRequests = getIncomingMessages();
		mReputationRequestsToAccept = new ArrayList<ReputationRequestMsg>();
		
		mLogger.info("T="+currentTimestep+": Prepare reputation accept/decline");
		//Go through each reputation request received...
        for (ReputationRequestMsg receivedMsg: reputationRequests)
        {
        	String toAgent = receivedMsg.getSender();
        	String aboutAgent = receivedMsg.getAppraiserID();
        	Era  era = receivedMsg.getEra();
        	
        	//Ask the DT if we should respond to this request
        	if( mDecisionTrees.respondToReputationRequest( toAgent, aboutAgent, era ) )
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

	/**
	 * Parse the incoming reputation requests accept/decline messages, coming
	 * from our requests for reputations.
	 * Determine if an agent didn't accept our request, and tell the TN (and DT) about it
	 * Then, go through the reputation accept/decline messages we generated in the last function
	 * and ask the DT whether we should actually generate a reputation.
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
						mDecisionTrees.agentDidNotAcceptReputationRequest(acceptMsg.getSender(), requestMsg.getEra());						
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
		//Now go through the reputation requests we accepted in the last method and generate the results.
        for (ReputationRequestMsg receivedMsg: mReputationRequestsToAccept)
        {
        	String toAgent = receivedMsg.getSender();
        	String aboutAgent = receivedMsg.getAppraiserID();
        	Era  era = receivedMsg.getEra();

        	//Ask the DT whether we should accept the response.
        	if( mDecisionTrees.provideReputationReply( toAgent, aboutAgent, era ) )
        	{
        		double repValue = mDecisionTrees.getReputationRequestValue(toAgent,
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

	/**
	 * Process the results from the last frame (final appraisal values etc)
	 * Go through each era and agent and ask the DT whether we should ask for a reputation
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
		//Go through each era and then each to and about agent and ask the DT whether we should ask
		//an agent about another agent, wrt an era.
        for (Era era: mCurrentEras)
        {
    		mLogger.info("\t Era="+era);
        	for( String toAgent: agentNames )
        	{
        		for( String aboutAgent: agentNames )
        		{
        			if( toAgent != getName() &&
        				toAgent != aboutAgent &&
        				mDecisionTrees.requestAgentReputationUpdate(toAgent, aboutAgent, era, currentTimestep) )
        			{
        				mLogger.info("\t\t to="+toAgent+" about="+aboutAgent);
                        ReputationRequestMsg msg = new ReputationRequestMsg(toAgent, null, era, aboutAgent);
                        sendOutgoingMessage(msg);
                        mReputationsRequested.add(msg);
                        mDecisionTrees.sentReputationRequest(toAgent, aboutAgent);
        			}
        		}
        	}
        }
	}

	/**
	 * For the given agent and era, get the new trust and inferred trust
	 * from the TN and pass it to the DT.
	 */	
	private void updateDecisionTreeTrustValues( String toAgent, Era era )
	{
    	double ourNewTrust = mTrustNetwork.getTrustValue(toAgent, era);
    	double ourNewInferredTrust = mTrustNetwork.getInferredTrustValue(toAgent, era);
    	mDecisionTrees.setAgentTrust(toAgent, era, ourNewTrust);
    	mDecisionTrees.setAgentPerceivedTrust(toAgent, era, ourNewInferredTrust);		
	}

	/**
	 * Use this method to sort through ordered opinions the sim delivers, by transactionID.
	 */	
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

    /**
	 * Reset the DT and TN for the frame.  Determine which eras we have paintings for in this frame
	 * Parse through the opinion reply messages we received for the last frame.
	 * Determine if an agent did not respond to us and tell the TN (and DT?) about the negative experience.
	 * Parse through the final appraisal values and pass the final appraisal and each agents opinion
	 * to the TN to update the trust values based on the experiences.
	 */	
    private void processFrameResults( )
    {
    	mLogger.info("Begin Frame: "+currentTimestep);

    	//Reset the DT and TN (counters etc)
    	mDecisionTrees.frameReset();
    	mTrustNetwork.frameReset();

    	//Determine which eras we have paintings for in this round.
		mCurrentEras = new ArrayList<Era>();		
    	for( AppraisalAssignment appraisal: assignedPaintings)
    	{
    		if( !mCurrentEras.contains(appraisal.getEra()) )
    		{
    			mCurrentEras.add(appraisal.getEra());
    		}
    	}     	

    	//If we're not in the first round, go through the final appraisal values
    	//and update the TN based on the results from each agents opinion.
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
					mDecisionTrees.agentDidNotProvideOpinion(requestMsg.getSender(), requestMsg.getAppraisalAssignment().getEra());
				}
			}
			    	
	    	
	        if(finalAppraisals != null)
	        {
	        	//Go through the final appraisals
	        	for(Appraisal appraisal: finalAppraisals)
	        	{
	            	mLogger.info("Received final appraisal: ID="+appraisal.getPaintingID()+" Value="+appraisal.getTrueValue());	        		
	        		for(OpinionReplyMsg msg: opinionReplies)
	        		{
	        			//Match the final appraisal to a received opinion reply.
	        			if(msg.getAppraisalAssignment().getPaintingID().equals(appraisal.getPaintingID()))
	        			{
	        				//Update the TN based on the opinion reply and final appraisal.
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
    
    protected void initLogging()
    {
		//Setup the logger
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
    }
    
    abstract DecisionTree createDecisionTree();
    abstract TrustNetwork createTrustNetwork();
}
