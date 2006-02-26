/*
 * NoteToken.java
 * Copyright 2003 (C) Devon Jones <soulcatcher@evilsoft.org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on December 15, 2003, 12:21 PM
 *
 * Current Ver: $Revision: 1.6 $
 * Last Editor: $Author: soulcatcher $
 * Last Edited: $Date: 2005/10/27 20:33:54 $
 *
 */
package plugin.exporttokens;

import pcgen.core.NoteItem;
import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

//NOTE
public class NoteToken extends Token
{
	public static final String TOKENNAME = "NOTE";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	public String getTokenName()
	{
		return TOKENNAME;
	}

	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh)
	{
		StringTokenizer tok = new StringTokenizer(tokenSource, ".");
		tok.nextToken();
		StringBuffer sb = new StringBuffer();

		String name = tok.nextToken();
		List noteList = getNoteList(pc, name);

		String beforeHeader = "<b>";
		String afterHeader = "</b><br>";
		String beforeValue = "";
		String afterValue = "<br>";
		String token = "ALL";

		if(tok.hasMoreTokens())
		{
			beforeHeader = tok.nextToken();
			if ("NAME".equals(beforeHeader))
			{
				token = "NAME";
				beforeHeader = afterHeader = beforeValue = afterValue = "";
				if (tok.hasMoreTokens() && !"ALL".equals(token))
				{
					beforeHeader = tok.nextToken();
				}
				if (tok.hasMoreTokens())
				{
					afterHeader = tok.nextToken();
				}
			}
			else if ("VALUE".equals(beforeHeader))
			{
				token = "VALUE";
				beforeHeader = afterHeader = beforeValue = afterValue = "";
				if (tok.hasMoreTokens())
				{
					beforeValue = tok.nextToken();
				}
				if (tok.hasMoreTokens())
				{
					afterValue = tok.nextToken();
				}
			}
			else if ("ALL".equals(beforeHeader))
			{
				token = "ALL";
				if (tok.hasMoreTokens())
				{
					beforeHeader = tok.nextToken();
				}
				if (tok.hasMoreTokens())
				{
					afterHeader = tok.nextToken();
				}
				if (tok.hasMoreTokens())
				{
					beforeValue = tok.nextToken();
				}
				if (tok.hasMoreTokens())
				{
					afterValue = tok.nextToken();
				}
			}
		}

		for (int i = 0; i < noteList.size(); i++)
		{
			NoteItem ni = (NoteItem) noteList.get(i);

			if ("ALL".equals(token))
			{
				sb.append(ni.getExportString(beforeHeader, afterHeader, beforeValue, afterValue));
			}
			else if ("NAME".equals(token))
			{
				sb.append(ni.getName());
			}
			else if ("VALUE".equals(token))
			{
				StringTokenizer cTok = new StringTokenizer(ni.getValue(), "\r\n");

				while (cTok.hasMoreTokens())
				{
					sb.append(beforeValue);
					sb.append(cTok.nextToken());
					sb.append(afterValue);
				}
			}
		}

		return sb.toString();
	}

	public boolean isEncoded() {
		return false;
	}


	public static List getNoteList(PlayerCharacter pc, String name) {
		ArrayList noteList = new ArrayList();
		List resultList;

		buildSubTree(noteList, pc.getNotesList(), -1);

		if ("ALL".equals(name))
		{
			resultList = noteList;
		}
		else
		{
			resultList = new ArrayList();
			try
			{
				int i = Integer.parseInt(name);

				if ((i >= 0) || (i < noteList.size()))
				{
					resultList.add(noteList.get(i));
				}
			}
			catch (NumberFormatException e)
			{
				resultList = (ArrayList) noteList.clone();

				for (int i = resultList.size() - 1; i >= 0; --i)
				{
					NoteItem ni = (NoteItem) resultList.get(i);

					if (!ni.getName().equalsIgnoreCase(name))
					{
						resultList.remove(i);
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * Populate the target list with the children of the specified node.
	 * This will recursively build up a list of the nodes in the base
	 * list in breadth-first order. <br />
	 * The initial call should have a parentNode of -1. This will add all
	 * children of the hard-coded base nodes.
	 *
	 * @param targetList The list to be populated.
	 * @param baseList The source list for notes
	 * @param parentNode The id of the node to be processed.
	 */
	private static void buildSubTree(
		List targetList,
		ArrayList baseList,
		int parentNode)
	{
		for (Iterator baseIter = baseList.iterator(); baseIter.hasNext();)
		{
			NoteItem note = (NoteItem) baseIter.next();
			if (note.getParentId() == parentNode
				|| (parentNode == -1 && note.getParentId() < 0))
			{
				targetList.add(note);
				buildSubTree(targetList, baseList, note.getId());
			}
		}
	}
}

