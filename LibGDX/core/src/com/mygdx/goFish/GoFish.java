package com.mygdx.goFish;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.Cards.Card;
import com.mygdx.Cards.Deck;

public class GoFish {
	private Player p1;
	private Player p2;
	private Player p3;
	private Player p4;
	private AssetManager manager;
	private static int deckIterator;
	private static Deck deck;
	private String currentPlay;

	public Player getPlayer(int player) {
		if (player == 1)
			return p1;
		else if (player == 2)
			return p2;
		else if (player == 3)
			return p3;
		else if (player == 4)
			return p4;
		else
			return new Player("Not a player");
	}

	public static void play(Player currentPlayer, Player otherPlayer, String rank) {
		if (otherPlayer.hasCard(rank)) {
			Card toMove = otherPlayer.getCard(otherPlayer.getIndexOfCardTaken());
			currentPlayer.addCard(toMove); // Add other player's card to current player's hand
			otherPlayer.removeCard(toMove); // Remove the card that was taken by current player
		} else
			currentPlayer.addCard(fish());
	}

	public static Card fish() {
		deckIterator++;
		return deck.getCard(deckIterator - 1);
	}

	public int[] getScore() {
		int result[] = new int[4];

		result[0] = p1.getScore();
		result[1] = p2.getScore();
		result[2] = p3.getScore();
		result[3] = p4.getScore();

		return result;
	}

	public static Deck getDeck() {
		return deck;
	}

}