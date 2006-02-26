/*
 * PurchaseModeFrame.java
 * Copyright 2002 (C) Chris Ryan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package pcgen.gui;

import pcgen.core.*;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import pcgen.gui.utils.JComboBoxEx;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Chris Ryan
 * @version    $Revision: 1.45 $
 */
final class PurchaseModeFrame extends JDialog
{
	static final long serialVersionUID = -5244500546425680322L;
	private static String s_TITLE = "Purchase Mode Configuration";
	private static final int STANDARD_MIN_PURCHASE_SCORE = 8;
	private static final int STANDARD_MAX_PURCHASE_SCORE = 18;
	private JButton addMethodButton = null;
	private JButton cancelButton;
	private JButton okButton;
	private JButton purchaseScoreMaxDecreaseButton;
	private JButton purchaseScoreMaxIncreaseButton;
	private JButton purchaseScoreMinDecreaseButton;
	private JButton purchaseScoreMinIncreaseButton;
	private JButton removeMethodButton = null;
	private JButton resetButton;
	private JComboBoxEx currentPurchaseMethods = null;
	private JLabel methodPointsLabel = null;
	private JLabel purchaseScoreMaxLabel;
	private JLabel purchaseScoreMinLabel;
	private JLabel savedMethodLabel = null;
	private JLabel statusBar;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel purchaseMethodButtonPanel;
	private JPanel purchaseMethodNamePanel;

	//private JLabel purchaseMethodNegativeCostAllowedLabel;
	private JPanel purchaseMethodPanel;
	private JPanel purchaseMethodPointsPanel;
	private JScrollPane jScrollPane1;
	private JTable abilityScoreCostTable;
	private JTextField purchaseMethodPointsEdit;
	private JTextField purchaseScoreMaxEdit;
	private JTextField purchaseScoreMinEdit;
	private PurchaseModel purchaseModel = null;

	/** Creates new form PurchaseModeFrame */
	public PurchaseModeFrame()
	{
		initComponents();
	}

	/** Creates new form PurchaseModeFrame
	 * @param parent
	 * */
	public PurchaseModeFrame(JDialog parent)
	{
		super(parent);

		initComponents();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		new PurchaseModeFrame().setVisible(true);
	}

	//
	// Pop up a window to get information about a new purchase method
	//
	private void addMethodButtonActionPerformed()
	{
		NewPurchaseMethodDialog npmd = new NewPurchaseMethodDialog(this, true);
		npmd.setVisible(true);

		if (!npmd.getWasCancelled())
		{
			final String methodName = npmd.getEnteredName();

			if (SettingsHandler.getGame().getPurchaseMethodByName(methodName) == null)
			{
				PointBuyMethod pbm = new PointBuyMethod(methodName, Integer.toString(npmd.getEnteredPoints()));
				currentPurchaseMethods.addItem(pbm);
				currentPurchaseMethods.setSelectedItem(pbm);
			}
			else
			{
				ShowMessageDelegate.showMessageDialog("Cannot add method. Name already exists.", Constants.s_APPNAME, MessageType.ERROR);
			}
		}
	}

	private void cancelButtonActionPerformed()
	{
		this.dispose();
	}

	private int convertStringToInt(String valueString)
	{
		int value;

		try
		{
			value = Integer.parseInt(valueString);
		}
		catch (NumberFormatException nfe)
		{
			// bad value
			value = -1;
		}

		return value;
	}

	/**
	 * Display info about the selected purchase method.
	 */
	private void currentPurchaseMethodsActionPerformed()
	{
		final PointBuyMethod method = (PointBuyMethod) currentPurchaseMethods.getSelectedItem();

		if (method == null)
		{
			removeMethodButton.setEnabled(false);
			purchaseMethodPointsEdit.setText("");
		}
		else
		{
			purchaseMethodPointsEdit.setText(method.getPointFormula());
			removeMethodButton.setEnabled(true);
		}
	}

	/** Exit Purchase Mode Frame */
	private void exitForm()
	{
		// TODO
	}

