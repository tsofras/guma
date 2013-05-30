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

package guma.simulator;

import guma.simulator.AbstractSimulator;
import java.util.Arrays;
import guma.arithmetic.Praxis;

public class MultiplicationSimulator extends AbstractSimulator
{

	/**
	*Array List that stores intermediate results
	*/
	private Number[] endiamesa=null;

	/**
	*Shows what intermediate result will be chosen 
	*/
	private int endiamesoApotelesmaIndex=0;
		
	/**
	*Variable that shows if needed to display a message for apending zeros
	*/
	private boolean apendMessage=false;
	
	/**
	*When 2 numbers when multiplying have zeros in the end then we ignore them and we apend them into the final result to the end
	*/
	private int apendZeros=0;
	
	/**
	*This variable shows if the intermediate results will be added directly or not
	*/
	private boolean spartial=true;
	
	/**
	*Constructor Method
	*@param telestis1: the first operator of the number that we will simulate the first operation
	*@param telestis2: the second operator of the number that we will simulate the second operation
	*@param spartial: shows if the results will be added directly or not
	*/
	public MultiplicationSimulator(int telestis1,int telestis2, boolean spartial)
	{
		super(telestis1,telestis2);
		type=Praxis.MULTIPLICATION;
		this.spartial=spartial;
		
		endiamesa=new Number[this.telestis2.length()];
		

		//initilizing the matrix for storing the intermediate results
		for(int i=0;i<endiamesa.length;i++)
		{
			endiamesa[i]=new Number(this.telestis1.length()+i+1,true);
		}
		
		//setting the first intermediate result
		endiamesoApotelesmaIndex=0;

		apendZeros+=this.telestis1.getendZeroCount()+this.telestis2.getendZeroCount();
		this.telestis1.setSelectedDigitToEnd(true);
		this.telestis1.setSeperator("(");
			
		this.telestis2.setSelectedDigitToEnd(true);
		this.telestis2.setSeperator("(");
			
		
		
		if(apendZeros>0)
		{
			apendMessage=true;
		}
		
		
	}
	
	public MultiplicationSimulator(int telestis1,int telestis2)
	{
		this(telestis1,telestis2,false);
	}

	/**
	*This Method does the next step of an arithmetic praxis Simulation
	*Returns true if it has next step to do
	*/	
	public boolean next()
	{
		message="";
		int tempTelestis2;
		int tempTelestis1;
		if(apendMessage)
		{
			message="Ανγωούμε τα 0 που έχουν στο τέλος οι αριθμοί και εκτελούμε την πράξη του πολλαπλασιαμού από τα μη μηδενικα στοιχεία";
			apendMessage=false;
			return true;
		}
		
		try
		{
			tempTelestis2=telestis2.getDigit();
			
			try
			{
				tempTelestis1=telestis1.getDigit();
				message="Πολλαπλασιάζουμε τα ψηφία "+tempTelestis1+"*"+tempTelestis2;
				temp=tempTelestis1*tempTelestis2;
				message+=" και ο πολαπλασιαμός έχει αποτέλεσμα: "+temp;
				if(kratoumeno!=0)
				{
					message+="\n Επειδή από την προηγούμενη πράξη έχουμε κρατούμενο το προσθέτουμε στο αποτέλεσμα. ";	
					temp+=kratoumeno;
				}

				byte tempM[]=Number.seperateDigits((int)temp);
				
				if(tempM.length>1)
				{
					message+="\n Η πράξη μας είχε διψήφιο αποτέλεσμα άρα το πρώτο ψηφίο το κρατάμε κρατούμενο. ";
					kratoumeno=tempM[0];
					endiamesa[endiamesoApotelesmaIndex].setDigit(tempM[tempM.length-1]);
				}
				else
				{
					kratoumeno=0;
					endiamesa[endiamesoApotelesmaIndex].setDigit((byte)temp);
				}
								
				endiamesa[endiamesoApotelesmaIndex].previousDigit();
				telestis1.previousDigit();
				return true;	
			}
			catch(IndexOutOfBoundsException oo)
			{
				telestis2.previousDigit();//Go to previous Digit
				
				//We set selected digit to begin pf telestis 1 until we select all digits of telestis2
				telestis1.setSelectedDigitToBegin();
				
				if(kratoumeno>0)
				{
				 	message+="Βάζουμε το κρατούμενο στην αρχή του αποτελέσματος";
				 	endiamesa[endiamesoApotelesmaIndex].setDigit(kratoumeno);
				 	kratoumeno=0;
				}
				
				if(telestis2.getDigitPos()>=0 && endiamesoApotelesmaIndex<endiamesa.length)
				{
					message+="Βάζουμε ένα 0 κάτω από το "+endiamesa[endiamesoApotelesmaIndex].getDigit(endiamesa[endiamesoApotelesmaIndex].length()-1);
					
					endiamesoApotelesmaIndex++;
					endiamesa[endiamesoApotelesmaIndex].setDigit((byte)0);
					endiamesa[endiamesoApotelesmaIndex].setDigitPos(endiamesoApotelesmaIndex+1,true);//Select One digit from the end
				}
				return true;
			}

		}
		catch(IndexOutOfBoundsException o)//If we have no mode digits on 
		{
			temp=0;
			message="Προσθαίτουμε τα ενδιάμεσα αποτελέσματα.\n";
			
				for(int i=0;i<endiamesa.length;i++)
				{
					int temp1=endiamesa[i].getValue();
					temp+=temp1;
				}
				temp*=(int)Math.pow(10,apendZeros);
			
				result=new Number(temp);
			
			message+="Τέλος προσομοίωσης";
			return false;
		}
	}
	
	/**
	*@override
	*/
	public String toString()
	{
		String s="";
		
			s+="<table><tr><td></td>"+getTelestis1("<td>","</td>","<td><font color=\"blue\">","</font></td>")
							+"</tr><tr><td>"+ type+"</td>"+getTelestis2("<td>","</td>","<td><font color=\"blue\">","</font></td>")+
							"</tr></table><hr><br><table>";
		for(int i=0;i<=endiamesoApotelesmaIndex;i++)
		{
			s+="<tr>";
			for(int j=0;j<(endiamesa.length-i);j++)
			{
				s+="<td>\t</td>";
			}
			s+=endiamesa[i].toString("<td>","</td>","<td><font color=\"blue\">","</font></td>");
			s+="</tr>";
		}
		s+="</table><hr>"+"<table><tr>"+getResult("<td>","</td>")+"</tr></table>";
	
		return s;
	}		
}
