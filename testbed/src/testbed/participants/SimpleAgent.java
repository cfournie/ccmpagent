package testbed.participants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import testbed.sim.Appraisal;
import testbed.sim.AppraisalAssignment;
import testbed.sim.Era;
import testbed.sim.Opinion;
import testbed.sim.Weight;

/**
 * An agent with a simple reputation model
 * 
 * @author Laurent Vercouter
 *
 */
public class SimpleAgent extends Agent {

	private Map<String,Double>          reputations;
	private Map<String,Map<Era,Double>> certainties;
    private List<ReputationRequestMsg>  reputationRequestsToAccept;
    private List<OpinionRequestMsg>     opinionRequests;

	@Override
	public void initializeAgent() {
		reputations = new Hashtable<String,Double>();
		for (String name: agentNames) {
			reputations.put(name,new Double(1.0));
		}
		certainties = new Hashtable<String,Map<Era,Double>>();
		
		// print my expertise (for debugging)
		String s = getName()+" expertise ";
		for (Era e: eras) {
		    s += String.format("%5.1f", 1-super.myExpertiseValues.get(e.getName()));
		}
		System.out.println(s);
	}

    /* 
     * --- --- --- --- --- --- --- --- --- 
     *         Reputation Protocol
     * --- --- --- --- --- --- --- --- --- 
     */
	
	@Override
	public void prepareReputationRequests() {
		//System.out.println("Timestep:" + currentTimestep);
		//for (String agent: reputations.keySet()) {
		//	System.out.println("- " + agent + ": " + reputations.get(agent));
		//}
	    
        // First collect the replies to the previous opinion transactions
        List<OpinionReplyMsg> opinionReplies = getIncomingMessages();
        if (finalAppraisals != null) {
        	for (Appraisal appraisal: finalAppraisals) {
        	    //System.out.print("ID: " + appraisal.getPaintingID() + ", real: " + appraisal.getTrueValue());
        		for (OpinionReplyMsg msg: opinionReplies) {
        			if (msg.getAppraisalAssignment().getPaintingID().equals(appraisal.getPaintingID())) {
            			double difference = Math.abs(appraisal.getTrueValue() - msg.getOpinion().getAppraisedValue());
            			difference = difference / ((double)appraisal.getTrueValue());
            			//System.out.println(", "+ msg.getOpinion().getOpinionProvider() + ": " + difference + " "+msg.getOpinion().getAppraisedValue());
             			double reputation = reputations.get(msg.getOpinion().getOpinionProvider()).doubleValue();
             			if (difference > 0.5) reputation = reputation - 0.03;
             			else reputation = reputation + 0.03;
             			if (reputation > 1) reputation = 1;
             			if (reputation < 0) reputation = 0;
             			reputations.put(msg.getOpinion().getOpinionProvider(), reputation);
        			}
        		}
        	}
        }
	}

	@Override
	public void prepareReputationAcceptsAndDeclines() {
		// first collect the reputation request messages
        List<ReputationRequestMsg> reputationRequests = getIncomingMessages();
        reputationRequestsToAccept = new ArrayList<ReputationRequestMsg>();

        // my agent accepts to answer to all the requests
        for (ReputationRequestMsg receivedMsg: reputationRequests) {
            reputationRequestsToAccept.add(receivedMsg);
            ReputationAcceptOrDeclineMsg msg = receivedMsg.reputationAcceptOrDecline(true);
            sendOutgoingMessage(msg);
        }
	}

	@Override
	public void prepareReputationReplies() {
		// first collect the reputation accept or decline messages
	    List<ReputationAcceptOrDeclineMsg> reputationAcceptsAndDeclines = getIncomingMessages();

        for (ReputationRequestMsg receivedMsg: reputationRequestsToAccept) {
            double repValue = reputations.get(receivedMsg.getAppraiserID());
            ReputationReplyMsg msg = receivedMsg.reputationReply(repValue);
            sendOutgoingMessage(msg);
        }
	}
	
	/* 
     * --- --- --- --- --- --- --- --- --- 
     *         Certainty Protocol
     * --- --- --- --- --- --- --- --- --- 
     */
   
	@Override
	public void prepareCertaintyRequests() {
		// first collect the replies to the previous reputation transactions
		// here they are not used, we don't care about the others' reputation model
	    List<ReputationReplyMsg> reputationReplies = getIncomingMessages();
	
        // ask certainty to the first n agents I do not know the certainty
	    int nSent = 0;
	    Collections.shuffle(agentNames); // shuffle the agents list to select other agents 
        for (String agentToAsk: agentNames) {
            if (!agentToAsk.equals(getName())) {
        		for (AppraisalAssignment assignment: assignedPaintings) {
        		    Map<Era,Double> agCert = certainties.get(agentToAsk); 
        		    if (agCert == null || !agCert.containsKey(assignment.getEra())) {
        		        //System.out.println(nSent+" ** sending cert request to "+agentToAsk+"/"+assignment.getEra() );
        		        CertaintyRequestMsg msg = new CertaintyRequestMsg(agentToAsk,null,assignment.getEra());
        		        sendOutgoingMessage(msg);
        		        nSent++;
        	            if (nSent >= super.maxNbCertaintyRequests)
        	                return;
        		    }
        		}
            }
        }
	}

