import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;

import java.util.List;

/**
 * ExampleAgent returns the bid that maximizes its own utility for half of the negotiation session.
 * In the second half, it offers a random bid. It only accepts the bid on the table in this phase,
 * if the utility of the bid is higher than Example Agent's last bid.
 */
public class LDAgent extends AbstractNegotiationParty {
    private final String description = "LDAgent";

    private Bid lastReceivedOffer; // offer on the table
    private Bid myLastOffer;
    private Bid OfferStore[];
    public GeneticAlgorithm GA;
    public int index;
    @Override
    public void init(NegotiationInfo info) {
        super.init(info);
        OfferStore = new Bid[10];
        //产生10个随机的bid
        for (int i=0;i<10;i++) {
        		OfferStore[i] = generateRandomBid();
        }
        index = 9;
        GA = new GeneticAlgorithm();
        GA.ipop = new double[10];
        for (int i=0;i<10;i++) {
        		GA.ipop[i] = Math.random();
        }
    }
    
    /**
     * When this function is called, it is expected that the Party chooses one of the actions from the possible
     * action list and returns an instance of the chosen action.
     *
     * @param list
     * @return
     */
    @Override
    public Action chooseAction(List<Class<? extends Action>> list) {
        // According to Stacked Alternating Offers Protocol list includes
        // Accept, Offer and EndNegotiation actions only.
        double time = getTimeLine().getTime(); // Gets the time, running from t = 0 (start) to t = 1 (deadline).
                                               // The time is normalized, so agents need not be
                                               // concerned with the actual internal clock.


        // First half of the negotiation offering the max utility (the best agreement possible) for Example Agent
        if(time <0) {
        		if (index==9)
        		{
        			generateBidWithUPDOWNUtilityOften(0.9, 1);
        			index=-1;
        		}
            index++;
            return new Offer(this.getPartyId(),OfferStore[index]);
        } 
    		else {

            // Accepts the bid on the table in this phase,
            // if the utility of the bid is higher than Example Agent's last bid.
            if (lastReceivedOffer != null
                && myLastOffer != null
                && this.utilitySpace.getUtility(lastReceivedOffer) > this.utilitySpace.getUtility(myLastOffer)) {

                return new Accept(this.getPartyId(), lastReceivedOffer);
            } else {
                // Offering a random bid
               // myLastOffer = generateRandomBid();
            		//double MaxUtility = getUtility(this.getMaxUtilityBid());
           
                    GA.select();
                    double uti = 0;
                    int i=0;
                    while(i<10)
                    {
                        if(GA.ipop[i]>uti)
                            uti = GA.ipop[i];
                        i++;
                    }

            		myLastOffer = generateBidWithUPDOWNUtility(uti-0.1,uti+0.1);
                return new Offer(this.getPartyId(), myLastOffer);
            }
        }
    }
    
    /**
     * This method is called to inform the party that another NegotiationParty chose an Action.
     * @param sender
     * @param act
     */
    @Override
    public void receiveMessage(AgentID sender, Action act) {
        super.receiveMessage(sender, act);

        if (act instanceof Offer) { // sender is making an offer
            Offer offer = (Offer) act;

            // storing last received offer
            lastReceivedOffer = offer.getBid();
        }
    }

    /**
     * A human-readable description for this party.
     * @return
     */
    @Override
    public String getDescription() {
        return description;
    }

    private Bid getMaxUtilityBid() {
        try {
            return this.utilitySpace.getMaxUtilityBid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //产生一个具有上下限的utility
    public Bid generateBidWithUPDOWNUtility(double utilityDownThreshold,double utilityUpThreshold) {
        Bid randomBid;
        double utility;
        do {
            randomBid = generateRandomBid();
            try {
                utility = utilitySpace.getUtility(randomBid);
            } catch (Exception e)
            {
                utility = 0.0;
            }
        }
        while (utility < utilityDownThreshold || utility > utilityUpThreshold);
        return randomBid;
    }
    public void generateBidWithUPDOWNUtilityOften(double utilityDownThreshold,double utilityUpThreshold) {
        Bid randomBid;
        double utility;
        for (int i=0;i<10;i++){
	        do {
		            randomBid = generateRandomBid();
		            try {
		                utility = utilitySpace.getUtility(randomBid);
		            } catch (Exception e)
		            {
		                utility = 1;
		            }
	        }while (utility < utilityDownThreshold || utility > utilityUpThreshold);
	        
	        OfferStore[i] = randomBid; 
        }
    }

}