	private void initComponents()
	{
		GridBagConstraints gridBagConstraints;

		jPanel1 = new JPanel();
		purchaseScoreMinIncreaseButton = new JButton();
		purchaseScoreMinDecreaseButton = new JButton();
		purchaseScoreMaxIncreaseButton = new JButton();
		purchaseScoreMaxDecreaseButton = new JButton();
		cancelButton = new JButton();
		resetButton = new JButton();
		purchaseScoreMinLabel = new JLabel();
		purchaseScoreMinEdit = new JTextField();
		purchaseScoreMaxLabel = new JLabel();
		purchaseScoreMaxEdit = new JTextField();
		statusBar = new JLabel();
		jPanel2 = new JPanel();
		currentPurchaseMethods = new JComboBoxEx();
		currentPurchaseMethods.setAutoSort(true);
		savedMethodLabel = new JLabel();
		methodPointsLabel = new JLabel();
		purchaseMethodPointsEdit = new JTextField();
		purchaseMethodPanel = new JPanel();
		purchaseMethodNamePanel = new JPanel();
		purchaseMethodPointsPanel = new JPanel();
		purchaseMethodButtonPanel = new JPanel();
		addMethodButton = new JButton();
		removeMethodButton = new JButton();

		jPanel3 = new JPanel();
		okButton = new JButton();
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					CustomData.writePurchaseModeConfiguration();
				}
			});

		jScrollPane1 = new JScrollPane();

		getContentPane().setLayout(new GridBagLayout());

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(s_TITLE);
		addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent evt)
				{
					exitForm();
				}
			});

		jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));

		purchaseScoreMinLabel.setText("Purchase Score Min:");
		purchaseScoreMinLabel.setToolTipText("The ability score can not go below this value");
		purchaseScoreMinLabel.setPreferredSize(new Dimension(140, 15));
		jPanel1.add(purchaseScoreMinLabel);

		purchaseScoreMinEdit.setHorizontalAlignment(SwingConstants.RIGHT);
		purchaseScoreMinEdit.setPreferredSize(new Dimension(30, 20));
		jPanel1.add(purchaseScoreMinEdit);

		purchaseScoreMinIncreaseButton.setText("+");
		purchaseScoreMinIncreaseButton.setToolTipText("Increase score minimum");
		purchaseScoreMinIncreaseButton.setMargin(new Insets(2, 2, 2, 2));
		purchaseScoreMinIncreaseButton.setPreferredSize(new Dimension(30, 20));
		purchaseScoreMinIncreaseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					purchaseScoreMinIncreaseButtonActionPerformed();
				}
			});

		jPanel1.add(purchaseScoreMinIncreaseButton);

		purchaseScoreMinDecreaseButton.setText("-");
		purchaseScoreMinDecreaseButton.setToolTipText("Decrease score minimum");
		purchaseScoreMinDecreaseButton.setMargin(new Insets(2, 2, 2, 2));
		purchaseScoreMinDecreaseButton.setPreferredSize(new Dimension(30, 20));
		purchaseScoreMinDecreaseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					purchaseScoreMinDecreaseButtonActionPerformed();
				}
			});

		jPanel1.add(purchaseScoreMinDecreaseButton);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel1, gridBagConstraints);

		jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));

		purchaseScoreMaxLabel.setText("Purchase Score Max:");
		purchaseScoreMaxLabel.setToolTipText("The ability score can not go above this value");
		purchaseScoreMaxLabel.setPreferredSize(new Dimension(140, 15));
		jPanel2.add(purchaseScoreMaxLabel);

		purchaseScoreMaxEdit.setHorizontalAlignment(SwingConstants.RIGHT);
		purchaseScoreMaxEdit.setPreferredSize(new Dimension(30, 20));
		jPanel2.add(purchaseScoreMaxEdit);

		purchaseScoreMaxIncreaseButton.setText("+");
		purchaseScoreMaxIncreaseButton.setToolTipText("Increase score maximum");
		purchaseScoreMaxIncreaseButton.setMargin(new Insets(2, 2, 2, 2));
		purchaseScoreMaxIncreaseButton.setPreferredSize(new Dimension(30, 20));
		purchaseScoreMaxIncreaseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					purchaseScoreMaxIncreaseButtonActionPerformed();
				}
			});

		jPanel2.add(purchaseScoreMaxIncreaseButton);

		purchaseScoreMaxDecreaseButton.setText("-");
		purchaseScoreMaxDecreaseButton.setToolTipText("Decrease score maximum");
		purchaseScoreMaxDecreaseButton.setMargin(new Insets(2, 2, 2, 2));
		purchaseScoreMaxDecreaseButton.setPreferredSize(new Dimension(30, 20));
		purchaseScoreMaxDecreaseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					purchaseScoreMaxDecreaseButtonActionPerformed();
				}
			});

		jPanel2.add(purchaseScoreMaxDecreaseButton);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel2, gridBagConstraints);

		purchaseMethodPanel.setLayout(new GridBagLayout());
		purchaseMethodPanel.setBorder(BorderFactory.createEtchedBorder());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(purchaseMethodPanel, gridBagConstraints);

		purchaseMethodNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
		savedMethodLabel.setText("Saved Methods:");
		savedMethodLabel.setPreferredSize(new Dimension(140, 15));
		purchaseMethodNamePanel.add(savedMethodLabel);
		purchaseMethodNamePanel.add(currentPurchaseMethods);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(purchaseMethodNamePanel, gridBagConstraints);
		purchaseMethodPanel.add(purchaseMethodNamePanel, gridBagConstraints);

		purchaseMethodPointsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
		methodPointsLabel.setText("Points:");
		methodPointsLabel.setPreferredSize(new Dimension(140, 15));
		purchaseMethodPointsPanel.add(methodPointsLabel);
		purchaseMethodPointsEdit.setHorizontalAlignment(SwingConstants.RIGHT);
		purchaseMethodPointsEdit.setEditable(false);

		//purchaseMethodPointsEdit.setText("10");
		purchaseMethodPointsEdit.setPreferredSize(new Dimension(90, 20));
		purchaseMethodPointsPanel.add(purchaseMethodPointsEdit);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;