	@Override
	public void prepareCertaintyReplies() {
		// first collect the opinion request messages
        List<CertaintyRequestMsg> certRequests = getIncomingMessages();
        for (CertaintyRequestMsg receivedMsg: certRequests) {
            // our agent is honest and give its real expertise as certainty
            String eraName = receivedMsg.getEra().getName();
            double myExpertise = myExpertiseValues.get(eraName);
            CertaintyReplyMsg msg = receivedMsg.certaintyReply(1-myExpertise);
            sendOutgoingMessage(msg);
        }
	}

	
    /* 
     * --- --- --- --- --- --- --- --- --- 
     *         Opinion Protocol
     * --- --- --- --- --- --- --- --- --- 
     */
    
	@Override
    public void prepareOpinionRequests() {
		// first collect the opinion request confirmation messages
	    List<CertaintyReplyMsg> opinionCertainties = getIncomingMessages();
	    for (CertaintyReplyMsg msg: opinionCertainties) {
            Map<Era,Double> agCert = certainties.get(msg.getSender());
            if (agCert == null) {
                agCert = new HashMap<Era,Double>();
                certainties.put(msg.getSender(), agCert);
            }
            agCert.put(msg.getEra(), msg.getCertainty());
	    }
	    

        // send opinion requests to the first n agents with minimum reputation
	    // (one opinion for assignment)
        for (AppraisalAssignment assignment: assignedPaintings) {
            int nSent = 0;
            for (String agentToAsk: agentNames) {
                if (!agentToAsk.equals(getName())) {
                    // only ask agents with a reputation more than 0.5
                    if (reputations.get(agentToAsk) > 0.5) {
                        OpinionRequestMsg msg = new OpinionRequestMsg(agentToAsk,null,assignment);
                        sendOutgoingMessage(msg);
                        nSent++;
                        if (nSent >= super.maxNbOpinionRequests)
                            return;
                        break; // go to the next assignment
                    }
                }
            }
        }        
    }        

	@Override
    public void prepareOpinionCreationOrders() {
        //Order an opinion for each OpinionRequestConfirmationMsg received
        opinionRequests = getIncomingMessages();
        for (OpinionRequestMsg receivedMsg: opinionRequests) {
            double cg;
            // if the requester is trusted we pay 80% for the opinion
            // otherwise we pay 0.01
            if (reputations.get(receivedMsg.getSender()) > 0.5)
            	cg = opinionCost * 0.8; // spend 80% of the opinion cost  
            else 
                cg = 0.01;
            OpinionOrderMsg msg = receivedMsg.opinionOrder(cg);
            sendOutgoingMessage(msg);
        }
    }

	@Override
	public void prepareOpinionProviderWeights() {
        for (String agentToWeight: agentNames) {
            if (!agentToWeight.equals(getName())) {
                // use of reputation
                double wt = reputations.get(agentToWeight);
                if (wt > 0.8) {
                    for (Era thisEra: eras) {
                        WeightMsg msg = new WeightMsg(new Weight(getName(),agentToWeight, wt, thisEra.getName()));
                        sendOutgoingMessage(msg);
                    }
                }
                
                /*// use of certainty
                Map<Era,Double> cert = certainties.get(agentToWeight); 
                for (Era thisEra: eras) {
                    if (cert != null && cert.get(thisEra) != null) {
                    	double wt = cert.get(thisEra).doubleValue();
                    	WeightMsg msg = new WeightMsg(new Weight(getName(),agentToWeight, wt, thisEra.getName()));
                    	sendOutgoingMessage(msg);
                    }
                }
                */
            }
        }
	}

	@Override
    public void prepareOpinionReplies() {
        for (OpinionRequestMsg receivedMsg: opinionRequests) {
            Opinion op = findOpinion(receivedMsg.getTransactionID());
            OpinionReplyMsg msg = receivedMsg.opinionReply(op);
            sendOutgoingMessage(msg);
        }
    }

    private Opinion findOpinion(String _transactionID) {
        for (Opinion op: createdOpinions) {
            if (op.getTransactionID().equals(_transactionID)) {
                return op;
            }
        }
        return null;
    }
}
