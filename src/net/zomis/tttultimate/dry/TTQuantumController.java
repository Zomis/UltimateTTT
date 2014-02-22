package net.zomis.tttultimate.dry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.Winnable;


public class TTQuantumController extends TTController {

	// http://en.wikipedia.org/wiki/Quantum_tic_tac_toe
	
	// x Entaglements needs to be seen somehow, ViewStrategy? TTBase.data?
	// x Classical marks should be subscripted with the letter and subscript of the collapsed move
	
	private final Map<TTBase, Integer> subscripts; // TODO: Keep track of the subscripting-entaglements
	private TTBase firstPlaced;
	private Integer collapse;
	private int counter;
	
	public TTQuantumController() {
		super(new TTFactoryImpl().ultimate());
		this.subscripts = new HashMap<>();
		this.onReset();
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		if (collapse == null && tile.isWon())
			return false;
		
		if (collapse != null) {
			return subscripts.get(tile) == collapse;
		}
		

		if (firstPlaced != null) { // x Play two moves before switching, not on the same board
			return tile.getParent() != firstPlaced;
		}
		return !tile.isWon() && !tile.getParent().isWon();
	}

	@Override
	protected boolean performPlay(TTBase tile) {
		if (collapse != null) {
			collapse = null;
			// The new player should choose a field that should be collapsed
			performCollapse(tile);
			game.determineWinner();
			if (game.getWonBy() == TTPlayer.XO) {
				// TODO: If two players get TTT same time, the player whose TTT has the lowest *maximum* subscript gets one point and the other player 1/2
				game.setPlayedBy(tieBreak());
				
			}
			return true;
		}
		
		tile.setPlayedBy(currentPlayer);
		subscripts.put(tile, counter);
		
		if (firstPlaced != null) {
			firstPlaced = null;
			nextPlayer();
			if (isEntaglementCycleCreated(tile)) {
				collapse = counter;
				// TEST: when a cycle has been created the next player must choose the field that should be collapsed
			}
			counter++;
		}
		else firstPlaced = tile.getParent();
		
		return true;
	}
	
	private TTPlayer tieBreak() {
		TTWinCondition lowestWin = null;
		for (TTWinCondition cond : game.getWinConds()) {
			TTPlayer pl = cond.determineWinnerNew();
			if (pl == TTPlayer.NONE)
				continue;
			if (lowestWin == null || highestSubscript(cond) < highestSubscript(lowestWin))
				lowestWin = cond;
		}
		return lowestWin.determineWinnerNew();
	}
	
	private int highestSubscript(TTWinCondition cond) {
		int highest = 0;
		for (Winnable tile : cond) {
			Integer value = subscripts.get(tile);
			if (value == null)
				throw new NullPointerException("Position doesn't have a subscript: " + cond);
			highest = Math.max(highest, value);
		}
		return highest;
	}
	

	private void performCollapse(TTBase tile) {
		if (!tile.isWon())
			throw new IllegalArgumentException("Cannot collapse tile " + tile);
		if (tile.hasSubs())
			throw new AssertionError(subscripts.toString());
		if (tile.getParent().isWon())
			throw new AssertionError();
		
		TTBase tangled = findEntanglement(tile);
		if (tangled != null) {
			subscripts.remove(tangled);
			tangled.reset();
		}
		
		TTPlayer winner = tile.getWonBy();
		Integer value = subscripts.remove(tile);
		
		for (TTBase ff : TicUtils.getAllSubs(tile.getParent())) {
			subscripts.remove(ff);
		}
		tile.getParent().reset();
		tile.getParent().setPlayedBy(winner);
		subscripts.put(tile.getParent(), value);
		collapseCheck();
	}

	private boolean isEntaglementCycleCreated(TTBase tile) {
		return isEntaglementCycleCreated(tile, new HashSet<TTBase>(), new HashSet<TTBase>());
	}
	private boolean isEntaglementCycleCreated(TTBase tile, Set<TTBase> scannedAreas, Set<TTBase> scannedTiles) {
		if (tile.getParent() == null || tile.hasSubs())
			throw new IllegalArgumentException();
		
		scannedTiles.add(tile);
		if (scannedAreas.contains(tile.getParent()))
			return true;
		scannedAreas.add(tile.getParent());
		
		TTBase area = tile.getParent();
		
		Collection<TTBase> subs = TicUtils.getAllSubs(area);
		for (TTBase sub : subs) {
			if (sub == tile)
				continue;
			if (!sub.isWon())
				continue;
			
			if (scannedTiles.contains(sub))
				return true;
			
			scannedTiles.add(sub);
			TTBase tangled = findEntanglement(sub);
			if (tangled != null) {
				boolean recursive = isEntaglementCycleCreated(tangled, scannedAreas, scannedTiles);
				if (recursive)
					return true;
			}
		}
		return false;
		
//		int oldSize = scannedAreas.size();
//		while (true) {
//			if (scannedAreas.contains(area))
//				return true;
//			
//			if (scannedAreas.size() == oldSize)
//				return false;
//			throw new UnsupportedOperationException();
//			
//		}
		
		// TODO: Detect entanglement cycles
	}

	private void collapseCheck() {
		// TEST: When a field does not have an entaglement anymore, collapse it
		
		for (Entry<TTBase, Integer> ee : new HashMap<>(this.subscripts).entrySet()) {
			// remove those that should be removed first, to make a clean scan later
			if (!ee.getKey().isWon()) {
				subscripts.remove(ee.getKey());
			}
		}
		
		for (Entry<TTBase, Integer> ee : this.subscripts.entrySet()) {
			if (ee.getKey().hasSubs())
				continue;
			TTBase match = findEntanglement(ee.getKey());
			if (match == null) {
				performCollapse(ee.getKey());
				collapseCheck();
				return;
			}
		}
	}

	private TTBase findEntanglement(TTBase key) {
		if (!subscripts.containsKey(key))
			return null;
		int match = subscripts.get(key);
		for (Entry<TTBase, Integer> ee : this.subscripts.entrySet()) {
			if (ee.getKey() == key)
				continue;
			if (ee.getValue() == match) {
				return ee.getKey();
			}
		}
		return null;
	}

	@Override
	protected void onReset() {
		this.subscripts.clear();
		this.collapse = null;
		this.firstPlaced = null;
		this.counter = 1;
	}

	@Override
	public String getViewFor(TTBase tile) {
		if (!tile.isWon() && tile.getParent().isWon()) {
			tile = tile.getParent();
		}
		Integer sub = subscripts.get(tile);
		return super.getViewFor(tile) + (sub != null ? sub : "");
	}
	
}
