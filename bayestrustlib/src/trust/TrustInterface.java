package trust;

import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

public interface TrustInterface {
	public boolean addPeer(Peer py) throws DuplicatePeerException;
	public void storeEncounter(Context ck, Peer py, double level) throws LevelRangeException;
	public void storeRecommendation(Context ck, Peer pr, Peer py, double level) throws LevelRangeException;
	public double getOverallTrust(Context ck, Peer py);
	public double getOverallTrustConfidence(Context ck, Peer py);
}
