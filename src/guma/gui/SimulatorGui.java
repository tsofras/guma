/**
*GUMA a simple math game for elementary school students
*	Copyright (C) 2012-1013  Dimitrios Desyllas (pc_magas)
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*	Contact with me by at this address: pc_magas@yahoo.gr
*/

package guma.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import guma.core.*;
import guma.enums.PraxisType;
import guma.arithmetic.Praxis;
import guma.ui.simulator.*;

public class SimulatorGui extends JFrame implements ActionListener,UpdateSimulatorUI
{
	/**
	*Button Panel
	*/
	private JPanel butonPanel=new JPanel();
		
	/**
	*Button that allows you to move on the next step of Operation Simulation
	*/
	private JButton next= new JButton("Επόμενο Βήμα");
	
	/**
	*Close Window Button
	*/
	private JButton close=new JButton("Κλείσιμο");
	
	/**
	*Panel for havinh together the area that shows the carry and the label
	*/
	private JPanel carryPanel= new JPanel();
	
	/**
	*Label that shows the a value for carry column
	*/
	private JLabel carryLabel=new JLabel("Κρατούμενα/Δανεικά:");
	
	/**
	*Showing the Carry
	*/
	private JLabel carry=new JLabel();
	
	/**
	*Showing the operation
	*/
	private JLabel praxis=new JLabel();
	
	/**
	*Showing the message
	*/
	private JLabel message=new JLabel();
	
	/**
	*Controller that we use as interface between GUI and simulator
	*/
	private SimulatorController s=null;
	
	/**
	*Constructor
	*@param telestis1: the first operator of the operation we want to simulate
	*@param telestis2: the second operator of the operation we want to simulate
	*@param praxisType: the type of operation we want to simulate
	*/
	public SimulatorGui(int telestis1, int telestis2, PraxisType praxisType)
	{
		super();
		setSize(500,345);
		setTitle("Προσομοίωση Πράξης");
		setLayout(new BorderLayout());
		add(butonPanel,BorderLayout.SOUTH);
		butonPanel.setLayout(new FlowLayout());
		
		butonPanel.add(next);
		butonPanel.add(close);
		
		carryPanel.setLayout(new GridLayout(2,1));
		carryPanel.add(carryLabel);
		carryPanel.add(carry);
		
		add(carryPanel,BorderLayout.EAST);
		add(message,BorderLayout.NORTH);
		add(praxis,BorderLayout.CENTER);
		
		next.addActionListener(this);
		close.addActionListener(this);
		
		s=new SimulatorController(telestis1,telestis2,praxisType);
	}
	
	/**
	*@override
	*/	
	public void actionPerformed(ActionEvent e)
	{
    	Object option=e.getSource();//get who caused the Action
    	if(option==next)
    	{
    		SimulatorUI u=s.next();
    		updateUI(u);
    	}
    	else if(option==close)
    	{
    		dispose();
    	}
	}
	
	/**
	*@override
	*/
	public void updateUI(SimulatorUI u)
	{
		System.out.println("Updating UI\n Message"+u.getMessage());
		next.setEnabled(u.getNext());
		message.setText("<html><body><center>"+u.getMessage()+"</center></body></html>");
		carry.setText("<html><body><center>"+u.getCarryList()+"</center></body></html>");
		praxis.setText("<html><body><center>"+u.getOperation()+"</center></body></html>");
	}
	
	/**
	*Shows the Simulator GUI
	*@param telestis1: the first operator of the operation we want to simulate
	*@param telestis2: the second operator of the operation we want to simulate
	*@param praxisType: the type of operation we want to simulate
	*/
	public void showSimulator(Component parent)
	{
		setVisible(true);
	 	//JOptionPane.showMessageDialog(parent,this);
	}
}
