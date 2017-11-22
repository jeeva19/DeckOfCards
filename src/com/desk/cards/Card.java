package com.desk.cards;

import java.util.ArrayList;
import java.util.List;

public class Card
{
	public enum Rank
	{
		DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}

	public enum Suit
	{
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	private final Rank rank;
	private final Suit suit;

	private Card(Rank rank, Suit suit)
	{
		this.rank = rank;
		this.suit = suit;
	}

	public Rank rank()
	{
		return rank;
	}

	public Suit suit()
	{
		return suit;
	}

	public String toString()
	{
		return rank + " of " + suit;
	}

	private static final List<String> protoDeck = new ArrayList<String>();
	static
	{
		for (Suit suit : Suit.values())
			for (Rank rank : Rank.values())
				protoDeck.add(new Card(rank, suit).toString());
	}

	public static ArrayList<String> newDeck()
	{
		return new ArrayList<String>(protoDeck);
	}
}
