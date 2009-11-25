package trust;

import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

public interface TrustInterface {
	
	public boolean addPeer(Peer py) throws DuplicatePeerException;
	public void storeEncounter(Context ck, Peer py, int lb) throws LevelRangeException;
	public void storeRecommendation(Context ck, Peer py, int lb) throws LevelRangeException;
	public int getOverallTrust(Context ck, Peer py);
	public double getOverallTrustConfidence(Context ck, Peer py);
}