//		getContentPane().add(purchaseMethodPointsPanel, gridBagConstraints);
		purchaseMethodPanel.add(purchaseMethodPointsPanel, gridBagConstraints);

		currentPurchaseMethods.setPreferredSize(new Dimension(140, 21));
		currentPurchaseMethods.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent evt)
				{
					currentPurchaseMethodsActionPerformed();
				}
			});

		purchaseMethodButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addMethodButton.setText("New");
		addMethodButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					addMethodButtonActionPerformed();
				}
			});
		purchaseMethodButtonPanel.add(addMethodButton);
		removeMethodButton.setText("Remove");
		removeMethodButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					removeMethodButtonActionPerformed();
				}
			});
		purchaseMethodButtonPanel.add(removeMethodButton);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		purchaseMethodPanel.add(purchaseMethodButtonPanel, gridBagConstraints);

		statusBar.setText("Set the cost for each ability score");
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipadx = 1;
		gridBagConstraints.ipady = 1;
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(statusBar, gridBagConstraints);

		jPanel3.setLayout(new FlowLayout(FlowLayout.RIGHT));

		okButton.setText("OK");
		okButton.setToolTipText("Accept these values");
		okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					okButtonActionPerformed();
				}
			});

		jPanel3.add(okButton);

		resetButton.setText("Reset");
		resetButton.setToolTipText("Reset to saved values");
		resetButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					resetButtonActionPerformed();
				}
			});

		jPanel3.add(resetButton);
		cancelButton.setText("Cancel");
		cancelButton.setToolTipText("Cancel Purchase Mode Configuration");
		cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					cancelButtonActionPerformed();
				}
			});

		jPanel3.add(cancelButton);

