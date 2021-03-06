/**
*GUMA a simple math game for elementary school students
*	Copyright (C) 2012-2013  Dimitrios Desyllas (pc_magas)
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*Contact with me by main at thes address: pc_magas@yahoo.gr
*/

package guma.core;

import guma.arithmetic.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.*;

import java.io.*;
import java.io.IOException;
import java.io.File;
import guma.core.GameOverException;
import guma.core.TriesEndException;
import guma.enums.PraxisType;

import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
*Basic Game Class
*@author Dimitrios Desyllas
*/

public class Game
{

	/**
	*This variable counts the operations that the user solved, regardless if he gave the correct answer or not. 
	*/

	private int praksisCounter=0;

	/**
	*Saves the current score
	*/
	private long score=0;
	
	/**
	*Stores the remaining tries
	*/
	private byte tries=3;
	
	/**
	*This Matrix Stores all tha arithmetic operations that the user has to answer
	*/
	private Praxis[] p=null;

	/**
	*Gets the wrong result posiiton 
	*/
	private int wrongResultPos;	

	/**
	*Shows if game saved or not
	*/
	private boolean saved=false;

	/**
	*Boollean that helps us out to check what kind of error we will throw
	*/
	private boolean end=false;
	
	final private static Map<Character,PraxisType> praxisTypeMap = new HashMap<Character,PraxisType>();
	{
		praxisTypeMap.put('+', PraxisType.ADDING);
		praxisTypeMap.put('-', PraxisType.SUBSTRACTION);
		praxisTypeMap.put('*', PraxisType.MULTIPLICATION);
		praxisTypeMap.put('/', PraxisType.SUBSTRACTION);
	}
	
	public static PraxisType getPraxisType(char c){
		return praxisTypeMap.get(c);
	}



	/**
	*Creator method
	*@param pli8os The number of arithmetic praxis that a game should have.
	*@param maxNum The maximum value that can have a number as Operator.
	*@param typosPraksewn Matrix that contains the correct characters for the praxis we wat to play.
	*/

	public Game(int pli8os,int maxNum,char [] praxisType)
	{
		p=new Praxis[pli8os];
		Random r=new Random();
		for(int i=0;i<p.length;i++)
		{
			do
			{
				char praxisChar = praxisType[r.nextInt(praxisType.length)];
				
				p[i]=Praxis.makePraxis(getPraxisType(praxisChar),maxNum+1);	
			}
			while(p[i]==null);

			/*Stirring the praxis matrix for maximum randomness*/
			int times=r.nextInt(100);
	
			for(int j=0;j<times;j++)
			{
				int pos1=r.nextInt(praxisType.length);
				int pos2=r.nextInt(praxisType.length);
				char temp=praxisType[pos1];
				praxisType[pos1]=praxisType[pos2];
				praxisType[pos2]=temp;
				
			}
		}
		praksisCounter=p.length-1;
	}

	/**
	*Constryuctor method thar creates a Game from Seperate elements
	*@param p An array of Praxis
	*@param praxisCounter Counter of remaining praxis
	*@param wrongResultPos position of wrong result
	*@param tries the remaining tries
	*@param score the current score 
	*/
	public Game(Praxis[] p,int praxisCounter,int wrongResultPos,byte tries, long score)
	{
		this.p=p;
		this.praksisCounter=praxisCounter;
		this.wrongResultPos=wrongResultPos;
		this.tries=tries;
		this.score=score;
	}
	
	/**
	*Checks if an arithmetis result is correct
	*@param result The result that user gives to check if it is corrent
	*/
	public boolean checkApotelesma(int result[]) throws TriesEndException,GameOverException
	{
		wrongResultPos=p[praksisCounter].getWrongResult(result);
		
		if(praksisCounter>=0 && !end)
		{
			if(wrongResultPos<0)/*If the gived result is correct*/
			{
				
					score+=tries;/*Add tries to the score*/
					tries=3;
					praksisCounter--;
				
			}
			else
			{
				tries--;/*reduce tries*/
				if(tries==0)/*No more tries*/
				{
					if(praksisCounter>0)
					{
						tries=3;
						/*Throw the correct error*/
						throw new TriesEndException("Δώσατε Λάθος αποτέλεσμα 3 φορές.\n"+p[praksisCounter--].toFullString()+"\nΤο πρόγραμμα μεταβαίνει στην επόμενη πράξη.");
					}
					else
					{
						System.out.println("Tries End");
						end=true;
						throw new TriesEndException("Δώσατε Λάθος αποτέλεσμα 3 φορές.\n"+p[praksisCounter--].toFullString()+"\nΤο πρόγραμμα μεταβαίνει στην επόμενη πράξη.");
						
					}
				}
			}
		}	
		else
		{
			System.out.println("Game OVER");
			throw new GameOverException("Τέλος Παιχνιδιού \n Σκόρ: "+calculateScore()+'/'+p.length);
		}
		
		saved=false;
		return wrongResultPos<0?true:false;
	}


	/**
	*returns where the result is wrong
	*/
	public int getWrongResultPos()
	{
		return wrongResultPos;
	}	

	/**
	*Shows current praxis as a string
	*/
	
	public String toString() throws GameOverException
	{
		String s="";
		System.out.println(praksisCounter);
		if(praksisCounter>=0)
		{
			s=p[praksisCounter].toString();
		}
		else
		{
			
			throw new GameOverException("Τέλος Παιχνιδιού \n Σκόρ: "+calculateScore()+'/'+p.length);

		}
		return s;
	}
	
	/**
	*Returns the precious or current arithmetic operation that the user is requested to answer
	*@param previous: tur if we need the previous operation or not
	*/

