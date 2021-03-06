/*
 * Copyright 2015 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.cdom.content;

import pcgen.base.calculation.Modifier;
import pcgen.base.util.FormatManager;

/**
 * DefaultVarValue is actually a shell (unused at runtime). It is leveraged by
 * (and necessary for) CDOMLineLoader to create an object for tokens to process
 * and unparse, but the values stored here are not used at runtime.
 */
public class DefaultVarValue extends UserContent
{

	private FormatManager<?> formatManager;

	private Modifier<?> modifier;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplayName()
	{
		return getKeyName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLSTformat()
	{
		return getDisplayName();
	}

	/**
	 * Sets the FormatManager that manages the data format for which this object
	 * holds the default value.
	 * 
	 * @param fmtManager
	 *            the FormatManager that manages the data format for which this
	 *            object holds the default value
	 */
	public void setFormatManager(FormatManager<?> fmtManager)
	{
		formatManager = fmtManager;
	}

	/**
	 * Returns the FormatManager that manages the data format for which this
	 * object holds the default value.
	 * 
	 * @return the FormatManager that manages the data format for which this
	 *         object holds the default value
	 */
	public FormatManager<?> getFormatManager()
	{
		return formatManager;
	}

	/**
	 * Sets the Modifier to be used as the default Modifier for the format
	 * contained by this DefaultVarValue.
	 * 
	 * @param modifier
	 *            the Modifier to be used as the default Modifier for the format
	 *            contained by this DefaultVarValue
	 */
	public void setModifier(Modifier<?> modifier)
	{
		this.modifier = modifier;
	}

	/**
	 * Returns the Modifier containing the default variable value.
	 * 
	 * @return the Modifier containing the default variable value
	 */
	public Modifier<?> getModifier()
	{
		return modifier;
	}
}