/////////////////////////////////////////////////
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel3, gridBagConstraints);

		jScrollPane1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));
		jScrollPane1.setPreferredSize(new Dimension(100, 200));

		purchaseModel = new PurchaseModel();
		renewAbilityScoreCostTable();

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(jScrollPane1, gridBagConstraints);

		pack();

		initializeCurrentPurchaseMethods();
	}

	private void initializeCurrentPurchaseMethods()
	{
		//
		// Set up the current methods combo's contents
		//
		final int methodCount = SettingsHandler.getGame().getPurchaseMethodCount();

		if (methodCount > 0)
		{
			final PointBuyMethod[] methods = new PointBuyMethod[methodCount];

			for (int i = 0; i < methodCount; ++i)
			{
				methods[i] = SettingsHandler.getGame().getPurchaseMethod(i);
			}

			currentPurchaseMethods.setModel(new DefaultComboBoxModel(methods));
		}

		currentPurchaseMethodsActionPerformed(); // Get into correct state
	}

	private void okButtonActionPerformed()
	{
		purchaseModel.keepNewValues();
		this.dispose();
	}

	private void purchaseScoreMaxDecreaseButtonActionPerformed()
	{
		int oldValue = purchaseModel.getPurchaseScoreMax();

		// get the current value from the edit field
		String valueString = purchaseScoreMaxEdit.getText();

		// convert it to an integer
		int value = convertStringToInt(valueString);

		// bad value?
		if (value == -1)
		{
			// set a status message
			statusBar.setText("Bad value for purchase maximum score, fixing...");
		}
		else
		{
			// decrease the value in the model
			if (!purchaseModel.setPurchaseScoreMax(value - 1))
			{
				// set a status message
				statusBar.setText("Purchase Score Maximum value can no go below Purchase Score Minimum!");
			}
		}

		// ensure the edit value gets updated correctly
		updatePurchaseScoreMax(oldValue);
	}

	private void purchaseScoreMaxIncreaseButtonActionPerformed()
	{
		int oldValue = purchaseModel.getPurchaseScoreMax();

		// get the current value from the edit field
		String valueString = purchaseScoreMaxEdit.getText();

		// convert it to an integer
		int value = convertStringToInt(valueString);
		boolean updateOk = false;

		if (!Globals.checkRule(RuleConstants.ABILRANGE))
		{
			if (value == STANDARD_MAX_PURCHASE_SCORE)
			{
				statusBar.setText("May not increase score past " + STANDARD_MAX_PURCHASE_SCORE + " in standard mode");

				return;
			}
		}

		// bad value?
		if (value == -1)
		{
			// set a status message
			statusBar.setText("Bad value for purchase maximum score, fixing...");
		}
		else
		{
			// increase the value in the model
			updateOk = purchaseModel.setPurchaseScoreMax(value + 1);
		}

		// ensure the edit value gets updated correctly
		updatePurchaseScoreMax(oldValue);

		if (updateOk)
		{
			purchaseModel.setValueAt(new Integer(purchaseModel.predictNextPurchaseCostMax()),
			    purchaseModel.getRowCount() - 1, 1);
		}
	}

	private void purchaseScoreMinDecreaseButtonActionPerformed()
	{
		int oldValue = purchaseModel.getPurchaseScoreMin();

		// get the current value from the edit field
		String valueString = purchaseScoreMinEdit.getText();

		// convert it to an integer
		int value = convertStringToInt(valueString);
		boolean updateOk = false;

		if (!Globals.checkRule(RuleConstants.ABILRANGE))
		{
			if (value == STANDARD_MIN_PURCHASE_SCORE)
			{
				statusBar.setText("May not decrease score past " + STANDARD_MIN_PURCHASE_SCORE + " in standard mode");

				return;
			}
		}

		// bad value?
		if (value == -1)
		{
			// set a status message
			statusBar.setText("Bad value for purchase minimum score, fixing...");
		}
		else
		{
			// decrease the value in the model
			if (!(updateOk = purchaseModel.setPurchaseScoreMin(value - 1)))
			{
				// set a status message
				statusBar.setText("Purchase Score Minimum value is 0!");
			}
		}

		// ensure the edit value gets updated correctly
		updatePurchaseScoreMin(oldValue);

		if (updateOk)
		{
			purchaseModel.setValueAt(new Integer(purchaseModel.predictNextPurchaseCostMin()), 0, 1);
		}
	}

	private void purchaseScoreMinIncreaseButtonActionPerformed()
	{
		int oldValue = purchaseModel.getPurchaseScoreMin();

		// get the current value from the edit field
		String valueString = purchaseScoreMinEdit.getText();

		// convert it to an integer
		int value = convertStringToInt(valueString);

		// bad value?
		if (value == -1)
		{
			// set a status message
			statusBar.setText("Bad value for purchase minimum score, fixing...");
		}
		else
		{
			// increase the value in the model
			if (!purchaseModel.setPurchaseScoreMin(value + 1))
			{
				// set a status message
				statusBar.setText("Purchase Score Minimum value can not exceed Purchase Score Maximum Value!");
			}
		}

		// ensure the edit value gets updated correctly
		updatePurchaseScoreMin(oldValue);
	}

	/**
	 * Remove the current selection from the list of purchase methods.
	 */
	private void removeMethodButtonActionPerformed()
	{
		final PointBuyMethod method = (PointBuyMethod) currentPurchaseMethods.getSelectedItem();

		if (method != null)
		{
			currentPurchaseMethods.removeItem(method);
		}
	}

	private void renewAbilityScoreCostTable()
	{
		abilityScoreCostTable = new JTable();

		abilityScoreCostTable.setBorder(new BevelBorder(BevelBorder.LOWERED));
		abilityScoreCostTable.setModel(purchaseModel);
		abilityScoreCostTable.setToolTipText("Set the cost for each ability score");
		jScrollPane1.setViewportView(abilityScoreCostTable);
	}

	private void resetButtonActionPerformed()
	{
		//renewAbilityScoreCostTable();
		purchaseModel.copySavedToCurrent();
		updatePurchaseScoreMin(-1);
		updatePurchaseScoreMax(-1);
		purchaseModel.fireTableStructureChanged();

		initializeCurrentPurchaseMethods();
	}

	private void updatePurchaseScoreMax(int oldValue)
	{
		int score = purchaseModel.getPurchaseScoreMax();
		purchaseScoreMaxEdit.setText(Integer.toString(score));

		if ((oldValue != -1) && (oldValue != score))
		{
			purchaseModel.appendRows(score - oldValue);
			purchaseModel.fireTableStructureChanged();
		}
	}

	private void updatePurchaseScoreMin(int oldValue)
	{
		int score = purchaseModel.getPurchaseScoreMin();
		purchaseScoreMinEdit.setText(Integer.toString(score));

		if ((oldValue != -1) && (oldValue != score))
		{
			purchaseModel.prependRows(score - oldValue);
			purchaseModel.fireTableStructureChanged();
		}
	}

	private class PurchaseModel extends AbstractTableModel
	{
		private boolean[] canEdit = new boolean[]{ false, true };
		private String[] columnHeaders = new String[]{ "Ability Score", "Cost" };
		private Object[][] currentValues = null;
		private Object[][] savedValues = null;
		private Class[] types = new Class[]{ Integer.class, Integer.class };
		private int currentPurchaseScoreMax = 10;
		private int currentPurchaseScoreMin = 10; // Start at the average stat
		private int savedPurchaseScoreMax = 0;
		private int savedPurchaseScoreMin = 0;

		PurchaseModel()
		{
			super();

			// initialise the saved values
			initValues();

			// copy the saved values to the current values
			copySavedToCurrent();
		}

		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return canEdit[columnIndex];
		}

		public Class getColumnClass(int columnIndex)
		{
			return types[columnIndex];
		}

		public int getColumnCount()
		{
			return columnHeaders.length;
		}

		public String getColumnName(int param)
		{
			return columnHeaders[param];
		}

		public int getRowCount()
		{
			return currentValues.length;
		}

		public void setValueAt(Object obj, int row, int column)
		{
			if ((row < 0) || (row >= currentValues.length))
			{
				throw new ArrayIndexOutOfBoundsException("Row index out of bounds: " + row);
			}

			if ((column == 0) || (column == 1))
			{
				currentValues[row][column] = obj;
				fireTableCellUpdated(row, column);
			}
			else
			{
				throw new ArrayIndexOutOfBoundsException("Column index out of bounds: " + column);
			}
		}

		public Object getValueAt(int row, int column)
		{
			if ((row < 0) || (row >= currentValues.length))
			{
				throw new ArrayIndexOutOfBoundsException("Row index out of bounds: " + row);
			}

			if ((column == 0) || (column == 1))
			{
				return currentValues[row][column];
			}
			throw new ArrayIndexOutOfBoundsException("Column index out of bounds: " + column);
		}

		public void copySavedToCurrent()
		{
			if (savedValues != null)
			{
				currentPurchaseScoreMin = savedPurchaseScoreMin;
				currentPurchaseScoreMax = savedPurchaseScoreMax;

				final int nrEntries = currentPurchaseScoreMax - currentPurchaseScoreMin + 1;

				currentValues = new Object[nrEntries][2];

				for (int i = 0; i < nrEntries; ++i)
				{
					currentValues[i][0] = savedValues[i][0];
					currentValues[i][1] = savedValues[i][1];
				}
			}
		}

		public void initValues()
		{
			// get the ability score costs from settings
			int[] scoreCosts = SettingsHandler.getGame().getAbilityScoreCost();

			if (scoreCosts != null)
			{
				// get the save values from the settings
				savedPurchaseScoreMin = SettingsHandler.getGame().getPurchaseScoreMin();
				savedPurchaseScoreMax = SettingsHandler.getGame().getPurchaseScoreMax();

				savedValues = new Object[scoreCosts.length][2];

				for (int i = savedPurchaseScoreMin, index; i <= savedPurchaseScoreMax; ++i)
				{
					index = i - savedPurchaseScoreMin;
					savedValues[index][0] = new Integer(i);
					savedValues[index][1] = new Integer(scoreCosts[index]);
				}
			}
			else
			{
				savedPurchaseScoreMin = 10;
				savedPurchaseScoreMax = 10;

				scoreCosts = new int[1];
				scoreCosts[0] = 0;

				savedValues = new Object[1][2];
				savedValues[0][0] = new Integer(10);
				savedValues[0][1] = new Integer(0);
			}

			//
			// Make sure the min/max buttons have the correct info
			//
			purchaseScoreMinEdit.setText(Integer.toString(savedPurchaseScoreMin));
			purchaseScoreMaxEdit.setText(Integer.toString(savedPurchaseScoreMax));
		}

		/** Scale rises in the maximum purchase cost <strong>after</strong> a new, empty cost row has been added.
		 * @return int */
		public int predictNextPurchaseCostMax()
		{
			int maxIndex = getRowCount() - 2; // have already added the new row
			int max = ((Integer) getValueAt(maxIndex, 1)).intValue();

			if (getRowCount() == 2) // initial and one empty
			{
				return max + 1;
			}

			int penultimate = ((Integer) getValueAt(maxIndex - 1, 1)).intValue();

			return max + (max - penultimate);
		}

		/** Scale drops in the minimum purchase cost <strong>after</strong> a new, empty cost row has been added.
		 * @return int*/
		public int predictNextPurchaseCostMin()
		{
			int minIndex = 1; // Have already added the new row
			int min = ((Integer) getValueAt(minIndex, 1)).intValue();

			if (getRowCount() == 2) // initial and one empty
			{
				return min - 1;
			}

			int penultimate = ((Integer) getValueAt(minIndex + 1, 1)).intValue();

			return min - (penultimate - min);
		}

		/** Setter for property purchaseScoreMax.
		 * @param purchaseScoreMax New value of property purchaseScoreMax.
		 * @return true or false
		 */
		boolean setPurchaseScoreMax(int purchaseScoreMax)
		{
			if ((purchaseScoreMax >= 0) && (purchaseScoreMax >= currentPurchaseScoreMin))
			{
				currentPurchaseScoreMax = purchaseScoreMax;

				return true;
			}

			return false;
		}

		/** Getter for property purchaseScoreMax.
		 * @return Value of property purchaseScoreMax.
		 */
		int getPurchaseScoreMax()
		{
			return currentPurchaseScoreMax;
		}

		/** Setter for property purchaseScoreMin.
		 * @param purchaseScoreMin New value of property purchaseScoreMin.
		 * @return true or false
		 */
		boolean setPurchaseScoreMin(int purchaseScoreMin)
		{
			if ((purchaseScoreMin >= 0) && (purchaseScoreMin <= currentPurchaseScoreMax))
			{
				currentPurchaseScoreMin = purchaseScoreMin;

				return true;
			}

			return false;
		}

		/** Getter for property purchaseScoreMin.
		 * @return Value of property purchaseScoreMin.
		 */
		int getPurchaseScoreMin()
		{
			return currentPurchaseScoreMin;
		}

		void appendRows(int nrRows)
		{
			final int nrEntries = currentPurchaseScoreMax - currentPurchaseScoreMin + 1;

			Object[][] newValues = new Object[nrEntries][2];

			if (nrRows < 0)
			{
				// removing rows
				System.arraycopy(currentValues, 0, newValues, 0, nrEntries);
			}
			else
			{
				// adding rows
				System.arraycopy(currentValues, 0, newValues, 0, currentValues.length);

				final int preLength = currentValues.length;

				for (int i = 0; i < nrRows; ++i)
				{
					final int score = (i + currentPurchaseScoreMax) - nrRows + 1;
					int preVal = -1;
					newValues[i + preLength][0] = new Integer(score);

					if ((i + preLength) != 0)
					{
						preVal = ((Integer) newValues[(i + preLength) - 1][1]).intValue();
					}

					newValues[i + preLength][1] = new Integer(preVal + 1);
				}
			}

			currentValues = newValues;
		}

		void keepNewValues()
		{
			// set the current values into the settings
			SettingsHandler.getGame().clearPointBuyStatCosts();

			for (int i = currentPurchaseScoreMin; i <= currentPurchaseScoreMax; ++i)
			{
				SettingsHandler.getGame().addPointBuyStatCost(i,
				    ((Integer) currentValues[i - currentPurchaseScoreMin][1]).intValue());
			}

			SettingsHandler.getGame().clearPurchaseModeMethods();

			for (int i = 0, x = currentPurchaseMethods.getItemCount(); i < x; ++i)
			{
				final PointBuyMethod pbm = (PointBuyMethod) currentPurchaseMethods.getItemAt(i);
				SettingsHandler.getGame().addPurchaseModeMethod(pbm.getMethodName(), pbm.getPointFormula());
			}
		}

		void prependRows(int nrRows)
		{
			final int nrEntries = currentPurchaseScoreMax - currentPurchaseScoreMin + 1;

			Object[][] newValues = new Object[nrEntries][2];

			if (nrRows > 0)
			{
				// removing rows
				System.arraycopy(currentValues, nrRows, newValues, 0, nrEntries);
			}
			else
			{
				// adding rows
				nrRows = Math.abs(nrRows);
				System.arraycopy(currentValues, 0, newValues, nrRows, currentValues.length);

				//final int tblStart = ((Integer) currentValues[0][1]).intValue();
				for (int i = 0; i < nrRows; ++i)
				{
					final int score = i + currentPurchaseScoreMin;
					newValues[i][0] = new Integer(score);

//					newValues[i][1] = new Integer(tblStart - nrRows + i);
				}
			}

			currentValues = newValues;
		}
	}
}
