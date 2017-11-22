package com.desk.cards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class DeckOfCards
{
	private static List<String> deckofCards = new ArrayList<String>();
	private static boolean shuffled = false;
	private static int dealCardNumber = 0;
	private static final String CARDS_SHUFFLED = "cardsShuffled";
	private static final String DEALT_NUMBER = "dealtNumber";
	private static final String SHUFFLED_SEQ = "shuffledSeq";

	private static Properties readProperties()
	{
		Properties prop = new Properties();
		InputStream input = null;
		try
		{
			input = new FileInputStream("cardDeck.properties");
			prop.load(input);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	private static void writeProperties(Map<String, String> map)
	{
		Properties prop = new Properties();
		OutputStream output = null;
		try
		{
			output = new FileOutputStream("cardDeck.properties");
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				if (entry.getKey() != null && entry.getValue() != null)
				{
					prop.setProperty(entry.getKey(), entry.getValue());
				}
			}
			prop.store(output, null);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

	private static void loadCurrentStateofDesk()
	{
		Properties prop = readProperties();
		shuffled = prop.getProperty(CARDS_SHUFFLED) == null ? false : Boolean.parseBoolean(prop.getProperty(CARDS_SHUFFLED));
		if (shuffled)
		{
			dealCardNumber = prop.getProperty(DEALT_NUMBER) == null ? 0 : Integer.parseInt(prop.getProperty(DEALT_NUMBER));
			String cardsSeq = prop.getProperty(SHUFFLED_SEQ);
			if (cardsSeq != null)
			{
				String shuffledCrads[] = cardsSeq.split(",");
				deckofCards = Arrays.asList(shuffledCrads);
			}
		}
		else
		{
			deckofCards = Card.newDeck();
			dealCardNumber = 0;
		}
	}

	public static void shuffleCards()
	{
		Collections.shuffle(deckofCards);
		Map<String, String> map = new HashMap<String, String>();
		map.put(CARDS_SHUFFLED, "true");
		map.put(DEALT_NUMBER, "0");
		String token = "";
		for (String card : deckofCards)
		{
			if (token.length() == 0)
			{
				token = card;
			}
			else
			{
				token = token + "," + card;
			}

		}
		map.put(SHUFFLED_SEQ, token);
		writeProperties(map);
	}

	public static String dealOneCard()
	{
		loadCurrentStateofDesk();
		Properties read = readProperties();
		Map<String, String> map = new HashMap<String, String>();
		if (dealCardNumber < 52 && shuffled)
		{
			map.put(CARDS_SHUFFLED, read.getProperty(CARDS_SHUFFLED));
			map.put(SHUFFLED_SEQ, read.getProperty(SHUFFLED_SEQ));
			map.put(DEALT_NUMBER, String.valueOf(++dealCardNumber));
			writeProperties(map);
			return deckofCards.get(dealCardNumber - 1);
		}
		else
		{
			map.put(CARDS_SHUFFLED, "false");
			map.put(SHUFFLED_SEQ, "");
			map.put(DEALT_NUMBER, "0");
			writeProperties(map);
			shuffled = false;
			dealCardNumber = 0;
			return "ALL 52 CARDS RETURNED FROM DECK. \n PlEASE SHUFFLE AGAIN";
		}
	}

	public static void main(String[] args)
	{
		loadCurrentStateofDesk();
		playCards();
	}

	private static void playCards()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your Choice");
		System.out.println("1 SHUFFLE CARDS");
		System.out.println("2 DEAL ONE CARD");
		System.out.println("3 EXIT");
		int choice = scan.nextInt();

		switch (choice)
		{
			case 1:
				shuffleCards();
				System.out.println("Crads Shuffle done!");
				playCards();
				break;
			case 2:
				System.out.println("YOUR CARD IS --> " + dealOneCard());
				playCards();
				break;
			case 3:
				scan.close();
				System.out.println("Thanks Visit Again");
				break;
			default:
				System.out.println("Invalid Choice");
				playCards();
				break;
		}
	}
}
