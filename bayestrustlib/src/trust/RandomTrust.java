package trust;

import java.util.HashSet;
import java.util.List;

import trust.model.exceptions.DuplicatePeerException;
import trust.model.exceptions.LevelRangeException;
import trust.model.math.Misc;
import trust.model.math.Stats;
import trust.model.primitives.Context;
import trust.model.primitives.Peer;

public class RandomTrust implements TrustInterface{
	protected Stats stats;
	protected Misc misc;
	protected HashSet<Peer> p = new HashSet<Peer>();
	
	public RandomTrust(int nLevels, List<Context> contexts) {
		this.stats = new Stats(nLevels);
		this.misc = new Misc(stats);
	}

	public boolean addPeer(Peer py) throws DuplicatePeerException {
		if (this.p.contains(py))
			throw new DuplicatePeerException(py);
		
		return this.p.add(py);
	}

	public int getOverallTrust(Context ck, Peer py) {
		return (int)(Math.random() * stats.getN());
	}

	public double getOverallTrustConfidence(Context ck, Peer py) {
		return (int)(Math.random() * stats.getN());
	}

	public void storeEncounter(Context ck, Peer py, int lb) throws LevelRangeException {
		misc.checkLevel(lb);
	}

	public void storeRecommendation(Context ck, Peer py, int lb) throws LevelRangeException {
		misc.checkLevel(lb);
	}
}