	public Praxis getPraxis(boolean previous)throws GameOverException
	{

			if(!previous)
			{
				System.out.println("We get the "+praksisCounter+"th praxis");
				return p[praksisCounter];	
			}
			else
			{
				if(praksisCounter<(p.length-1))
				{
					System.out.println("We get the "+(praksisCounter+1)+"th praxis");
					return p[praksisCounter+1];
				}
				else
				{
					return null;
				}
			}
		
		
	}
	
	/**
	*Returns the precious or current arithmetic operation that the user is requested to answer
	*/
	public Praxis getCurrentPraxis() throws GameOverException
	{
		
		if(!(praksisCounter<0) && !end)
		{
			return getPraxis(false);
		}
		else
		{
			throw new GameOverException("Τέλος Παιχνιδιού \n Σκόρ: "+calculateScore()+'/'+p.length);

		}
	}

	/**
	*Stores the current progress
	*@param f The file we want to store the current progress
	*/
	
	public void save(File f) throws IOException
	{
		
		if(f.exists())/* If file exists delete is in order to replace it*/
		{
			f.delete();
		}		
		
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(f));

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<game tries=\""+tries+"\""+" score=\""+score+"\""+" praxisCounter=\""+praksisCounter+"\" wrongResultPos=\""+wrongResultPos+"\">");
			for(int i=0;i<p.length;i++)
			{
				out.println("<praxis>"+p[i]+"</praxis>");
			}

			out.println("</game>");
			saved=true;
			out.close();
		}
		catch(IOException e)/*Error Ocurred*/
		{
			e.printStackTrace();
			throw new 
IOException("Το αρχείο δεν είναι προσβάσιμο ή δεν είναι δυνατόν να αποθηκεύσεται στον κατάλογο:\n"+f.getAbsolutePath()+"\n Μπορείτε να δοκιμάσετε με ένα άλλο αρχείο η να ρυθμίσετε τα δικαιόματα στον κατάλογο αυτό");/*Shows the Correct message as an exception*/

		}
	}

	/**
	*Loads a saved progress
	*@param f The file we want to load the current progress
	*/

	public static Game load(File f) throws IOException,ClassNotFoundException
	{
		int praxisCounter=0;
		long score=0;
 		byte tries=3;
		Praxis[] p=null;
		int wrongResultPos=0;	

		try
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
           		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
           		Document doc = docBuilder.parse(f);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("game");
 			if(nList != null && nList.getLength() > 0)
			{
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
					Node nNode = nList.item(temp);
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
 						Element eElement = (Element) nNode;
	
						tries=Byte.parseByte(eElement.getAttribute("tries"));
						praxisCounter=Integer.parseInt(eElement.getAttribute("praxisCounter"));
						score=Long.parseLong(eElement.getAttribute("score"));
						wrongResultPos=Integer.parseInt(eElement.getAttribute("wrongResultPos"));
						
						NodeList praxisList=eElement.getElementsByTagName("praxis");
						
						System.out.println("Value= "+tries);
			
						p=new Praxis[praxisList.getLength()];

						if(p==null)
						{
							throw new ClassNotFoundException("Το αρχείο δεν περιέχει σωστά δεδομένα");
						}

						for(int i=0;i<praxisList.getLength();i++)
						{
							
							String tempS2[]=praxisList.item(i).getTextContent().split(" ");
							char praxisChar = tempS2[1].charAt(0);
							p[i]= Praxis.makePraxis(getPraxisType(praxisChar),Integer.parseInt(tempS2[0]),Integer.parseInt(tempS2[2]));
							if(p[i]==null)
							{
								throw new ClassNotFoundException("Το αρχείο δεν περιέχει σωστά δεδομένα");
							}
							else
							{
								System.out.println("Loading praxis: "+p[i].toString()+ "Needed to be taken "+p[i].getResults()+" results");
							}
						}
                    				
					}
				}
			}

		}
		catch(DOMException d)
		{
			throw new ClassNotFoundException("Το αρχείο δεν περιέχει σωστά δεδομένα");
		}
		catch(Exception e)/*If a file IO error occured*/
		{
			e.printStackTrace();
			throw new 
IOException("Το αρχείο δεν είναι προσβάσιμο ή δεν είναι δυνατόν να αναγνωστεί. \n Μπορείτε να δοκιμάσετε με ένα άλλο αρχείο");
		}
		return new Game(p,praxisCounter,wrongResultPos,tries,score);

	}
	
	/**
	*Loads a saved progress
	*@param path: The path of the file we want to load the current progress
	*/
	public static Game load(String path) throws IOException,ClassNotFoundException
	{
		return Game.load(new File(path));
	}
	
	
	/**
	*Return the Current Score
	*/

	public long getScore()
	{
		return score;
	}
	
	/**
	*Returns the current tries
	*/
	public byte getTries()
	{
		return tries;
	}

	/**
	*Returning the number of arithmetic operations
	*/
	public int getPraxisNum()
	{
		return p.length;
	}

	/**
	*Returning the number of remaining arithmetic operetions
	*/
	public int getRemainingPraxis()
	{
		return p.length-praksisCounter;
	}

	/**
	*Division may need 2 results so with this method we get how many results we need
	*/
	public int getResultsNum()
	{
		return this.getCurrentPraxis().getResults();
	}

	/**
	*Function that tells you if the game is saved or not
	*/
	public boolean isSaved()
	{
		return saved;
	}
	
	/**
	*Calculates the score and brings out the current score with perfect to be the 10
	*/
	private float calculateScore()
	{
		return (float)Math.round((double)((float)score/3));
	}
}
