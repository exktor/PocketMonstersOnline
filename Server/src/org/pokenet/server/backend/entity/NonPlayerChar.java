package org.pokenet.server.backend.entity;

import java.util.HashMap;
import java.util.Random;

import org.pokenet.server.GameServer;
import org.pokenet.server.battle.Pokemon;

/**
 * Represents a Non Playable Character
 * @author shadowkanji
 *
 */
public class NonPlayerChar extends Char {
	/*
	 * Trainers can have an more than 6 possible Pokemon.
	 * When a battle is started with this NPC, it'll check the min party size.
	 * If you have a party bigger than min party,
	 * it'll generate a party of random size between minParty and your party size + 1.
	 * (Unless your party size is 6)
	 */
	private HashMap<String, Integer> m_possiblePokemon;
	private int m_minPartySize = 1;
	
	/**
	 * Constructor
	 */
	public NonPlayerChar() {}
	
	/**
	 * Sets the possible Pokemon this trainer can have
	 * @param pokes
	 */
	public void setPossiblePokemon(HashMap<String, Integer> pokes) {
		m_possiblePokemon = pokes;
	}
	
	/**
	 * Sets the minimum sized party this npc should have
	 * @param size
	 */
	public void setPartySize(int size) {
		m_minPartySize = (size > 6 ? 6 : size);
	}
	
	/**
	 * Returns a dynamically generated Pokemon party based on how well trained a player is
	 * @param p
	 * @return
	 */
	public Pokemon [] getParty(PlayerChar p) {
		Pokemon [] party = new Pokemon[6];
		Pokemon poke;
		int level;
		String name;
		Random r = GameServer.getServiceManager().getDataService().getBattleMechanics().getRandom();
		if(m_minPartySize < p.getPartyCount()) {
			/*
			 * The player has more Pokemon, generate a random party
			 */
			/*
			 * First, get a random party size that is greater than m_minPartySize
			 * and less than or equal to the amount of pokemon in the player's party + 1
			 */
			int pSize = r.nextInt(p.getPartyCount() + 1 > 6 ? 6 : p.getPartyCount() + 1);
			while(pSize < m_minPartySize) {
				pSize = r.nextInt(p.getPartyCount() + 1 > 6 ? 6 : p.getPartyCount() + 1);
			}
			/*
			 * Now generate the random Pokemon
			 */
			for(int i = 0; i <= pSize; i++) {
				//Select a random Pokemon
				name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
				level = m_possiblePokemon.get(name);
				//Ensure levels are the similiar
				while(level < p.getHighestLevel() - 3) {
					level = r.nextInt(p.getHighestLevel() + 5);
				}
				poke = Pokemon.getRandomPokemon(name, level);
				party[i] = poke;
			}
		} else {
			/*
			 * Generate a party of size m_minPartySize
			 */
			for(int i = 0; i < m_minPartySize; i++) {
				//Select a random Pokemon from this list of possible Pokemons
				name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
				level = m_possiblePokemon.get(name);
				//Ensure levels are the similiar
				while(level < p.getHighestLevel() - 3) {
					level = r.nextInt(p.getHighestLevel() + 5);
				}
				poke = Pokemon.getRandomPokemon(name, level);
				party[i] = poke;
			}
		}
		return party;
	}
}
