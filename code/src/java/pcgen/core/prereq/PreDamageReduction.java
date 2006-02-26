/*
 * PreDamageReduction.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on November 28, 2003
 *
 * Current Ver: $Revision: 1.11 $
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:37 $
 *
 */
package pcgen.core.prereq;

import pcgen.core.PlayerCharacter;

import java.util.StringTokenizer;

/**
 * @author wardc
 *
 */
public class PreDamageReduction extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character) {
		int runningTotal = 0;

		// Parse the character's DR into a lookup map
		final String aDR = character.calcDR(); // Silver/10;Good/5;Magic/15
		if (aDR != null)
		{
			final StringTokenizer characterDRTokenizer = new StringTokenizer(aDR, ";"); //$NON-NLS-1$

			while (characterDRTokenizer.hasMoreTokens())
			{
				final StringTokenizer drTokenizer = new StringTokenizer(characterDRTokenizer.nextToken(), "/"); //$NON-NLS-1$
				final String aValue = drTokenizer.nextToken();
				final String aType = drTokenizer.nextToken();

				if (aType.equalsIgnoreCase( prereq.getKey())) {
					final int characterValue = Integer.parseInt(aValue);
					final int targetValue = Integer.parseInt( prereq.getOperand() );

					runningTotal = prereq.getOperator().compare(characterValue, targetValue);
					break;
				}
			}
		}

		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "DR"; //$NON-NLS-1$
	}
}
