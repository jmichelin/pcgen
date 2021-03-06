/*
 * Copyright 2014 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.rules.persistence.token;

import pcgen.base.calculation.BasicCalculation;
import pcgen.base.calculation.CalculationModifier;
import pcgen.base.calculation.Modifier;
import pcgen.base.calculation.NEPCalculation;
import pcgen.base.formula.base.FormulaManager;
import pcgen.base.formula.base.LegalScope;
import pcgen.base.util.FormatManager;
import pcgen.cdom.content.ProcessCalculation;

/**
 * An AbstractSetModifierFactory is a ModifierToken/BasicCalculation that
 * returns a specific value (independent of the input) when the
 * AbstractSetModifierFactory is processed.
 */
public abstract class AbstractSetModifierFactory<T> implements
		ModifierFactory<T>, BasicCalculation<T>
{

	/**
	 * Returns the value provided in the constructor. The input value and
	 * FormulaManager are ignored.
	 * 
	 * @see pcgen.base.modifier.Modifier#process(java.lang.Object,
	 *      pcgen.base.formula.manager.FormulaManager)
	 */
	@Override
	public T process(T previousValue, T argument)
	{
		return argument;
	}

	/**
	 * Returns the inherent priority of an AbstractSetModifierFactory. This is
	 * used if two Modifiers have the same User Priority. Lower values are
	 * processed first.
	 * 
	 * @see pcgen.base.modifier.Modifier#getInherentPriority()
	 */
	@Override
	public int getInherentPriority()
	{
		return 0;
	}

	/**
	 * Returns an Identifier for this type of Modifier
	 * 
	 * @see pcgen.base.modifier.Modifier#getIdentification()
	 */
	@Override
	public String getIdentification()
	{
		return "SET";
	}

	/**
	 * @see pcgen.rules.persistence.token.ModifierFactory#getModifier(int,
	 *      java.lang.String, pcgen.base.formula.manager.FormulaManager,
	 *      pcgen.base.formula.base.LegalScope, pcgen.base.format.FormatManager)
	 */
	@Override
	public Modifier<T> getModifier(int userPriority, String instructions,
		FormulaManager ignored, LegalScope varScope,
		FormatManager<T> formatManager)
	{
		//TODO if this is Skill, fixed doesn't work :/
		return getFixedModifier(userPriority, formatManager, instructions);
	}

	@Override
	public Modifier<T> getFixedModifier(int userPriority,
		FormatManager<T> fmtManager, String instructions)
	{
		T n = fmtManager.convert(instructions);
		NEPCalculation<T> calc = new ProcessCalculation<T>(n, this, fmtManager);
		return new CalculationModifier<T>(calc, userPriority);
	}

}
